package mocksims.project.backend.repository;

import mocksims.project.backend.api.domain.OrderHistoryRequest;
import mocksims.project.backend.exception.MockSimsCustomException;

import java.util.List;

public interface OrderHistoryRepository {
    public List<mocksims.project.backend.api.domain.OrderHistoryRecord> getOrderHistory(OrderHistoryRequest orderHistoryRequest) throws MockSimsCustomException;
}
