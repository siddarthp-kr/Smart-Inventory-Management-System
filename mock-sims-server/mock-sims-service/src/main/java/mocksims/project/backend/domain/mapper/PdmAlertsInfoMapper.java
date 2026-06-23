package mocksims.project.backend.domain.mapper;

import mocksims.project.backend.api.domain.PdmAlertInfoRecord;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

@Component
public class PdmAlertsInfoMapper implements RowMapper<PdmAlertInfoRecord> {

    private static final String PRODUCT_ORDER_ID = "product_order_id";
    private static final String UPC_NUMBER = "upc_number";
    private static final String EXPIRATION_DATE = "expiration_date";
    private static final String STORE_NUMBER = "store_number";
    private static final String DIVISION_NUMBER = "division_number";
    private static final String DEPARTMENT_NUMBER = "department_number";
    private static final String FIRST_MARKDOWN_PERCENT = "first_markdown_percent";
    private static final String DAYS_BEFORE_EXP_TO_MARKDOWN_NUMBER = "days_before_exp_to_markdown_number";
    private static final String DAYS_BEFORE_EXP_TO_RFI_NUMBER = "days_before_exp_to_rfi_number";
    private static final String QOD_NUMBER = "qod_number";
    private static final String QOM_NUMBER = "qom_number";
    private static final String QUANTITY = "quantity";


    @Override
    public PdmAlertInfoRecord mapRow(ResultSet resultSet, int index) throws SQLException {
        PdmAlertInfoRecord pdmAlertInfoRecord = new PdmAlertInfoRecord();

        pdmAlertInfoRecord.setProductOrderId(resultSet.getInt(PRODUCT_ORDER_ID));
        pdmAlertInfoRecord.setStoreNumber(resultSet.getString(STORE_NUMBER));
        pdmAlertInfoRecord.setDivisionNumber(resultSet.getString(DIVISION_NUMBER));
        pdmAlertInfoRecord.setDepartmentNumber(resultSet.getString(DEPARTMENT_NUMBER));
        pdmAlertInfoRecord.setUpcNumber(resultSet.getString(UPC_NUMBER));
        pdmAlertInfoRecord.setQuantity(resultSet.getInt(QUANTITY));

        Date expirationDate = resultSet.getDate(EXPIRATION_DATE);
        pdmAlertInfoRecord.setExpirationDate(expirationDate != null ? expirationDate.toLocalDate() : null);

        pdmAlertInfoRecord.setFirstMarkdownPercent(resultSet.getObject(FIRST_MARKDOWN_PERCENT, Integer.class));
        pdmAlertInfoRecord.setDaysBeforeExpToMD(resultSet.getObject(DAYS_BEFORE_EXP_TO_MARKDOWN_NUMBER, Integer.class));
        pdmAlertInfoRecord.setDaysBeforeExpToRFI(resultSet.getObject(DAYS_BEFORE_EXP_TO_RFI_NUMBER, Integer.class));

        pdmAlertInfoRecord.setQodNumber(resultSet.getInt(QOD_NUMBER));
        pdmAlertInfoRecord.setQomNumber(resultSet.getInt(QOM_NUMBER));

        return pdmAlertInfoRecord;
    }

}
