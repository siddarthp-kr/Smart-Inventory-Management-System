package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.OrderHistoryRequest;
import mocksims.project.backend.api.domain.OrderHistoryResponse;

public interface OrderHistoryService {
    public OrderHistoryResponse getOrderHistory(OrderHistoryRequest orderHistoryRequest);
}
