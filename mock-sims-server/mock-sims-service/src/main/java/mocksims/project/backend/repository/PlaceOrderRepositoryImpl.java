package mocksims.project.backend.repository;

import mocksims.project.backend.exception.MockSimsCustomException;
import mocksims.project.backend.exception.RowNotFoundException;
import mocksims.project.backend.service.PlaceOrderServiceImpl;
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
    private static final String ORDER_RECEIVED_TIME = "ORDER_RECEIVED_TIME";

    private static final String PRODUCT_ORDER_ID = "PRODUCT_ORDER_ID";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    private static final String ORDER_DATE = "ORDER_DATE";
    private static final String IS_ACTIVE = "IS_ACTIVE";

    private static final String SUBCOMMODITY_NUMBER = "SUBCOMMODITY_NUMBER";

    private static final String SQL_UPDATE_BOH_INFO = "UPDATE PRODUCT_BOH_INFO  SET qod_number = qod_number + :QUANTITY  WHERE upc_number = :UPC_NUMBER AND store_number = :STORE_NUMBER AND division_number = :DIVISION_NUMBER";
    private static final String SQL_INSERT_ORDER_TRANSACTION_INFO = "INSERT INTO ORDER_TRANSACTION_INFO (store_number, division_number, user_euid, order_placed_time, order_received_time) VALUES (:STORE_NUMBER, :DIVISION_NUMBER, :USER_EUID, :ORDER_PLACED_TIME, :ORDER_RECEIVED_TIME)";
    private static final String SQL_QUERY_PRODUCT_BASIC_INFO_SUBCOMMODITY_NUMBER = "SELECT subcommodity_number FROM PRODUCT_BASIC_INFO WHERE upc_number = :UPC_NUMBER";

    private static final String SQL_QUERY_MD_RULES_EXPIRATION_DATE = "SELECT days_after_order_to_set_exp FROM MARKDOWN_RULES WHERE subcommodity_number = :SUBCOMMODITY_NUMBER";

    private static final String SQL_INSERT_PRODUCT_INVENTORY_INFO = "INSERT INTO PRODUCT_INVENTORY_INFO (product_order_id, upc_number, quantity, expiration_date, order_date, is_active) VALUES (:PRODUCT_ORDER_ID, :UPC_NUMBER, :QUANTITY, :EXPIRATION_DATE, :ORDER_DATE, :IS_ACTIVE)";


    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PlaceOrderRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(Objects.requireNonNull(jdbcTemplate.getDataSource()));
    }

    @Override
    public void updateBohInfo(String storeNumber, String divisionNumber, String upcNumber, int quantity) throws DataAccessException{

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue(QUANTITY, quantity)
                .addValue(UPC_NUMBER, upcNumber)
                .addValue(STORE_NUMBER, storeNumber)
                .addValue(DIVISION_NUMBER, divisionNumber);

        int numRowsUpdated = 0;

        try {
            numRowsUpdated = namedParameterJdbcTemplate.update(SQL_UPDATE_BOH_INFO, mapSqlParameterSource);
        } catch (DataAccessException e) {
            throw new MockSimsCustomException(500, e.getMessage());
        }

        if(numRowsUpdated == 0){
            throw new MockSimsCustomException(404, "Row Not Found: BOH information record for product " + upcNumber + " does not exist at store " + storeNumber + " in division " + divisionNumber);
        }
    }

    @Override
    public Long insertOrderTransactionInfo(String storeNumber, String divisionNumber, String userEuid, LocalDateTime timeOrderPlaced, LocalDateTime timeOrderReceived) throws DataAccessException{

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue(STORE_NUMBER, storeNumber)
                .addValue(DIVISION_NUMBER, divisionNumber)
                .addValue(USER_EUID, userEuid)
                .addValue(ORDER_PLACED_TIME, timeOrderPlaced)
                .addValue(ORDER_RECEIVED_TIME, timeOrderReceived);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            namedParameterJdbcTemplate.update(SQL_INSERT_ORDER_TRANSACTION_INFO, mapSqlParameterSource, keyHolder, new String[] {"product_order_id"});
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
    public void insertProductInventoryInfo(String upcNumber, int quantity, long orderId, LocalDate orderDate, LocalDate expirationDate, boolean orderIsActive) throws DataAccessException {

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue(PRODUCT_ORDER_ID, orderId)
                .addValue(UPC_NUMBER, upcNumber)
                .addValue(QUANTITY, quantity)
                .addValue(EXPIRATION_DATE, expirationDate)
                .addValue(ORDER_DATE, orderDate)
                .addValue(IS_ACTIVE, orderIsActive);

        try {
            namedParameterJdbcTemplate.update(SQL_INSERT_PRODUCT_INVENTORY_INFO, mapSqlParameterSource);
        } catch (DataAccessException e){
            throw new MockSimsCustomException(500, "Failed to insert product inventory info for order " + orderId + ". " + e.getMessage());
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