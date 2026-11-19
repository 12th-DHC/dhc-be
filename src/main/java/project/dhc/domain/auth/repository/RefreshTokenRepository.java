package project.dhc.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.dhc.domain.admin.entity.Admin;
import project.dhc.domain.auth.entity.RefreshToken;
import project.dhc.domain.user.entity.Room;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token); // token 값을 기준으로 RefreshToken을 조회
    void deleteByAdmin(Admin admin); // 어드민 RefreshToken 삭제
    void deleteByRoom(Room room); // 유저 RefreshToken 삭제
}
