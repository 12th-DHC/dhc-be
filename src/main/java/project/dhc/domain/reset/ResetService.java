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

        if (!passwordEncoder.matches(
                request.getAdminPassword(),
                admin.getAdminPassword()
        )) {
            throw InvalidPasswordException.EXCEPTION;
        }

        cleaningCheckRepository.deleteAllInBatch();

        List<Room> rooms = roomRepository.findAll();

        for (Room room : rooms) {
            room.setRoomPassword(passwordEncoder.encode("1234"));
            room.setAName(null);
            room.setBName(null);
            room.setAEmail(null);
            room.setBEmail(null);
        }
    }
}