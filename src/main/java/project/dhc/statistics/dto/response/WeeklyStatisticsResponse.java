package project.dhc.statistics.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WeeklyStatisticsResponse {

    private int userCount;
    private int passedCount;
    private int indPassedCount;
}
