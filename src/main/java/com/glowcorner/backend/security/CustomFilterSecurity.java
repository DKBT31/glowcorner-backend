package com.glowcorner.backend.security;

import com.glowcorner.backend.entity.mongoDB.User;
import com.glowcorner.backend.enums.Role;
import com.glowcorner.backend.repository.UserRepository;
import com.glowcorner.backend.utils.JwtUtilHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class CustomFilterSecurity {

    @Autowired
    private CustomJwtFilter customJwtFilter;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtilHelper jwtUtilHelper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        String[] publicUrls = {
                "/swagger-ui/**", "/swagger-ui.html", "/api-docs/**",
                "/swagger-ui-custom/**", "/swagger-ui-custom",
                "/favicon.ico",
                "/auth/login", "/auth/login/token/google",
                "/oauth2/authorization/google", "/login/oauth2/code/google", "/auth/login/google/**",
                "/auth/oauth2/callback",
                "/v3/api-docs/**",
                "/auth/signup", "/auth/register",
                "/auth/forgot-password",
                "/auth/change-password",
                "/api/customer/**",
                "/api/products/**",
                "/api/customer/products/filter",
                "/api/feedbacks",
                "/api/quizzes",
                "/api/promotions",
                "/api/user/**",
                "/api/skin-care-routines/skinType/**",
                "/api/payment/stripe/create-intent",
                "/api/cart/**",
                "/api/quizzes/**",
                "/api/orders/**",
                "/api/skin-care-routines/**",
                "/api/manager/**",
        };

        // String[] showUrls =
        // {"/api/manager/users/**","/api/products/**","/api/orders/**","/api/cart/**","/api/categories","/api/skin-care-routines/**","/api/promotions","/api/feedbacks/**","/api/quizzes/**"};
        String[] showUrls = { "/api/orders/customer/**", "/api/cart/**", "/api/categories",
                "/api/skin-care-routines/user/**" };
        String[] updateUrls = { "/api/cart/**", "/api/skin-care-routines/**", "/api/orders/staff/**" };
        String[] postUrls = { "/api/cart/**", "/api/orders/customer/**" };
        String[] postManagerUrls = { "/api/skin-care-routines" };
        String[] deleteUrls = { "/api/cart/**" };
        String[] showManagerUrls = { "/api/orders/manager", "/api/feedbacks/**", "/api/products/**",
                "/api/categories/**", "/api/skin-care-routines/**" };
        String[] showStaffUrls = { "/api/quizzes/**", "/api/answer-options/question/**" };
        String[] adminUrls = { "/api/manager/users/**" };

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(publicUrls).permitAll()
                .requestMatchers(HttpMethod.GET, showUrls).hasRole("CUSTOMER")
                .requestMatchers(HttpMethod.GET, showManagerUrls).hasAnyRole("MANAGER")
                .requestMatchers(HttpMethod.GET, showStaffUrls).hasAnyRole("STAFF")
                .requestMatchers(HttpMethod.PUT, updateUrls).hasAnyRole("CUSTOMER", "STAFF")
                .requestMatchers(HttpMethod.POST, postUrls).hasRole("CUSTOMER")
                .requestMatchers(HttpMethod.POST, postManagerUrls).hasRole("MANAGER")
                .requestMatchers(HttpMethod.DELETE, deleteUrls).hasRole("CUSTOMER")
                .requestMatchers(adminUrls).hasRole("MANAGER")
                .anyRequest().authenticated()
                .and()
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(authEndpoint -> authEndpoint
                                .authorizationRequestRepository(new HttpSessionOAuth2AuthorizationRequestRepository()))
                        .userInfoEndpoint(userInfo -> userInfo.oidcUserService(oidcUserService()))
                        .successHandler((request, response, authentication) -> {
                            OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
                            String email = oidcUser.getEmail();

                            Optional<User> user = userRepository.findByEmail(email);
                            if (user.isEmpty()) {
                                response.sendRedirect("/");
                                return;
                            }

                            String role = user.get().getRole().name();
                            String jwtToken = jwtUtilHelper.generateToken(email, role);

                            response.sendRedirect("/auth/oauth2/callback");
                        }));

        http.addFilterBefore(customJwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        OidcUserService delegate = new OidcUserService();
        return userRequest -> {
            OidcUser oidcUser = delegate.loadUser(userRequest);
            String email = oidcUser.getEmail();

            Optional<User> userOptional = userRepository.findByEmail(email);
            User user;
            if (userOptional.isEmpty()) {
                // Tạo user mới nếu không tồn tại
                user = new User();
                user.setEmail(email);
                user.setRole(Role.CUSTOMER);
                userRepository.save(user);
            } else {
                user = userOptional.get();
            }

            List<SimpleGrantedAuthority> authorities = oidcUser.getAuthorities().stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                    .collect(Collectors.toList());
            authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

            return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
        };
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://10.0.2.2:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
