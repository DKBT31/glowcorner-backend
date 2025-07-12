package com.glowcorner.backend.entity.mongoDB.SkincareRoutine;


import com.glowcorner.backend.entity.mongoDB.Product;
import com.glowcorner.backend.enums.SkinType;
import com.glowcorner.backend.enums.Status.RoutineStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.List;


@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "skincareroutine")
public class SkinCareRoutine {


   @Id
   String id;


   String routineID;


   SkinType skinType;


   String routineName;


   String routineDescription;


   RoutineStatus status;


   List<Product> products;
}
