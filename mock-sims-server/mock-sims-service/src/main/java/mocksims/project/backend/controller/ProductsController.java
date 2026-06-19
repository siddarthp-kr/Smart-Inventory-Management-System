package mocksims.project.backend.controller;

import mocksims.project.backend.api.domain.ProductsRequest;
import mocksims.project.backend.api.domain.ProductsResponse;
import mocksims.project.backend.domain.MockSimsConstants;
import mocksims.project.backend.exception.MockSimsCustomException;
import mocksims.project.backend.service.ProductsService;
import mocksims.project.backend.util.ValidationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/order")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductsController {
    private static final Logger LOG = LoggerFactory.getLogger(ProductsController.class);
    private final ProductsService productsService;

    /**
     *
     * @param productsService this service helps to retrieve product UPCs and names
     */
    public ProductsController(ProductsService productsService){
        this.productsService = productsService;
    }

    /**
     * Get response to retriee all product UPCs and names from the PRODUCT_BASIC_INFO for Ordering search functionality
     * @return response entity which is the list of the products and 200 Status code
     */

    @GetMapping(value = MockSimsConstants.GET_PRODUCTS_ENDPOINT)
    public ResponseEntity<ProductsResponse> getProducts(
            @RequestParam String storeNumber,
            @RequestParam String divisionNumber) {

        ProductsResponse response = new ProductsResponse();

        if (ValidationHelper.validateStoreNumber(storeNumber)
                && ValidationHelper.validateDivisionNumber(divisionNumber)) {

            try {
                ProductsRequest request = ProductsRequest.builder()
                        .storeNumber(storeNumber)
                        .divisionNumber(divisionNumber)
                        .build();

                response = productsService.getProducts(request);
                return ResponseEntity.ok(response);

            } catch (MockSimsCustomException error) {
                LOG.error("Failed to get products.", error);
                return ResponseEntity.status(error.getErrorCode()).body(response);
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }
}

