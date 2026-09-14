package project.dhc.domain.excel;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dhc.domain.cleaning.CleaningCheck;
import project.dhc.domain.cleaning.CleaningCheckRepository;
import project.dhc.domain.excel.dto.CleaningExcelRow;
import project.dhc.domain.user.entity.Room;
import project.dhc.domain.user.repository.RoomRepository;
import project.dhc.global.exception.exceptions.InvalidWeekStartDateException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExcelService {
    private final RoomRepository roomRepository;
    private final CleaningCheckRepository cleaningCheckRepository;
    private final CleaningExcelGenerator cleaningExcelGenerator;

    @Transactional(readOnly = true)
    public byte[] export(LocalDate weekStartDate) {
        if (weekStartDate == null || weekStartDate.getDayOfWeek() != DayOfWeek.MONDAY) {
            throw InvalidWeekStartDateException.EXCEPTION;
        }

        Map<Integer, Map<LocalDate, CleaningCheck>> checksByRoom = new HashMap<>();
        for (CleaningCheck check : cleaningCheckRepository.findWeeklyChecks(weekStartDate, weekStartDate.plusDays(4))) {
            // 동일 날짜의 중복 기록이 있다면 recordId가 가장 큰 최신 기록을 사용한다.
            checksByRoom.computeIfAbsent(check.getRoom().getRoomNumber(), ignored -> new HashMap<>())
                    .put(check.getDate(), check);
        }

        List<CleaningExcelRow> rows = new ArrayList<>();
        for (Room room : roomRepository.findAll(Sort.by("roomNumber"))) {
            Map<LocalDate, CleaningCheck> checks = checksByRoom.getOrDefault(room.getRoomNumber(), Map.of());
            rows.add(createRow(room.getRoomNumber(), room.getAName(), true, weekStartDate, checks));
            rows.add(createRow(room.getRoomNumber(), room.getBName(), false, weekStartDate, checks));
        }
        return cleaningExcelGenerator.generate(weekStartDate, rows);
    }

    private CleaningExcelRow createRow(int roomNumber, String name, boolean aStudent,
                                       LocalDate startDate, Map<LocalDate, CleaningCheck> checks) {
        List<String> dailyResults = new ArrayList<>();
        int total = 0;
        boolean exemption = false;
        for (int day = 0; day < 5; day++) {
            CleaningCheck check = checks.get(startDate.plusDays(day));
            if (check == null) {
                dailyResults.add("");
                continue;
            }
            String normal = aStudent ? check.getANotpassReason() : check.getBNotpassReason();
            List<String> reasons = new ArrayList<>(parseReasons(normal));
            total += reasons.size();
            exemption |= reasons.contains("3") || reasons.contains("4");

            String individual = aStudent ? check.getAIndNotpassReason() : check.getBIndNotpassReason();
            if ((day == 1 || day == 4) && individual != null && !individual.isBlank()) {
                reasons.add("x");
                total++;
            }
            dailyResults.add(String.join("/", reasons));
        }
        int adjustedTotal = total - (exemption ? 1 : 0);
        return new CleaningExcelRow(roomNumber, name == null ? "" : name, dailyResults, adjustedTotal >= 3);
    }

    private List<String> parseReasons(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }
        return Arrays.stream(value.split("/"))
                .map(String::trim)
                .filter(reason -> !reason.isEmpty())
                .distinct()
                .toList();
    }
}
