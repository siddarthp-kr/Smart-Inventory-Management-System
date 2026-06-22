package mocksims.project.backend.domain.mapper;
import mocksims.project.backend.api.domain.OrderHistoryRecord;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class OrderHistoryMapper implements RowMapper<OrderHistoryRecord> {

    private static final String GENERAL_ORDER_ID = "general_order_id";
    private static final String USER_EUID = "user_euid";
    private static final String ORDER_DATE = "order_date";
    private static final String UPC_NUMBER = "upc_number";
    private static final String PRODUCT_NAME = "product_name";
    private static final String QUANTITY = "quantity";

    @Override
    public OrderHistoryRecord mapRow(ResultSet rs, int index) throws SQLException {
        OrderHistoryRecord orderHistoryRecord = new OrderHistoryRecord();

        orderHistoryRecord.setOrderId(rs.getInt(GENERAL_ORDER_ID));
        orderHistoryRecord.setUserEuid(rs.getString(USER_EUID));
        orderHistoryRecord.setOrderPlacedDate(rs.getDate(ORDER_DATE).toLocalDate());
        orderHistoryRecord.setUpcNumber(rs.getString(UPC_NUMBER));
        orderHistoryRecord.setProductName(rs.getString(PRODUCT_NAME));
        orderHistoryRecord.setQuantity(rs.getInt(QUANTITY));

        return orderHistoryRecord;
    }
}
