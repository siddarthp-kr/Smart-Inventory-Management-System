package mocksims.project.backend.domain.mapper;

import mocksims.project.backend.api.domain.GetMarkdownRuleRecord;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GetMarkdownRulesMapper implements RowMapper<GetMarkdownRuleRecord> {

    private static final String SUBCOMMODITY_NUMBER = "subcommodity_number";
    private static final String FIRST_MARKDOWN_PERCENT = "first_markdown_percent";
    private static final String CAN_BE_MARKED_DOWN = "can_be_marked_down";
    private static final String DAYS_BEFORE_EXP_TO_MARKDOWN_NUMBER = "days_before_exp_to_markdown_number";
    private static final String DAYS_BEFORE_EXP_TO_RFI_NUMBER = "days_before_exp_to_rfi_number";
    private static final String DAYS_AFTER_ORDER_TO_SET_EXP = "days_after_order_to_set_exp";

    @Override
    public GetMarkdownRuleRecord mapRow(ResultSet rs, int index) throws SQLException {
        GetMarkdownRuleRecord getMarkdownRuleRecord = new GetMarkdownRuleRecord();

        getMarkdownRuleRecord.setSubcommodityNumber(rs.getString(SUBCOMMODITY_NUMBER));
        getMarkdownRuleRecord.setFirstMarkdownPercent(rs.getObject(FIRST_MARKDOWN_PERCENT, Integer.class));
        getMarkdownRuleRecord.setCanBeMarkedDown(rs.getObject(CAN_BE_MARKED_DOWN, Boolean.class));
        getMarkdownRuleRecord.setDaysBeforeExpToMD(rs.getObject(DAYS_BEFORE_EXP_TO_MARKDOWN_NUMBER, Integer.class));
        getMarkdownRuleRecord.setDaysBeforeExpToRFI(rs.getObject(DAYS_BEFORE_EXP_TO_RFI_NUMBER, Integer.class));
        getMarkdownRuleRecord.setDaysAfterOrderToSetExp(rs.getObject(DAYS_AFTER_ORDER_TO_SET_EXP, Integer.class));

        return getMarkdownRuleRecord;
    }
}