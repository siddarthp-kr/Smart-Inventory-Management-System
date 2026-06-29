package mocksims.project.backend.repository;

import mocksims.project.backend.api.domain.GetMarkdownRuleRecord;
import mocksims.project.backend.domain.mapper.GetMarkdownRulesMapper;
import mocksims.project.backend.exception.MockSimsCustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class GetMarkdownRulesRepositoryImpl implements GetMarkdownRulesRepository {

    private static final Logger LOG = LoggerFactory.getLogger(GetMarkdownRulesRepositoryImpl.class);

    private static final String SQL_GET_MARKDOWN_RULES = """
            SELECT
                subcommodity_number,
                first_markdown_percent,
                can_be_marked_down,
                days_before_exp_to_markdown_number,
                days_before_exp_to_rfi_number,
                days_after_order_to_set_exp
            FROM MARKDOWN_RULES
            ORDER BY subcommodity_number
            """;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public GetMarkdownRulesRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<GetMarkdownRuleRecord> getMarkdownRules() {
        try {
            return namedParameterJdbcTemplate.query(SQL_GET_MARKDOWN_RULES, new GetMarkdownRulesMapper());
        } catch (DataAccessException error) {
            LOG.error("Failed to retrieve markdown rules.", error);
            throw new MockSimsCustomException(500, "Failed to retrieve markdown rules.");
        }
    }
}