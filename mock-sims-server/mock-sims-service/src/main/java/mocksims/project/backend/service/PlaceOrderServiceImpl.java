package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.PlaceOrderItem;
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
import java.util.Map;


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

        //change this order ID to be the general order ID - this will be a FK in th product_inventory_info table that references the order transaction table
        long orderId = placeOrderRepository.insertOrderTransactionInfo(placeOrderRequest.getStoreNumber(), placeOrderRequest.getDivisionNumber(), placeOrderRequest.getUserEuid(), timeOrderPlaced, timeOrderReceived);


        placeOrderRepository.updateBohInfo(placeOrderRequest.getStoreNumber(), placeOrderRequest.getDivisionNumber(), placeOrderRequest.getItems());

        //These methods store the information they are getting within the placeOrderRequest items list

        for(PlaceOrderItem item: placeOrderRequest.getItems()){
            String subcommodityNumber = placeOrderRepository.getSubcommodityNumber(item.getUpcNumber());
            //this will return null if the subcommodity is not eligible for markdown
            Integer numberOfDaysBeforeExpiration = placeOrderRepository.getNumberOfDaysBeforeExpiration(subcommodityNumber);

            LocalDate expirationDate;

            //maybe move this logic into the getNumberOfDaysBeforeExpiration
            if(numberOfDaysBeforeExpiration != null){
                item.setExpirationDate(timeOrderReceived.toLocalDate().plusDays(numberOfDaysBeforeExpiration));
                item.setIsActive(true);
            } else {
                item.setExpirationDate(null);
                item.setIsActive(false);
            }
        }


        placeOrderRepository.insertProductInventoryInfo(placeOrderRequest.getItems(), orderId, timeOrderReceived.toLocalDate());

        placeOrderResponse.setResponseCode(200);
        placeOrderResponse.setResponseMessage("Order placed successfully");
        placeOrderResponse.setOrderId(orderId);

        return placeOrderResponse;
    }
}
