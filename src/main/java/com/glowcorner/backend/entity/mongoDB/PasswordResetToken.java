package com.glowcorner.backend.entity.mongoDB;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "password_reset_tokens")
@Data
public class PasswordResetToken {
    @Id
    private String id;
    private String token; // Passcode 6 số
    private String email;
    private LocalDateTime expiryDate;

    public PasswordResetToken() {}

    public PasswordResetToken(String token, String email) {
        this.token = token;
        this.email = email;
        this.expiryDate = LocalDateTime.now().plusMinutes(15); // Hết hạn sau 15 phút
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}