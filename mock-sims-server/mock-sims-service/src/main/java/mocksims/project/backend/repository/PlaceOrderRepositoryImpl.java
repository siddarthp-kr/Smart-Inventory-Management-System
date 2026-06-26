package mocksims.project.backend.repository;

import mocksims.project.backend.api.domain.PlaceOrderItem;
import mocksims.project.backend.exception.MockSimsCustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class PlaceOrderRepositoryImpl implements PlaceOrderRepository {

    private static final Logger LOG = LoggerFactory.getLogger(PlaceOrderRepositoryImpl.class);

    private static final String QUANTITY = "QUANTITY";
    private static final String UPC_NUMBER = "UPC_NUMBER";
    private static final String STORE_NUMBER = "STORE_NUMBER";
    private static final String DIVISION_NUMBER = "DIVISION_NUMBER";

    private static final String USER_EUID = "USER_EUID";
    private static final String ORDER_PLACED_TIME = "ORDER_PLACED_TIME";

    private static final String GENERAL_ORDER_ID = "GENERAL_ORDER_ID";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    private static final String ORDER_DATE = "ORDER_DATE";
    private static final String IS_ACTIVE = "IS_ACTIVE";

    private static final String SUBCOMMODITY_NUMBER = "SUBCOMMODITY_NUMBER";
    private static final String QOD_BEFORE_TRANSACTION = "QOD_BEFORE_TRANSACTION";


    private static final String SQL_INSERT_ORDER_TRANSACTION_INFO = "INSERT INTO ORDER_TRANSACTION_INFO (store_number, division_number, placed_by_user_euid, order_placed_time, order_received) VALUES (:STORE_NUMBER, :DIVISION_NUMBER, :USER_EUID, :ORDER_PLACED_TIME, FALSE)";
    private static final String SQL_QUERY_PRODUCT_BASIC_INFO_SUBCOMMODITY_NUMBER = "SELECT subcommodity_number FROM PRODUCT_BASIC_INFO WHERE upc_number = :UPC_NUMBER";

    private static final String SQL_QUERY_MD_RULES_EXPIRATION_DATE = "SELECT days_after_order_to_set_exp FROM MARKDOWN_RULES WHERE subcommodity_number = :SUBCOMMODITY_NUMBER";

    private static final String SQL_INSERT_PRODUCT_INVENTORY_INFO = "INSERT INTO PRODUCT_INVENTORY_INFO (general_order_id, upc_number, quantity, expiration_date, order_date, is_active) VALUES (:GENERAL_ORDER_ID, :UPC_NUMBER, :QUANTITY, :EXPIRATION_DATE, :ORDER_DATE, :IS_ACTIVE)";

    private static final String SQL_INSERT_ORDER_MOVEMENT_TRANSACTIONS = "INSERT INTO ORDER_MOVEMENT_TRANSACTIONS (general_order_id,upc_number,quantity,qod_before_transaction) VALUES (:GENERAL_ORDER_ID,:UPC_NUMBER,:QUANTITY,:QOD_BEFORE_TRANSACTION)";



    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PlaceOrderRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(Objects.requireNonNull(jdbcTemplate.getDataSource()));
    }

    @Override
    public Long insertOrderTransactionInfo(String storeNumber, String divisionNumber, String userEuid, LocalDateTime timeOrderPlaced) throws DataAccessException{

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue(STORE_NUMBER, storeNumber)
                .addValue(DIVISION_NUMBER, divisionNumber)
                .addValue(USER_EUID, userEuid)
                .addValue(ORDER_PLACED_TIME, timeOrderPlaced);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            namedParameterJdbcTemplate.update(SQL_INSERT_ORDER_TRANSACTION_INFO, mapSqlParameterSource, keyHolder, new String[] {"general_order_id"});
        } catch (DataAccessException e){
            throw new MockSimsCustomException(500, "Failed to perform order placed by user " + userEuid + " at " + timeOrderPlaced.toString() + ". " + e.getMessage());
        }


        Number key = keyHolder.getKey();

        LOG.info("Inserted order transaction successfully. ID: {}, User: {}, Time: {}", key, userEuid, timeOrderPlaced);

        if(key == null){
            throw new MockSimsCustomException(500, "Could not retrieve product_order_id for order place by user " + userEuid + " at " + timeOrderPlaced.toString() + ".");
        }

        return key.longValue();
    }

    @Override
    public void insertProductInventoryInfo(long orderId, LocalDate orderDate, List<PlaceOrderItem> items) throws DataAccessException {
        try {
            List<MapSqlParameterSource> batchArgs = new ArrayList<>();

            for(PlaceOrderItem item: items){
                MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                        .addValue(GENERAL_ORDER_ID, orderId)
                        .addValue(UPC_NUMBER, item.getUpcNumber())
                        .addValue(QUANTITY, item.getQuantity())
                        .addValue(EXPIRATION_DATE, item.getExpirationDate())
                        .addValue(ORDER_DATE, orderDate)
                        .addValue(IS_ACTIVE, item.getIsActive());

                batchArgs.add(mapSqlParameterSource);
            }

            namedParameterJdbcTemplate.batchUpdate(SQL_INSERT_PRODUCT_INVENTORY_INFO, batchArgs.toArray(new MapSqlParameterSource[batchArgs.size()]));
        } catch (DataAccessException e){
            throw new MockSimsCustomException(500, "Failed to insert product inventory info for order " + orderId + ". " + e.getMessage());
        }
    }


    @Override
    public void insertOrderMovementTransactions(long orderId, List<PlaceOrderItem> items) throws DataAccessException {
        try {
            List<MapSqlParameterSource> batchArgs = new ArrayList<>();

            for (PlaceOrderItem item : items) {
                MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                        .addValue(GENERAL_ORDER_ID, orderId)
                        .addValue(UPC_NUMBER, item.getUpcNumber())
                        .addValue(QUANTITY, item.getQuantity())
                        .addValue(QOD_BEFORE_TRANSACTION, null);

                batchArgs.add(mapSqlParameterSource);
            }

            namedParameterJdbcTemplate.batchUpdate(SQL_INSERT_ORDER_MOVEMENT_TRANSACTIONS, batchArgs.toArray(new MapSqlParameterSource[0]));
        } catch (DataAccessException error) {
            throw new MockSimsCustomException(500, "Failed to insert order movement transactions for order " + orderId + ". " + error.getMessage());
        }
    }

    @Override
    public String getSubcommodityNumber(String upcNumber) throws DataAccessException {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource().addValue(UPC_NUMBER, upcNumber);

        String subcommodityNumber = "";

        try {
            subcommodityNumber = namedParameterJdbcTemplate.queryForObject(SQL_QUERY_PRODUCT_BASIC_INFO_SUBCOMMODITY_NUMBER, mapSqlParameterSource, String.class);
        } catch (DataAccessException e){
            throw new MockSimsCustomException(500, "Failed to get subcommodity for upc " + upcNumber + ". " + e.getMessage());
        }

        return subcommodityNumber;
    }

    @Override
    public Integer getNumberOfDaysBeforeExpiration(String subcommodityNumber) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource().addValue(SUBCOMMODITY_NUMBER, subcommodityNumber);

        Integer numberOfDaysBeforeExpiration = 0;

        try{
            numberOfDaysBeforeExpiration = namedParameterJdbcTemplate.queryForObject(SQL_QUERY_MD_RULES_EXPIRATION_DATE, mapSqlParameterSource, Integer.class);
        } catch (DataAccessException e){
            throw new MockSimsCustomException(500, "Failed to get day number for subcommodity " + subcommodityNumber + ". " + e.getMessage());
        }

        return numberOfDaysBeforeExpiration;
    }
}