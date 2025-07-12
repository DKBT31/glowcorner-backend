package com.glowcorner.backend.model.DTO.request.User;

import lombok.Data;

@Data
public class ForgotPasswordRequest {
    private String email;
}
