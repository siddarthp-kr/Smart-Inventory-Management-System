package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.GetMarkdownRuleRecord;
import mocksims.project.backend.repository.GetMarkdownRulesRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GetMarkdownRulesServiceImpl implements GetMarkdownRulesService {

    private final GetMarkdownRulesRepository getMarkdownRulesRepository;

    public GetMarkdownRulesServiceImpl(GetMarkdownRulesRepository getMarkdownRulesRepository) {
        this.getMarkdownRulesRepository = getMarkdownRulesRepository;
    }

    @Override
    public List<GetMarkdownRuleRecord> getMarkdownRules() {
        return getMarkdownRulesRepository.getMarkdownRules();
    }

}
