package mocksims.project.backend.repository;

import mocksims.project.backend.api.domain.ReceiveOrderItemRecord;
import mocksims.project.backend.exception.MockSimsCustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

@Repository
public class ReceiveOrderRepositoryImpl implements ReceiveOrderRepository {

    private static final Logger LOG = LoggerFactory.getLogger(ReceiveOrderRepositoryImpl.class);

    private static final String STORE_NUMBER = "STORE_NUMBER";
    private static final String DIVISION_NUMBER = "DIVISION_NUMBER";
    private static final String GENERAL_ORDER_ID = "GENERAL_ORDER_ID";
    private static final String UPC_NUMBER = "UPC_NUMBER";
    private static final String UPC_NUMBERS = "UPC_NUMBERS";
    private static final String QUANTITY = "QUANTITY";
    private static final String QOD_BEFORE_TRANSACTION = "QOD_BEFORE_TRANSACTION";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    private static final String ORDER_DATE = "ORDER_DATE";
    private static final String IS_ACTIVE = "IS_ACTIVE";
    private static final String RECEIVED_BY_USER_EUID = "RECEIVED_BY_USER_EUID";
    private static final String ORDER_RECEIVED_TIME = "ORDER_RECEIVED_TIME";
    private static final String CANCELLED_BY_USER_EUID = "CANCELLED_BY_USER_EUID";
    private static final String ORDER_CANCELLED_TIME = "ORDER_CANCELLED_TIME";

    private static final String SQL_GET_ORDER_STATUS = """
            SELECT order_status
            FROM ORDER_TRANSACTION_INFO
            WHERE general_order_id = :GENERAL_ORDER_ID
              AND store_number = :STORE_NUMBER
              AND division_number = :DIVISION_NUMBER
            """;

    private static final String SQL_GET_ORDER_ITEMS = """
            SELECT
                upc_number,
                quantity
            FROM ORDER_MOVEMENT_TRANSACTIONS
            WHERE general_order_id = :GENERAL_ORDER_ID
            """;

    private static final String SQL_GET_QOD_BY_UPCS = """
            SELECT
                upc_number,
                qod_number
            FROM PRODUCT_BOH_INFO
            WHERE store_number = :STORE_NUMBER
              AND division_number = :DIVISION_NUMBER
              AND upc_number IN (:UPC_NUMBERS)
            """;

    private static final String SQL_GET_EXPIRATION_RULES_BY_UPCS = """
            SELECT
                p.upc_number,
                mr.days_after_order_to_set_exp
            FROM PRODUCT_BASIC_INFO p
            INNER JOIN MARKDOWN_RULES mr
                ON p.subcommodity_number = mr.subcommodity_number
            WHERE p.upc_number IN (:UPC_NUMBERS)
              AND mr.can_be_marked_down = TRUE
            """;

    private static final String SQL_UPDATE_QOD_BEFORE_TRANSACTION = """
            UPDATE ORDER_MOVEMENT_TRANSACTIONS
            SET qod_before_transaction = :QOD_BEFORE_TRANSACTION
            WHERE general_order_id = :GENERAL_ORDER_ID
              AND upc_number = :UPC_NUMBER
            """;

    private static final String SQL_INCREMENT_QOD = """
            UPDATE PRODUCT_BOH_INFO
            SET qod_number = qod_number + :QUANTITY
            WHERE store_number = :STORE_NUMBER
              AND division_number = :DIVISION_NUMBER
              AND upc_number = :UPC_NUMBER
            """;

    private static final String SQL_INSERT_PRODUCT_INVENTORY_INFO = """
            INSERT INTO PRODUCT_INVENTORY_INFO (
                general_order_id,
                upc_number,
                quantity,
                expiration_date,
                order_date,
                is_active
            )
            VALUES (
                :GENERAL_ORDER_ID,
                :UPC_NUMBER,
                :QUANTITY,
                :EXPIRATION_DATE,
                :ORDER_DATE,
                :IS_ACTIVE
            )
            """;

    private static final String SQL_UPDATE_ORDER_RECEIVED = """
            UPDATE ORDER_TRANSACTION_INFO
            SET order_status = 'RECEIVED',
                action_by_user_euid = :RECEIVED_BY_USER_EUID,
                order_action_time = :ORDER_RECEIVED_TIME
            WHERE general_order_id = :GENERAL_ORDER_ID
              AND store_number = :STORE_NUMBER
              AND division_number = :DIVISION_NUMBER
              AND order_status = 'PLACED'
            """;

    private static final String SQL_UPDATE_ORDER_CANCELLED = """
            UPDATE ORDER_TRANSACTION_INFO
            SET order_status = 'CANCELLED',
                action_by_user_euid = :CANCELLED_BY_USER_EUID,
                order_action_time = :ORDER_CANCELLED_TIME
            WHERE general_order_id = :GENERAL_ORDER_ID
              AND store_number = :STORE_NUMBER
              AND division_number = :DIVISION_NUMBER
              AND order_status = 'PLACED'
            """;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ReceiveOrderRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(Objects.requireNonNull(jdbcTemplate.getDataSource()));
    }

    @Override
    public String getOrderStatus(String storeNumber, String divisionNumber, Long orderId
    ) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(STORE_NUMBER, storeNumber)
                .addValue(DIVISION_NUMBER, divisionNumber)
                .addValue(GENERAL_ORDER_ID, orderId);

        try {
            return namedParameterJdbcTemplate.queryForObject(SQL_GET_ORDER_STATUS, params, String.class);
        } catch (EmptyResultDataAccessException error) {
            throw new MockSimsCustomException(404, "Order not found.");
        } catch (DataAccessException error) {
            LOG.error("Failed to get order status for order {}", orderId, error);
            throw new MockSimsCustomException(500, "Failed to retrieve order status.");
        }
    }

    @Override
    public List<ReceiveOrderItemRecord> getOrderItems(Long orderId) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue(GENERAL_ORDER_ID, orderId);

        try {
            return namedParameterJdbcTemplate.query(SQL_GET_ORDER_ITEMS, params, (rs, rowNum) -> new ReceiveOrderItemRecord(
                            rs.getString("upc_number"),
                            rs.getObject("quantity", Integer.class)
                    )
            );
        } catch (DataAccessException error) {
            LOG.error("Failed to get order items for order {}", orderId, error);
            throw new MockSimsCustomException(500, "Failed to retrieve order items.");
        }
    }

    @Override
    public Map<String, Integer> getQodNumbersByUpc(String storeNumber, String divisionNumber, List<String> upcNumbers) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(STORE_NUMBER, storeNumber)
                .addValue(DIVISION_NUMBER, divisionNumber)
                .addValue(UPC_NUMBERS, upcNumbers);

        try {
            return namedParameterJdbcTemplate.query(SQL_GET_QOD_BY_UPCS, params, rs -> {
                        Map<String, Integer> qodByUpc = new HashMap<>();
                        while (rs.next()) {
                            qodByUpc.put(
                                    rs.getString("upc_number"),
                                    rs.getObject("qod_number", Integer.class)
                            );
                        }
                        return qodByUpc;
                    }
            );
        } catch (DataAccessException error) {
            LOG.error("Failed to get QOD values for UPCs {}", upcNumbers, error);
            throw new MockSimsCustomException(500, "Failed to retrieve QOD values for order items.");
        }
    }

    @Override
    public Map<String, Integer> getDaysAfterOrderToSetExpByUpc(List<String> upcNumbers) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue(UPC_NUMBERS, upcNumbers);

        try {
            return namedParameterJdbcTemplate.query(SQL_GET_EXPIRATION_RULES_BY_UPCS, params, rs -> {
                        Map<String, Integer> daysAfterOrderToSetExpByUpc = new HashMap<>();
                        while (rs.next()) {
                            daysAfterOrderToSetExpByUpc.put(
                                    rs.getString("upc_number"),
                                    rs.getObject("days_after_order_to_set_exp", Integer.class)
                            );
                        }
                        return daysAfterOrderToSetExpByUpc;
                    }
            );
        } catch (DataAccessException error) {
            LOG.error("Failed to get expiration rules for UPCs {}", upcNumbers, error);
            throw new MockSimsCustomException(500, "Failed to retrieve expiration rules for order items.");
        }
    }

    @Override
    public void batchUpdateQodBeforeTransaction(Long orderId, Map<String, Integer> qodBeforeTransactionByUpc) {
        List<MapSqlParameterSource> batchArgs = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : qodBeforeTransactionByUpc.entrySet()) {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue(GENERAL_ORDER_ID, orderId)
                    .addValue(UPC_NUMBER, entry.getKey())
                    .addValue(QOD_BEFORE_TRANSACTION, entry.getValue());

            batchArgs.add(params);
        }

        try {
            int[] rowsUpdated = namedParameterJdbcTemplate.batchUpdate(SQL_UPDATE_QOD_BEFORE_TRANSACTION, batchArgs.toArray(new MapSqlParameterSource[0]));

            if (IntStream.of(rowsUpdated).sum() != qodBeforeTransactionByUpc.size()) {
                throw new MockSimsCustomException(500, "Failed to update qod_before_transaction for all order items.");
            }

        } catch (DataAccessException error) {
            LOG.error("Failed to batch update qod_before_transaction for order {}", orderId, error);
            throw new MockSimsCustomException(500, "Failed to update qod_before_transaction for order " + orderId);
        }
    }

    @Override
    public void batchIncrementQod(String storeNumber, String divisionNumber, List<ReceiveOrderItemRecord> orderItems) {
        List<MapSqlParameterSource> batchArgs = new ArrayList<>();

        for (ReceiveOrderItemRecord item : orderItems) {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue(STORE_NUMBER, storeNumber)
                    .addValue(DIVISION_NUMBER, divisionNumber)
                    .addValue(UPC_NUMBER, item.getUpcNumber())
                    .addValue(QUANTITY, item.getQuantity());

            batchArgs.add(params);
        }

        try {
            int[] rowsUpdated = namedParameterJdbcTemplate.batchUpdate(SQL_INCREMENT_QOD, batchArgs.toArray(new MapSqlParameterSource[0]));

            if (IntStream.of(rowsUpdated).sum() != orderItems.size()) {
                throw new MockSimsCustomException(500, "Failed to increment QOD for all order items.");
            }

        } catch (DataAccessException error) {
            LOG.error("Failed to batch increment QOD for order items.", error);
            throw new MockSimsCustomException(500, "Failed to increment QOD for order items.");
        }
    }

    @Override
    public void batchInsertProductInventoryInfo(Long orderId, List<ReceiveOrderItemRecord> orderItems, Map<String, LocalDate> expirationDateByUpc, LocalDate orderDate) {
        List<MapSqlParameterSource> batchArgs = new ArrayList<>();

        for (ReceiveOrderItemRecord item : orderItems) {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue(GENERAL_ORDER_ID, orderId)
                    .addValue(UPC_NUMBER, item.getUpcNumber())
                    .addValue(QUANTITY, item.getQuantity())
                    .addValue(EXPIRATION_DATE, expirationDateByUpc.get(item.getUpcNumber()))
                    .addValue(ORDER_DATE, orderDate)
                    .addValue(IS_ACTIVE, true);

            batchArgs.add(params);
        }

        try {
            int[] rowsInserted = namedParameterJdbcTemplate.batchUpdate(SQL_INSERT_PRODUCT_INVENTORY_INFO, batchArgs.toArray(new MapSqlParameterSource[0]));

            if (IntStream.of(rowsInserted).sum() != orderItems.size()) {
                throw new MockSimsCustomException(500, "Failed to insert product inventory info for all order items.");
            }

        } catch (DataAccessException error) {
            LOG.error("Failed to batch insert product inventory info.", error);
            throw new MockSimsCustomException(500, "Failed to insert product inventory info for order items.");
        }
    }

    @Override
    public void updateOrderStatusToReceived(String storeNumber, String divisionNumber, Long orderId, String actionByUserEuid, LocalDateTime orderActionTime) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(STORE_NUMBER, storeNumber)
                .addValue(DIVISION_NUMBER, divisionNumber)
                .addValue(GENERAL_ORDER_ID, orderId)
                .addValue(RECEIVED_BY_USER_EUID, actionByUserEuid)
                .addValue(ORDER_RECEIVED_TIME, orderActionTime);

        try {
            int rowsUpdated = namedParameterJdbcTemplate.update(SQL_UPDATE_ORDER_RECEIVED, params);

            if (rowsUpdated != 1) {
                throw new MockSimsCustomException(409, "Order cannot be received because it has already been actioned or could not be updated.");
            }

        } catch (DataAccessException error) {
            LOG.error("Failed to update received status for order {}", orderId, error);
            throw new MockSimsCustomException(500, "Failed to update order received status.");
        }
    }

    @Override
    public void updateOrderStatusToCancelled(String storeNumber, String divisionNumber, Long orderId, String actionByUserEuid, LocalDateTime orderActionTime
    ) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(STORE_NUMBER, storeNumber)
                .addValue(DIVISION_NUMBER, divisionNumber)
                .addValue(GENERAL_ORDER_ID, orderId)
                .addValue(CANCELLED_BY_USER_EUID, actionByUserEuid)
                .addValue(ORDER_CANCELLED_TIME, orderActionTime);

        try {
            int rowsUpdated = namedParameterJdbcTemplate.update(SQL_UPDATE_ORDER_CANCELLED, params);

            if (rowsUpdated != 1) {
                throw new MockSimsCustomException(409, "Order cannot be cancelled because it has already been actioned or could not be updated.");
            }

        } catch (DataAccessException error) {
            LOG.error("Failed to cancel order {}", orderId, error);
            throw new MockSimsCustomException(500, "Failed to cancel order.");
        }
    }
}
