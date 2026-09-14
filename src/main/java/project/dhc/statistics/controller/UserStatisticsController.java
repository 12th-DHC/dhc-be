package project.dhc.statistics.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.dhc.statistics.dto.request.StatisticsRequest;
import project.dhc.statistics.dto.response.WeeklyUserStatisticsResponse;
import project.dhc.statistics.service.UserStatisticsService;

import java.util.List;

@RestController // HTTP 요청을 처리하는 controller
@RequiredArgsConstructor // final 필드 자동 생성
@RequestMapping("/users/st")
public class UserStatisticsController {

    private final UserStatisticsService userStatisticsService;

    // 유저 주차별 통계 조회
    @PostMapping("/weekly")
    public List<WeeklyUserStatisticsResponse> getWeeklyStatistics(
            @RequestHeader("Authorization")
            String token, // JWT 토큰
            @RequestBody
            StatisticsRequest request // 조회할 월
    ) {
        return userStatisticsService.getWeeklyStatistics(
                token,
                request
        );
    }
}
