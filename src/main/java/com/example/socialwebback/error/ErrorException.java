package com.example.socialwebback.error;

import com.example.socialwebback.dto.Response;
import lombok.Getter;

@Getter
public class ErrorException implements Response {

    private final String code;

    private final String message;

    public ErrorException(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
