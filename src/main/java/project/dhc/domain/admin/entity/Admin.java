package project.dhc.domain.admin.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @Column(nullable = false, unique = true)
    private String adminUsername;

    // 관리자 로그인 아이디
    @Column(unique = true)
    private String adminUsername;

    // 관리자 비밀번호
    @Column(nullable = false) // null값을 허용 X
    private String adminPassword;

    // 관리자 계정 생성용 Builder
    @Builder
    public Admin(String adminUsername, String adminPassword) {
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    public void changePassword(String newPassword) {
        this.adminPassword = newPassword;
    }
}