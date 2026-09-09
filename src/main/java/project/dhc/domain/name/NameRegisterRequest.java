package project.dhc.domain.name;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import project.dhc.domain.name.enums.Area;

public record NameRegisterRequest(
        @NotNull(message = "구역은 필수입니다.")
        Area area,

        @NotBlank(message = "이름은 필수 입력 값입니다.")
        String name
) {
}