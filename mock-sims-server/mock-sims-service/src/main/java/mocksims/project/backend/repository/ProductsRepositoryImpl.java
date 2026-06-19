package mocksims.project.backend.repository;

import mocksims.project.backend.api.domain.ProductItem;
import mocksims.project.backend.domain.mapper.ProductsMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

@Repository
public class ProductsRepositoryImpl implements ProductsRepository{
    private static final Logger LOG = LoggerFactory.getLogger(ProductsRepositoryImpl.class);
    private final ProductsMapper productsMapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ProductsRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, ProductsMapper productsMapper){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.productsMapper = productsMapper;
    }

    /**
     * SQL query to retrieve all product UPCs and names from PRODUCT_BASIC_INFO ordered in alphabetical order by name
     */

    private static final String GET_PRODUCTS = """
            SELECT p.upc_number, p.product_name
            FROM PRODUCT_BASIC_INFO p
            INNER JOIN PRODUCT_BOH_INFO b
                ON p.upc_number = b.upc_number
            WHERE b.store_number = :storeNumber
              AND b.division_number = :divisionNumber
            ORDER BY p.product_name
            """;

    /**
     * Retrieves for ordering search functionality
     * @return list of product item objects which contains the product UPCs and names
     */

    @Override
    public List<ProductItem> getProducts(String storeNumber, String divisionNumber) {
        LOG.info("Retrieving all product UPCs and names for store {} and division {}", storeNumber, divisionNumber);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("storeNumber", storeNumber)
                .addValue("divisionNumber", divisionNumber);

        return namedParameterJdbcTemplate.query(
                GET_PRODUCTS,
                params,
                productsMapper
        );
    }
}
