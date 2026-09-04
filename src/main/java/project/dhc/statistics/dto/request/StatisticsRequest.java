package project.dhc.statistics.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class StatisticsRequest {

    private int month;
    private int week;
}
