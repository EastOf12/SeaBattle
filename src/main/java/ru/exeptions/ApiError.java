package ru.exeptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

import static ru.util.DateConstant.DATE_TIME_PATTERN;

@Data
@Builder
public class ApiError {
    private final String message;
    private final String reason;
    private final String status;
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private final LocalDateTime timestamp;
}
