package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.ProductsRequest;
import mocksims.project.backend.api.domain.ProductsResponse;

public interface ProductsService {
    ProductsResponse getProducts(ProductsRequest productsRequest);
}
