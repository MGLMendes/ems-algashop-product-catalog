package contracts.product

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        headers {
            accept 'application/json'
        }
        url("api/v1/products/019c90e8-40fa-7ab0-a458-34f237b97987")
    }
    response {
        status 200
        headers {
            contentType "application/json"
        }
        body([
                id: fromRequest().path(3),
                addedAt: anyIso8601WithOffset(),
                name: "Notebook X11",
                brand: "Deep Driver",
                regularPrice: "1500,00",
                salePrice: 1000.00,
                inStock: false,
                enabled: true,
                categoryId: "",
                description: "A gamer Notebook"
        ])
    }
}