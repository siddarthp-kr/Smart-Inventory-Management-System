package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.PlaceOrderRequest;
import mocksims.project.backend.api.domain.PlaceOrderResponse;
import mocksims.project.backend.exception.RowNotFoundException;
import mocksims.project.backend.repository.PlaceOrderRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
public class PlaceOrderServiceImpl implements PlaceOrderService{

    PlaceOrderRepository placeOrderRepository;

    public PlaceOrderServiceImpl(PlaceOrderRepository placeOrderRepository){
        this.placeOrderRepository = placeOrderRepository;
    }

    @Override
    @Transactional
    public PlaceOrderResponse placeOrder(PlaceOrderRequest placeOrderRequest) {
        PlaceOrderResponse placeOrderResponse = new PlaceOrderResponse();
        LocalDateTime timeOrderPlaced = LocalDateTime.now();
        /* THIS IS HARDCODED, can change to simulate real use-case */
        LocalDateTime timeOrderReceived = timeOrderPlaced.plusMinutes(30);

        placeOrderRepository.updateBohInfo(placeOrderRequest.getStoreNumber(), placeOrderRequest.getDivisionNumber(), placeOrderRequest.getUpcNumber(), placeOrderRequest.getQuantity());
        long orderId = placeOrderRepository.updateOrderTransactionInfo(placeOrderRequest.getStoreNumber(), placeOrderRequest.getDivisionNumber(), placeOrderRequest.getUserEuid());
        placeOrderRepository.updateProductInventoryInfo(placeOrderRequest.getUpcNumber(), placeOrderRequest.getQuantity(), orderId);

        placeOrderResponse.setResponseCode(200);
        placeOrderResponse.setResponseMessage("Order placed successfully");


        return placeOrderResponse;
    }
}
