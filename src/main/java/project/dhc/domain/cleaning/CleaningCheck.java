package project.dhc.domain.cleaning;

import jakarta.persistence.*;
import lombok.*;
import project.dhc.domain.cleaning.dto.CleaningCheckRequest;
import project.dhc.domain.user.entity.Room;

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

    @Column(name = "a_passed", nullable = false)
    private Boolean aPassed;

    @Column(name = "a_notpass_reason")
    private String aNotpassReason;

    @Column(name = "a_ind_passed", nullable = false)
    private Boolean aIndPassed;

    @Column(name = "a_ind_notpass_reason")
    private String aIndNotpassReason;

    @Column(name = "b_passed", nullable = false)
    private Boolean bPassed;

    @Column(name = "b_notpass_reason")
    private String bNotpassReason;

    @Column(name = "b_ind_passed", nullable = false)
    private Boolean bIndPassed;

    @Column(name = "b_ind_notpass_reason")
    private String bIndNotpassReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_number", referencedColumnName = "room_number",nullable = false)
    private Room room;
    public void update(CleaningCheckRequest request) {
        this.date = request.getDate();
        this.aPassed = request.getAPassed();
        this.aNotpassReason = request.getANotpassReason();
        this.aIndPassed = request.getAIndPassed();
        this.aIndNotpassReason = request.getAIndNotpassReason();
        this.bPassed = request.getBPassed();
        this.bNotpassReason = request.getBNotpassReason();
        this.bIndPassed = request.getBIndPassed();
        this.bIndNotpassReason = request.getBIndNotpassReason();
    }
}