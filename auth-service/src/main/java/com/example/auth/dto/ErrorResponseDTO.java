package com.example.auth.dto;

import lombok.Data;

@Data
public class ErrorResponseDTO {
    private Integer code;
    private String message;
}
