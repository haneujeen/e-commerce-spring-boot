/**
 * The ProductController class is a REST controller that handles HTTP requests related to products.
 * It interacts with the ProductService to perform CRUD operations on the ProductEntity objects
 * and convert them to/from ProductDTO objects.
 */

package com.example.shop.controller;

import com.example.shop.dto.ProductDTO;
import com.example.shop.dto.ResponseDTO;
import com.example.shop.model.ProductEntity;
import com.example.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    // Test method to get ProductDTO response
    @GetMapping("/product-dto")
    public ResponseEntity<?> getProductDTO() {
        // Create a sample ProductEntity object
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId("1");
        productEntity.setTitle("Furniture Product");
        productEntity.setMaterial("Wood");
        productEntity.setPrice(1000.0);

        // Convert the ProductEntity to ProductDTO and send the response
        ProductDTO response = new ProductDTO(productEntity);
        return ResponseEntity.ok().body(response);
    }

    @Autowired
    private ProductService service;

    // Test method to get message response
    @GetMapping("/service-message")
    public ResponseEntity<?> getMessage() {
        String message = service.getMessage();

        List<String> list = new ArrayList<>();
        list.add(message);
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();

        return ResponseEntity.ok().body(response);
    }

    // Retrieves the title of a ProductEntity object from the database through the ProductService,
    // wraps it in a ResponseDTO object and sends it as an HTTP response.
    @GetMapping("/service-title")
    public ResponseEntity<?> getTitle() {
        String title = service.getProductTitle();

        List<String> list = new ArrayList<>();
        list.add(title);
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();

        return ResponseEntity.ok().body(response);
    }

    /**
     * Endpoint for creating a new product.
     *
     * @param dto the ProductDTO object to be created
     * @return a ResponseEntity with a list of ProductDTO objects wrapped in a ResponseDTO object
     */
    @PostMapping()
    public ResponseEntity<?> createProduct(@RequestBody ProductDTO dto) {
        try {
            // Creating a test user ID to assign to the created product entity
            String testUserId = "test-user";

            // Convert the received ProductDTO object to a ProductEntity object
            ProductEntity entity = ProductDTO.toEntity(dto);

            // Set the ID of the ProductEntity object to null to ensure it gets a new ID
            entity.setId(null);

            // Set the user ID of the ProductEntity object to the test user ID
            entity.setUserId(testUserId);

            // Call the ProductService's create method to create the new product entity in the database
            List<ProductEntity> entities = service.create(entity);

            // Convert the created ProductEntity objects to ProductDTO objects
            List<ProductDTO> dtos = entities.stream()
                    .map(ProductDTO::new)
                    .collect(Collectors.toList());

            // Create a ResponseDTO object to wrap the ProductDTO objects and send it as an HTTP response
            ResponseDTO<ProductDTO> response = ResponseDTO.<ProductDTO>builder()
                    .data(dtos)
                    .build();

            // Return an HTTP response with a status code of 200 (OK) and the ResponseDTO object as the body
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            // If an exception is caught, create a ResponseDTO object with an error message and send it as a bad request
            String errorMessage = e.getMessage();
            ResponseDTO<ProductDTO> response = ResponseDTO.<ProductDTO>builder()
                    .errorMessage(errorMessage)
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}