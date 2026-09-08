package project.dhc.domain.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import project.dhc.domain.email.enums.Area;

public record EmailRegisterRequest(
        @NotNull(message = "구역은 필수입니다.")
        Area area,

        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String email
) {
}