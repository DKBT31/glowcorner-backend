package com.glowcorner.backend.service.implement;


import com.glowcorner.backend.entity.mongoDB.Product;
import com.glowcorner.backend.entity.mongoDB.Promotion;
import com.glowcorner.backend.enums.Category;
import com.glowcorner.backend.enums.SkinType;
import com.glowcorner.backend.enums.Status.ProductStatus;
import com.glowcorner.backend.model.DTO.ProductDTO;
import com.glowcorner.backend.model.DTO.request.Product.CreateProductRequest;
import com.glowcorner.backend.model.mapper.CreateMapper.Product.CreateProductRequestMapper;
import com.glowcorner.backend.model.mapper.Product.ProductMapper;
import com.glowcorner.backend.repository.ProductRepository;
import com.glowcorner.backend.repository.PromotionRepository;
import com.glowcorner.backend.service.interfaces.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProductServiceImp implements ProductService {


   private final ProductRepository productRepository;


   private final CreateProductRequestMapper createProductRequestMapper;


   private final ProductMapper productMapper;


   private final PromotionRepository promotionRepository;


   @Autowired
   private MongoTemplate mongoTemplate;


   public ProductServiceImp(ProductRepository productRepository, CreateProductRequestMapper createProductRequestMapper, ProductMapper productMapper, PromotionRepository promotionRepository) {
       this.productRepository = productRepository;
       this.createProductRequestMapper = createProductRequestMapper;
       this.productMapper = productMapper;
       this.promotionRepository = promotionRepository;
   }


   // Get all products
   @Override
   public List<ProductDTO> getAllProducts() {
       List<Product> products = productRepository.findAll();
       return products.stream()
               .map(product -> {
                   ProductDTO productDTO = productMapper.toDTO(product);
                   calculateDiscountedPrice(productDTO);
                   return productDTO;
               })
               .collect(Collectors.toList());
   }


   // Get product by ID
   @Override
   public ProductDTO getProductById(String productId) {
       Product product = productRepository.findByProductID(productId)
               .orElseThrow(() -> new RuntimeException("Product not found"));
       ProductDTO productDTO = productMapper.toDTO(product);
       calculateDiscountedPrice(productDTO);
       return productDTO;
   }


   // Get products by skin types
   @Override
   public List<ProductDTO> getProductsBySkinType(SkinType skinType) {
       List<Product> products = productRepository.findBySkinTypesIn(skinType);
       return products.stream()
               .map(product -> {
                   ProductDTO productDTO = productMapper.toDTO(product);
                   calculateDiscountedPrice(productDTO);
                   return productDTO;
               })
               .collect(Collectors.toList());
   }


   // Get products by category
   @Override
   public List<ProductDTO> getProductsByCategory(Category category) {
       List<Product> products = productRepository.findByCategory(category);
       return products.stream()
               .map(product -> {
                   ProductDTO productDTO = productMapper.toDTO(product);
                   calculateDiscountedPrice(productDTO);
                   return productDTO;
               })
               .collect(Collectors.toList());
   }


   // Get products by product name
   @Override
   public List<ProductDTO> getProductsByProductName(String productName) {
       List<Product> products = productRepository.findByProductNameContainingIgnoreCase(productName);
       return products.stream()
               .map(product -> {
                   ProductDTO productDTO = productMapper.toDTO(product);
                   calculateDiscountedPrice(productDTO);
                   return productDTO;
               })
               .collect(Collectors.toList());
   }


   // Get products by filter
   @Override
   public List<ProductDTO> getProductsByFilter(
           List<SkinType> skinTypes,
           List<Category> categories,
           Long minPrice,
           Long maxPrice
   ) {
       Query query = new Query();


       // Lọc theo skinType (nếu có)
       if (skinTypes != null && !skinTypes.isEmpty()) {
           query.addCriteria(Criteria.where("skinType").in(skinTypes));
       }


       // Lọc theo category (nếu có)
       if (categories != null && !categories.isEmpty()) {
           query.addCriteria(Criteria.where("category").in(categories));
       }


       // Lọc theo price range (kết hợp gte và lte trong một Criteria)
       if (minPrice != null || maxPrice != null) {
           Criteria priceCriteria = Criteria.where("price");
           if (minPrice != null) {
               priceCriteria.gte(minPrice);
           }
           if (maxPrice != null) {
               priceCriteria.lte(maxPrice);
           }
           query.addCriteria(priceCriteria);
       }


       // Thực hiện truy vấn
       List<Product> products = mongoTemplate.find(query, Product.class);
       return products.stream()
               .map(product -> {
                   ProductDTO productDTO = productMapper.toDTO(product);
                   calculateDiscountedPrice(productDTO);
                   return productDTO;
               })
               .collect(Collectors.toList());
   }


   // Create product
   @Override
   public ProductDTO createProduct(CreateProductRequest request) {
       Product product = createProductRequestMapper.fromCreateRequest(request);
       product = productRepository.save(product);
       return productMapper.toDTO(product);
   }


   // Update product
   @Override
   public ProductDTO updateProduct(String productId, ProductDTO productDTO) {
       try {
           // Find existing product
           Product existingProduct = productRepository.findByProductID(productId)
                   .orElseThrow(() -> new RuntimeException("Product not found"));


           // Update
           if (productDTO.getProductName() != null) existingProduct.setProductName(productDTO.getProductName());
           if (productDTO.getDescription() != null) existingProduct.setDescription(productDTO.getDescription());
           if (productDTO.getPrice() != null) existingProduct.setPrice(productDTO.getPrice());
           if (productDTO.getSkinTypes() != null) existingProduct.setSkinTypes(productDTO.getSkinTypes());
           if (productDTO.getCategory() != null) existingProduct.setCategory(productDTO.getCategory());
           if (productDTO.getRating() != null) existingProduct.setRating(productDTO.getRating());
           if (productDTO.getImage_url() != null) existingProduct.setImage_url(productDTO.getImage_url());


           // Save update
           Product updatedProduct = productRepository.save(existingProduct);


           // Convert updated product entity to DTO
           return productMapper.toDTO(updatedProduct);
       } catch (Exception e) {
           throw new RuntimeException("Fail to update product: " + e.getMessage(), e);
       }
   }


   // Delete product
   @Override
   public void deleteProduct(String productId) {
       Product existingProduct = productRepository.findByProductID(productId)
               .orElseThrow(() -> new RuntimeException("Product not found"));
       existingProduct.setStatus(ProductStatus.DISABLE);
       productRepository.save(existingProduct);
   }


   /* Customer */

   // Get all active products
   @Override
   public List<ProductDTO> getAllActiveProducts() {
       List<Product> products = productRepository.findAll()
               .stream()
               .filter(product -> product.getStatus() == ProductStatus.ACTIVE)
               .toList();
       return products.stream()
               .map(product -> {
                   ProductDTO productDTO = productMapper.toDTO(product);
                   calculateDiscountedPrice(productDTO);
                   return productDTO;
               })
               .collect(Collectors.toList());
   }

    // Get active product by ID
    @Override
    public ProductDTO getActiveProductById(String productId) {
        Product product = productRepository.findByProductID(productId)
                .filter(p -> p.getStatus() == ProductStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Product not found or not active"));
        ProductDTO productDTO = productMapper.toDTO(product);
        calculateDiscountedPrice(productDTO);
        return productDTO;
    }

    // Get active products by skin types
    @Override
    public List<ProductDTO> getActiveProductsBySkinType(SkinType skinType) {
        List<Product> products = productRepository.findBySkinTypesIn(skinType)
                .stream()
                .filter(product -> product.getStatus() == ProductStatus.ACTIVE)
                .toList();
        return products.stream()
                .map(product -> {
                    ProductDTO productDTO = productMapper.toDTO(product);
                    calculateDiscountedPrice(productDTO);
                    return productDTO;
                })
                .collect(Collectors.toList());
    }

    // Get active products by category
    @Override
    public List<ProductDTO> getActiveProductsByCategory(Category category) {
        List<Product> products = productRepository.findByCategory(category)
                .stream()
                .filter(product -> product.getStatus() == ProductStatus.ACTIVE)
                .toList();
        return products.stream()
                .map(product -> {
                    ProductDTO productDTO = productMapper.toDTO(product);
                    calculateDiscountedPrice(productDTO);
                    return productDTO;
                })
                .collect(Collectors.toList());
    }

    // Get active products by product name
    @Override
    public List<ProductDTO> getActiveProductsByProductName(String productName) {
        List<Product> products = productRepository.findByProductNameContainingIgnoreCase(productName)
                .stream()
                .filter(product -> product.getStatus() == ProductStatus.ACTIVE)
                .toList();
        return products.stream()
                .map(product -> {
                    ProductDTO productDTO = productMapper.toDTO(product);
                    calculateDiscountedPrice(productDTO);
                    return productDTO;
                })
                .collect(Collectors.toList());
    }

    // Get active products by filter
    @Override
    public List<ProductDTO> getActiveProductsByFilter(
            List<SkinType> skinTypes,
            List<Category> categories,
            Long minPrice,
            Long maxPrice
    ) {
        Query query = new Query();

        // Filter by skinType (if any)
        if (skinTypes != null && !skinTypes.isEmpty()) {
            query.addCriteria(Criteria.where("skinTypes").in(skinTypes));
        }

        // Filter by category (if any)
        if (categories != null && !categories.isEmpty()) {
            query.addCriteria(Criteria.where("category").in(categories));
        }

        // Filter by price range (combine gte and lte in one Criteria)
        if (minPrice != null || maxPrice != null) {
            Criteria priceCriteria = Criteria.where("price");
            if (minPrice != null) {
                priceCriteria.gte(minPrice);
            }
            if (maxPrice != null) {
                priceCriteria.lte(maxPrice);
            }
            query.addCriteria(priceCriteria);
        }

        // Filter by status ACTIVE
        query.addCriteria(Criteria.where("status").is(ProductStatus.ACTIVE));

        // Execute query
        List<Product> products = mongoTemplate.find(query, Product.class);
        return products.stream()
                .map(product -> {
                    ProductDTO productDTO = productMapper.toDTO(product);
                    calculateDiscountedPrice(productDTO);
                    return productDTO;
                })
                .collect(Collectors.toList());
    }


   // Calculator
   private void calculateDiscountedPrice(ProductDTO productDTO) {
       LocalDate now = LocalDate.now();
       Promotion activePromotion = promotionRepository.findActivePromotion(
               now, now, List.of(productDTO.getProductID()))
               .orElse(null);
       if (activePromotion != null) {
           productDTO.setDiscountedPrice(productDTO.getPrice() - (productDTO.getPrice() * activePromotion.getDiscount() / 100));
       } else {
           productDTO.setDiscountedPrice(productDTO.getPrice());
       }
   }
}
