package project.dhc.domain.excel.dto;

import java.util.List;

public record CleaningExcelRow(int roomNumber, String name, List<String> dailyResults, boolean penalized) {
    public CleaningExcelRow {
        dailyResults = List.copyOf(dailyResults);
    }
}
