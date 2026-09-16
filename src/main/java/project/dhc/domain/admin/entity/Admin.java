package project.dhc.domain.admin.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "btl_admin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Admin {
    @Id // 기본키
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;

    // 관리자 로그인 아이디
    @Column(nullable = false, unique = true)
    private String adminUsername;

    // 관리자 비밀번호
    @Column(nullable = false) // null값을 허용 X
    private String adminPassword;
}