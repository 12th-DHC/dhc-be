package project.dhc.domain.reset;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dhc.domain.admin.entity.Admin;
import project.dhc.domain.admin.repository.AdminRepository;
import project.dhc.domain.cleaning.CleaningCheckRepository;
import project.dhc.domain.reset.dto.AdminResetRequest;
import project.dhc.domain.user.entity.Room;
import project.dhc.domain.user.repository.RoomRepository;
import project.dhc.global.exception.exceptions.AdminNotFoundException;
import project.dhc.global.exception.exceptions.InvalidPasswordException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ResetService {

    private final AdminRepository adminRepository;
    private final RoomRepository roomRepository;
    private final PasswordEncoder passwordEncoder;
    private final CleaningCheckRepository cleaningCheckRepository;

    public void reset(AdminResetRequest request) {

        Admin admin = adminRepository.findById(1L)
                .orElseThrow(() -> AdminNotFoundException.EXCEPTION);

        if (!passwordEncoder.matches(request.getAdminPassword(), admin.getAdminPassword())) throw InvalidPasswordException.EXCEPTION;

        cleaningCheckRepository.deleteAllInBatch();

        roomRepository.deleteAllInBatch();

        List<Room> rooms = new ArrayList<>();

        createRooms(rooms, 101, 109);
        createRooms(rooms, 201, 222);
        createRooms(rooms, 301, 327);
        createRooms(rooms, 401, 423);
        createRooms(rooms, 501, 518);

        roomRepository.saveAll(rooms);
    }
    private void createRooms(List<Room> rooms, int start, int end) {
        for (int roomNumber = start; roomNumber <= end; roomNumber++) {
            Room room = new Room();
            room.setRoomNumber(roomNumber);
            room.setRoomPassword(passwordEncoder.encode("1234"));
            rooms.add(room);
        }
    }
}