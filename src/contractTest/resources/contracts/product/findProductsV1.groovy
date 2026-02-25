package contracts.product

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        headers {
            accept 'application/json'
        }
        url("/api/v1/products") {
            queryParameters {
                parameter("size",
                value(
                        test(10),
                        stub(optional(anyNumber()))
                ))
                parameter("number",
                value(
                        test(5),
                        stub(optional(anyNumber()))
                ))
            }
        }
    }
    response {
        status 200
        headers {
            contentType "application/json"
        }
        body([
                size: fromRequest().query("size"),
                number: 0,
                totalElements: 2,
                totalPages: 1,
                content: [
                        [
                                id: anyUuid(),
                                addedAt: anyIso8601WithOffset(),
                                name: "Notebook X11",
                                brand: "Deep Diver",
                                regularPrice: "1500.00",
                                salePrice: 1000.00,
                                inStock: true,
                                enabled: true,
                                category: [
                                        id: anyUuid(),
                                        name: "Notebook",
                                ],
                                description: "A Gamer Notebook",
                        ],
                        [
                                id: anyUuid(),
                                addedAt: anyIso8601WithOffset(),
                                name: "Notebook X26",
                                brand: "Deep Diver",
                                regularPrice: "2500.00",
                                salePrice: 2000.00,
                                inStock: true,
                                enabled: true,
                                category: [
                                        id: anyUuid(),
                                        name: "Notebook",
                                ],
                                description: "A Gamer Notebook"
                        ]
                ]
        ])
    }
}