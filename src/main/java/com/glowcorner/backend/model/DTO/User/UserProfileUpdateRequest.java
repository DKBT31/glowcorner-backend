package com.glowcorner.backend.model.DTO.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserProfileUpdateRequest {

    String fullName;
    String email;
    String phone;
    String address;
    String avatar_url;
    String skinType; // Use String instead of SkinType enum to avoid Jackson issues

}
