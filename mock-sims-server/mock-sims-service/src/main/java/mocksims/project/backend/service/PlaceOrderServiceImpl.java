package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.PlaceOrderRequest;
import mocksims.project.backend.api.domain.PlaceOrderResponse;
import mocksims.project.backend.repository.PlaceOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
        /* THIS IS HARDCODED, but can change to simulate real use-case */
        LocalDateTime timeOrderReceived = timeOrderPlaced.plusMinutes(30);

        placeOrderRepository.updateBohInfo(placeOrderRequest.getStoreNumber(), placeOrderRequest.getDivisionNumber(), placeOrderRequest.getUpcNumber(), placeOrderRequest.getQuantity());
        long orderId = placeOrderRepository.insertOrderTransactionInfo(placeOrderRequest.getStoreNumber(), placeOrderRequest.getDivisionNumber(), placeOrderRequest.getUserEuid(), timeOrderPlaced, timeOrderReceived);

        String subcommodityNumber = placeOrderRepository.getSubcommodityNumber(placeOrderRequest.getUpcNumber());
        Integer numberOfDaysBeforeExpiration = placeOrderRepository.getNumberOfDaysBeforeExpiration(subcommodityNumber);

        LocalDate expirationDate = timeOrderReceived.toLocalDate().plusDays(numberOfDaysBeforeExpiration);

        placeOrderRepository.insertProductInventoryInfo(placeOrderRequest.getUpcNumber(), placeOrderRequest.getQuantity(), orderId, timeOrderReceived.toLocalDate(), expirationDate);

        //This only runs if one of the above lines throws an error
        placeOrderResponse.setResponseCode(200);
        placeOrderResponse.setResponseMessage("Order placed successfully"); // add order number in message in log

        return placeOrderResponse;
    }
}
