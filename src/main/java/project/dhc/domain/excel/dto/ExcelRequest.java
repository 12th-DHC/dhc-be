package project.dhc.domain.excel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExcelRequest {

    @NotNull(message = "주 시작일은 필수입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate weekStartDate;
}
