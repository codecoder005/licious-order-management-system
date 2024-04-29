package com.licious.oms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionResponse {
    private String errorMessage;
    private LocalDateTime timestamp = LocalDateTime.now();
    private Integer statusCode;
    private Map<String, Object> errors;
}
