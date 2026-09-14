package project.dhc.domain.reset.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminResetRequest {
    @NotBlank
    private String adminPassword;
}
