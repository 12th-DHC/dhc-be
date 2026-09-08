package project.dhc.domain.auth.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.dhc.domain.admin.entity.Admin;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_token")
@Getter
@Setter
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenId;

    // 어떤 관리자에게 발급된 토큰인지 확인
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    // refreshToken 값
    @Column(nullable = false, unique = true)
    private String token;

    // 만료 시간
    @Column(nullable = false)
    private LocalDateTime expiration;
}
