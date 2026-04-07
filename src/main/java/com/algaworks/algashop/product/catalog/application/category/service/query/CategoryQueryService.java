package com.algaworks.algashop.product.catalog.application.category.service.query;

import com.algaworks.algashop.product.catalog.application.category.output.CategoryDetailOutput;
import com.algaworks.algashop.product.catalog.presentation.model.PageModel;

import java.util.UUID;

public interface CategoryQueryService {
    PageModel<CategoryDetailOutput> filter(Integer size, Integer number);

    CategoryDetailOutput findById(UUID categoryId);
}