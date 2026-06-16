package mocksims.project.backend.controller;

import mocksims.project.backend.api.domain.PlaceOrderRequest;
import mocksims.project.backend.api.domain.PlaceOrderResponse;
import mocksims.project.backend.domain.MockSimsConstants;
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
                LOG.info("Order placed successfully");
            } catch (RowNotFoundException rowNotFoundException){
                LOG.error("Error: Failed to place order. See details below: ", rowNotFoundException);
                placeOrderResponse.setResponseCode(404);
                placeOrderResponse.setResponseMessage("Error: Product information is missing from database");
            } catch (DataAccessException dataAccessException){
                LOG.error("Error: Failed to place order. See details below: ", dataAccessException);
                placeOrderResponse.setResponseCode(500);
                placeOrderResponse.setResponseMessage("Error: server failed to place order");
            } catch (IllegalStateException illegalStateException){
                LOG.error("Failed to get generated key for Product Order ID. See details below: ", illegalStateException);
                placeOrderResponse.setResponseCode(500);
                placeOrderResponse.setResponseMessage("Error: server failed to place order");
            }
        } else {
            placeOrderResponse.setResponseCode(400);
            placeOrderResponse.setResponseMessage("Error: Order request has invalid parameters");
            LOG.error("Error: Invalid order request parameters");
        }

        return placeOrderResponse;
    }

}