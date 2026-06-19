package mocksims.project.backend.repository;

import mocksims.project.backend.api.domain.ProductItem;
import java.util.List;

public interface ProductsRepository {
    List<ProductItem> getProducts(String storeNumber, String divisionNumber);
}
