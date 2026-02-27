package com.algawors.algashop.product.catalog.application.category.query;

import com.algawors.algashop.product.catalog.application.category.output.CategoryDetailOutput;

import java.util.UUID;

public class CategoryOutputTestDataBuilder {

    private CategoryOutputTestDataBuilder() {
    }

    public static CategoryDetailOutput.CategoryDetailOutputBuilder aCategory() {
        return CategoryDetailOutput.builder()
                .id(UUID.randomUUID())
                .name("Electronics")
                .enabled(true);
    }

    public static CategoryDetailOutput.CategoryDetailOutputBuilder aDisabledCategory() {
        return CategoryDetailOutput.builder()
                .id(UUID.randomUUID())
                .name("Books")
                .enabled(false);
    }
}