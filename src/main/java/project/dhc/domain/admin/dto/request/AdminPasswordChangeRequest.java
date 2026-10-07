package project.dhc.domain.admin.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminPasswordChangeRequest {

    @NotBlank // null이나 " "같은 빈 문자열을 막아줌
    private String currentPassword;

    @NotBlank
    private String newPassword;
}
