package mocksims.project.backend.repository;

import mocksims.project.backend.exception.RowNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Objects;

/*
    Need to add asynchronous handling
    Need to add custom exception for
 */
@Repository
public class PlaceOrderRepositoryImpl implements PlaceOrderRepository {

    private static final String QUANTITY = "QUANTITY";
    private static final String UPC_NUMBER = "UPC_NUMBER";
    private static final String STORE_NUMBER = "STORE_NUMBER";
    private static final String DIVISION_NUMBER = "DIVISION_NUMBER";

    private static final String USER_EUID = "USER_EUID";
    private static final String ORDER_PLACED_TIME = "ORDER_PLACED_TIME";
    private static final String ORDER_RECEIVED_TIME = "ORDER_RECEIVED_TIME";

    private static final String PRODUCT_ORDER_ID = "PRODUCT_ORDER_ID";

    private static final String SQL_UPDATE_BOH_INFO = "UPDATE PRODUCT_BOH_INFO  SET qod_number = qod_number + :QUANTITY  WHERE upc_number = :UPC_NUMBER AND store_number = :STORE_NUMBER AND division_number = :DIVISION_NUMBER";
    private static final String SQL_INSERT_ORDER_TRANSACTION_INFO = "INSERT INTO ORDER_TRANSACTION_INFO (product_order_id, store_number, division_number, user_euid, order_placed_time, order_received_time) VALUES (:PRODUCT_ORDER_ID, :STORE_NUMBER, :DIVISION_NUMBER, :USER_EUID, :ORDER_PLACED_TIME, :ORDER_RECEIVED_TIME)";
    private static final String SQL_INSERT_PRODUCT_INVENTORY_INFO = "";


    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PlaceOrderRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(Objects.requireNonNull(jdbcTemplate.getDataSource()));
    }

    //this MUST have a row - needs a separate exception from daException for the row not being found (if num rows affected = 0)
    public void updateBohInfo(String storeNumber, String divisionNumber, String upcNumber, int quantity) throws DataAccessException{

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue(QUANTITY, quantity)
                .addValue(UPC_NUMBER, upcNumber)
                .addValue(STORE_NUMBER, storeNumber)
                .addValue(DIVISION_NUMBER, divisionNumber);

        int numRowsUpdated = namedParameterJdbcTemplate.update(SQL_UPDATE_BOH_INFO, mapSqlParameterSource);

        if(numRowsUpdated == 0){
            throw new RowNotFoundException(404, "Error: BOH information record for product " + upcNumber + " does not exist at store " + storeNumber + " in division " + divisionNumber);
        }
    }
    public long updateOrderTransactionInfo(String storeNumber, String divisionNumber, String userEuid, LocalDateTime timeOrderPlaced, LocalDateTime timeOrderReceived) throws DataAccessException{


        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue(STORE_NUMBER, storeNumber)
                .addValue(DIVISION_NUMBER, divisionNumber)
                .addValue(USER_EUID, userEuid)
                .addValue(ORDER_PLACED_TIME, timeOrderPlaced)
                .addValue(ORDER_RECEIVED_TIME, timeOrderReceived);

        //implement the keyholder here

        //implement the insert function here

        return 0;
    }
    public void updateProductInventoryInfo(String upcNumber, int quantity, long orderId){

    }
}