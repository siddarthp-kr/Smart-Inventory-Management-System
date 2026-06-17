package mocksims.project.backend.repository;

import mocksims.project.backend.api.domain.OrderHistoryRequest;
import mocksims.project.backend.domain.mapper.OrderHistoryMapper;
import mocksims.project.backend.exception.MockSimsCustomException;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import mocksims.project.backend.api.domain.OrderHistoryRecord;

import java.util.List;
import java.util.Objects;

@Repository
public class OrderHistoryRepositoryImpl implements OrderHistoryRepository {
    private static final String SQL_GET_ORDER_HISTORY = "SELECT \n" +
            "    ord.product_order_id,\n" +
            "    ord.user_euid,\n" +
            "    inv.upc_number,\n" +
            "    inv.quantity,\n" +
            "    inv.order_date,\n" +
            "    prod.product_name\n" +
            "FROM ORDER_TRANSACTION_INFO ord\n" +
            "INNER JOIN PRODUCT_INVENTORY_INFO inv ON ord.product_order_id = inv.product_order_id\n" +
            "INNER JOIN PRODUCT_BASIC_INFO prod ON inv.upc_number = prod.upc_number\n" +
            "WHERE ord.store_number = :STORE_NUMBER AND ord.division_number = :DIVISION_NUMBER\n" +
            "ORDER BY ord.product_order_id DESC;";

    private static final String STORE_NUMBER = "STORE_NUMBER";
    private static final String DIVISION_NUMBER = "DIVISION_NUMBER";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public OrderHistoryRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(Objects.requireNonNull(jdbcTemplate.getDataSource()));
    }

    public List<OrderHistoryRecord> getOrderHistory(OrderHistoryRequest orderHistoryRequest) throws MockSimsCustomException {
        OrderHistoryMapper orderHistoryMapper = new OrderHistoryMapper();

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue(STORE_NUMBER, orderHistoryRequest.getStoreNumber())
                .addValue(DIVISION_NUMBER, orderHistoryRequest.getDivisionNumber());
        List<OrderHistoryRecord> orders;
        try {
            orders = namedParameterJdbcTemplate.query(SQL_GET_ORDER_HISTORY, mapSqlParameterSource, orderHistoryMapper);
        } catch (DataAccessException e){
            throw new MockSimsCustomException(500, "Failed to get order history for division " + orderHistoryRequest.getDivisionNumber() + " store " + orderHistoryRequest.getStoreNumber() + ". " + e.getMessage());
        }

        return orders;
    }
}
