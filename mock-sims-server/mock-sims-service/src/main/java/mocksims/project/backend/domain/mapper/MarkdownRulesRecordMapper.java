package mocksims.project.backend.domain.mapper;

import mocksims.project.backend.api.domain.MarkdownRulesRecord;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MarkdownRulesRecordMapper implements RowMapper<MarkdownRulesRecord> {

    @Override
    public MarkdownRulesRecord mapRow(ResultSet rs, int index) throws SQLException {
        MarkdownRulesRecord markdownRulesRecord = new MarkdownRulesRecord();

        markdownRulesRecord.setDaysBeforeExpToMd(rs.getInt("days_before_exp_to_markdown_number"));
        markdownRulesRecord.setDaysBeforeExpToRfi(rs.getInt("days_before_exp_to_rfi_number"));

        return markdownRulesRecord;
    }
}
