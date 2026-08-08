package project.dhc.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EmailRegisterRequest(
        @NotBlank(message = "구역은 필수 입력 값입니다.")
        @Pattern(regexp = "^[AB]$", message = "구역은 A 또는 B만 입력 가능합니다.")
        String area,

        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String email
) {
}