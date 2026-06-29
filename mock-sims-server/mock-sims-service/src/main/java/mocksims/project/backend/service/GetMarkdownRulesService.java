package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.GetMarkdownRuleRecord;
import java.util.List;

public interface GetMarkdownRulesService {
    public List<GetMarkdownRuleRecord> getMarkdownRules();
}
