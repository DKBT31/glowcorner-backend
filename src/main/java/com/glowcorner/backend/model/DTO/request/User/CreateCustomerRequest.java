package com.glowcorner.backend.model.DTO.request.User;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCustomerRequest {

    String fullName;
    String email;
    String phone;
    String address;
    String avatar_url;

}
