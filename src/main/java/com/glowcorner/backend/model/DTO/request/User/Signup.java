package com.glowcorner.backend.model.DTO.request.User;

import lombok.Data;

@Data
public class Signup {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phone;
}
