package com.glowcorner.backend.model.mapper.CreateMapper.Product;


import com.glowcorner.backend.entity.mongoDB.Product;
import com.glowcorner.backend.enums.Status.ProductStatus;
import com.glowcorner.backend.model.DTO.request.Product.CreateProductRequest;
import com.glowcorner.backend.service.implement.CounterServiceImpl;
import org.springframework.stereotype.Component;


@Component
public class CreateProductRequestMapper {


   private final CounterServiceImpl counterService;


   public CreateProductRequestMapper(CounterServiceImpl counterService) {
       this.counterService = counterService;
   }


   public Product fromCreateRequest(CreateProductRequest request) {
       if (request == null) {
           return null;
       }


       Product product = new Product();
       product.setProductID(counterService.getNextProductID());
       product.setProductName(request.getProductName());
       product.setDescription(request.getDescription());
       product.setPrice(request.getPrice());
       product.setCategory(request.getCategory());
       product.setSkinTypes(request.getSkinTypes());
       product.setRating(request.getRating());
       product.setImage_url(request.getImage_url());
       product.setStatus(ProductStatus.ACTIVE);


       return product;
   }
}
