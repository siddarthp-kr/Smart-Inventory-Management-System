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

        if(ValidationHelper.validateUpcNumber(placeOrderRequest.getUpcNumber())
        && ValidationHelper.validateDivisionNumber(placeOrderRequest.getDivisionNumber())
                && ValidationHelper.validateStoreNumber(placeOrderRequest.getStoreNumber())
                && ValidationHelper.validateUserEuid(placeOrderRequest.getUserEuid())
                && placeOrderRequest.getQuantity() > 0
        ){
            try {
                placeOrderResponse = placeOrderService.placeOrder(placeOrderRequest);
            } catch (Exception e){
                System.out.println("Error: could not place order. See below: ");
                System.out.println(e.getMessage());
            }

        } else {
            placeOrderResponse.setResponseCode(400);
            placeOrderResponse.setResponseMessage("Error: Order request has invalid parameters");
        }


        return placeOrderResponse;
    }

}