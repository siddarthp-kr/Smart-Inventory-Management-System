package mocksims.project.backend.repository;

import mocksims.project.backend.api.domain.MarkdownItemRequest;

import java.time.LocalDateTime;

public interface MarkdownItemRepository {

    public Integer getQodNumber(String storeNumber, String divisionNumber, String upcNumber);

    public Integer getQomNumber(String storeNumber, String divisionNumber, String upcNumber);

    public Double getStandardPrice(String upcNumber);

    public void updateQodAndQom(String storeNumber, String divisionNumber, String upcNumber, Integer quantity);

    public void updatePdmAlert(Integer alertId, LocalDateTime actionedTime, String userEuid);

    public Integer getFirstMarkdownPercent(Integer alertId);

    public void insertMarkdownTransactionInfo(MarkdownItemRequest markdownItemRequest, Double originalPrice, Double newPrice, LocalDateTime actionedTime, Integer qodBeforeTransaction, Integer qomBeforeTransaction);

}
