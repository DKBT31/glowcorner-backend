package com.glowcorner.backend.controller.ProductController;

import com.glowcorner.backend.enums.Category;
import com.glowcorner.backend.enums.SkinType;
import com.glowcorner.backend.model.DTO.ProductDTO;
import com.glowcorner.backend.model.DTO.response.ResponseData;
import com.glowcorner.backend.service.interfaces.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Product Management System (Customer)", description = "Operations pertaining to active products in the Product Management System")
@RestController
@RequestMapping("/api/customer/products")
public class CustomerProductController {

    private final ProductService productService;

    public CustomerProductController(ProductService productService) {
        this.productService = productService;
    }

    // Get all active products
    @Operation(summary = "Get all active products", description = "Retrieve a list of all active products")
    @GetMapping
    public ResponseEntity<ResponseData> getAllActiveProducts() {
        try {
            List<ProductDTO> products = productService.getAllActiveProducts();
            if (products.isEmpty()) {
                return ResponseEntity.ok(new ResponseData(404, false, "No active products found", null, null, null));
            }
            return ResponseEntity.ok(new ResponseData(200, true, "Active products found", products, null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseData(500, false, "Failed to retrieve active products: " + e.getMessage(), null, null, null));
        }
    }

    // Get active product by ID
    @Operation(summary = "Get an active product by ID", description = "Retrieve a single active product using its ID")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> getActiveProductById(@PathVariable String id) {
        ProductDTO product = productService.getActiveProductById(id);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseData(404, false, "Active product with ID: " + id + " not found", null, null, null));
        }
        return ResponseEntity.ok(new ResponseData(200, true, "Active product found", product, null, null));
    }

    // Get active products by skin type
    @Operation(summary = "Get active products by skin type", description = "Retrieve a list of active products using their skin type")
    @GetMapping("/skinType/{skinType}")
    public ResponseEntity<ResponseData> getActiveProductsBySkinType(@PathVariable SkinType skinType) {
        List<ProductDTO> products = productService.getActiveProductsBySkinType(skinType);
        if (products.isEmpty()) {
            return ResponseEntity.ok(new ResponseData(404, false, "No active products found for skin type", null, null, null));
        }
        return ResponseEntity.ok(new ResponseData(200, true, "Active products found", products, null, null));
    }

    // Get active products by category
    @Operation(summary = "Get active products by category", description = "Retrieve a list of active products using their category")
    @GetMapping("/category/{category}")
    public ResponseEntity<ResponseData> getActiveProductsByCategory(@PathVariable Category category) {
        List<ProductDTO> products = productService.getActiveProductsByCategory(category);
        if (products.isEmpty()) {
            return ResponseEntity.ok(new ResponseData(404, false, "No active products found for category", null, null, null));
        }
        return ResponseEntity.ok(new ResponseData(200, true, "Active products found", products, null, null));
    }

    // Get active products by product name
    @Operation(summary = "Get active products by name", description = "Retrieve a list of active products using their name")
    @GetMapping("/name/{productName}")
    public ResponseEntity<ResponseData> getActiveProductsByProductName(@PathVariable String productName) {
        List<ProductDTO> products = productService.getActiveProductsByProductName(productName);
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseData(404, false, "No active products found with name: " + productName, null, null, null));
        }
        return ResponseEntity.ok(new ResponseData(200, true, "Active products found", products, null, null));
    }

    // Get active products by filter
    @Operation(summary = "Get active products by filter", description = "Retrieve a list of active products using filter criteria")
    @GetMapping("/filter")
    public ResponseEntity<ResponseData> filterActiveProducts(
            @RequestParam(value = "skinTypes", required = false) String skinTypes,
            @RequestParam(value = "categories", required = false) String categories,
            @RequestParam(value = "minPrice", required = false) Long minPrice,
            @RequestParam(value = "maxPrice", required = false) Long maxPrice
    ) {
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

        List<ProductDTO> products = productService.getActiveProductsByFilter(skinTypeList, categoryList, minPrice, maxPrice);

        if (products == null || products.isEmpty()) {
            return ResponseEntity.ok(new ResponseData(404, false, "No active products matching the filter criteria", null, null, null));
        }

        return ResponseEntity.ok(new ResponseData(200, true, "Active products found", products, null, null));
    }
}