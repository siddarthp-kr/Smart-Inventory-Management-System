package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.OrderHistoryRequest;
import mocksims.project.backend.api.domain.OrderHistoryResponse;
import mocksims.project.backend.repository.OrderHistoryRepository;
import org.springframework.stereotype.Service;
import mocksims.project.backend.api.domain.OrderHistoryRecord;

import java.util.List;

@Service
public class OrderHistoryServiceImpl implements OrderHistoryService {

    private final OrderHistoryRepository orderHistoryRepository;

    public OrderHistoryServiceImpl(OrderHistoryRepository orderHistoryRepository){
        this.orderHistoryRepository = orderHistoryRepository;
    }

    public OrderHistoryResponse getOrderHistory(OrderHistoryRequest orderHistoryRequest){
        OrderHistoryResponse orderHistoryResponse = new OrderHistoryResponse();
        List<OrderHistoryRecord> orders = orderHistoryRepository.getOrderHistory(orderHistoryRequest);

        orderHistoryResponse.setOrders(orders);
        orderHistoryResponse.setOrderHistoryResponseCode(200);
        orderHistoryResponse.setOrderHistoryResponseMessage("Successfully retrieved order history");

        return orderHistoryResponse;
    }
}
