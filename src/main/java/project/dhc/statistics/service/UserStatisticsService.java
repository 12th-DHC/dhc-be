package project.dhc.statistics.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dhc.global.util.JwtTokenProvider;
import project.dhc.statistics.dto.request.StatisticsRequest;
import project.dhc.statistics.dto.response.WeeklyUserStatisticsResponse;
import project.dhc.statistics.entity.CleaningChecked;
import project.dhc.statistics.repository.CleaningCheckedRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserStatisticsService {

    private final CleaningCheckedRepository cleaningCheckedRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // 주차별 미완료 통계 조회
    @Transactional(readOnly = true)
    public List<WeeklyUserStatisticsResponse> getWeeklyStatistics(
            String token,
            StatisticsRequest request
    ) {
        // JWT에서 방 번호 가져오기
        Integer roomNumber = Integer.valueOf(
                jwtTokenProvider.getSubject(token)
        );

        // 선택한 월의 1일
        LocalDate firstDay = LocalDate.of(
                LocalDate.now().getYear(),
                request.getMonth(),
                1
        );

        // 해당 날짜가 속한 주의 월요일
        LocalDate firstMonday = firstDay.with(
                TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY) // 현재 날짜 기준으로 가장 최근에 지난 월요일의 날짜를 계산
        );

        List<WeeklyUserStatisticsResponse> result = new ArrayList<>();

        // 1 ~ 5주차 계산
        for(int week = 1; week <= 5; week++) {
            LocalDate startDate = firstMonday.plusWeeks(week - 1); // 해당 주 월요일
            LocalDate endDate = startDate.plusDays(6); // 해당 주 일요일

            List<CleaningChecked> checks =
                    cleaningCheckedRepository.findByRoom_RoomNumberAndCheckDateBetween(
                            roomNumber,
                            startDate,
                            endDate
                    ); // 해당 방의 청소 검사 결과 조회

            // 미완료 개수
            int incompleteCount = (int) checks.stream().filter(check -> !check.isPassed()).count();

            result.add(new WeeklyUserStatisticsResponse(
                    week,
                    incompleteCount
            ));
        }
        return result;
    }
}