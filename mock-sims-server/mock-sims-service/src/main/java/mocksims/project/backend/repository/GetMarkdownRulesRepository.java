package mocksims.project.backend.repository;

import mocksims.project.backend.api.domain.GetMarkdownRuleRecord;
import java.util.List;

public interface GetMarkdownRulesRepository {
    public List <GetMarkdownRuleRecord> getMarkdownRules();
}
