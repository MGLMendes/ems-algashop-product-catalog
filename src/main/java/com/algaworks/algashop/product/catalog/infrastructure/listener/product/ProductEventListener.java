package com.algaworks.algashop.product.catalog.infrastructure.listener.product;

import com.algaworks.algashop.product.catalog.domain.model.product.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductEventListener {

    @EventListener(ProductPriceChangedEvent.class)
    public void handle(ProductPriceChangedEvent event) {
        log.info("Product {} regular price changed from {} to {} and sale price from {} to {}",
                event.getProductId(),
                event.getOldRegularPrice(),
                event.getNewRegularPrice(),
                event.getOldSalePrice(),
                event.getNewSalePrice());
    }

    @EventListener(ProductPlacedOnSaleEvent.class)
    public void handle(ProductPlacedOnSaleEvent event) {
        log.info("Product {} placed to sale. Regular Price {} and Sale Price {}",
                event.getProductId(),
                event.getRegularPrice(),
                event.getSalePrice());
    }

    @EventListener(ProductAddedEvent.class)
    public void handle(ProductAddedEvent event) {
        log.info("ProductAddedEvent " + event);
    }

    @EventListener(ProductDelistedEvent.class)
    public void handle(ProductDelistedEvent  event) {
        log.info("ProductDelistedEvent  " + event);
    }

    @EventListener(ProductListedEvent.class)
    public void handle(ProductListedEvent event) {
        log.info("ProductListedEvent " + event);
    }
}
