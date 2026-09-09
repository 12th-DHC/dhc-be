package project.dhc.domain.admin.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.dhc.domain.admin.entity.Admin;
import project.dhc.domain.admin.repository.AdminRepository;
import project.dhc.global.exception.exceptions.AdminNotFoundException;
import project.dhc.global.exception.exceptions.InvalidPasswordException;

@Service
@RequiredArgsConstructor
public class AdminPasswordService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public void changePassword(
            Long adminId,
            String currentPassword,
            String newPassword
    ) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(() -> AdminNotFoundException.EXCEPTION);

        // 비밀번호 확인
        if(!passwordEncoder.matches(
                currentPassword,
                admin.getAdminPassword()
        )) {
            throw InvalidPasswordException.EXCEPTION;
        }
        // 새 비밀번호 암호화
        admin.setAdminPassword(
                passwordEncoder.encode(newPassword)
        );

        adminRepository.save(admin);
    }
}
