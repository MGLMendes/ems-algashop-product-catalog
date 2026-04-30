package com.algaworks.algashop.product.catalog.domain.model.product;

import com.algaworks.algashop.product.catalog.domain.model.DomainException;
import com.algaworks.algashop.product.catalog.domain.model.category.Category;
import com.algaworks.algashop.product.catalog.domain.utility.IdGenerator;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.*;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TextScore;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Document(collection = "products")
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@CompoundIndex(name = "pidx_product_by_category_enabledTrue_salePrice",
        def = "{'category.id': 1, 'salePrice': 1}",
        partialFilter = "{'enabled': true}")
@CompoundIndex(name = "pidx_product_by_category_enabledTrue_addedAt",
        def = "{'category.id': 1, 'addedAt': -1}",
        partialFilter = "{'enabled': true}")
public class Product extends AbstractAggregateRoot<Product> {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @TextIndexed(weight = 1)
    private String name;

    @Indexed(name = "idx_product_by_brand")
    private String brand;

    @TextIndexed(weight = 5)
    @Setter
    private String description;

    private Integer quantityInStock;

    private Boolean enabled;

    private BigDecimal regularPrice;

    private BigDecimal salePrice;

    @Version
    private Long version;

    @CreatedDate
    private OffsetDateTime addedAt;

    @LastModifiedDate
    private OffsetDateTime updatedAt;

    @CreatedBy
    private UUID createdByUserId;

    @LastModifiedBy
    private UUID modifiedByUserId;

    private ProductCategory category;

    private Integer discountPercentageRounded;

    @TextScore
    private Float score;

    @Builder
    public Product(String name, String brand, String description, Boolean enabled,
                   BigDecimal regularPrice, BigDecimal salePrice, Category category) {
        setId(IdGenerator.generateTimeBasedUUID());
        setName(name);
        setBrand(brand);
        setDescription(description);
        setEnabled(enabled);
        setRegularPrice(regularPrice);
        setSalePrice(salePrice);
        setQuantityInStock(0);
        setCategory(category);
    }

    public void setName(String name) {
        if (StringUtils.isEmpty(name)) {
            throw  new IllegalArgumentException("name cannot be empty");
        }
        this.name = name;
    }

    public void setBrand(String brand) {
        if (StringUtils.isEmpty(brand)) {
            throw  new IllegalArgumentException("brand cannot be empty");
        }
        this.brand = brand;
    }

    public void setCategory(Category category) {
        Objects.requireNonNull(category, "category cannot be null");
        this.category = ProductCategory.of(category);
    }

    public void changePrice(BigDecimal regularPrice, BigDecimal salePrice) {
        Objects.requireNonNull(regularPrice, "regularPrice cannot be null");
        Objects.requireNonNull(salePrice, "salePrice cannot be null");

        BigDecimal oldRegularPrice = this.regularPrice;
        BigDecimal oldSalePrice = this.salePrice;

        boolean wasOnSale = hasDiscount();

       if (regularPrice.compareTo(salePrice) < 0) {
            throw new DomainException("Sale Price cannot be greater than regular price");
        }

        this.setRegularPrice(regularPrice);
        this.setSalePrice(salePrice);

        if (pricesDidNotChange(oldRegularPrice, oldSalePrice)) {
            return;
        }

        registerPriceChangedEvent(oldRegularPrice, oldSalePrice);

        if (isNewlyOnSale(wasOnSale)) {
            registerProductPlacedOnSaleEvent();
        }
    }

    private boolean pricesDidNotChange(BigDecimal oldRegularPrice, BigDecimal oldSalePrice) {
        return Objects.equals(this.regularPrice, oldRegularPrice) && Objects.equals(this.salePrice, oldSalePrice);
    }

    private boolean isNewlyOnSale(boolean wasOnSale) {
        return hasDiscount() && !wasOnSale;
    }


    private void registerProductPlacedOnSaleEvent() {
        super.registerEvent(ProductPlacedOnSaleEvent.builder()
                .productId(this.id)
                .regularPrice(this.regularPrice)
                .salePrice(this.salePrice)
                .build());
    }

    private void registerPriceChangedEvent(BigDecimal oldRegularPrice, BigDecimal oldSalePrice) {
        super.registerEvent(
                ProductPriceChangedEvent.builder()
                        .productId(this.id)
                        .newSalePrice(this.salePrice)
                        .newRegularPrice(this.regularPrice)
                        .oldRegularPrice(oldRegularPrice)
                        .oldSalePrice(oldSalePrice)
                        .build()
        );
    }

    private void setRegularPrice(BigDecimal regularPrice) {
        Objects.requireNonNull(regularPrice, "regularPrice cannot be null");
        if (regularPrice.signum() == -1) {
            throw  new IllegalArgumentException("Regular price cannot be less than zero");
        }

        this.regularPrice = regularPrice;
        this.calculateDiscountPercentage();
    }

    private void setSalePrice(BigDecimal salePrice) {
        Objects.requireNonNull(salePrice, "salePrice cannot be null");
        if (salePrice.signum() == -1) {
            throw  new IllegalArgumentException("Sale price cannot be less than zero");
        }

        this.salePrice = salePrice;
        this.calculateDiscountPercentage();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void disable() {
        setEnabled(false);
    }

    public void enable() {
        setEnabled(true);
    }

    private void setId(UUID id) {
        Objects.requireNonNull(id, "id cannot be null");
        this.id = id;
    }

    private void setQuantityInStock(Integer quantityInStock) {
        Objects.requireNonNull(quantityInStock, "quantityInStock cannot be null");
        if (quantityInStock < 0) {
            throw  new DomainException("quantityInStock cannot be less than zero");
        }
        this.quantityInStock = quantityInStock;
    }

    public boolean isInStock() {
        return this.quantityInStock != null && this.quantityInStock > 0;
    }

    public boolean hasDiscount() {
        return this.discountPercentageRounded != null && this.discountPercentageRounded > 0;
    }

    private void calculateDiscountPercentage() {
        if (regularPrice == null || salePrice == null || regularPrice.signum() == 0) {
            discountPercentageRounded = 0;
            return;
        }

        discountPercentageRounded = BigDecimal.ONE
                .subtract(salePrice.divide(regularPrice, 4, RoundingMode.HALF_UP))
                .multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_UP).intValue();
    }

}
