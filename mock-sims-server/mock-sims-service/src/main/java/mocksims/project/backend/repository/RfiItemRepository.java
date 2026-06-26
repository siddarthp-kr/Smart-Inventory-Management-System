package mocksims.project.backend.repository;

import mocksims.project.backend.api.domain.RfiItemRequest;

import java.time.LocalDateTime;

public interface RfiItemRepository {

    public Integer getQodNumber(String storeNumber, String divisionNumber, String upcNumber);
    public Integer getQomNumber(String storeNumber, String divisionNumber, String upcNumber);
    public void decrementQod(String storeNumber, String divisionNumber, String upcNumber, Integer quantity);
    public void updatePdmAlert(Integer alertId, LocalDateTime actionedTime, String userEuid);
    public void insertRfiTransactionInfo(RfiItemRequest rfiItemRequest, Integer qodBeforeTransaction, Integer qomBeforeTransaction, LocalDateTime actionedTime);


}
