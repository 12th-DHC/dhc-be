package project.dhc.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.dhc.entity.Admin;
import project.dhc.repository.AdminRepository;
import project.dhc.dto.request.AdminLoginRequest;
import project.dhc.dto.response.LoginResponse;

@Service // 비즈니스 로직
@RequiredArgsConstructor // Lombok이 생성자를 자동으로 생성
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(AdminLoginRequest request) {
        Admin admin = adminRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("관리자가 존재하지 않습니다."));

        // 입력한 비밀번호와 DB의 BCrypt 해시 비교
        if(!passwordEncoder.matches(
                request.getAdminPassword(),
                admin.getAdminPassword()
        )) {
            throw new RuntimeException("비밀번호가 올바르지 않습니다.");
        }

        // 로그인 성공
        return new LoginResponse(200, "어드민 로그인 완료");
    }
}
