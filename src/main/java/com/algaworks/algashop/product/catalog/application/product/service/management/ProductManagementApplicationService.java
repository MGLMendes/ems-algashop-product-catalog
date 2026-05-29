package com.algaworks.algashop.product.catalog.application.product.service.management;

import com.algaworks.algashop.product.catalog.application.product.input.ProductInput;
import com.algaworks.algashop.product.catalog.application.product.output.ProductDetailOutput;
import com.algaworks.algashop.product.catalog.application.utility.Mapper;
import com.algaworks.algashop.product.catalog.domain.model.category.Category;
import com.algaworks.algashop.product.catalog.domain.model.category.CategoryNotFoundException;
import com.algaworks.algashop.product.catalog.domain.model.category.CategoryRepository;
import com.algaworks.algashop.product.catalog.domain.model.product.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductManagementApplicationService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final StockMovementRepository  stockMovementRepository;
    private final StockService stockService;
    private final Mapper mapper;

    @CachePut(cacheNames = "algashop:products:v1", key = "#result.id", condition = "#productInput.enabled == true")
    public ProductDetailOutput create(ProductInput productInput) {
        Product product = mapToProduct(productInput);
        productRepository.save(product);
        return mapper.convert(product, ProductDetailOutput.class);

    }

    @CachePut(cacheNames = "algashop:products:v1", key = "#result.id", condition = "#productInput.enabled == true")
    @CacheEvict(cacheNames = "algashop:products:v1", key = "#productId", condition = "#productInput.enabled == false")
    public ProductDetailOutput update(UUID productId, ProductInput productInput) {
        Product product = findProduct(productId);
        Category category = findCategory(productInput.getCategoryId());
        updateProduct(product, productInput);
        product.setCategory(category);
        productRepository.save(product);
        return mapper.convert(product, ProductDetailOutput.class);
    }

    @CacheEvict(cacheNames = "algashop:products:v1", key = "#productId")
    public void disable(UUID productId) {
        Product product = findProduct(productId);
        product.disable();
        productRepository.save(product);
    }

    public void enable(UUID productId) {
        Product product = findProduct(productId);
        product.enable();
        productRepository.save(product);
    }

    @Transactional
    public void restock(UUID productId, int quantity) {
        Product product = findProduct(productId);
        StockMovement restock = stockService.restock(product, quantity);
        stockMovementRepository.save(restock);
    }

    @Transactional
    public void withdraw(UUID productId, int quantity) {
        Product product = findProduct(productId);
        StockMovement withdraw = stockService.withdraw(product, quantity);
        stockMovementRepository.save(withdraw);
    }

    private Product mapToProduct(ProductInput productInput) {
        Category category = findCategory(productInput.getCategoryId());
        return Product.builder()
                .name(productInput.getName())
                .brand(productInput.getBrand())
                .description(productInput.getDescription())
                .regularPrice(productInput.getRegularPrice())
                .salePrice(productInput.getSalePrice())
                .enabled(productInput.getEnabled())
                .category(category)
                .build();
    }

    private void updateProduct(Product product, ProductInput productInput) {
        product.setName(productInput.getName());
        product.setBrand(productInput.getBrand());
        product.setDescription(productInput.getDescription());
        product.setEnabled(productInput.getEnabled());
        product.changePrice(productInput.getRegularPrice(), productInput.getSalePrice());
    }

    private Product findProduct(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    private Category findCategory(UUID categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new CategoryNotFoundException(categoryId)
        );
    }
}
