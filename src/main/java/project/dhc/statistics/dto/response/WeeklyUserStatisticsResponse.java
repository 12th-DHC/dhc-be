package project.dhc.statistics.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WeeklyUserStatisticsResponse {

    private int week; // 주차
    private int incompleteCount; // 미완료 횟수
}