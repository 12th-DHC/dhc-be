package project.dhc.domain.excel;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.dhc.domain.excel.dto.ExcelRequest;

@RestController
@RequestMapping("/admin/excel")
@RequiredArgsConstructor
public class ExcelController {
    private final ExcelService excelService;

    @PostMapping(produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> export(@Valid @RequestBody ExcelRequest request) {
        byte[] file = excelService.export(request.getWeekStartDate());
        String filename = "cleaning-" + request.getWeekStartDate() + ".xlsx";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(filename).build().toString())
                .contentLength(file.length)
                .body(file);
    }
}
