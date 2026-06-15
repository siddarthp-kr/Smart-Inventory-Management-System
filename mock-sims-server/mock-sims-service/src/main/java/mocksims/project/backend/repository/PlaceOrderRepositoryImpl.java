package mocksims.project.backend.repository;

import mocksims.project.backend.exception.CustomException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

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


    private static final String SQL_UPDATE_BOH_INFO = "UPDATE PRODUCT_BOH_INFO  SET qod_number = qod_number + :QUANTITY  WHERE upc_number = :UPC_NUMBER AND store_number = :STORE_NUMBER AND division_number = :DIVISION_NUMBER";
    private static final String SQL_INSERT_ORDER_TRANSACTION_INFO = "";
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
            throw new CustomException(404, "Error: BOH information record for product " + upcNumber + " does not exist");
        }
    }
    public void  updateOrderTransactionInfo(String storeNumber, String divisionNumber, String userEuid){

    }
    public void updateProductInventoryInfo(String upcNumber, int quantity){

    }
}