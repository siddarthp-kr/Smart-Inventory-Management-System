package mocksims.project.backend.controller;

import mocksims.project.backend.api.domain.PlaceOrderRequest;
import mocksims.project.backend.api.domain.PlaceOrderResponse;
import mocksims.project.backend.domain.MockSimsConstants;
import mocksims.project.backend.exception.MockSimsCustomException;
import mocksims.project.backend.exception.RowNotFoundException;
import mocksims.project.backend.service.PlaceOrderService;
import mocksims.project.backend.util.ValidationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/order")
@CrossOrigin(origins = "http://localhost:4200/")
public class PlaceOrderController{
    private final PlaceOrderService placeOrderService;

    public PlaceOrderController(PlaceOrderService placeOrderService){
        this.placeOrderService = placeOrderService;
    }

    private static final Logger LOG = LoggerFactory.getLogger(PlaceOrderController.class);

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
                LOG.info("Order {} placed successfully", placeOrderResponse.getOrderId());
            } catch (MockSimsCustomException customException){
                LOG.info("Failed to place order. See details below: ", customException);
                placeOrderResponse.setResponseCode(customException.getErrorCode());
                placeOrderResponse.setResponseMessage("Failed to place order due to internal server error");
            }
        } else {
            placeOrderResponse.setResponseCode(400);
            placeOrderResponse.setResponseMessage("Order request has invalid parameters");
            LOG.error("Error: Invalid order request parameters");
        }

        return placeOrderResponse;
    }

}