package project.dhc.cleaning.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CleaningCheckRequest {

    @NotNull
    private Integer roomNumber;
    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate date;

    @NotNull
    private Boolean aPassed;
    private String aNotpassReason;
    @NotNull
    private Boolean aIndPassed;
    private String aIndNotpassReason;

    @NotNull
    private Boolean bPassed;
    private String bNotpassReason;
    @NotNull
    private Boolean bIndPassed;
    private String bIndNotpassReason;
}
