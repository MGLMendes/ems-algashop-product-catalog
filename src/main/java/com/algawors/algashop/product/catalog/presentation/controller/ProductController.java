package com.algawors.algashop.product.catalog.presentation.controller;

import com.algawors.algashop.product.catalog.application.product.input.ProductInput;
import com.algawors.algashop.product.catalog.application.product.output.CategoryMinimalOutput;
import com.algawors.algashop.product.catalog.application.product.output.ProductDetailOutput;
import com.algawors.algashop.product.catalog.application.product.query.ProductQueryService;
import com.algawors.algashop.product.catalog.application.product.service.ProductManagementApplicationService;
import com.algawors.algashop.product.catalog.presentation.model.PageModel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductQueryService productQueryService;
    private final ProductManagementApplicationService productManagementApplicationService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ProductDetailOutput create(@RequestBody @Valid ProductInput productInput) {
        UUID productId = productManagementApplicationService.create(productInput);
        return productQueryService.findById(productId);
    }

    @GetMapping("/{productId}")
    public ProductDetailOutput getProduct(@PathVariable UUID productId) {
        return productQueryService.findById(productId);

    }

    @GetMapping
    public PageModel<ProductDetailOutput> filter(
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "number", required = false) Integer number
    ) {
        return productQueryService.filter(size, number);
    }
}
