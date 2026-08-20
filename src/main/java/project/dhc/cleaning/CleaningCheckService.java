package project.dhc.cleaning;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dhc.cleaning.dto.CleaningCheckRequest;
import project.dhc.entity.Room;
import project.dhc.global.exception.exceptions.CleaningSearchNotFoundException;
import project.dhc.global.exception.exceptions.RoomNotFoundException;
import project.dhc.repository.AdminRepository;
import project.dhc.repository.RoomRepository;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class CleaningCheckService {
    private final CleaningCheckRepository cleaningCheckRepository;
    private final RoomRepository roomRepository;
    private final AdminRepository adminRepository;

    public void registerCleaningCheck(Integer roomNumber, CleaningCheckRequest request) {
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> RoomNotFoundException.EXCEPTION);

        cleaningCheckRepository
                .findByRoomRoomNumberAndDate(roomNumber, request.getDate())
                .ifPresentOrElse(
                        check -> check.update(request),
                        () -> {
                            CleaningCheck check = CleaningCheck.builder()
                                    .date(request.getDate())
                                    .aPassed(request.getAPassed())
                                    .aNotpassReason(request.getANotpassReason())
                                    .aIndPassed(request.getAIndPassed())
                                    .aIndNotpassReason(request.getAIndNotpassReason())
                                    .bPassed(request.getBPassed())
                                    .bNotpassReason(request.getBNotpassReason())
                                    .bIndPassed(request.getBIndPassed())
                                    .bIndNotpassReason(request.getBIndNotpassReason())
                                    .room(room)
                                    .build();

                            cleaningCheckRepository.save(check);
                        }
                );
    }
    public CleaningSearch getCleaningSearch(int roomNumber, LocalDate date)
    {
        return cleaningCheckRepository.findCleaningSearch(roomNumber, date)
                .orElseThrow(() -> CleaningSearchNotFoundException.EXCEPTION);
    }
}
