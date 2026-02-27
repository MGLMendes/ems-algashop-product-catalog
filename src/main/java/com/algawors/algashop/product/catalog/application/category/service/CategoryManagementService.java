package com.algawors.algashop.product.catalog.application.category.service;

import com.algawors.algashop.product.catalog.application.category.input.CategoryInput;
import jakarta.validation.Valid;

import java.util.UUID;

public class CategoryManagementService {
    public UUID create(@Valid CategoryInput input) {
        return null;
    }

    public void update(UUID categoryId, CategoryInput input) {
    }

    public void disable(UUID categoryId) {

    }
}