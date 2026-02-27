package contracts.product

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        headers {
            accept "application/json"
        }
        url("/api/v1/products/019c9fa1-c066-7284-9b95-5d482cf47a62")
    }
    response {
        status 404
        body ([
                instance: fromRequest().path(),
                type: "/errors/not-found",
                title: "Not Found",

        ])
    }
}
