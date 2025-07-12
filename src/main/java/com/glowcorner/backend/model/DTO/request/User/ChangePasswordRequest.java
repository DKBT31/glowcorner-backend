package com.glowcorner.backend.model.DTO.request.User;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String email;
    private String passcode;
    private String newPassword;
}
