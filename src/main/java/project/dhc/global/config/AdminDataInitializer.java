package project.dhc.global.config;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.dhc.domain.admin.entity.Admin;
import project.dhc.domain.admin.repository.AdminRepository;

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
                createAdmin(admin01Username, admin01Password);
                createAdmin(admin02Username, admin02Password);
                createAdmin(admin03Username, admin03Password);
                createAdmin(admin04Username, admin04Password);
        };
    }
    private void createAdmin(
            String username,
            String password
    ) {
        if (adminRepository.findByAdminUsername(username).isEmpty()) {
            Admin admin = Admin.builder().adminUsername(username).adminPassword(passwordEncoder.encode(password)).build();

            adminRepository.save(admin); // 완성된 어드민 객체 db에 저장
        }
    }
}