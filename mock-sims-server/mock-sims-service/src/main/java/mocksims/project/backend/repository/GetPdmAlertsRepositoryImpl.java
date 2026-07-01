package mocksims.project.backend.repository;

import mocksims.project.backend.api.domain.GetPdmAlertRecord;
import mocksims.project.backend.domain.mapper.GetPdmAlertsMapper;
import mocksims.project.backend.exception.MockSimsCustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class GetPdmAlertsRepositoryImpl implements GetPdmAlertsRepository {

    private static final Logger LOG = LoggerFactory.getLogger(GetPdmAlertsRepositoryImpl.class);

    private final String STORE_NUMBER = "STORE_NUMBER";
    private final String DIVISION_NUMBER = "DIVISION_NUMBER";

    private final String SQL_GET_PDM_ALERTS = """
            SELECT alert_id, upc_number, department_number, expiration_date, markdown_after_date, rfi_after_date
            FROM (
                SELECT alert_id,upc_number,department_number,expiration_date,markdown_after_date,rfi_after_date,
                    ROW_NUMBER() OVER (PARTITION BY upc_number ORDER BY expiration_date ASC, alert_id ASC) AS row_num
                FROM PDM_ALERTS
                WHERE division_number = :DIVISION_NUMBER AND store_number = :STORE_NUMBER AND is_active = TRUE AND markdown_after_date <= CURRENT_DATE) ranked_alerts
            WHERE row_num = 1 ORDER BY expiration_date ASC;
            """;

    private final String SQL_GET_ALERT_COUNT = """
            SELECT COUNT(DISTINCT upc_number)
            FROM PDM_ALERTS
            WHERE division_number = :DIVISION_NUMBER
              AND store_number = :STORE_NUMBER
              AND is_active = TRUE
              AND markdown_after_date <= CURRENT_DATE;
            """;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public GetPdmAlertsRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Override
    public List<GetPdmAlertRecord> getPdmAlerts(String storeNumber, String divisionNumber){
        List<GetPdmAlertRecord> alerts;

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource().addValue(STORE_NUMBER, storeNumber).addValue(DIVISION_NUMBER, divisionNumber);

        try {
            alerts = namedParameterJdbcTemplate.query(SQL_GET_PDM_ALERTS, mapSqlParameterSource, new GetPdmAlertsMapper());
        } catch (DataAccessException e){
            LOG.error("Failed to get PDM alerts from PDM_ALERTS.", e);
            throw new MockSimsCustomException(500, "Failed to get PDM alerts");
        }

        return alerts;
    }

    @Override
    public Integer getPdmAlertCount(String storeNumber, String divisionNumber) {

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource().addValue(STORE_NUMBER, storeNumber).addValue(DIVISION_NUMBER, divisionNumber);

        try {
            return namedParameterJdbcTemplate.queryForObject(SQL_GET_ALERT_COUNT, mapSqlParameterSource, Integer.class);
        } catch (DataAccessException e){
            LOG.error("Failed to get PDM alert count from PDM_ALERTS", e);
            throw new MockSimsCustomException(500, String.format("Failed to get PDM alert count for store %s in division %s.", storeNumber, divisionNumber));
        }


    }

}
