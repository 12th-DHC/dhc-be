package project.dhc.statistics.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DailyStatisticsResponse {

    private int userCount;
    private int checkedCount;
    private int passedCount;
    private int indPassedCount;
}
