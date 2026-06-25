package mocksims.project.backend.domain.mapper;


import mocksims.project.backend.api.domain.GetPdmAlertRecord;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

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
        getPdmAlertRecord.setExpirationDate(rs.getDate(EXPIRATION_DATE).toLocalDate());
        getPdmAlertRecord.setMdAfterDate(rs.getDate(MARKDOWN_AFTER_DATE).toLocalDate());
        getPdmAlertRecord.setRfiAfterDate(rs.getDate(RFI_AFTER_DATE).toLocalDate());

        return getPdmAlertRecord;
    }
}
