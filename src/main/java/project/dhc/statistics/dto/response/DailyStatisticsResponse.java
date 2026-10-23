package project.dhc.statistics.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DailyStatisticsResponse {

    private int userCount;
    private int checkedCount;
    private int passedCount;
    private int indPassedCount;
}
