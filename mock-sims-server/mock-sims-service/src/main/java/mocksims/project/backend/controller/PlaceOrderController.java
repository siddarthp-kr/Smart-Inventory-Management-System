package mocksims.project.backend.controller;

import mocksims.project.backend.api.domain.PlaceOrderRequest;
import mocksims.project.backend.api.domain.PlaceOrderResponse;
import mocksims.project.backend.domain.MockSimsConstants;
import mocksims.project.backend.service.PlaceOrderService;
import mocksims.project.backend.util.ValidationHelper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/order")
@CrossOrigin(origins = "http://localhost:4200/")
public class PlaceOrderController{
    private final PlaceOrderService placeOrderService;

    public PlaceOrderController(PlaceOrderService placeOrderService){
        this.placeOrderService = placeOrderService;
    }

    @PostMapping(value = MockSimsConstants.PLACE_ORDER_ENDPOINT)
    public PlaceOrderResponse placeOrder(@RequestBody PlaceOrderRequest placeOrderRequest){
        PlaceOrderResponse placeOrderResponse = new PlaceOrderResponse();

        //validate storeNumber
        //validate divisionNumber
        if(ValidationHelper.validateUpcNumber(placeOrderRequest.getUpcNumber())
        && ValidationHelper.validateDivisionNumber(placeOrderRequest.getDivisionNumber())
                && ValidationHelper.validateStoreNumber(placeOrderRequest.getStoreNumber())
        ){
            placeOrderResponse = placeOrderService.placeOrder(placeOrderRequest);
        } else {
            placeOrderResponse.setResponseCode(400);
        }


        return placeOrderResponse;
    }

}