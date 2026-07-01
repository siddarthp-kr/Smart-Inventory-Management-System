package mocksims.project.backend.repository;

import mocksims.project.backend.api.domain.MarkdownItemRequest;
import mocksims.project.backend.exception.MockSimsCustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class MarkdownItemRepositoryImpl implements MarkdownItemRepository {

    private final String SQL_GET_QOD = """
            SELECT qod_number
            FROM PRODUCT_BOH_INFO
            WHERE store_number = :STORE_NUMBER AND division_number = :DIVISION_NUMBER AND upc_number = :UPC_NUMBER;""";

    private final String SQL_GET_QOM = """
            SELECT qom_number
            FROM PRODUCT_BOH_INFO
            WHERE store_number = :STORE_NUMBER AND division_number = :DIVISION_NUMBER AND upc_number = :UPC_NUMBER;""";

    private final String SQL_GET_PRICE = """
            SELECT standard_price
            FROM PRODUCT_BASIC_INFO
            WHERE upc_number = :UPC_NUMBER;
            """;

    private final String SQL_UPDATE_QOD_AND_QOM = """
            UPDATE PRODUCT_BOH_INFO
            SET qod_number = qod_number - :QUANTITY,
                qom_number = qom_number + :QUANTITY
            WHERE store_number = :STORE_NUMBER AND division_number = :DIVISION_NUMBER AND upc_number = :UPC_NUMBER;
            """;
    private final String SQL_UPDATE_ALERT_ACTION_INFO = """
            UPDATE PDM_ALERTS
            SET is_active = :IS_ACTIVE, alert_actioned_time = :ACTIONED_TIME, alert_actioned_user_euid = :USER_EUID, alert_actioned_code = :ACTION_CODE
            WHERE alert_id = :ALERT_ID;
            """;

    private final String SQL_GET_FIRST_MARKDOWN_PERCENT = """
            SELECT first_markdown_percent
            FROM PDM_ALERTS
            WHERE alert_id = :ALERT_ID;
            """;

    private final String SQL_INSERT_MD_TRANSACTION_INFO = """
            INSERT INTO MD_TRANSACTIONS (user_euid, store_number, division_number, upc_number, qod_before_transaction, qom_before_transaction, action_time, quantity_marked_down, original_price, new_price)
            VALUES(:USER_EUID, :STORE_NUMBER, :DIVISION_NUMBER, :UPC_NUMBER, :QOD_BEFORE_TRANSACTION, :QOM_BEFORE_TRANSACTION, :ACTION_TIME, :QUANTITY_MARKED_DOWN, :ORIGINAL_PRICE, :NEW_PRICE);
            """;
    private final String SQL_DEACTIVATE_ACTIVE_DUE_DUPLICATE_ALERTS_FOR_UPC = """
        UPDATE PDM_ALERTS
        SET is_active = FALSE,
            alert_actioned_time = :ACTIONED_TIME,
            alert_actioned_user_euid = :USER_EUID,
            alert_actioned_code = :ACTION_CODE
        WHERE store_number = :STORE_NUMBER
          AND division_number = :DIVISION_NUMBER
          AND upc_number = :UPC_NUMBER
          AND is_active = TRUE
          AND markdown_after_date <= CURRENT_DATE
        """;

    private final String QOD_BEFORE_TRANSACTION = "QOD_BEFORE_TRANSACTION";
    private final String QOM_BEFORE_TRANSACTION = "QOM_BEFORE_TRANSACTION";
    private final String ACTION_TIME = "ACTION_TIME";
    private final String QUANTITY_MARKED_DOWN = "QUANTITY_MARKED_DOWN";
    private final String ORIGINAL_PRICE = "ORIGINAL_PRICE";
    private final String NEW_PRICE = "NEW_PRICE";

    private final String STORE_NUMBER = "STORE_NUMBER";
    private final String DIVISION_NUMBER = "DIVISION_NUMBER";
    private final String UPC_NUMBER = "UPC_NUMBER";
    private final String QUANTITY = "QUANTITY";

    private final String ALERT_ID = "ALERT_ID";
    private final String ACTIONED_TIME = "ACTIONED_TIME";
    private final String ACTION_CODE = "ACTION_CODE";
    private final String IS_ACTIVE = "IS_ACTIVE";
    private final String USER_EUID = "USER_EUID";



    private static final Logger LOG = LoggerFactory.getLogger(MarkdownItemRepositoryImpl.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public MarkdownItemRepositoryImpl (JdbcTemplate jdbcTemplate){
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Override
    public Integer getQodNumber(String storeNumber, String divisionNumber, String upcNumber) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue(DIVISION_NUMBER, divisionNumber)
                .addValue(STORE_NUMBER, storeNumber)
                .addValue(UPC_NUMBER, upcNumber);

        try {
            return namedParameterJdbcTemplate.queryForObject(SQL_GET_QOD, mapSqlParameterSource, Integer.class);
        } catch (DataAccessException e){
            LOG.error("Failed to get QOD Number for UPC {} at Store {} in Division {}.", upcNumber, storeNumber, divisionNumber, e);
            throw new MockSimsCustomException(500, "Failed to get QOD number");
        }
    }

    @Override
    public Integer getQomNumber(String storeNumber, String divisionNumber, String upcNumber) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue(DIVISION_NUMBER, divisionNumber)
                .addValue(STORE_NUMBER, storeNumber)
                .addValue(UPC_NUMBER, upcNumber);

        try {
            return namedParameterJdbcTemplate.queryForObject(SQL_GET_QOM, mapSqlParameterSource, Integer.class);
        } catch (DataAccessException e){
            LOG.error("Failed to get QOM Number for UPC {} at Store {} in Division {}.", upcNumber, storeNumber, divisionNumber, e);
            throw new MockSimsCustomException(500, "Failed to get QOM number");
        }
    }

    @Override
    public Double getStandardPrice(String upcNumber) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource().addValue(UPC_NUMBER, upcNumber);

        try {
            return namedParameterJdbcTemplate.queryForObject(SQL_GET_PRICE, mapSqlParameterSource, Double.class);
        } catch (DataAccessException e) {
            LOG.error("Failed to get price of UPC {}", upcNumber, e);
            throw new MockSimsCustomException(500, "Failed to get price of item");
        }
    }

    @Override
    public void updateQodAndQom(String storeNumber, String divisionNumber, String upcNumber, Integer quantity) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue(DIVISION_NUMBER, divisionNumber)
                .addValue(STORE_NUMBER, storeNumber)
                .addValue(UPC_NUMBER, upcNumber)
                .addValue(QUANTITY, quantity);

        int numRowsUpdated;

        try {
            numRowsUpdated = namedParameterJdbcTemplate.update(SQL_UPDATE_QOD_AND_QOM, mapSqlParameterSource);
        } catch (DataAccessException e){
            LOG.error("Failed to update BOH.", e);
            throw new MockSimsCustomException(500, "Error updating BOH.");
        }

        if(numRowsUpdated != 1){
            LOG.error("Failed to update BOH. Did not update exactly one row.");
            throw new MockSimsCustomException(500, "Error updating BOH.");
        }
    }


    @Override
    public void updatePdmAlert(Integer alertId, LocalDateTime actionedTime, String userEuid) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue(ALERT_ID, alertId)
                .addValue(ACTIONED_TIME, actionedTime)
                .addValue(IS_ACTIVE, false)
                .addValue(USER_EUID, userEuid)
                .addValue(ACTION_CODE, "MD");

        int numRowsUpdated;

        try {
            numRowsUpdated = namedParameterJdbcTemplate.update(SQL_UPDATE_ALERT_ACTION_INFO, mapSqlParameterSource);
        } catch (DataAccessException e){
            LOG.error("Failed to update PDM Alert.", e);
            throw new MockSimsCustomException(500, "Failed to update PDM alert.");
        }

        if(numRowsUpdated != 1){
            LOG.error("Failed to update PDM Alert ID = {}. Did not update exactly row.", alertId);
            throw new MockSimsCustomException(500, "Failed to update PDM alert.");
        }
    }

    @Override
    public Integer getFirstMarkdownPercent(Integer alertId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource().addValue(ALERT_ID, alertId);

        try{
            return namedParameterJdbcTemplate.queryForObject(SQL_GET_FIRST_MARKDOWN_PERCENT, mapSqlParameterSource, Integer.class);
        } catch (DataAccessException e){
            LOG.error("Failed to get first markdown percent.", e);
            throw new MockSimsCustomException(500, "Failed to get first markdown percent.");
        }
    }



    @Override
    public void insertMarkdownTransactionInfo(MarkdownItemRequest markdownItemRequest, Double originalPrice, Double newPrice, LocalDateTime actionedTime, Integer qodBeforeTransaction, Integer qomBeforeTransaction) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue(USER_EUID, markdownItemRequest.getUserEuid())
                .addValue(STORE_NUMBER, markdownItemRequest.getStoreNumber())
                .addValue(DIVISION_NUMBER, markdownItemRequest.getDivisionNumber())
                .addValue(UPC_NUMBER, markdownItemRequest.getUpcNumber())
                .addValue(QOD_BEFORE_TRANSACTION, qodBeforeTransaction)
                .addValue(QOM_BEFORE_TRANSACTION, qomBeforeTransaction)
                .addValue(ACTION_TIME, actionedTime)
                .addValue(QUANTITY_MARKED_DOWN, markdownItemRequest.getQuantity())
                .addValue(ORIGINAL_PRICE, originalPrice)
                .addValue(NEW_PRICE, newPrice);

        try {
            namedParameterJdbcTemplate.update(SQL_INSERT_MD_TRANSACTION_INFO, mapSqlParameterSource);
        } catch (DataAccessException e){
            LOG.error("Failed to insert markdown transaction info.", e);
            throw new MockSimsCustomException(500, "Failed to insert markdown transaction info.");
        }
    }

    @Override
    public void deactivateActiveDueDuplicateAlertsForUpc(String storeNumber, String divisionNumber, String upcNumber, LocalDateTime actionedTime, String userEuid) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue(STORE_NUMBER, storeNumber)
                .addValue(DIVISION_NUMBER, divisionNumber)
                .addValue(UPC_NUMBER, upcNumber)
                .addValue(ACTIONED_TIME, actionedTime)
                .addValue(USER_EUID, userEuid)
                .addValue(ACTION_CODE, "DP");

        try {
            namedParameterJdbcTemplate.update(SQL_DEACTIVATE_ACTIVE_DUE_DUPLICATE_ALERTS_FOR_UPC, mapSqlParameterSource);
        } catch (DataAccessException error) {
            LOG.error("Failed to deactivate active due duplicate PDM alerts for UPC {} at Store {} in Division {}.", upcNumber, storeNumber, divisionNumber, error);

            throw new MockSimsCustomException(500, "Failed to deactivate duplicate active due PDM alerts.");
        }
    }

}
