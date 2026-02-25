package com.algawors.algashop.product.catalog.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ProductDetailOutput create(@RequestBody ProductInput productInput) {
        return ProductDetailOutput.builder()
                .id(UUID.randomUUID())
                .name(productInput.getName())
                .brand(productInput.getBrand())
                .addedAt(OffsetDateTime.now())
                .description(productInput.getDescription())
                .regularPrice(productInput.getRegularPrice())
                .salePrice(productInput.getSalePrice())
                .categoryId(productInput.getCategoryId())
                .inStock(false)
                .enabled(productInput.getEnabled())
                .category(
                        CategoryMinimalOutput.builder()
                                .id(productInput.getCategoryId())
                                .name("Notebook")
                                .build()
                )
                .build();
    }

    @GetMapping("/{productId}")
    public ProductDetailOutput getProduct(@PathVariable UUID productId) {
        return ProductDetailOutput.builder()
                .id(productId)
                .addedAt(OffsetDateTime.now())
                .name("Notebook X11")
                .brand("Deep Diver")
                .description("A Gamer Notebook")
                .regularPrice(new BigDecimal("1500.00"))
                .salePrice(new BigDecimal("1000.0"))
                .inStock(true)
                .enabled(true)
                .categoryId(UUID.randomUUID())
                .category(CategoryMinimalOutput.builder()
                        .id(UUID.randomUUID())
                        .name("Notebook")
                        .build())
                .build();
    }
}
