package com.example.board.common.util.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiUtils {
    private ApiUtils() {

    }
    public static <T> ResponseEntity<ApiResult<T>> success(T response, HttpStatus status) {
        return new ResponseEntity<>(new ApiResult<>(response, null), status);
    }

    public static ResponseEntity<ApiResult<Void>> error(Exception e, HttpStatus status) {
        return new ResponseEntity<>(new ApiResult<>(null, new ApiError(e.getMessage())), status);
    }

    @Getter
    public static class ApiResult<T> {
        private final T response;
        private final ApiError error;

        public ApiResult(T response, ApiError error) {
            this.response = response;
            this.error = error;
        }
    }

    @Getter
    public static class ApiError {
        private String message;

        public ApiError(String message) {
            this.message = message;
        }
    }
}
