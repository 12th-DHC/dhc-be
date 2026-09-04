package project.dhc.statistics.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.dhc.statistics.dto.request.StatisticsRequest;
import project.dhc.statistics.dto.response.DailyStatisticsResponse;
import project.dhc.statistics.dto.response.WeeklyStatisticsResponse;
import project.dhc.statistics.service.AdminStatisticsService;

@RestController // 통계 API를 처리하는 controller
@RequiredArgsConstructor // final 필드 생성자 자동 생성
@RequestMapping("/admin/statistics")
public class AdminStatisticsController {

    private final AdminStatisticsService adminStatisticsService;

    // 오늘 통계 조회
    @GetMapping("/today")
    public DailyStatisticsResponse getTodayStatistics() {
        return adminStatisticsService.getTodayStatistics();
    }

    // 주차별 통계 조회
    @PostMapping("weekly")
    public WeeklyStatisticsResponse getWeeklyStatistics(
            @RequestBody StatisticsRequest request
            ) {
        return adminStatisticsService.getWeeklyStatistics(request);
    }
}