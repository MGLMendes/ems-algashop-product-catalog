package com.algaworks.algashop.product.catalog.application.category.service.query;

import com.algaworks.algashop.product.catalog.application.category.output.CategoryDetailOutput;
import com.algaworks.algashop.product.catalog.presentation.model.PageModel;

import java.util.UUID;

public interface CategoryQueryService {
    PageModel<CategoryDetailOutput> filter(CategoryFilter categoryFilter);

    CategoryDetailOutput findById(UUID categoryId);
}