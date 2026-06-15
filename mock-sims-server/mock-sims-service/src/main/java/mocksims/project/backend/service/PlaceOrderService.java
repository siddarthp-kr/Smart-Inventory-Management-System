package mocksims.project.backend.service;


import mocksims.project.backend.api.domain.PlaceOrderRequest;
import mocksims.project.backend.api.domain.PlaceOrderResponse;

public interface PlaceOrderService {
    public PlaceOrderResponse placeOrder(PlaceOrderRequest placeOrderRequest);
}
