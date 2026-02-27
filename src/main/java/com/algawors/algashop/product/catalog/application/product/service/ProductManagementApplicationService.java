package com.algawors.algashop.product.catalog.application.product.service;

import com.algawors.algashop.product.catalog.application.product.input.ProductInput;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductManagementApplicationService {

    public UUID create(ProductInput productInput) {
        return null;
    }

    public void update(UUID productId, ProductInput productInput) {
    }

    public void disable(UUID productId) {

    }
}
