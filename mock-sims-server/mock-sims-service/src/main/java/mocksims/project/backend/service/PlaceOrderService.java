package mocksims.project.backend.service;


import mocksims.project.backend.api.domain.PlaceOrderRequest;
import mocksims.project.backend.api.domain.PlaceOrderResponse;

public interface PlaceOrderService {
    /**
     * Calls the necessary repository methods and performs business logic to store
     * order transaction information in PRODUCT_BOH_INFO, PRODUCT_INVENTORY_INFO,
     * and ORDER_TRANSACTION_INFO
     * @param placeOrderRequest
     *      The request object containing Store Number, Division Number, User Euid, UPC Number, and Product Quantity
     * @return
     *      Response object containing response code and response message
     */
    public PlaceOrderResponse placeOrder(PlaceOrderRequest placeOrderRequest);
}
