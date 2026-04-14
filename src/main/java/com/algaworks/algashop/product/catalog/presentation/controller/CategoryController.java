package com.algaworks.algashop.product.catalog.presentation.controller;


import com.algaworks.algashop.product.catalog.application.category.input.CategoryInput;
import com.algaworks.algashop.product.catalog.application.category.output.CategoryDetailOutput;
import com.algaworks.algashop.product.catalog.application.category.service.management.CategoryManagementService;
import com.algaworks.algashop.product.catalog.application.category.service.query.CategoryFilter;
import com.algaworks.algashop.product.catalog.application.category.service.query.CategoryQueryService;
import com.algaworks.algashop.product.catalog.presentation.model.PageModel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryQueryService categoryQueryService;
    private final CategoryManagementService categoryManagementService;

    @GetMapping
    public PageModel<CategoryDetailOutput> filter(CategoryFilter categoryFilter) {
        return categoryQueryService.filter(categoryFilter);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDetailOutput create(@RequestBody @Valid CategoryInput input) {
        UUID categoryId = categoryManagementService.create(input);
        return categoryQueryService.findById(categoryId);
    }

    @GetMapping("/{categoryId}")
    public CategoryDetailOutput findById(@PathVariable UUID categoryId) {
        return categoryQueryService.findById(categoryId);
    }

    @PutMapping("/{categoryId}")
    public CategoryDetailOutput update(
            @PathVariable UUID categoryId,
            @RequestBody @Valid CategoryInput input) {
        categoryManagementService.update(categoryId, input);
        return categoryQueryService.findById(categoryId);
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disable(@PathVariable UUID categoryId) {
        categoryManagementService.disable(categoryId);
    }
}