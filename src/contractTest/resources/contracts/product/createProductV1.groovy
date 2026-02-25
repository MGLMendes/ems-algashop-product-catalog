package contracts.product

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method POST()
        headers {
            accept "application/json"
            contentType "application/json"
        }
        urlPath("/api/v1/products") {
            body([
                    name: value(
                            test("Notebook X11"),
                            stub(nonBlank())
                    ),
                    brand:  value(
                            test("Deep Diver"),
                            stub(nonBlank())
                    ),
                    regularPrice:  value(
                            test(1500.0),
                            stub(number())
                    ),
                    salePrice:  value(
                            test(1000.0),
                            stub(number())
                    ),
                    enabled:  value(
                            test(true),
                            stub(anyBoolean())
                    ),
                    categoryId: value(
                    test("019c9531-68c5-78b9-9679-7e3e4b0687fe"),
                    stub(nonBlank())
            ),
                    description: value(
                            test("A Gamer Notebook"),
                            stub(optional(nonBlank()))
                    )
            ])
        }
    }
    response {
        status 201
        headers {
            contentType "application/json"
        }
        body([
                id: anyUuid(),
                addedAt: anyIso8601WithOffset(),
                name: fromRequest().body('$.name'),
                brand: fromRequest().body('$.brand'),
                regularPrice: fromRequest().body('$.regularPrice'),
                salePrice: fromRequest().body('$.salePrice'),
                inStock: false,
                enabled: fromRequest().body('$.enabled'),
                category: [
                        id: fromRequest().body('$.categoryId'),
                        name: "Notebook"
                ],
                description: fromRequest().body('$.description'),
        ])
    }
}
