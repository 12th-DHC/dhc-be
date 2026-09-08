package project.dhc.domain.cleaning;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CleaningSearch {
    private LocalDate date;
    private Boolean aPassed;
    private String aNotpassReason;
    private Boolean aIndPassed;
    private String aIndNotpassReason;
    private Boolean bPassed;
    private String bNotpassReason;
    private Boolean bIndPassed;
    private String bIndNotpassReason;
}
