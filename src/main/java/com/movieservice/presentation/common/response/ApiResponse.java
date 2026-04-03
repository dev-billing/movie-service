package com.movieservice.presentation.common.response;

import org.springframework.http.HttpStatus;

public record ApiResponse<T>(
        int code,
        String message,
        T data
) {
    public static ApiResponse<Void> ok() {
        return new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.name(), null);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.name(), data);
    }

    public static ApiResponse<?> fail(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }
}

