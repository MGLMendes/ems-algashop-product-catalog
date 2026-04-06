package com.algawors.algashop.product.catalog.application.product.service.query;

import com.algawors.algashop.product.catalog.application.product.output.ProductDetailOutput;
import com.algawors.algashop.product.catalog.application.product.output.ProductSummaryOutput;
import com.algawors.algashop.product.catalog.presentation.model.PageModel;
import org.springframework.stereotype.Service;

import java.util.UUID;

public interface ProductQueryService {

   PageModel<ProductSummaryOutput> filter(Integer size, Integer number);
   ProductDetailOutput findById(UUID productId);
}
