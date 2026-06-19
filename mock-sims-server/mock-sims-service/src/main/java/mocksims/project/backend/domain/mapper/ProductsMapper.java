package mocksims.project.backend.domain.mapper;

import mocksims.project.backend.api.domain.ProductItem;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ProductsMapper implements RowMapper<ProductItem> {

    private static final String UPC_NUMBER = "upc_number";
    private static final String PRODUCT_NAME = "product_name";

    /**
     * Maps row from PRODUCT_BASIC_INFO into ProductItem object
     * @param rs the {@code ResultSet} to map (pre-initialized for the current row)
     * @param index the number of the current row
     * @return ProductItem which contains the product UPCs and names
     * @throws SQLException if the row can't be rad from the results
     */
    @Override
    public ProductItem mapRow(ResultSet rs, int index) throws SQLException {
        ProductItem productItem = new ProductItem();

        productItem.setUpcNumber(rs.getString(UPC_NUMBER));
        productItem.setProductName(rs.getString(PRODUCT_NAME));

        return productItem;
    }
}