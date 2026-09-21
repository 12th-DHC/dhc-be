package project.dhc.domain.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import project.dhc.domain.excel.dto.CleaningExcelRow;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Component
public class CleaningExcelGenerator {
    private static final String[] HEADERS = {"호실", "이름", "월", "화", "수", "목", "금", "벌점"};

    public byte[] generate(LocalDate weekStartDate, List<CleaningExcelRow> rows) {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("주간 청소 결과");
            CellStyle body = createStyle(workbook, false);
            CellStyle header = createStyle(workbook, true);
            CellStyle title = workbook.createCellStyle();
            title.cloneStyleFrom(header);
            Font titleFont = workbook.createFont();
            titleFont.setFontName("맑은 고딕");
            titleFont.setFontHeightInPoints((short) 16);
            titleFont.setBold(true);
            title.setFont(titleFont);
            cell(sheet, 0, 0, "청결호실 점검 결과표 (" + weekStartDate + " ~ " + weekStartDate.plusDays(4) + ")", title);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 43));
            sheet.getRow(0).setHeightInPoints(30);

            int lastRow = 3;
            for (int floor = 1; floor <= 5; floor++) {
                int startColumn = (floor - 1) * 9;
                int currentFloor = floor;
                List<CleaningExcelRow> floorRows = rows.stream()
                        .filter(row -> row.roomNumber() / 100 == currentFloor)
                        .sorted(Comparator.comparingInt(CleaningExcelRow::roomNumber))
                        .toList();
                cell(sheet, 1, startColumn, floor + "층", header);
                sheet.addMergedRegion(new CellRangeAddress(1, 1, startColumn, startColumn + 7));
                for (int col = 0; col < HEADERS.length; col++) {
                    cell(sheet, 2, startColumn + col, HEADERS[col], header);
                    sheet.setColumnWidth(startColumn + col, (col == 1 ? 10 : col >= 2 && col <= 6 ? 13 : 6) * 256);
                }
                if (floor < 5) {
                    sheet.setColumnWidth(startColumn + 8, 2 * 256);
                }
                int rowIndex = 3;
                int roomStartRow = 3;
                Integer previousRoom = null;
                for (CleaningExcelRow result : floorRows) {
                    if (previousRoom != null && previousRoom != result.roomNumber()) {
                        mergeRoom(sheet, roomStartRow, rowIndex - 1, startColumn);
                        roomStartRow = rowIndex;
                    }
                    for (int col = 0; col < 8; col++) {
                        cell(sheet, rowIndex, startColumn + col, "", body);
                    }
                    cell(sheet, rowIndex, startColumn, String.valueOf(result.roomNumber()), body);
                    cell(sheet, rowIndex, startColumn + 1, result.name(), body);
                    for (int day = 0; day < 5; day++) {
                        cell(sheet, rowIndex, startColumn + 2 + day, result.dailyResults().get(day), body);
                    }
                    if (result.penalized()) {
                        sheet.getRow(rowIndex).getCell(startColumn + 7).setCellValue(-1);
                    }
                    sheet.getRow(rowIndex).setHeightInPoints(24);
                    previousRoom = result.roomNumber();
                    rowIndex++;
                }
                mergeRoom(sheet, roomStartRow, rowIndex - 1, startColumn);
                lastRow = Math.max(lastRow, rowIndex - 1);
            }
            sheet.createFreezePane(0, 3);
            sheet.setDisplayGridlines(false);
            sheet.setFitToPage(true);
            sheet.setHorizontallyCenter(true);
            sheet.getPrintSetup().setLandscape(true);
            sheet.getPrintSetup().setPaperSize(PrintSetup.A3_PAPERSIZE);
            sheet.getPrintSetup().setFitWidth((short) 1);
            sheet.getPrintSetup().setFitHeight((short) 0);
            sheet.setRepeatingRows(new CellRangeAddress(0, 2, -1, -1));
            workbook.setPrintArea(0, 0, 43, 0, lastRow);
            workbook.write(output);
            return output.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException("엑셀 파일 생성에 실패했습니다.", e);
        }
    }

    private CellStyle createStyle(Workbook workbook, boolean bold) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        Font font = workbook.createFont();
        font.setFontName("맑은 고딕");
        font.setFontHeightInPoints((short) 10);
        font.setBold(bold);
        style.setFont(font);
        if (bold) {
            style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        return style;
    }

    private void cell(Sheet sheet, int rowIndex, int column, String value, CellStyle style) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }
        Cell cell = row.getCell(column);
        if (cell == null) {
            cell = row.createCell(column);
        }
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void mergeRoom(Sheet sheet, int firstRow, int lastRow, int column) {
        if (lastRow > firstRow) {
            sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, column, column));
        }
    }
}
