package com.glowcorner.backend.controller.ProductController;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glowcorner.backend.enums.SkinType;
import com.glowcorner.backend.enums.Category;
import com.glowcorner.backend.model.DTO.ProductDTO;
import com.glowcorner.backend.model.DTO.request.Product.CreateProductRequest;
import com.glowcorner.backend.model.DTO.response.ResponseData;
import com.glowcorner.backend.service.interfaces.CloudinaryService;
import com.glowcorner.backend.service.interfaces.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Product Management System", description = "Operations pertaining to products in the Product Management System")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    private final CloudinaryService cloudinaryService;

    public ProductController(ProductService productService, CloudinaryService cloudinaryService) {
        this.productService = productService;
        this.cloudinaryService = cloudinaryService;
    }

    // Get all products
    @Operation(summary = "Get all products", description = "Retrieve a list of all available products")
    @GetMapping
    public ResponseEntity<ResponseData> getAllProducts() {
        try {
            List<ProductDTO> products = productService.getAllProducts();
            if (products.isEmpty()) {
                return ResponseEntity.ok(new ResponseData(404, false, "No products found", null, null, null));
            }
            return ResponseEntity.ok(new ResponseData(200, true, "Products found", products, null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseData(500, false, "Failed to retrieve products: " + e.getMessage(), null, null, null));
        }
    }

    // Get product by id
    @Operation(summary = "Get a product by ID", description = "Retrieve a single product using its ID")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> getProductById(@PathVariable String id) {
        ProductDTO product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseData(404, false, "Product with ID: " + id + " not found", null, null, null));
        }
        return ResponseEntity.ok(new ResponseData(200, true, "Product found", product, null, null));
    }

    // Get products by skinType
    @Operation(summary = "Get products by category", description = "Retrieve a list of products using their category")
    @GetMapping("/skinType/{skinType}")
    public ResponseEntity<ResponseData> getProductsByCategory(@PathVariable SkinType skinType) {
        List<ProductDTO> products = productService.getProductsBySkinType(skinType);
        if (products.isEmpty()) {
            return ResponseEntity.ok(new ResponseData(404,true,"Products found", null,null,null) )   ;    }
        return ResponseEntity.ok(new ResponseData(200, true, "Products found", products, null, null));
    }

    // Get products by category
    @Operation(summary = "Get products by category", description = "Retrieve a list of products using their category")
    @GetMapping("/category/{category}")
    public ResponseEntity<ResponseData> getProductsByCategory(@PathVariable Category category) {
        List<ProductDTO> products = productService.getProductsByCategory(category);
        if (products.isEmpty()) {
            return ResponseEntity.ok(new ResponseData(404, true, "There is no product in category", null, null, null));
        }
        return ResponseEntity.ok(new ResponseData(200, true, "Products found", products, null, null));
    }

    // Get products by filter
    @Operation(summary = "Get products by filter", description = "Retrieve a list of products using filter criteria")
    @GetMapping("/filter")
    public ResponseEntity<ResponseData> filterProducts(
            @RequestParam(value = "skinTypes", required = false) String skinTypes,
            @RequestParam(value = "categories", required = false) String categories,
            @RequestParam(value = "minPrice", required = false) Long minPrice,
            @RequestParam(value = "maxPrice", required = false) Long maxPrice
    ) {
        // Kiểm tra giá trị maxPrice
        if (maxPrice != null && (maxPrice <= 0 || maxPrice == Long.MAX_VALUE)) {
            maxPrice = null;
        }

        List<SkinType> skinTypeList = skinTypes != null
                ? Arrays.stream(skinTypes.split(","))
                .map(String::trim)
                .map(SkinType::valueOf)
                .collect(Collectors.toList())
                : null;

        List<Category> categoryList = categories != null
                ? Arrays.stream(categories.split(","))
                .map(String::trim)
                .map(Category::valueOf)
                .collect(Collectors.toList())
                : null;

        // Gọi service để lấy danh sách sản phẩm
        List<ProductDTO> products = productService.getProductsByFilter(skinTypeList, categoryList, minPrice, maxPrice);

        // Kiểm tra nếu không tìm thấy sản phẩm
        if (products == null || products.isEmpty()) {
            return ResponseEntity.ok(new ResponseData(404, true, "There is no product matching the filter criteria", null, null, null));
        }

        return ResponseEntity.ok(new ResponseData(200, true, "Products found", products, null, null));
    }


    // Get products by product name
    @Operation(summary = "Get products by name", description = "Retrieve a list of products using their name")
    @GetMapping("/name/{productName}")
    public ResponseEntity<ResponseData> getProductsByProductName(@PathVariable String productName) {
        List<ProductDTO> products = productService.getProductsByProductName(productName);
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseData(404, false, "There is no product with name: " + productName, null, null, null));
        }
        return ResponseEntity.ok(new ResponseData(200, true, "Products found", products, null, null));
    }

    // Create a new product
    @Operation(summary = "Create a new product", description = "Add a new product to the catalog")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseData> createProduct(
            @RequestPart("product") @Parameter(
                    description = "Product data in JSON format",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateProductRequest.class)
                    )
            ) String productJson,
            @RequestPart(value = "image", required = false) @Parameter(
                    description = "Product image file",
                    content = @Content(mediaType = MediaType.IMAGE_PNG_VALUE)
            ) MultipartFile imageFile) {
        try {
            System.out.println("Received product JSON: " + productJson);
            System.out.println("Received image file: " + (imageFile != null ? imageFile.getOriginalFilename() : "No file"));

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // Bỏ qua các trường không nhận diện
            CreateProductRequest request = objectMapper.readValue(productJson, CreateProductRequest.class);

            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = cloudinaryService.uploadFile(imageFile);
                System.out.println("Uploaded image URL: " + imageUrl);
                request.setImage_url(imageUrl);
            }

            ProductDTO created = productService.createProduct(request);
            return ResponseEntity.ok(new ResponseData(200, true, "Product created successfully", created, null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseData(500, false, "Failed to create product: " + e.getMessage(), null, null, null));
        }
    }


    // Update product
    @Operation(summary = "Update a product", description = "Update an existing product using its ID")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseData> updateProduct(
            @PathVariable String id,
            @RequestPart(value = "product", required = false) @Parameter(
                    description = "Product data in JSON format",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateProductRequest.class)
                    )
            ) String productJson, // Nhận dữ liệu dưới dạng String
            @RequestPart(value = "image", required = false) @Parameter(
                    description = "Product image file",
                    content = @Content(mediaType = MediaType.IMAGE_PNG_VALUE)) MultipartFile imageFile) {
        try {
            ProductDTO productDTO;
            if (productJson != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                productDTO = objectMapper.readValue(productJson, ProductDTO.class);
            } else {
                productDTO = productService.getProductById(id);
                if (productDTO == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseData(404, false, "Product with ID: " + id + " not found", null, null, null));
                }
            }

            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = cloudinaryService.uploadFile(imageFile);
                productDTO.setImage_url(imageUrl); // Override image_url only if a new one is uploaded
            }

            ProductDTO updated = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(new ResponseData(200, true, "Product updated successfully", updated, null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseData(500, false, "Failed to update product: " + e.getMessage(), null, null, null));
        }
    }

    // Delete product
    @Operation(summary = "Delete a product by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
