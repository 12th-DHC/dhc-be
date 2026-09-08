package project.dhc.statistics.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyUserStatisticsResponse {

    private int week; // 주차
    private int incompleteCount; // 미완료 횟수
}
