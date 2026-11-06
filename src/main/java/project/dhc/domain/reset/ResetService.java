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
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ResetService {

    private static final int[] ROOM_COUNTS_BY_FLOOR = {9, 22, 27, 23, 18};

    private final AdminRepository adminRepository;
    private final RoomRepository roomRepository;
    private final PasswordEncoder passwordEncoder;
    private final CleaningCheckRepository cleaningCheckRepository;

    public void reset(AdminResetRequest request) {
        Admin admin = adminRepository.findById(1L)
                .orElseThrow(() -> AdminNotFoundException.EXCEPTION);

        if (!passwordEncoder.matches(
                request.getAdminPassword(),
                admin.getAdminPassword()
        )) {
            throw InvalidPasswordException.EXCEPTION;
        }

        cleaningCheckRepository.deleteAllInBatch();

        List<Room> rooms = roomRepository.findAll();
        Map<Integer, Room> roomsByNumber = rooms.stream()
                .collect(Collectors.toMap(Room::getRoomNumber, Function.identity()));
        String encodedPassword = passwordEncoder.encode("1234");

        for (int floor = 1; floor <= ROOM_COUNTS_BY_FLOOR.length; floor++) {
            for (int number = 1; number <= ROOM_COUNTS_BY_FLOOR[floor - 1]; number++) {
                int roomNumber = floor * 100 + number;
                if (!roomsByNumber.containsKey(roomNumber)) {
                    Room room = new Room();
                    room.setRoomNumber(roomNumber);
                    rooms.add(room);
                }
            }
        }

        for (Room room : rooms) {
            room.setRoomPassword(encodedPassword);
            room.setAName(null);
            room.setBName(null);
            room.setAEmail(null);
            room.setBEmail(null);
        }

        roomRepository.saveAll(rooms);
    }
}
