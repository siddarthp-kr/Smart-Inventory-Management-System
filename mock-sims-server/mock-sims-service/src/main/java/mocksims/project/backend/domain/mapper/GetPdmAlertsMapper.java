package mocksims.project.backend.domain.mapper;


import mocksims.project.backend.api.domain.GetPdmAlertRecord;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class GetPdmAlertsMapper implements RowMapper<GetPdmAlertRecord> {

    private final String ALERT_ID = "alert_id";
    private final String UPC_NUMBER = "upc_number";
    private final String DEPARTMENT_NUMBER = "department_number";
    private final String EXPIRATION_DATE = "expiration_date";
    private final String MARKDOWN_AFTER_DATE = "markdown_after_date";
    private final String RFI_AFTER_DATE = "rfi_after_date";

    @Override
    public GetPdmAlertRecord mapRow(ResultSet rs, int index) throws SQLException {
        GetPdmAlertRecord getPdmAlertRecord = new GetPdmAlertRecord();

        getPdmAlertRecord.setAlertId(rs.getInt(ALERT_ID));
        getPdmAlertRecord.setUpcNumber(rs.getString(UPC_NUMBER));
        getPdmAlertRecord.setDepartmentNumber(rs.getString(DEPARTMENT_NUMBER));

        LocalDate expirationDate = rs.getDate(EXPIRATION_DATE).toLocalDate();
        LocalDate mdBeforeDate = LocalDate.now().plusDays(ChronoUnit.DAYS.between(rs.getDate(MARKDOWN_AFTER_DATE).toLocalDate(), expirationDate));
        LocalDate rfiBeforeDate = LocalDate.now().plusDays(ChronoUnit.DAYS.between(rs.getDate(RFI_AFTER_DATE).toLocalDate(), expirationDate));

        getPdmAlertRecord.setExpirationDate(expirationDate);
        getPdmAlertRecord.setMdBeforeDate(mdBeforeDate);
        getPdmAlertRecord.setRfiBeforeDate(rfiBeforeDate);

        return getPdmAlertRecord;
    }
}
