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
                    name: "Notebook X11",
                    brand: "Deep Diver",
                    regularPrice: 1500.00,
                    salePrice: 1000.0,
                    enabled: true,
                    categoryId: "019c9531-68c5-78b9-9679-7e3e4b0687fe",
                    description: "A Gamer Notebook"
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
                name: "Notebook X11",
                brand: "Deep Diver",
                regularPrice: 1500.00,
                salePrice: 1000.0,
                inStock: false,
                enabled: true,
                category: [
                        id: "019c9531-68c5-78b9-9679-7e3e4b0687fe",
                        name: "Notebook"
                ],
                description: "A Gamer Notebook"
        ])
    }
}
