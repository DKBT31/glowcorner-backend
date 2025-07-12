package com.glowcorner.backend.service.interfaces;

import com.glowcorner.backend.model.DTO.request.User.ChangePasswordRequest;
import com.glowcorner.backend.model.DTO.request.User.ForgotPasswordRequest;

public interface AuthenticationService {
    boolean login(String username, String password);

    boolean signup(String username, String password, String email, String fullName, String phone);

    String forgotPassword(ForgotPasswordRequest request);

    String changePassword(ChangePasswordRequest request);
}
