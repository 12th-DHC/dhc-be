package project.dhc.statistics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dhc.domain.cleaning.CleaningCheck;
import project.dhc.domain.cleaning.CleaningCheckRepository;
import project.dhc.domain.user.entity.Room;
import project.dhc.domain.user.repository.RoomRepository;
import project.dhc.statistics.dto.request.StatisticsRequest;
import project.dhc.statistics.dto.response.DailyStatisticsResponse;
import project.dhc.statistics.dto.response.WeeklyStatisticsResponse;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminStatisticsService {

    private final CleaningCheckRepository cleaningCheckRepository;
    private final RoomRepository roomRepository;

    // 오늘 통계 조회
    @Transactional(readOnly = true)
    public DailyStatisticsResponse getTodayStatistics() {

        // 오늘 날짜
        LocalDate today = LocalDate.now();

        // 오늘 청소 검사 결과 조회
        List<CleaningCheck> checks =
                cleaningCheckRepository.findAllWithRoomByDateBetween(
                        today,
                        today
                );

        // 방마다 가장 최근 검사 기록 하나만 사용
        List<CleaningCheck> latestChecks = getLatestChecksPerRoom(checks);

        // 검사한 고유 방 수
        int checkCount = latestChecks.size();

        // A, B 기본 청소 통과 학생 수
        int passedCount = countPassed(latestChecks);

        // A, B 개별 청소 통과 학생 수
        int indPassedCount = countIndividualPassed(latestChecks);

        // 전체 등록 학생 수
        int userCount = countRegisteredStudents();

        // 통계 결과 반환
        return DailyStatisticsResponse.builder()
                .userCount(userCount)
                .checkedCount(checkCount)
                .passedCount(passedCount)
                .indPassedCount(indPassedCount)
                .build();
    }

    // 주간 통계 조회
    @Transactional(readOnly = true)
    public WeeklyStatisticsResponse getWeeklyStatistics(
            StatisticsRequest request
    ) {
        // 선택한 월의 1일
        LocalDate firstDay = LocalDate.of(
                request.getYear(),
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
        List<CleaningCheck> checks =
                cleaningCheckRepository.findAllWithRoomByDateBetween(
                        startDate,
                        endDate
                );

        // 방마다 가장 최근 검사 기록 하나만 사용
        List<CleaningCheck> latestChecks =
                getLatestChecksPerRoom(checks);

        // 전체 등록 학생 수
        int userCount = countRegisteredStudents();

        // A, B 기본 청소 통과 학생 수
        int passedCount = countPassed(latestChecks);

        // A, B 개별 청소 통과 학생 수
        int indPassedCount = countIndividualPassed(latestChecks);

        // 주간 통계 결과 반환
        return WeeklyStatisticsResponse.builder()
                .userCount(userCount)
                .passedCount(passedCount)
                .indPassedCount(indPassedCount)
                .build();
    }

    // 같은 방의 검사 기록 중 가장 최근 기록 하나만 선택
    private List<CleaningCheck> getLatestChecksPerRoom(
            List<CleaningCheck> checks
    ) {

        // 방 번호를 기준으로 최신 검사 기록 저장
        Map<Integer, CleaningCheck> latestChecksByRoom = new HashMap<>();

        for (CleaningCheck check : checks) {

            // 현재 검사 기록이 속한 방 번호
            Integer roomNumber = check.getRoom().getRoomNumber();

            // 해당 방의 기존 검사 기록
            CleaningCheck current = latestChecksByRoom.get(roomNumber);

            // 기존 기록이 없거나 새로운 기록이 더 최근이면 교체
            // 검사 날짜가 같으면 ID가 더 큰 기록을 선택
            if (current == null
                    || check.getDate().isAfter(current.getDate())
                    || (check.getDate().isEqual(current.getDate())
                    && check.getRecordId() > current.getRecordId())) {

                latestChecksByRoom.put(roomNumber, check);
            }
        }

        // 방마다 선택된 최신 검사 기록 반환
        return new ArrayList<>(latestChecksByRoom.values());
    }

    // A, B 기본 청소 통과 학생 수 계산
    private int countPassed(List<CleaningCheck> checks) {

        return (int) checks.stream()
                .mapToLong(check -> {
                    int count = 0;

                    // A 학생 기본 청소 통과 여부
                    if (Boolean.TRUE.equals(check.getAPassed())) {
                        count++;
                    }

                    // B 학생 기본 청소 통과 여부
                    if (Boolean.TRUE.equals(check.getBPassed())) {
                        count++;
                    }

                    return count;
                })
                .sum();
    }

    // A, B 개별 청소 통과 학생 수 계산
    private int countIndividualPassed(List<CleaningCheck> checks) {

        return (int) checks.stream()
                .mapToLong(check -> {
                    int count = 0;

                    // A 학생 개별 청소 통과 여부
                    if (Boolean.TRUE.equals(check.getAIndPassed())) {
                        count++;
                    }
                    // B 학생 개별 청소 통과 여부
                    if (Boolean.TRUE.equals(check.getBIndPassed())) {
                        count++;
                    }

                    return count;
                })
                .sum();
    }

    // 전체 등록 학생 수 계산
    private int countRegisteredStudents() {

        // 이메일 중복을 방지하기 위한 Set
        Set<String> studentEmails = new HashSet<>();

        // 모든 호실의 학생 정보 조회
        for (Room room : roomRepository.findAll()) {

            // A 학생 이메일 등록 여부
            if (room.getAEmail() != null
                    && !room.getAEmail().isBlank()) {
                studentEmails.add(room.getAEmail().trim());
            }

            // B 학생 이메일 등록 여부
            if (room.getBEmail() != null
                    && !room.getBEmail().isBlank()) {
                studentEmails.add(room.getBEmail().trim());
            }
        }

        // 중복 제거된 전체 학생 수 반환
        return studentEmails.size();
    }
}