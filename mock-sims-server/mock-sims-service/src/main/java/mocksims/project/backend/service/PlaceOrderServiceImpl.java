package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.PlaceOrderRequest;
import mocksims.project.backend.api.domain.PlaceOrderResponse;
import mocksims.project.backend.controller.PlaceOrderController;
import mocksims.project.backend.exception.MockSimsCustomException;
import mocksims.project.backend.exception.RowNotFoundException;
import mocksims.project.backend.repository.PlaceOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
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

    private static final Logger LOG = LoggerFactory.getLogger(PlaceOrderServiceImpl.class);

    @Override
    @Transactional
    public PlaceOrderResponse placeOrder(PlaceOrderRequest placeOrderRequest) {
        PlaceOrderResponse placeOrderResponse = new PlaceOrderResponse();
        LocalDateTime timeOrderPlaced = LocalDateTime.now();
        /* THIS IS HARDCODED, but can change to simulate real use-case */
        LocalDateTime timeOrderReceived = timeOrderPlaced.plusMinutes(30);

        boolean alertIsActive;

        placeOrderRepository.updateBohInfo(placeOrderRequest.getStoreNumber(), placeOrderRequest.getDivisionNumber(), placeOrderRequest.getUpcNumber(), placeOrderRequest.getQuantity());
        long orderId = placeOrderRepository.insertOrderTransactionInfo(placeOrderRequest.getStoreNumber(), placeOrderRequest.getDivisionNumber(), placeOrderRequest.getUserEuid(), timeOrderPlaced, timeOrderReceived);

        String subcommodityNumber = placeOrderRepository.getSubcommodityNumber(placeOrderRequest.getUpcNumber());
        //this will return null if the subcommodity is not eligible for markdown
        Integer numberOfDaysBeforeExpiration = placeOrderRepository.getNumberOfDaysBeforeExpiration(subcommodityNumber);

        LocalDate expirationDate;

        if(numberOfDaysBeforeExpiration != null){
            expirationDate = timeOrderReceived.toLocalDate().plusDays(numberOfDaysBeforeExpiration);
            alertIsActive = true;
        } else {
            expirationDate = null;
            alertIsActive = false;
        }

        placeOrderRepository.insertProductInventoryInfo(placeOrderRequest.getUpcNumber(), placeOrderRequest.getQuantity(), orderId, timeOrderReceived.toLocalDate(), expirationDate, alertIsActive);

        placeOrderResponse.setResponseCode(200);
        placeOrderResponse.setResponseMessage("Order placed successfully");
        placeOrderResponse.setOrderId(orderId);

        return placeOrderResponse;
    }
}
