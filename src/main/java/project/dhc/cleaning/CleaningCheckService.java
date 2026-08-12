package project.dhc.cleaning;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dhc.cleaning.dto.CleaningCheckRequest;
import project.dhc.entity.Admin;
import project.dhc.entity.Room;
import project.dhc.global.exception.exceptions.RoomNotFoundException;
import project.dhc.repository.AdminRepository;
import project.dhc.repository.RoomRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CleaningCheckService {
    private final CleaningCheckRepository cleaningCheckRepository;
    private final RoomRepository roomRepository;
    private final AdminRepository adminRepository;

    public void registerCleaningCheck(CleaningCheckRequest request) {
        Room room=roomRepository.findByRoomNumber(request.getRoomNumber())
                .orElseThrow(() ->RoomNotFoundException.EXCEPTION);

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
}
