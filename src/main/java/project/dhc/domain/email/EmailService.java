package project.dhc.domain.email;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dhc.domain.user.entity.Room;
import project.dhc.global.exception.exceptions.RoomNotFoundException;
import project.dhc.domain.user.repository.RoomRepository;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final RoomRepository roomRepository;

    @Transactional
    public void registerEmail(Integer roomNumber, EmailRegisterRequest request) {
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> RoomNotFoundException.EXCEPTION);
        switch (request.area()) {
            case A -> room.setAEmail(request.email());
            case B -> room.setBEmail(request.email());
        }
    }
}
