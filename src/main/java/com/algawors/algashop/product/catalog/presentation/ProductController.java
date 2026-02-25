package com.algawors.algashop.product.catalog.presentation;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ProductDetailOutput create(@RequestBody @Valid ProductInput productInput) {
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

    @GetMapping
    public PageModel<ProductDetailOutput> filter(
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "number", required = false) Integer number
    ) {
        var prod1 = ProductDetailOutput.builder()
                .id(UUID.randomUUID())
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

        var prod2 = ProductDetailOutput.builder()
                .id(UUID.randomUUID())
                .addedAt(OffsetDateTime.now())
                .name("Notebook X26")
                .brand("Deep Diver")
                .description("A Gamer Notebook")
                .regularPrice(new BigDecimal("2500.00"))
                .salePrice(new BigDecimal("2000.0"))
                .inStock(true)
                .enabled(true)
                .categoryId(UUID.randomUUID())
                .category(CategoryMinimalOutput.builder()
                        .id(UUID.randomUUID())
                        .name("Notebook")
                        .build())
                .build();
        return PageModel.<ProductDetailOutput>builder()
                .number(0)
                .size(size)
                .totalPages(1)
                .totalElements(2)
                .content(List.of(
                        prod1, prod2
                ))
                .build();
    }
}
