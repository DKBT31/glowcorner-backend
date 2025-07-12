package com.glowcorner.backend.service.implement;

import com.glowcorner.backend.entity.mongoDB.Authentication;
import com.glowcorner.backend.entity.mongoDB.Cart;
import com.glowcorner.backend.entity.mongoDB.PasswordResetToken;
import com.glowcorner.backend.entity.mongoDB.User;
import com.glowcorner.backend.enums.Role;
import com.glowcorner.backend.model.DTO.GoogleLoginDTO;
import com.glowcorner.backend.model.DTO.LoginDTO;
import com.glowcorner.backend.model.DTO.request.User.ChangePasswordRequest;
import com.glowcorner.backend.model.DTO.request.User.ForgotPasswordRequest;
import com.glowcorner.backend.repository.AuthenticationRepository;
import com.glowcorner.backend.repository.CartRepository;
import com.glowcorner.backend.repository.PasswordResetTokenRepository;
import com.glowcorner.backend.repository.UserRepository;
import com.glowcorner.backend.service.interfaces.AuthenticationService;
import com.glowcorner.backend.utils.JwtUtilHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImp implements AuthenticationService {

    private final AuthenticationRepository authenticationRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private CounterServiceImpl counterServiceImpl;
    @Autowired
    private CartRepository cartRepository;

    @Override
    public boolean login(String username, String password) {
        Optional<Authentication> authentication = authenticationRepository.findByUsername(username);
        return authentication.isPresent()
                && bCryptPasswordEncoder.matches(password, authentication.get().getPasswordHash());
    }

    @Override
    public boolean signup(String username, String password, String email, String fullName, String phone) {
        // Check if username already exists
        Optional<Authentication> existingUser = authenticationRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            return false;
        }

        // Check if email already exists
        Optional<User> existingEmail = userRepository.findByEmail(email);
        if (existingEmail.isPresent()) {
            return false;
        }

        String userID = counterServiceImpl.getNextUserID();

        User user = new User();
        user.setUserID(userID);
        user.setEmail(email);
        user.setFullName(fullName); // Set the full name
        user.setPhone(phone); // Set the phone
        user.setRole(Role.CUSTOMER); // Mặc định role là USER

        Authentication authentication = new Authentication();
        authentication.setUserID(userID);
        authentication.setUsername(username);
        authentication.setPasswordHash(bCryptPasswordEncoder.encode(password));
        authenticationRepository.save(authentication);

        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());
        cart.setUserID(userID);
        cartRepository.save(cart);
        userRepository.save(user);
        return true;
    }

    public String forgotPassword(ForgotPasswordRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (user.isEmpty()) {
            throw new RuntimeException("Email not found.");
        }

        // Tạo passcode 6 số
        String passcode = String.format("%06d", new Random().nextInt(999999));
        PasswordResetToken token = new PasswordResetToken(passcode, request.getEmail());

        // Xóa token cũ nếu tồn tại
        PasswordResetToken existingToken = passwordResetTokenRepository.findByEmail(request.getEmail());
        if (existingToken != null) {
            passwordResetTokenRepository.delete(existingToken);
        }

        // Lưu token mới
        passwordResetTokenRepository.save(token);

        // Gửi email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getEmail());
        message.setSubject("Password Reset Passcode");
        message.setText("Your passcode is: " + passcode + ". It expires in 15 minutes.");
        javaMailSender.send(message);

        return "Passcode sent to your email.";
    }

    public String changePassword(ChangePasswordRequest request) {
        PasswordResetToken token = passwordResetTokenRepository.findByToken(request.getPasscode());
        if (token == null || token.isExpired()) {
            throw new RuntimeException("Invalid or expired passcode.");
        }

        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (user.isEmpty()) {
            throw new RuntimeException("User not found.");
        }

        Authentication existingAuth = authenticationRepository.findByUserID(user.get().getUserID()).get();
        // Cập nhật mật khẩu
        existingAuth.setPasswordHash(bCryptPasswordEncoder.encode(request.getNewPassword()));
        authenticationRepository.save(existingAuth);

        // Xóa token sau khi sử dụng
        passwordResetTokenRepository.delete(token);

        return "Password changed successfully.";
    }

}
