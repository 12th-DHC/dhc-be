package project.dhc.global.config;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.dhc.domain.admin.entity.Admin;
import project.dhc.domain.admin.repository.AdminRepository;

@Configuration
@RequiredArgsConstructor
public class AdminDataInitializer {

    @Bean
    CommandLineRunner initAdmin(
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder
    ){
        return args -> {
            if(adminRepository.count() == 0) {
                createAdmin(
                        adminRepository,
                        passwordEncoder,
                        "admin01",
                        "1234"
                );
                createAdmin(
                        adminRepository,
                        passwordEncoder,
                        "admin02",
                        "1234"
                );
                createAdmin(
                        adminRepository,
                        passwordEncoder,
                        "admin03",
                        "1234"
                );
                createAdmin(
                        adminRepository,
                        passwordEncoder,
                        "admin04",
                        "1234"
                );
            }
        };
    }
    private void createAdmin(
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder,
            String username,
            String password
    ) {
        Admin admin = Admin.builder().adminUsername(username).adminPassword(passwordEncoder.encode(password)).build();

        adminRepository.save(admin); // 완성된 어드민 객체 db에 저장
    }
}