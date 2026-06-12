package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.PlaceOrderRequest;
import mocksims.project.backend.api.domain.PlaceOrderResponse;
import org.springframework.stereotype.Service;

@Service
public class PlaceOrderServiceImpl implements PlaceOrderService{

    @Override
    public PlaceOrderResponse placeOrder(PlaceOrderRequest placeOrderRequest){
        PlaceOrderResponse placeOrderResponse = new PlaceOrderResponse();
        //call method to update boh info
        //call method to update order transaction info
        //call method to update product inventory info
        return placeOrderResponse;
    }
}
