package com.glowcorner.backend.entity.mongoDB;

import com.glowcorner.backend.entity.mongoDB.SkincareRoutine.SkinCareRoutine;
import com.glowcorner.backend.enums.SkinType;
import com.glowcorner.backend.enums.Role;
import com.glowcorner.backend.enums.Status.UserStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "user")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    String id;

    String userID;

    String fullName;

    String email;

    String phone;

    String address;

    SkinType skinType;

    int loyalPoints;

    Role role;

    String avatar_url;

    UserStatus status;

    //One to One
    Authentication authentication;

    //One to one
    Cart cart;

    //One to Many
    List<Order> orders;

    //One to one
    SkinCareRoutine skinCareRoutine;


}
