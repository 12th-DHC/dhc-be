package project.dhc;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordHashTest {

    @Test
    void passord_hashing_test() {

        // BCrypt 암호화 객체 생성
        BCryptPasswordEncoder encoder =
                new BCryptPasswordEncoder();

        // 테스트할 비밀번호
        String password = "1234";

        // 비밀번호 해싱
        String hashedPassword =
                encoder.encode(password);

        // 해싱된 값이 원래 비밀번호와 다른지 확인
        assertThat(hashedPassword).isNotEqualTo(password);

        // 원래 비밀번호와 해싱된 비밀번호가 일치하는지 확인
        assertThat(encoder.matches(password, hashedPassword))
                .isTrue();
    }
    }