package project.dhc.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "btl_Room")
@Getter
@Setter
@NoArgsConstructor

public class Room {
    @Id // 기본키
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "roo_number", nullable = false, unique = true) // null 허용 X, 중복 허용 X
    private Integer roomNumber;

    @Column(name = "A_email")
    private String aEmail;

    @Column(name = "B_email")
    private String bEmail;

    @Column(nullable = false)
    private String adminPassword;
}
