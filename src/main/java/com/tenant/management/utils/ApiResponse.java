package com.tenant.management.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@JsonPropertyOrder({"success", "message", "data", "errors"})
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse implements Serializable {

    @JsonIgnore
    private static final long serialVersionUID = 7702134516418120340L;

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("message")
    private String message;

    @JsonProperty("errors")
    private List<ErrorDetail> errors;

    @JsonProperty("data")
    private Object data;

    @JsonIgnore
    private HttpStatus status;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ErrorDetail {
        private String code;
        private String message;
        private String field;
    }

    public static ApiResponse successResponse(String message, Object data) {
        return ApiResponse.builder()
                .success(true)
                .message(message)
                .data(data)
                .status(HttpStatus.OK)
                .build();
    }

    public static ApiResponse errorResponse(String message, List<ErrorDetail> errors) {
        return ApiResponse.builder()
                .success(false)
                .message(message)
                .errors(errors)
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }

    public static ApiResponse errorResponse(String message) {
        return ApiResponse.builder()
                .success(false)
                .message(message)
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }
}
