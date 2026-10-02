package project.dhc;

import org.junit.jupiter.api.Test;
import project.dhc.domain.cleaning.CleaningCheck;
import project.dhc.domain.cleaning.CleaningCheckRepository;
import project.dhc.domain.notification.*;
import project.dhc.domain.notification.dto.NotificationMessage;
import project.dhc.domain.user.entity.Room;
import project.dhc.global.exception.exceptions.NotificationDataIncompleteException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class NotificationServiceTest {
    private final CleaningCheckRepository checks = mock(CleaningCheckRepository.class);
    private final NotificationPublisher publisher = mock(NotificationPublisher.class);
    private final NotificationService service = new NotificationService(
            checks, new NotificationMessageMapper(), publisher);
    private final LocalDate date = LocalDate.of(2026, 9, 26);

    private Room room(int number) {
        Room room = new Room();
        room.setRoomNumber(number);
        room.setAEmail("a@example.com");
        room.setAName("학생A");
        room.setBEmail("b@example.com");
        room.setBName("학생B");
        return room;
    }

    private CleaningCheck check(Room room) {
        return CleaningCheck.builder().room(room).date(date)
                .aPassed(true).aIndPassed(true)
                .bPassed(false).bNotpassReason("1/2").bIndPassed(false).build();
    }

    @Test
    void publishesOnlyStudentsInRoomsWithChecksForRequestedDate() {
        Room room = room(200);
        when(checks.findAllWithRoomByDate(date)).thenReturn(List.of(check(room)));

        service.publish(date);

        verify(publisher).publishAll(List.of(
                new NotificationMessage("a@example.com", "학생A", 200, true, null),
                new NotificationMessage("b@example.com", "학생B", 200, false,
                        "침구정리, 개인물품 및 의복정리, 개인구역 청소")));
    }

    @Test
    void noChecksDoesNotPublishAnything() {
        when(checks.findAllWithRoomByDate(date)).thenReturn(List.of());

        service.publish(date);
        verifyNoInteractions(publisher);
    }

    @Test
    void missingStudentEmailPreventsAllPublishing() {
        Room room = room(200);
        room.setBEmail(null);
        when(checks.findAllWithRoomByDate(date)).thenReturn(List.of(check(room)));

        assertThrows(NotificationDataIncompleteException.class, () -> service.publish(date));
        verifyNoInteractions(publisher);
    }

    @Test
    void duplicateCheckPreventsAllPublishing() {
        Room room = room(200);
        when(checks.findAllWithRoomByDate(date)).thenReturn(List.of(check(room), check(room)));

        assertThrows(NotificationDataIncompleteException.class, () -> service.publish(date));
        verifyNoInteractions(publisher);
    }
}
