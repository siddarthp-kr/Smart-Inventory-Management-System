package mocksims.project.backend.repository;

import mocksims.project.backend.api.domain.MarkdownRulesRecord;

import java.time.LocalDate;

public interface PushBackExpirationRepository {

    public String getSubcommodityNumber(Integer alertId);
    public MarkdownRulesRecord getMarkdownRules(String subcommodityNumber);
    public void insertNewAlert(LocalDate newExpirationDate, LocalDate newRfiDate, LocalDate newMarkdownDate, Integer alertId);
    public void deactivateOldAlert(Integer alertId);

}
