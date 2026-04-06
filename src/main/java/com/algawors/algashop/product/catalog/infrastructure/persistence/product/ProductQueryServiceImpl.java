package com.algawors.algashop.product.catalog.infrastructure.persistence.product;

import com.algawors.algashop.product.catalog.application.product.output.ProductDetailOutput;
import com.algawors.algashop.product.catalog.application.product.output.ProductSummaryOutput;
import com.algawors.algashop.product.catalog.application.product.service.query.ProductQueryService;
import com.algawors.algashop.product.catalog.application.utility.Mapper;
import com.algawors.algashop.product.catalog.domain.model.product.Product;
import com.algawors.algashop.product.catalog.domain.model.product.ProductNotFoundException;
import com.algawors.algashop.product.catalog.domain.model.product.ProductRepository;
import com.algawors.algashop.product.catalog.presentation.model.PageModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductQueryServiceImpl implements ProductQueryService {

    private final ProductRepository productRepository;
    private final Mapper mapper;

    @Override
    public PageModel<ProductSummaryOutput> filter(Integer size, Integer number) {
        return null;
    }

    @Override
    public ProductDetailOutput findById(UUID productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException(productId)
        );
        return mapper.convert(product, ProductDetailOutput.class);
    }
}
