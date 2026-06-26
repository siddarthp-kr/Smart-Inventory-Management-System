package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.PlaceOrderRequest;
import mocksims.project.backend.api.domain.PlaceOrderResponse;
import mocksims.project.backend.repository.PlaceOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;


@Service
public class PlaceOrderServiceImpl implements PlaceOrderService{

    PlaceOrderRepository placeOrderRepository;

    public PlaceOrderServiceImpl(PlaceOrderRepository placeOrderRepository){
        this.placeOrderRepository = placeOrderRepository;
    }

    private static final Logger LOG = LoggerFactory.getLogger(PlaceOrderServiceImpl.class);

    @Override
    @Transactional
    public PlaceOrderResponse placeOrder(PlaceOrderRequest placeOrderRequest) {
        PlaceOrderResponse placeOrderResponse = new PlaceOrderResponse();
        LocalDateTime timeOrderPlaced = LocalDateTime.now();

        long orderId = placeOrderRepository.insertOrderTransactionInfo(placeOrderRequest.getStoreNumber(), placeOrderRequest.getDivisionNumber(), placeOrderRequest.getUserEuid(), timeOrderPlaced);

        //These methods store the information they are getting within the placeOrderRequest items list

        //Store one movement row per UPC
        placeOrderRepository.insertOrderMovementTransactions(
                orderId,
                placeOrderRequest.getItems()
        );

        placeOrderResponse.setResponseCode(200);
        placeOrderResponse.setResponseMessage("Order placed successfully");
        placeOrderResponse.setOrderId(orderId);
        return placeOrderResponse;
    }
}
