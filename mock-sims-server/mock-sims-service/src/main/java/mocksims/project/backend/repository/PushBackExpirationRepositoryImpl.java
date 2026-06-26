package mocksims.project.backend.repository;

import mocksims.project.backend.api.domain.MarkdownRulesRecord;
import mocksims.project.backend.domain.mapper.MarkdownRulesRecordMapper;
import mocksims.project.backend.exception.MockSimsCustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class PushBackExpirationRepositoryImpl implements PushBackExpirationRepository {

    private static final Logger LOG = LoggerFactory.getLogger(PushBackExpirationRepositoryImpl.class);


    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PushBackExpirationRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    private static final String ALERT_ID = "ALERT_ID";
    private static final String SUBCOMMDITY_NUMBER = "SUBCOMMODITY_NUMBER";

    private static final String NEW_MD_DATE = "NEW_MD_DATE";
    private static final String NEW_RFI_DATE = "NEW_RFI_DATE";
    private static final String NEW_EXPIRATION_DATE = "NEW_EXPIRATION_DATE";

    private static final String SQL_CREATE_NEW_ALERT = """
            INSERT INTO PDM_ALERTS (store_number, division_number, department_number, upc_number, quantity, expiration_date, markdown_after_date, rfi_after_date, first_markdown_percent, is_active)
            SELECT store_number, division_number, department_number, upc_number, quantity, :NEW_EXPIRATION_DATE, :NEW_MARKDOWN_DATE, :NEW_RFI_DATE, first_markdown_percent, is_active
            FROM PDM_ALERTS
            WHERE alert_id = :ALERT_ID;
            """;

    private static final String SQL_GET_SUBCOMMODITY_NUMBER = """
            SELECT subcommodity_number FROM PRODUCT_BASIC_INFO
            WHERE upc_number = (
                SELECT upc_number FROM PDM_ALERTS
                WHERE alert_id = :ALERT_ID
            );
            """;

    private static final String SQL_GET_MARKDOWN_RULES_INFO = """
            SELECT days_before_exp_to_markdown_number, days_before_exp_to_rfi_number
            FROM MARKDOWN_RULES
            WHERE subcommodity_number = :SUBCOMMODITY_NUMBER;
            """;

    private static final String SQL_DEACTIVATE_PDM_ALERT = """
            UPDATE PDM_ALERTS
            SET is_active = FALSE
            WHERE alert_id = :ALERT_ID;
            """;

    @Override
    public String getSubcommodityNumber(Integer alertId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource().addValue(ALERT_ID, alertId);

        try {
            return namedParameterJdbcTemplate.queryForObject(SQL_GET_SUBCOMMODITY_NUMBER, mapSqlParameterSource, String.class);
        } catch (DataAccessException e) {
            LOG.error("Failed to get subcommodity number for alert {}.", alertId, e);
            throw new MockSimsCustomException(500, "Failed to get subcommodity number for alert " + alertId + ".");
        }
    }

    @Override
    public MarkdownRulesRecord getMarkdownRules(String subcommodityNumber) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource().addValue(SUBCOMMDITY_NUMBER, subcommodityNumber);

        try {
            return namedParameterJdbcTemplate.queryForObject(SQL_GET_MARKDOWN_RULES_INFO, mapSqlParameterSource, new MarkdownRulesRecordMapper());
        } catch (DataAccessException e){
            LOG.error("Failed to get markdown rules information for subcommodity {}.", subcommodityNumber, e);
            throw new MockSimsCustomException(500, "Failed to get markdown rules information for subcommodity " + subcommodityNumber + ".");
        }
    }

    @Override
    public void insertNewAlert(LocalDate newExpirationDate, LocalDate newRfiDate, LocalDate newMarkdownDate, Integer alertId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue(NEW_EXPIRATION_DATE, newExpirationDate)
                .addValue(NEW_RFI_DATE, newRfiDate)
                .addValue(NEW_MD_DATE, newMarkdownDate)
                .addValue(ALERT_ID, alertId);

        try {
            namedParameterJdbcTemplate.update(SQL_CREATE_NEW_ALERT, mapSqlParameterSource);
        } catch (DataAccessException e){
            LOG.error("Failed to create new updated alert for alert {}.", alertId, e);
            throw new MockSimsCustomException(500, "Failed to create new update alert for alert " + alertId + ".");
        }

    }

    @Override
    public void deactivateOldAlert(Integer alertId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource().addValue(ALERT_ID, alertId);
        int numRowsUpdated;
        try {
            numRowsUpdated = namedParameterJdbcTemplate.update(SQL_DEACTIVATE_PDM_ALERT, mapSqlParameterSource);
        } catch(DataAccessException e){
            LOG.error("Failed to deactivate alert {}.", alertId, e);
            throw new MockSimsCustomException(500, "Failed to deactivate alert " + alertId + ".");
        }

        if(numRowsUpdated != 1){
            throw new MockSimsCustomException(500, "Failed to deactivate alert " + alertId + ". More or less than one row was updated");
        }

    }
}
