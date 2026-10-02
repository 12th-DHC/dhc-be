package project.dhc.domain.notification;

import lombok.RequiredArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dhc.domain.cleaning.CleaningCheck;
import project.dhc.domain.cleaning.CleaningCheckRepository;
import project.dhc.domain.notification.dto.NotificationMessage;
import project.dhc.domain.user.entity.Room;
import project.dhc.global.exception.exceptions.NotificationDataIncompleteException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final CleaningCheckRepository cleaningCheckRepository;
    private final NotificationMessageMapper mapper;
    private final NotificationPublisher publisher;

    @Transactional(readOnly = true)
    public void publish(LocalDate date) {
        List<CleaningCheck> checks = cleaningCheckRepository.findAllWithRoomByDate(date);
        if (checks.isEmpty()) {
            return;
        }

        List<NotificationMessage> messages = new ArrayList<>();
        Set<Integer> roomNumbers = new HashSet<>();
        for (CleaningCheck check : checks) {
            Room room = check.getRoom();
            if (!roomNumbers.add(room.getRoomNumber())) {
                throw NotificationDataIncompleteException.EXCEPTION;
            }
            try {
                messages.add(mapper.create(room.getAEmail(), room.getAName(), room.getRoomNumber(),
                        check.getAPassed(), check.getANotpassReason(), check.getAIndPassed()));
                messages.add(mapper.create(room.getBEmail(), room.getBName(), room.getRoomNumber(),
                        check.getBPassed(), check.getBNotpassReason(), check.getBIndPassed()));
            } catch (IllegalArgumentException | NullPointerException e) {
                throw NotificationDataIncompleteException.EXCEPTION;
            }
        }
        publisher.publishAll(messages);
    }
}
