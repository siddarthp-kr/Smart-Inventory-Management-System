package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.ReceiveOrderRequest;
import mocksims.project.backend.api.domain.ReceiveOrderResponse;

public interface ReceiveOrderService {

    /**
     * Receives a pending order by updating BOH/QOD, inserting received inventory,
     * updating qod_before_transaction, and marking the order as received.
     *
     * @param receiveOrderRequest
     *      Request containing store number, division number, user EUID, and order ID
     * @return
     *      Response object containing response code and response message
     */
    ReceiveOrderResponse receiveOrder(ReceiveOrderRequest receiveOrderRequest);
    ReceiveOrderResponse cancelOrder(ReceiveOrderRequest receiveOrderRequest);
}