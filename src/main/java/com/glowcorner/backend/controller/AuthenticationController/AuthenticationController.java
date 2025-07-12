package com.glowcorner.backend.controller.AuthenticationController;

import com.glowcorner.backend.entity.mongoDB.Cart;
import com.glowcorner.backend.entity.mongoDB.User;
import com.glowcorner.backend.enums.Role;
import com.glowcorner.backend.model.DTO.LoginDTO;
import com.glowcorner.backend.model.DTO.request.User.ChangePasswordRequest;
import com.glowcorner.backend.model.DTO.request.User.ForgotPasswordRequest;
import com.glowcorner.backend.model.DTO.request.User.Signup;
import com.glowcorner.backend.model.DTO.response.ResponseData;
import com.glowcorner.backend.repository.AuthenticationRepository;
import com.glowcorner.backend.entity.mongoDB.Authentication;
import com.glowcorner.backend.repository.CartRepository;
import com.glowcorner.backend.repository.UserRepository;
import com.glowcorner.backend.service.implement.CounterServiceImpl;
import com.glowcorner.backend.service.interfaces.AuthenticationService;
import com.glowcorner.backend.utils.JwtUtilHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Authentication Management System", description = "Operations pertaining to authentication in the Authentication Management System")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final JwtUtilHelper jwtUtilHelper;
    private final AuthenticationRepository authenticationRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CartRepository cartRepository;
    private final CounterServiceImpl counterServiceImpl;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @Operation(summary = "Login", description = "Authenticate user credentials", security = {})
    @PostMapping("/login")
    public ResponseEntity<ResponseData> login(@RequestBody LoginDTO loginDTO) {

        // 1️⃣ Try to find user by email first (mobile app sends email as username)
        Optional<User> userOpt = userRepository.findByEmail(loginDTO.getUsername());
        Authentication auth = null;

        if (userOpt.isPresent()) {
            // Found user by email, now find corresponding authentication record
            User user = userOpt.get();
            Optional<Authentication> authOpt = authenticationRepository.findByUserID(user.getUserID());
            if (authOpt.isPresent()) {
                auth = authOpt.get();
            }
        } else {
            // Fallback: try to find by username (for backward compatibility)
            Optional<Authentication> authOpt = authenticationRepository.findByUsername(loginDTO.getUsername());
            if (authOpt.isPresent()) {
                auth = authOpt.get();
                userOpt = userRepository.findByUserID(auth.getUserID());
            }
        }

        if (auth == null || userOpt.isEmpty()) {
            return ResponseUtil.error(HttpStatus.UNAUTHORIZED.value(), "User not found");
        }

        // 2️⃣ Kiểm tra password có đúng không
        if (!bCryptPasswordEncoder.matches(loginDTO.getPassword(), auth.getPasswordHash())) {
            return ResponseUtil.error(HttpStatus.UNAUTHORIZED.value(), "Invalid password");
        }

        // 3️⃣ Get user information
        User user = userOpt.get();

        // 4️⃣ Tạo JWT token
        String email = user.getEmail() != null ? user.getEmail() : "N/A";
        String role = user.getRole().name();
        String jwtToken = jwtUtilHelper.generateToken(email, role);

        // 5️⃣ Trả về thông tin user và token
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("email", email);
        responseData.put("fullName", user.getFullName());
        responseData.put("role", role);
        responseData.put("userID", user.getUserID());
        responseData.put("jwtToken", jwtToken);

        return ResponseUtil.success(responseData);
    }

    @Operation(summary = "Login with Google", description = "Authenticate user credentials with Google", security = {})
    @GetMapping("/login/google")
    public ResponseEntity<ResponseData> loginWithGoogle(@RequestParam String email) {
        // 1️⃣ Kiểm tra email trong userRepository
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseUtil.error(HttpStatus.UNAUTHORIZED.value(), "User not found");
        }

        User user = userOpt.get();

        // 2️⃣ Tạo JWT token
        String role = user.getRole().name();
        String jwtToken = jwtUtilHelper.generateToken(email, role);

        // 3️⃣ Trả về thông tin user và token
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("email", email);
        responseData.put("fullName", user.getFullName());
        responseData.put("role", role);
        responseData.put("jwtToken", jwtToken);

        return ResponseUtil.success(responseData);
    }

    @Operation(summary = "Signup", description = "Create a new user", security = {})
    @PostMapping("/signup")
    public ResponseEntity<ResponseData> signup(@RequestBody Signup signup) {
        // Check if username already exists
        Optional<Authentication> existingUser = authenticationRepository.findByUsername(signup.getUsername());
        if (existingUser.isPresent()) {
            return ResponseUtil.error(HttpStatus.BAD_REQUEST.value(), "Username already exists");
        }

        // Check if email already exists
        Optional<User> existingEmail = userRepository.findByEmail(signup.getEmail());
        if (existingEmail.isPresent()) {
            return ResponseUtil.error(HttpStatus.BAD_REQUEST.value(), "Email already exists");
        }

        boolean isCreated = authenticationService.signup(signup.getUsername(), signup.getPassword(), signup.getEmail(),
                signup.getFullName(), signup.getPhone());
        if (!isCreated) {
            return ResponseUtil.error(HttpStatus.BAD_REQUEST.value(), "Failed to create user");
        }

        return ResponseUtil.success("User created successfully");
    }

    @Operation(summary = "Register (alias for signup)", description = "Create a new user - mobile compatible endpoint", security = {})
    @PostMapping("/register")
    public ResponseEntity<ResponseData> register(@RequestBody Signup signup) {
        return signup(signup); // Delegate to existing signup method
    }

    @GetMapping("/oauth2/callback")
    public ResponseEntity<?> oauth2Callback(@RequestParam("code") String code) {
        try {
            Map<String, String> tokenResponse = getGoogleAccessToken(code);
            if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
                return ResponseUtil.error(HttpStatus.UNAUTHORIZED.value(), "Failed to obtain access token");
            }

            String accessToken = tokenResponse.get("access_token");
            Map<String, Object> userInfo = getGoogleUserInfo(accessToken);
            if (userInfo == null || !userInfo.containsKey("email")) {
                return ResponseUtil.error(HttpStatus.UNAUTHORIZED.value(), "Failed to obtain user info");
            }

            String email = (String) userInfo.get("email");
            String fullName = userInfo.containsKey("name") ? (String) userInfo.get("name") : "N/A";
            User user = findOrCreateUser(email, fullName);

            String role = user.getRole().name();
            String userID = user.getUserID();
            String jwtToken = jwtUtilHelper.generateToken(email, role);

            String frontendRedirectUrl = buildFrontendRedirectUrl(jwtToken, role, email, fullName, userID);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(frontendRedirectUrl))
                    .build();

        } catch (Exception e) {
            return ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error during Google login: " + e.getMessage());
        }
    }

    private Map<String, String> getGoogleAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> tokenRequest = new HashMap<>();
        tokenRequest.put("code", code);
        tokenRequest.put("client_id", clientId);
        tokenRequest.put("client_secret", clientSecret);
        tokenRequest.put("redirect_uri", redirectUri);
        tokenRequest.put("grant_type", "authorization_code");

        return restTemplate.postForObject("https://oauth2.googleapis.com/token", tokenRequest, Map.class);
    }

    private Map<String, Object> getGoogleUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + accessToken;
        return restTemplate.getForObject(userInfoUrl, Map.class);
    }

    private User findOrCreateUser(String email, String fullName) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            User user = new User();
            user.setUserID(counterServiceImpl.getNextUserID());
            user.setEmail(email);
            user.setFullName(fullName);
            user.setRole(Role.CUSTOMER);
            Cart cart = new Cart();
            cart.setItems(new ArrayList<>());
            cart.setUserID(user.getUserID());
            user.setCart(cart);
            cartRepository.save(cart);
            return userRepository.save(user);
        }
        return userOpt.get();
    }

    private String buildFrontendRedirectUrl(String jwtToken, String role, String email, String fullName, String userID)
            throws Exception {
        String encodedJwtToken = URLEncoder.encode(jwtToken, StandardCharsets.UTF_8.toString());
        String encodedRole = URLEncoder.encode(role, StandardCharsets.UTF_8.toString());
        String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8.toString());
        String encodedFullName = URLEncoder.encode(fullName, StandardCharsets.UTF_8.toString());
        String encodedUserId = URLEncoder.encode(userID, StandardCharsets.UTF_8.toString());

        return UriComponentsBuilder
                .fromUriString("http://localhost:3000/callback")
                .queryParam("jwtToken", encodedJwtToken)
                .queryParam("role", encodedRole)
                .queryParam("email", encodedEmail)
                .queryParam("fullName", encodedFullName)
                .queryParam("userID", encodedUserId)
                .build()
                .toUriString();
    }

    private static class ResponseUtil {
        static ResponseEntity<ResponseData> success(Object data) {
            return ResponseEntity.ok(new ResponseData(200, true, "Success", data, null, null));
        }

        static ResponseEntity<ResponseData> error(int status, String message) {
            return ResponseEntity.status(status).body(new ResponseData(status, false, message, null, null, null));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseData> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            String response = authenticationService.forgotPassword(request);
            return ResponseEntity.ok(new ResponseData(200, true, response, null, null, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseData(400, false, e.getMessage(), null, null, null));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<ResponseData> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            String response = authenticationService.changePassword(request);
            return ResponseEntity.ok(new ResponseData(200, true, response, null, null, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseData(400, false, e.getMessage(), null, null, null));
        }
    }
}