package project.dhc.statistics.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WeeklyStatisticsResponse {

    private int userCount;
    private int passedCount;
    private int indPassedCount;
}
