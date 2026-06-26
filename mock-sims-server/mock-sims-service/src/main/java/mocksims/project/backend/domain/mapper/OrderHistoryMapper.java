package mocksims.project.backend.domain.mapper;
import mocksims.project.backend.api.domain.OrderHistoryRecord;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class OrderHistoryMapper implements RowMapper<OrderHistoryRecord> {

    private static final String GENERAL_ORDER_ID = "general_order_id";
    private static final String PLACED_BY_USER_EUID = "placed_by_user_euid";
    private static final String ORDER_PLACED_TIME = "order_placed_time";
    private static final String ORDER_RECEIVED = "order_received";
    private static final String RECEIVED_BY_USER_EUID = "received_by_user_euid";
    private static final String ORDER_RECEIVED_TIME = "order_received_time";
    private static final String UPC_NUMBER = "upc_number";
    private static final String PRODUCT_NAME = "product_name";
    private static final String QUANTITY = "quantity";

    @Override
    public OrderHistoryRecord mapRow(ResultSet rs, int index) throws SQLException {
        OrderHistoryRecord orderHistoryRecord = new OrderHistoryRecord();

        orderHistoryRecord.setOrderId(rs.getInt(GENERAL_ORDER_ID));
        orderHistoryRecord.setPlacedByUserEuid(rs.getString(PLACED_BY_USER_EUID));
        Timestamp orderPlacedTimestamp = rs.getTimestamp(ORDER_PLACED_TIME);
        if (orderPlacedTimestamp != null) {
            orderHistoryRecord.setOrderPlacedTime(orderPlacedTimestamp.toLocalDateTime());
        }
        orderHistoryRecord.setOrderReceived(rs.getObject(ORDER_RECEIVED, Boolean.class));
        orderHistoryRecord.setReceivedByUserEuid(rs.getString(RECEIVED_BY_USER_EUID));
        Timestamp orderReceivedTimestamp = rs.getTimestamp(ORDER_RECEIVED_TIME);
        if (orderReceivedTimestamp != null) {
            orderHistoryRecord.setOrderReceivedTime(orderReceivedTimestamp.toLocalDateTime());
        }
        orderHistoryRecord.setUpcNumber(rs.getString(UPC_NUMBER));
        orderHistoryRecord.setProductName(rs.getString(PRODUCT_NAME));
        orderHistoryRecord.setQuantity(rs.getInt(QUANTITY));

        return orderHistoryRecord;
    }
}
