package project.dhc.domain.name;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dhc.domain.user.entity.Room;
import project.dhc.domain.user.repository.RoomRepository;
import project.dhc.global.exception.exceptions.RoomNotFoundException;

@Service
@RequiredArgsConstructor
public class NameService {

    private final RoomRepository roomRepository;

    @Transactional
    public void registerName(Integer roomNumber, NameRegisterRequest request) {
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> RoomNotFoundException.EXCEPTION);
        switch (request.area()) {
            case A -> room.setAName(request.name());
            case B -> room.setBName(request.name());
        }
    }
}
