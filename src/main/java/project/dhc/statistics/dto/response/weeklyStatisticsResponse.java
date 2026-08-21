package project.dhc.statistics.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class weeklyStatisticsResponse {

    private int userCount;
    private int passedCount;
    private int indPassedCount;
}
