package com.algaworks.algashop.product.catalog.presentation.controller;

import com.algaworks.algashop.product.catalog.application.product.input.ImageInput;
import com.algaworks.algashop.product.catalog.application.product.output.ImageOutput;
import com.algaworks.algashop.product.catalog.application.product.service.management.ProductImageManagementApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products/{productId}/images")
@RequiredArgsConstructor
public class ProductImagesController {

    private final ProductImageManagementApplicationService managementService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ImageOutput create(@PathVariable UUID productId,
                              @RequestBody @Valid ImageInput input) {
        return managementService.create(productId, input);
    }

    @DeleteMapping("{imageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID productId, @PathVariable UUID imageId) {
        managementService.delete(productId, imageId);
    }

    @PutMapping("{imageId}/primary")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void primary(@PathVariable UUID productId, @PathVariable UUID imageId) {
        managementService.primary(productId, imageId);
    }

}