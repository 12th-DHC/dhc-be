package project.dhc.domain.reset;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.dhc.domain.admin.entity.Admin;
import project.dhc.domain.reset.dto.AdminResetRequest;
import project.dhc.domain.admin.repository.AdminRepository;
import project.dhc.domain.user.repository.RoomRepository;
import project.dhc.global.exception.exceptions.AdminNotFoundException;
import project.dhc.global.exception.exceptions.InvalidPasswordException;

@Service
@RequiredArgsConstructor
public class ResetService {

    private final AdminRepository adminRepository;
    private final RoomRepository roomRepository;
    private final PasswordEncoder passwordEncoder;
    private final CleaningCheckRepository cleaningCheckRepository;
    Admin admin = adminRepository.findById(1L).orElseThrow(() -> AdminNotFoundException.EXCEPTION);

    public void reset(AdminResetRequest request) {
        if (!passwordEncoder.matches(request.getAdminPassword(), admin.getAdminPassword())) {
            throw InvalidPasswordException.EXCEPTION;
        }
    }
}
