package com.glowcorner.backend.model.DTO.request.User;

import com.glowcorner.backend.enums.SkinType;
import com.glowcorner.backend.enums.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserRequest {

    String fullName;
    String email;
    String phone;
    String address;
    Role role;
    SkinType skinType;
    String avatar_url;

}
