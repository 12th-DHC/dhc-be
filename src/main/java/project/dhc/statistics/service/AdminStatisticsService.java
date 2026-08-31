package project.dhc.statistics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dhc.statistics.dto.request.StatisticsRequest;
import project.dhc.statistics.dto.response.DailyStatisticsResponse;
import project.dhc.statistics.dto.response.WeeklyStatisticsResponse;
import project.dhc.statistics.entity.CleaningChecked;
import project.dhc.statistics.repository.CleaningCheckedRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminStatisticsService {

    private final CleaningCheckedRepository cleaningCheckedRepository;

    // 오늘 통계 조회
    @Transactional(readOnly = true) // DB 조회만 수행
    public DailyStatisticsResponse getTodayStatistics() {

        // 오늘 날짜
        LocalDate today = LocalDate.now();

        // 오늘 청소 검사 결과 조회
        List<CleaningChecked> checks = cleaningCheckedRepository.findByCheckDateBetween(
                today,
                today
        );

        // 검사한 방 수
        int checkCount = checks.size();

        // 통과한 방 수
        int passCount = (int) checks.stream().filter(CleaningChecked::isPassed).count();

        // A, B 개별 통과 수
        int indPassedCount = (int) checks.stream()
                .mapToLong(check -> {int count = 0;
                    if (check.isAPassed()) {
                        count++;
                    }
                    if (check.isBPassed()) {
                        count++;
                    }
                    return count;
                }).sum();

        // 전체 학생 수
        int userCount = checkCount * 2;

        // 통계 결과 변환
        return new DailyStatisticsResponse(
                userCount,
                checkCount,
                passCount,
                indPassedCount
        );
    }

    @Transactional(readOnly = true)
    public WeeklyStatisticsResponse getWeeklyStatistics(
            StatisticsRequest request
    ) {
        // 선택한 월의 1일
        LocalDate firstDay = LocalDate.of(
                LocalDate.now().getYear(),
                request.getMonth(),
                1
        );

        // 해당 날짜가 속한 주 월요일
        LocalDate firstMonday = firstDay.with(
                TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)
        );

        // 선택한 주차의 월요일
        LocalDate startDate = firstMonday.plusWeeks(
                request.getWeek() - 1
        );

        // 선택한 주차의 일요일
        LocalDate endDate = startDate.plusDays(6);

        // 선택한 주의 청소 검사 결과 조회

        List<CleaningChecked> checks = cleaningCheckedRepository.findByCheckDateBetween(
                startDate,
                endDate
        );

        // 검사 대상 학생 수
        int userCount = checks.size() * 2;

        // 통과한 검사 방 수
        int passCount = (int) checks.stream().filter(CleaningChecked::isBPassed).count();

        // A, B 학생의 개별 통과 학생 수
        int indPassCount = (int) checks.stream().mapToLong(check -> {
            int count = 0;

            if(check.isAPassed()) {
                count++;
            }
            if(check.isBPassed()) {
                count++;
            }
            return count;
        }).sum();

        // 주차별 통계 결과 반환
        return new WeeklyStatisticsResponse(
                userCount,
                passCount,
                indPassCount
        );
    }
}