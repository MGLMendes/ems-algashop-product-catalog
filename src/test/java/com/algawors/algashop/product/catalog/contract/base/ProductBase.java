package com.algawors.algashop.product.catalog.contract.base;

import com.algawors.algashop.product.catalog.application.product.input.ProductInput;
import com.algawors.algashop.product.catalog.application.product.output.ProductDetailOutput;
import com.algawors.algashop.product.catalog.application.product.query.ProductDetailOutputTestDataBuilder;
import com.algawors.algashop.product.catalog.application.product.query.ProductQueryService;
import com.algawors.algashop.product.catalog.application.product.service.ProductManagementApplicationService;
import com.algawors.algashop.product.catalog.presentation.controller.ProductController;
import com.algawors.algashop.product.catalog.presentation.model.PageModel;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@WebMvcTest(controllers = ProductController.class)
public class ProductBase {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockitoBean
    private ProductQueryService productQueryService;

    @MockitoBean
    private ProductManagementApplicationService productManagementApplicationService;

    public static final UUID validProductId = UUID.fromString("019c90e8-40fa-7ab0-a458-34f237b97987");
    public static final UUID createdProductId = UUID.fromString("019c9f9e-c0ae-7491-b1f9-9652cb52c75e");

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8).build());

        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();

        mockFindProducts();
        mockFilterProducts();
        mockCreateProduct();
    }

    private void mockCreateProduct() {
        Mockito.when(productManagementApplicationService.create(Mockito.any(ProductInput.class)))
                .thenReturn(createdProductId);

        Mockito.when(productQueryService.findById(createdProductId))
                .thenReturn(ProductDetailOutputTestDataBuilder.aProduct().inStock(false).build());
    }

    private void mockFindProducts() {
        Mockito.when(productQueryService.findById(validProductId))
                .thenReturn(ProductDetailOutputTestDataBuilder.aProduct().id(validProductId).build());
    }

    private void mockFilterProducts() {
        Mockito.when(productQueryService.filter(Mockito.any(), Mockito.any()))
                .then(
                        (answer) -> {
                            Integer size = answer.getArgument(0);
                            return PageModel.<ProductDetailOutput>builder()
                                    .number(0)
                                    .size(size)
                                    .totalPages(1)
                                    .totalElements(2)
                                    .content(List.of(
                                            ProductDetailOutputTestDataBuilder.aProduct().build(),
                                            ProductDetailOutputTestDataBuilder.aProductAlt1().build()
                                    ))
                                    .build();

                        }
                );
    }
}
