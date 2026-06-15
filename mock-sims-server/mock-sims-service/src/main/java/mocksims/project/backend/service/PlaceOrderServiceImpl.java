package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.PlaceOrderRequest;
import mocksims.project.backend.api.domain.PlaceOrderResponse;
import mocksims.project.backend.repository.PlaceOrderRepository;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlaceOrderServiceImpl implements PlaceOrderService{

    PlaceOrderRepository placeOrderRepository;

    public PlaceOrderServiceImpl(PlaceOrderRepository placeOrderRepository){
        this.placeOrderRepository = placeOrderRepository;
    }

    @Override
    @Transactional
    public PlaceOrderResponse placeOrder(PlaceOrderRequest placeOrderRequest){
        PlaceOrderResponse placeOrderResponse = new PlaceOrderResponse();
        try {
            placeOrderRepository.updateBohInfo(placeOrderRequest.getStoreNumber(), placeOrderRequest.getDivisionNumber(), placeOrderRequest.getUpcNumber(), placeOrderRequest.getQuantity());
            placeOrderRepository.updateOrderTransactionInfo(placeOrderRequest.getStoreNumber(), placeOrderRequest.getDivisionNumber(), placeOrderRequest.getUserEuid());
            placeOrderRepository.updateProductInventoryInfo(placeOrderRequest.getUpcNumber(), placeOrderRequest.getQuantity());
        } catch (DataAccessException dataAccessException){
            System.out.println("Error placing order");
            placeOrderResponse.setResponseCode(500);
            placeOrderResponse.setResponseMessage("Error: could not add new order to DB");
            throw dataAccessException;
        }

        return placeOrderResponse;
    }
}
