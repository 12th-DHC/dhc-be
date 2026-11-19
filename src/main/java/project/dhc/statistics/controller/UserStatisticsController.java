package project.dhc.statistics.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.dhc.global.response.ApiResponseMessage;
import project.dhc.statistics.dto.request.StatisticsRequest;
import project.dhc.statistics.dto.response.WeeklyUserStatisticsResponse;
import project.dhc.statistics.service.UserStatisticsService;

import java.util.List;

@RestController // HTTP 요청을 처리하는 controller
@RequiredArgsConstructor // final 필드 자동 생성
@RequestMapping("/users/statistics")
public class UserStatisticsController {

    private final UserStatisticsService userStatisticsService;

    // 유저 주차별 통계 조회
    @PostMapping("/weekly")
    public List<WeeklyUserStatisticsResponse> getWeeklyStatistics(
            @AuthenticationPrincipal String roomNumber,
            @RequestBody StatisticsRequest request // 조회할 월
    ) {
        return userStatisticsService.getWeeklyStatistics(
                roomNumber,
                request
        );
    }
}
