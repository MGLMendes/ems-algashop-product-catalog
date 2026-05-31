package com.algaworks.algashop.product.catalog.application.category.service.query;

import com.algaworks.algashop.product.catalog.application.category.output.CategoryDetailOutput;
import com.algaworks.algashop.product.catalog.presentation.model.PageModel;
import org.springframework.cache.annotation.Cacheable;

import java.util.UUID;

public interface CategoryQueryService {
    @Cacheable(cacheNames = "algashop:categories-filter:v1",
            key = "'default'",
            condition = "#categoryFilter.isCacheable()")
    PageModel<CategoryDetailOutput> filter(CategoryFilter categoryFilter);

    @Cacheable(cacheNames = "algashop:categories:v1", key = "#categoryId")
    CategoryDetailOutput findById(UUID categoryId);
}