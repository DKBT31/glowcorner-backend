package com.glowcorner.backend.entity.mongoDB;


import com.glowcorner.backend.enums.Status.FeedbackStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDate;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "feedback")
public class Feedback {


   @Id
   String id;


   String feedbackID;


   String customerID; // Refers to the userID in the User collection (foreign key in SQL)


   int rating; // Rating is represented as an integer


   String comment; // Comment is optional and maps to NVARCHAR(255)


   LocalDate feedbackDate; // Maps to the SQL DATE type

   FeedbackStatus status;

}
