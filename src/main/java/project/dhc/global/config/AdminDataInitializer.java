package project.dhc.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.dhc.domain.admin.entity.Admin;
import project.dhc.domain.admin.repository.AdminRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AdminDataInitializer {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.accounts.admin01.username}")
    private String admin01Username;

    @Value("${admin.accounts.admin01.password}")
    private String admin01Password;

    @Value("${admin.accounts.admin02.username}")
    private String admin02Username;

    @Value("${admin.accounts.admin02.password}")
    private String admin02Password;

    @Value("${admin.accounts.admin03.username}")
    private String admin03Username;

    @Value("${admin.accounts.admin03.password}")
    private String admin03Password;

    @Value("${admin.accounts.admin04.username}")
    private String admin04Username;

    @Value("${admin.accounts.admin04.password}")
    private String admin04Password;

    @Bean
    CommandLineRunner initAdmin() {
        return args -> {
            List<ConfiguredAdmin> configuredAdmins = List.of(
                    new ConfiguredAdmin(admin01Username, admin01Password),
                    new ConfiguredAdmin(admin02Username, admin02Password),
                    new ConfiguredAdmin(admin03Username, admin03Password),
                    new ConfiguredAdmin(admin04Username, admin04Password)
            );
            // 관리자 환경변수 누락 확인
            if (configuredAdmins.stream().anyMatch(a -> a.username().isBlank() || a.password().isBlank())) {
                throw new IllegalStateException("Admin usernames and passwords must not be blank");
            }

            // 기존 관리자 계정을 ID순으로 조회
            List<Admin> existingAdmins = new ArrayList<>(adminRepository.findAll().stream()
                    .sorted(Comparator.comparing(Admin::getAdminId))
                    .toList());

            for (ConfiguredAdmin configured : configuredAdmins) {
                if (adminRepository.findByAdminUsername(configured.username()).isPresent()) {
                    continue;
                }

                Admin legacyAdmin = existingAdmins.stream()
                        .filter(admin -> admin.getAdminUsername() == null || admin.getAdminUsername().isBlank())
                        .findFirst()
                        .orElse(null);
                if (legacyAdmin != null) {
                    // 기존 비밀번호를 유지하고 로그인 아이디만 연결
                    legacyAdmin.setAdminUsername(configured.username());
                    adminRepository.save(legacyAdmin);
                    continue;
                }

                // 설정된 관리자 계정 생성
                Admin admin = Admin.builder()
                        .adminUsername(configured.username())
                        .adminPassword(passwordEncoder.encode(configured.password()))
                        .build();
                existingAdmins.add(adminRepository.save(admin));
            }
        };
    }

    private record ConfiguredAdmin(String username, String password) {}
}
