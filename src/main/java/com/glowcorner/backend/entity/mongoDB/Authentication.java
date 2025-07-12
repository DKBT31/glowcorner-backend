package com.glowcorner.backend.entity.mongoDB;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "authentication")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Authentication {
    @Id
    String id;

    @Field("userID")
    String userID;

    String username;

    String passwordHash;
}
