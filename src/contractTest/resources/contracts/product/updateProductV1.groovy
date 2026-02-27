package contracts.product

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method PUT()
        headers {
            accept "application/json"
            contentType "application/json"
        }
        urlPath("/api/v1/products/019c90e8-40fa-7ab0-a458-34f237b97987") {
            body([
                    name: value(
                            test("Notebook X11"),
                            stub(nonBlank())
                    ),
                    brand: value(
                            test("Deep Diver"),
                            stub(nonBlank())
                    ),
                    regularPrice: value(
                            test(1500.00),
                            stub(number())
                    ),
                    salePrice: value(
                            test(1000.00),
                            stub(number())
                    ),
                    enabled: value(
                            test(true),
                            stub(anyBoolean())
                    ),
                    categoryId: value(
                            test("f5ab7a1e-37da-41e1-892b-a1d38275c2f2"),
                            stub(anyUuid())
                    ),
                    description: value(
                            test("A Gamer Notebook"),
                            stub(optional(nonBlank()))
                    )
            ])
        }
    }
    response {
        status 200
        headers {
            contentType 'application/json'
        }
        body([
                id: fromRequest().path(3),
                addedAt: anyIso8601WithOffset(),
                name: fromRequest().body('$.name'),
                brand: fromRequest().body('brand'),
                regularPrice: fromRequest().body('$.regularPrice'),
                salePrice: fromRequest().body('$.salePrice'),
                inStock: anyBoolean(),
                enabled: fromRequest().body('$.enabled'),
                category: [
                        id: anyUuid(),
                        name: "Notebook"
                ],
                description: fromRequest().body('$.description'),
        ])
    }
}