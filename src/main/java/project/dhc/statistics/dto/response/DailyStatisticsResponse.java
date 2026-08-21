package project.dhc.statistics.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DailyStatisticsResponse {

    private int userCount;
    private int checkedCount;
    private int passedCount;
    private int indPassedCount;
}
