package com.algaworks.algashop.product.catalog.application.product.service.query;

import com.algaworks.algashop.product.catalog.application.product.output.ProductDetailOutput;
import com.algaworks.algashop.product.catalog.application.product.output.ProductSummaryOutput;
import com.algaworks.algashop.product.catalog.presentation.model.PageModel;
import org.springframework.cache.annotation.Cacheable;

import java.util.UUID;

public interface ProductQueryService {
   PageModel<ProductSummaryOutput> filter(ProductFilter productFilter);
   @Cacheable(cacheNames = "algashop:products:v1", key = "#productId")
   ProductDetailOutput findById(UUID productId);
}
