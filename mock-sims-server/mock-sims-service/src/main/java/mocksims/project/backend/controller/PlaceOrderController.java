package mocksims.project.backend.controller;

import mocksims.project.backend.api.domain.PlaceOrderRequest;
import mocksims.project.backend.api.domain.PlaceOrderResponse;
import mocksims.project.backend.domain.MockSimsConstants;
import mocksims.project.backend.service.PlaceOrderService;
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

        //validate storeNumber
        //validate divisionNumber
        PlaceOrderResponse placeOrderResponse = placeOrderService.placeOrder(placeOrderRequest);

        return placeOrderResponse;
    }

}