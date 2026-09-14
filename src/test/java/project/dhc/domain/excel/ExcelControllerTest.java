package project.dhc.domain.excel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import project.dhc.global.exception.GlobalExceptionHandler;
import project.dhc.global.exception.exceptions.InvalidWeekStartDateException;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ExcelControllerTest {
    private final ExcelService service = mock(ExcelService.class);
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new ExcelController(service))
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    void returnsDownloadHeadersAndBytes() throws Exception {
        byte[] file = {1, 2, 3};
        when(service.export(LocalDate.of(2026, 9, 14))).thenReturn(file);
        mvc.perform(post("/admin/excel").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"weekStartDate\":\"2026-09-14\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"cleaning-2026-09-14.xlsx\""))
                .andExpect(content().bytes(file));
    }

    @Test
    void rejectsMissingAndMalformedDates() throws Exception {
        for (String json : new String[]{"{}", "{\"weekStartDate\":\"invalid\"}"}) {
            mvc.perform(post("/admin/excel").contentType(MediaType.APPLICATION_JSON).content(json))
                    .andExpect(status().isBadRequest());
        }
        verifyNoInteractions(service);
    }

    @Test
    void returnsBadRequestForNonMonday() throws Exception {
        when(service.export(LocalDate.of(2026, 9, 15))).thenThrow(InvalidWeekStartDateException.EXCEPTION);
        mvc.perform(post("/admin/excel").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"weekStartDate\":\"2026-09-15\"}"))
                .andExpect(status().isBadRequest());
    }
}
