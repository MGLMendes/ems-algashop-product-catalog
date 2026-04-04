package com.algawors.algashop.product.catalog.application.category.service;

import com.algawors.algashop.product.catalog.application.category.output.CategoryDetailOutput;
import com.algawors.algashop.product.catalog.presentation.model.PageModel;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class CategoryQueryService {
    public PageModel<CategoryDetailOutput> filter(Integer size, Integer number) {
        return null;
    }

    public CategoryDetailOutput findById(UUID categoryId) {
        return null;
    }
}