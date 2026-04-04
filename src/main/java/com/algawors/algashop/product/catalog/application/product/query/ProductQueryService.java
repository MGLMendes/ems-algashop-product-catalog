package com.algawors.algashop.product.catalog.application.product.query;

import com.algawors.algashop.product.catalog.application.product.output.ProductDetailOutput;
import com.algawors.algashop.product.catalog.presentation.model.PageModel;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductQueryService {

   public ProductDetailOutput findById(UUID productId){
      return  null;
   }
   public PageModel<ProductDetailOutput> filter(Integer size, Integer number) {
      return  null;
   }
}
