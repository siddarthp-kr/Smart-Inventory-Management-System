package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.ProductItem;
import mocksims.project.backend.api.domain.ProductsRequest;
import mocksims.project.backend.api.domain.ProductsResponse;

import mocksims.project.backend.exception.MockSimsCustomException;
import mocksims.project.backend.repository.ProductsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class ProductsServiceImpl implements ProductsService{
    private static final Logger LOG = LoggerFactory.getLogger(ProductsServiceImpl.class);
    private final ProductsRepository productsRepository;

    public ProductsServiceImpl(ProductsRepository productsRepository){
        this.productsRepository = productsRepository;
    }

    /**
     * Wraps inside a ProductResponse object
     * @return response containing list of products
     */

    @Override
    public ProductsResponse getProducts(ProductsRequest productsRequest) {
        ProductsResponse response = new ProductsResponse();

        try {
            List<ProductItem> products = productsRepository.getProducts(
                    productsRequest.getStoreNumber(),
                    productsRequest.getDivisionNumber()
            );

            response.setProducts(products);

        } catch (DataAccessException error) {
            LOG.error("Failed to retrieve products.", error);
            throw new MockSimsCustomException(500, "Error: Failed to retrieve products");
        }

        return response;
    }


}
