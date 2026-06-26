package mocksims.project.backend.repository;

import mocksims.project.backend.api.domain.RfiItemRequest;
import mocksims.project.backend.exception.MockSimsCustomException;
import mocksims.project.backend.service.RfiItemServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class RfiItemRepositoryImpl implements RfiItemRepository{

    private final String SQL_GET_QOD = """
            SELECT qod_number
            FROM PRODUCT_BOH_INFO
            WHERE store_number = :STORE_NUMBER AND division_number = :DIVISION_NUMBER AND upc_number = :UPC_NUMBER;""";

    private final String SQL_GET_QOM = """
            SELECT qom_number
            FROM PRODUCT_BOH_INFO
            WHERE store_number = :STORE_NUMBER AND division_number = :DIVISION_NUMBER AND upc_number = :UPC_NUMBER;""";

    private final String SQL_DECREMENT_QOD = """
        UPDATE PRODUCT_BOH_INFO
        SET qod_number = qod_number - :QUANTITY
        WHERE store_number = :STORE_NUMBER AND division_number = :DIVISION_NUMBER AND upc_number = :UPC_NUMBER;
    """;

    private final String SQL_UPDATE_ALERT_ACTION_INFO = """
            UPDATE PDM_ALERTS
            SET is_active = :IS_ACTIVE, alert_actioned_time = :ACTIONED_TIME, alert_actioned_user_euid = :USER_EUID, alert_actioned_code = :ACTION_CODE
            WHERE alert_id = :ALERT_ID;
            """;

    private final String SQL_INSERT_RFI_TRANSACTION_INFO = """
            INSERT INTO RFI_TRANSACTIONS (user_euid, store_number, division_number, upc_number, qod_before_transaction, qom_before_transaction, action_time, quantity_removed, reason_code)
            VALUES(:USER_EUID, :STORE_NUMBER, :DIVISION_NUMBER, :UPC_NUMBER, :QOD_BEFORE_TRANSACTION, :QOM_BEFORE_TRANSACTION, :ACTION_TIME, :QUANTITY_REMOVED, :REASON_CODE);
            """;

    private final String QOD_BEFORE_TRANSACTION = "QOD_BEFORE_TRANSACTION";
    private final String QOM_BEFORE_TRANSACTION = "QOM_BEFORE_TRANSACTION";
    private final String ACTION_TIME = "ACTION_TIME";
    private final String QUANTITY_REMOVED = "QUANTITY_REMOVED";
    private final String REASON_CODE = "REASON_CODE";

    private final String STORE_NUMBER = "STORE_NUMBER";
    private final String DIVISION_NUMBER = "DIVISION_NUMBER";
    private final String UPC_NUMBER = "UPC_NUMBER";
    private final String QUANTITY = "QUANTITY";

    private final String ALERT_ID = "ALERT_ID";
    private final String ACTIONED_TIME = "ACTIONED_TIME";
    private final String ACTION_CODE = "ACTION_CODE";
    private final String IS_ACTIVE = "IS_ACTIVE";
    private final String USER_EUID = "USER_EUID";


    private static final Logger LOG = LoggerFactory.getLogger(RfiItemRepositoryImpl.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public RfiItemRepositoryImpl (JdbcTemplate jdbcTemplate){
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }


    @Override
    public Integer getQodNumber(String storeNumber, String divisionNumber, String upcNumber) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue(DIVISION_NUMBER, divisionNumber)
                .addValue(UPC_NUMBER, upcNumber)
                .addValue(STORE_NUMBER, storeNumber);

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
                .addValue(UPC_NUMBER, upcNumber)
                .addValue(STORE_NUMBER, storeNumber);

        try {
            return namedParameterJdbcTemplate.queryForObject(SQL_GET_QOM, mapSqlParameterSource, Integer.class);
        } catch (DataAccessException e){
            LOG.error("Failed to get QOM Number for UPC {} at Store {} in Division {}.", upcNumber, storeNumber, divisionNumber, e);
            throw new MockSimsCustomException(500, "Failed to get QOM number");
        }
    }

    @Override
    public void decrementQod(String storeNumber, String divisionNumber, String upcNumber, Integer quantity) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue(DIVISION_NUMBER, divisionNumber)
                .addValue(UPC_NUMBER, upcNumber)
                .addValue(STORE_NUMBER, storeNumber)
                .addValue(QUANTITY, quantity);

        int numRowsUpdated;

        try {
            numRowsUpdated = namedParameterJdbcTemplate.update(SQL_DECREMENT_QOD, mapSqlParameterSource);
        } catch (DataAccessException e) {
            LOG.error("Failed to decrement QOD for UPC {} at Store {} in Division {}.", upcNumber, storeNumber, divisionNumber, e);
            throw new MockSimsCustomException(500, String.format("Failed to decrement QOD for UPC %s at Store %s in Division %s.", upcNumber, storeNumber, divisionNumber));
        }

        if (numRowsUpdated != 1) {
            LOG.error("Failed to decrement QOD for UPC {} at Store {} in Division {}. Did not update exactly one row.", upcNumber, storeNumber, divisionNumber);
            throw new MockSimsCustomException(500, String.format("Failed to decrement QOD for UPC %s at Store %s in Division %s.", upcNumber, storeNumber, divisionNumber));
        }
    }

    @Override
    public void updatePdmAlert(Integer alertId, LocalDateTime actionedTime, String userEuid) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue(ALERT_ID, alertId)
                .addValue(ACTIONED_TIME, actionedTime)
                .addValue(IS_ACTIVE, false)
                .addValue(USER_EUID, userEuid)
                .addValue(ACTION_CODE, "RFI");

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
    public void insertRfiTransactionInfo(RfiItemRequest rfiItemRequest, Integer qodBeforeTransaction, Integer qomBeforeTransaction, LocalDateTime actionedTime) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue(USER_EUID, rfiItemRequest.getUserEuid())
                .addValue(STORE_NUMBER, rfiItemRequest.getStoreNumber())
                .addValue(DIVISION_NUMBER, rfiItemRequest.getDivisionNumber())
                .addValue(UPC_NUMBER, rfiItemRequest.getUpcNumber())
                .addValue(QOD_BEFORE_TRANSACTION, qodBeforeTransaction)
                .addValue(QOM_BEFORE_TRANSACTION, qomBeforeTransaction)
                .addValue(ACTION_TIME, actionedTime)
                .addValue(QUANTITY_REMOVED, rfiItemRequest.getQuantity())
                .addValue(REASON_CODE, "OD");
        try {
            namedParameterJdbcTemplate.update(SQL_INSERT_RFI_TRANSACTION_INFO, mapSqlParameterSource);
        } catch (DataAccessException e){
            LOG.error("Failed to insert RFI transaction info for action on alert {}.", rfiItemRequest.getAlertId(), e);
            throw new MockSimsCustomException(500, String.format("Failed to insert RFI transaction info for action on alert %d.", rfiItemRequest.getAlertId()));
        }
    }
}
