package project.dhc.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "btl_admin")
@Getter
@Setter
@NoArgsConstructor

public class Admin {
    @Id // 기본키
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long adminId;

    @Column(nullable = false) // null값을 허용 X
    private String adminPassword;
}