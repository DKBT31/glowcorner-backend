package com.glowcorner.backend.entity.mongoDB;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "promotion")
public class Promotion {

    @Id
    String id; // Primary key for the promotion entity

    String promotionID;

    List<String> productIDs;

    String promotionName;

    int discount;

    LocalDate startDate; // Maps SQL DATE type for the promotion start date

    LocalDate endDate; // Maps SQL DATE type for the promotion end date


}