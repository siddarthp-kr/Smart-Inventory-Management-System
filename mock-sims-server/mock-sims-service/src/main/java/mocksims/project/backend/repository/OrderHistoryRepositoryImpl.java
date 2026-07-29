package mocksims.project.backend.repository;

import mocksims.project.backend.api.domain.OrderHistoryRequest;
import mocksims.project.backend.domain.mapper.OrderHistoryMapper;
import mocksims.project.backend.exception.MockSimsCustomException;
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
    private static final String SQL_GET_ORDER_HISTORY = "SELECT " +
            "    ord.general_order_id, " +
            "    ord.placed_by_user_euid, " +
            "    ord.order_placed_time," +
            "    ord.order_status," +
            "    ord.action_by_user_euid," +
            "    ord.order_action_time," +
            "    inv.upc_number, " +
            "    inv.quantity, " +
            "    prod.product_name " +
            "FROM ORDER_TRANSACTION_INFO ord " +
            "INNER JOIN ORDER_MOVEMENT_TRANSACTIONS inv ON ord.general_order_id = inv.general_order_id " +
            "INNER JOIN PRODUCT_BASIC_INFO prod ON inv.upc_number = prod.upc_number " +
            "WHERE ord.store_number = :STORE_NUMBER AND ord.division_number = :DIVISION_NUMBER " +
            "ORDER BY ord.general_order_id DESC " +
            "LIMIT 500";

    private static final String STORE_NUMBER = "STORE_NUMBER";
    private static final String DIVISION_NUMBER = "DIVISION_NUMBER";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public OrderHistoryRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(Objects.requireNonNull(jdbcTemplate.getDataSource()));
    }

    @Override
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
