package project.dhc.statistics.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.dhc.domain.user.entity.Room;

import java.time.LocalDate;

@Entity
@Table(name = "btl_cleaning_check")
@Getter
@Setter
@NoArgsConstructor
public class CleaningChecked {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cleaningCheckId;

    // 검사한 방
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    // 검사 날짜
    @Column(nullable = false)
    private LocalDate checkDate;

    // 검사 여부
    @Column(nullable = false)
    private boolean checking;

    // 통과 여부
    @Column(nullable = false)
    private boolean passed;

    // A학생 통과 여부
    @Column(nullable = false)
    private boolean aPassed;

    // B학생 통과 여부
    @Column(nullable = false)
    private boolean bPassed;
}