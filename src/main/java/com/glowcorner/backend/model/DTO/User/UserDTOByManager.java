package com.glowcorner.backend.model.DTO.User;


import com.glowcorner.backend.entity.mongoDB.Order;
import com.glowcorner.backend.entity.mongoDB.SkincareRoutine.SkinCareRoutine;
import com.glowcorner.backend.enums.SkinType;
import com.glowcorner.backend.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserDTOByManager {

    String userID;
    String fullName;
    String email;
    String phone;
    String address;
    SkinType skinType;
    int loyalPoints;

    Role role;

    String avatar_url;

    List<Order> orders;

    SkinCareRoutine skinCareRoutine;
}
