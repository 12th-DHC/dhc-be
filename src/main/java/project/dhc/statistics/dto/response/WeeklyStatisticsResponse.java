package project.dhc.statistics.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyStatisticsResponse {

    private int userCount;
    private int passedCount;
    private int indPassedCount;
}
