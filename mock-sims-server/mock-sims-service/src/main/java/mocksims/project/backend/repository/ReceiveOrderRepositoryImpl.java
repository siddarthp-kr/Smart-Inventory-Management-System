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
import java.util.List;
import java.util.Objects;

@Repository
public class ReceiveOrderRepositoryImpl implements ReceiveOrderRepository {

    private static final Logger LOG = LoggerFactory.getLogger(ReceiveOrderRepositoryImpl.class);

    private static final String STORE_NUMBER = "STORE_NUMBER";
    private static final String DIVISION_NUMBER = "DIVISION_NUMBER";
    private static final String GENERAL_ORDER_ID = "GENERAL_ORDER_ID";
    private static final String UPC_NUMBER = "UPC_NUMBER";
    private static final String QUANTITY = "QUANTITY";
    private static final String QOD_BEFORE_TRANSACTION = "QOD_BEFORE_TRANSACTION";
    private static final String SUBCOMMODITY_NUMBER = "SUBCOMMODITY_NUMBER";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    private static final String ORDER_DATE = "ORDER_DATE";
    private static final String IS_ACTIVE = "IS_ACTIVE";
    private static final String RECEIVED_BY_USER_EUID = "RECEIVED_BY_USER_EUID";
    private static final String ORDER_RECEIVED_TIME = "ORDER_RECEIVED_TIME";

    private static final String SQL_GET_ORDER_RECEIVED_STATUS = """
            SELECT order_received
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

    private static final String SQL_GET_QOD = """
            SELECT qod_number
            FROM PRODUCT_BOH_INFO
            WHERE store_number = :STORE_NUMBER
              AND division_number = :DIVISION_NUMBER
              AND upc_number = :UPC_NUMBER
            """;

    private static final String SQL_INCREMENT_QOD = """
            UPDATE PRODUCT_BOH_INFO
            SET qod_number = qod_number + :QUANTITY
            WHERE store_number = :STORE_NUMBER
              AND division_number = :DIVISION_NUMBER
              AND upc_number = :UPC_NUMBER
            """;

    private static final String SQL_UPDATE_QOD_BEFORE_TRANSACTION = """
            UPDATE ORDER_MOVEMENT_TRANSACTIONS
            SET qod_before_transaction = :QOD_BEFORE_TRANSACTION
            WHERE general_order_id = :GENERAL_ORDER_ID
              AND upc_number = :UPC_NUMBER
            """;

    private static final String SQL_GET_SUBCOMMODITY_NUMBER = """
            SELECT subcommodity_number
            FROM PRODUCT_BASIC_INFO
            WHERE upc_number = :UPC_NUMBER
            """;

    private static final String SQL_GET_DAYS_BEFORE_EXPIRATION = """
            SELECT days_after_order_to_set_exp
            FROM MARKDOWN_RULES
            WHERE subcommodity_number = :SUBCOMMODITY_NUMBER AND can_be_marked_down = TRUE
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
            SET order_received = TRUE,
                received_by_user_euid = :RECEIVED_BY_USER_EUID,
                order_received_time = :ORDER_RECEIVED_TIME
            WHERE general_order_id = :GENERAL_ORDER_ID
              AND store_number = :STORE_NUMBER
              AND division_number = :DIVISION_NUMBER
              AND order_received = FALSE
            """;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ReceiveOrderRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
                Objects.requireNonNull(jdbcTemplate.getDataSource())
        );
    }

    @Override
    public Boolean getOrderReceivedStatus(String storeNumber, String divisionNumber, Long orderId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(STORE_NUMBER, storeNumber)
                .addValue(DIVISION_NUMBER, divisionNumber)
                .addValue(GENERAL_ORDER_ID, orderId);

        try {
            return namedParameterJdbcTemplate.queryForObject(
                    SQL_GET_ORDER_RECEIVED_STATUS,
                    params,
                    Boolean.class
            );
        } catch (EmptyResultDataAccessException error) {
            throw new MockSimsCustomException(404, "Order not found.");
        } catch (DataAccessException error) {
            LOG.error("Failed to get received status for order {}", orderId, error);
            throw new MockSimsCustomException(500, "Failed to retrieve order received status.");
        }
    }

    @Override
    public List<ReceiveOrderItemRecord> getOrderItems(Long orderId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(GENERAL_ORDER_ID, orderId);

        try {
            return namedParameterJdbcTemplate.query(
                    SQL_GET_ORDER_ITEMS,
                    params,
                    (rs, rowNum) -> new ReceiveOrderItemRecord(
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
    public Integer getQodNumber(String storeNumber, String divisionNumber, String upcNumber) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(STORE_NUMBER, storeNumber)
                .addValue(DIVISION_NUMBER, divisionNumber)
                .addValue(UPC_NUMBER, upcNumber);

        try {
            return namedParameterJdbcTemplate.queryForObject(
                    SQL_GET_QOD,
                    params,
                    Integer.class
            );
        } catch (EmptyResultDataAccessException error) {
            throw new MockSimsCustomException(404, "BOH record not found for UPC " + upcNumber);
        } catch (DataAccessException error) {
            LOG.error("Failed to get QOD for UPC {}", upcNumber, error);
            throw new MockSimsCustomException(500, "Failed to retrieve QOD for UPC " + upcNumber);
        }
    }

    @Override
    public void incrementQod(String storeNumber, String divisionNumber, String upcNumber, Integer quantity) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(STORE_NUMBER, storeNumber)
                .addValue(DIVISION_NUMBER, divisionNumber)
                .addValue(UPC_NUMBER, upcNumber)
                .addValue(QUANTITY, quantity);

        try {
            int rowsUpdated = namedParameterJdbcTemplate.update(SQL_INCREMENT_QOD, params);

            if (rowsUpdated != 1) {
                throw new MockSimsCustomException(404, "Failed to update QOD for UPC " + upcNumber);
            }
        } catch (DataAccessException error) {
            LOG.error("Failed to increment QOD for UPC {}", upcNumber, error);
            throw new MockSimsCustomException(500, "Failed to increment QOD for UPC " + upcNumber);
        }
    }

    @Override
    public void updateQodBeforeTransaction(Long orderId, String upcNumber, Integer qodBeforeTransaction) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(GENERAL_ORDER_ID, orderId)
                .addValue(UPC_NUMBER, upcNumber)
                .addValue(QOD_BEFORE_TRANSACTION, qodBeforeTransaction);

        try {
            int rowsUpdated = namedParameterJdbcTemplate.update(SQL_UPDATE_QOD_BEFORE_TRANSACTION, params);

            if (rowsUpdated != 1) {
                throw new MockSimsCustomException(
                        404,
                        "Failed to update qod_before_transaction for UPC " + upcNumber
                );
            }
        } catch (DataAccessException error) {
            LOG.error("Failed to update qod_before_transaction for UPC {}", upcNumber, error);
            throw new MockSimsCustomException(
                    500,
                    "Failed to update qod_before_transaction for UPC " + upcNumber
            );
        }
    }

    @Override
    public String getSubcommodityNumber(String upcNumber) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(UPC_NUMBER, upcNumber);

        try {
            return namedParameterJdbcTemplate.queryForObject(
                    SQL_GET_SUBCOMMODITY_NUMBER,
                    params,
                    String.class
            );
        } catch (DataAccessException error) {
            LOG.error("Failed to get subcommodity for UPC {}", upcNumber, error);
            throw new MockSimsCustomException(500, "Failed to get subcommodity for UPC " + upcNumber);
        }
    }


    @Override
    public Integer getNumberOfDaysBeforeExpiration(String subcommodityNumber) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(SUBCOMMODITY_NUMBER, subcommodityNumber);

        try {
            return namedParameterJdbcTemplate.queryForObject(
                    SQL_GET_DAYS_BEFORE_EXPIRATION,
                    params,
                    Integer.class
            );
        } catch (EmptyResultDataAccessException error) {
            return null;
        } catch (DataAccessException error) {
            LOG.error("Failed to get expiration rule for subcommodity {}", subcommodityNumber, error);
            throw new MockSimsCustomException(
                    500,
                    "Failed to get expiration rule for subcommodity " + subcommodityNumber
            );
        }
    }

    @Override
    public void insertProductInventoryInfo(
            Long orderId,
            String upcNumber,
            Integer quantity,
            LocalDate expirationDate,
            LocalDate orderDate,
            Boolean isActive
    ) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(GENERAL_ORDER_ID, orderId)
                .addValue(UPC_NUMBER, upcNumber)
                .addValue(QUANTITY, quantity)
                .addValue(EXPIRATION_DATE, expirationDate)
                .addValue(ORDER_DATE, orderDate)
                .addValue(IS_ACTIVE, isActive);

        try {
            namedParameterJdbcTemplate.update(SQL_INSERT_PRODUCT_INVENTORY_INFO, params);
        } catch (DataAccessException error) {
            LOG.error("Failed to insert product inventory info for order {}, UPC {}", orderId, upcNumber, error);
            throw new MockSimsCustomException(
                    500,
                    "Failed to insert product inventory info for UPC " + upcNumber
            );
        }
    }

    @Override
    public void updateOrderReceived(
            String storeNumber,
            String divisionNumber,
            Long orderId,
            String receivedByUserEuid,
            LocalDateTime orderReceivedTime
    ) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(STORE_NUMBER, storeNumber)
                .addValue(DIVISION_NUMBER, divisionNumber)
                .addValue(GENERAL_ORDER_ID, orderId)
                .addValue(RECEIVED_BY_USER_EUID, receivedByUserEuid)
                .addValue(ORDER_RECEIVED_TIME, orderReceivedTime);

        try {
            int rowsUpdated = namedParameterJdbcTemplate.update(SQL_UPDATE_ORDER_RECEIVED, params);

            if (rowsUpdated != 1) {
                throw new MockSimsCustomException(
                        409,
                        "Order has already been received or could not be updated."
                );
            }
        } catch (DataAccessException error) {
            LOG.error("Failed to update received status for order {}", orderId, error);
            throw new MockSimsCustomException(500, "Failed to update order received status.");
        }
    }
}