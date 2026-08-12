package project.dhc.cleaning;

import jakarta.persistence.*;
import lombok.*;
import project.dhc.entity.Admin;
import project.dhc.entity.Room;

import java.time.LocalDate;

@Entity
@Table(name ="btl_CleaningCheck")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder

public class CleaningCheck {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="record_id")
    private Long recordId;

    @Column(name = "date", nullable=false)
    private LocalDate date;

    @Column(name = "A_passed", nullable = false)
    private Boolean aPassed;

    @Column(name = "A_notpass_reason")
    private String aNotpassReason;

    @Column(name = "A_ind_passed", nullable = false)
    private Boolean aIndPassed;

    @Column(name = "A_ind_notpass_reason")
    private String aIndNotpassReason;

    @Column(name = "B_passed", nullable = false)
    private Boolean bPassed;

    @Column(name = "B_notpass_reason")
    private String bNotpassReason;

    @Column(name = "B_ind_passed", nullable = false)
    private Boolean bIndPassed;

    @Column(name = "B_ind_notpass_reason")
    private String bIndNotpassReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_number", referencedColumnName = "room_number",nullable = false)
    private Room room;
}