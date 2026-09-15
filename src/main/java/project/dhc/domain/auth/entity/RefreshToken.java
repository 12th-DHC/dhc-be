package project.dhc.domain.auth.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.dhc.domain.admin.entity.Admin;
import project.dhc.domain.user.entity.Room;

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

    // 어드민
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    // 유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    // refreshToken 값
    @Column(nullable = false, unique = true)
    private String token;

    // 만료 시간
    @Column(nullable = false)
    private LocalDateTime expiration;
}
