package project.dhc.domain.excel;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.data.domain.Sort;
import project.dhc.domain.cleaning.CleaningCheck;
import project.dhc.domain.cleaning.CleaningCheckRepository;
import project.dhc.domain.user.entity.Room;
import project.dhc.domain.user.repository.RoomRepository;
import project.dhc.global.exception.exceptions.InvalidWeekStartDateException;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExcelServiceTest {
    private static final LocalDate MONDAY = LocalDate.of(2026, 9, 14);
    private final RoomRepository rooms = mock(RoomRepository.class);
    private final CleaningCheckRepository checks = mock(CleaningCheckRepository.class);
    private final ExcelService service = new ExcelService(rooms, checks, new CleaningExcelGenerator());
    private Room room;

    @BeforeEach
    void setUp() {
        room = new Room();
        room.setRoomNumber(101);
        room.setAName("학생A");
        room.setBName("학생B");
        when(rooms.findAll(Sort.by("roomNumber"))).thenReturn(List.of(room));
    }

    @ParameterizedTest
    @CsvSource({
            "1/2,       false",
            "1/2/5,     true",
            "1/2/3,     false",
            "1/2/4,     false",
            "1/2/3/4,   true"
    })
    void calculatesPenaltyWithOnlyOneWeeklyExemption(String reasons, boolean expected) throws Exception {
        when(checks.findWeeklyChecks(MONDAY, MONDAY.plusDays(4))).thenReturn(List.of(
                check(0, reasons, null, null, null)));
        try (var workbook = openExport()) {
            var penalty = workbook.getSheetAt(0).getRow(3).getCell(7);
            if (expected) {
                assertEquals(-1, penalty.getNumericCellValue());
            } else {
                assertEquals("", penalty.getStringCellValue());
            }
        }
    }

    @Test
    void displaysExampleAndCountsIndividualAsOneOnTuesdayAndFridayOnly() throws Exception {
        when(checks.findWeeklyChecks(MONDAY, MONDAY.plusDays(4))).thenReturn(List.of(
                check(0, "1/2/3", "1/2/3", null, "1"),
                check(1, "1/2", "2/3", null, "1/2/3"),
                check(4, null, null, null, "2")));
        try (var workbook = openExport()) {
            var sheet = workbook.getSheetAt(0);
            assertEquals("1/2/3", sheet.getRow(3).getCell(2).getStringCellValue());
            assertEquals("1/2/x", sheet.getRow(3).getCell(3).getStringCellValue());
            assertEquals(-1, sheet.getRow(3).getCell(7).getNumericCellValue());
            assertEquals("", sheet.getRow(4).getCell(2).getStringCellValue());
            assertEquals("x", sheet.getRow(4).getCell(3).getStringCellValue());
            assertEquals("x", sheet.getRow(4).getCell(6).getStringCellValue());
            assertEquals("", sheet.getRow(4).getCell(7).getStringCellValue());
        }
    }

    @Test
    void exemptionIsNotRepeatedAcrossDays() throws Exception {
        when(checks.findWeeklyChecks(MONDAY, MONDAY.plusDays(4))).thenReturn(List.of(
                check(0, "3/4", null, null, null),
                check(1, "3/4", null, null, null)));
        try (var workbook = openExport()) {
            assertEquals(-1, workbook.getSheetAt(0).getRow(3).getCell(7).getNumericCellValue());
        }
    }

    @Test
    void rendersFiveFloorsAndBlankDaysWithoutRecords() throws Exception {
        Room fifthFloor = new Room();
        fifthFloor.setRoomNumber(501);
        fifthFloor.setAName("5층학생");
        when(rooms.findAll(Sort.by("roomNumber"))).thenReturn(List.of(room, fifthFloor));
        when(checks.findWeeklyChecks(MONDAY, MONDAY.plusDays(4))).thenReturn(List.of());
        try (var workbook = openExport()) {
            var sheet = workbook.getSheetAt(0);
            assertTrue(sheet.getRow(0).getCell(0).getStringCellValue().contains("2026-09-14"));
            for (int floor = 0; floor < 5; floor++) {
                assertEquals((floor + 1) + "층", sheet.getRow(1).getCell(floor * 9).getStringCellValue());
                assertEquals("벌점", sheet.getRow(2).getCell(floor * 9 + 7).getStringCellValue());
            }
            assertEquals("501", sheet.getRow(3).getCell(36).getStringCellValue());
            assertEquals("5층학생", sheet.getRow(3).getCell(37).getStringCellValue());
            assertEquals("", sheet.getRow(3).getCell(2).getStringCellValue());
            assertEquals("", sheet.getRow(3).getCell(7).getStringCellValue());
            assertTrue(sheet.getMergedRegions().stream().anyMatch(region -> region.formatAsString().equals("A4:A5")));
        }
    }

    @Test
    void rejectsMissingOrNonMondayDateBeforeQuerying() {
        assertThrows(InvalidWeekStartDateException.class, () -> service.export(null));
        assertThrows(InvalidWeekStartDateException.class, () -> service.export(MONDAY.plusDays(1)));
        verifyNoInteractions(checks);
    }

    @Test
    void includesCleaningResultsEvenWhenStudentNamesAreMissing() throws Exception {
        room.setAName(null);
        room.setBName("");
        when(checks.findWeeklyChecks(MONDAY, MONDAY.plusDays(4))).thenReturn(List.of(
                check(0, "1/2/3", null, null, null),
                check(1, "1/2", "2/3", null, "1/2/3")));
        try (var workbook = openExport()) {
            var sheet = workbook.getSheetAt(0);
            assertEquals("", sheet.getRow(3).getCell(1).getStringCellValue());
            assertEquals("1/2/3", sheet.getRow(3).getCell(2).getStringCellValue());
            assertEquals("1/2/x", sheet.getRow(3).getCell(3).getStringCellValue());
            assertEquals(-1, sheet.getRow(3).getCell(7).getNumericCellValue());
            assertEquals("", sheet.getRow(4).getCell(1).getStringCellValue());
            assertEquals("x", sheet.getRow(4).getCell(3).getStringCellValue());
        }
    }

    @Test
    void alwaysReservesTwoRowsForRoomWithoutNamesOrChecks() throws Exception {
        room.setAName(null);
        room.setBName(null);
        when(checks.findWeeklyChecks(MONDAY, MONDAY.plusDays(4))).thenReturn(List.of());
        try (var workbook = openExport()) {
            var sheet = workbook.getSheetAt(0);
            assertEquals("", sheet.getRow(3).getCell(1).getStringCellValue());
            assertEquals("", sheet.getRow(4).getCell(1).getStringCellValue());
            assertTrue(sheet.getMergedRegions().stream()
                    .anyMatch(region -> region.formatAsString().equals("A4:A5")));
        }
    }

    private CleaningCheck check(int day, String normalA, String individualA, String normalB, String individualB) {
        return CleaningCheck.builder().recordId((long) day).date(MONDAY.plusDays(day)).room(room)
                .aNotpassReason(normalA).aIndNotpassReason(individualA)
                .bNotpassReason(normalB).bIndNotpassReason(individualB).build();
    }

    private XSSFWorkbook openExport() throws Exception {
        return new XSSFWorkbook(new ByteArrayInputStream(service.export(MONDAY)));
    }
}
