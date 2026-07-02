package mocksims.project.backend.controller;

import mocksims.project.backend.api.domain.ReceiveOrderRequest;
import mocksims.project.backend.api.domain.ReceiveOrderResponse;
import mocksims.project.backend.domain.MockSimsConstants;
import mocksims.project.backend.exception.MockSimsCustomException;
import mocksims.project.backend.service.ReceiveOrderService;
import mocksims.project.backend.util.ValidationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/order")
@CrossOrigin(origins = "http://localhost:4200/")
public class ReceiveOrderController {

    private final ReceiveOrderService receiveOrderService;

    public ReceiveOrderController(ReceiveOrderService receiveOrderService) {
        this.receiveOrderService = receiveOrderService;
    }

    private static final Logger LOG = LoggerFactory.getLogger(ReceiveOrderController.class);

    @PostMapping(value = MockSimsConstants.RECEIVE_ORDER_ENDPOINT)
    public ReceiveOrderResponse receiveOrder(@RequestBody ReceiveOrderRequest receiveOrderRequest) {
        ReceiveOrderResponse receiveOrderResponse = new ReceiveOrderResponse();

        if (receiveOrderRequest != null
                && receiveOrderRequest.getOrderId() != null
                && receiveOrderRequest.getOrderId() > 0
                && ValidationHelper.validateDivisionNumber(receiveOrderRequest.getDivisionNumber())
                && ValidationHelper.validateStoreNumber(receiveOrderRequest.getStoreNumber())
                && ValidationHelper.validateUserEuid(receiveOrderRequest.getUserEuid())) {

            try {
                receiveOrderResponse = receiveOrderService.receiveOrder(receiveOrderRequest);
                LOG.info("Order {} received successfully", receiveOrderRequest.getOrderId());

            } catch (MockSimsCustomException customException) {
                LOG.info("Failed to receive order. See details below: ", customException);
                receiveOrderResponse.setResponseCode(customException.getErrorCode());
                receiveOrderResponse.setResponseMessage(customException.getMessage());
            }

        } else {
            receiveOrderResponse.setResponseCode(400);
            receiveOrderResponse.setResponseMessage("Receive order request has invalid parameters");
            LOG.error("Error: Invalid receive order request parameters");
        }

        return receiveOrderResponse;
    }


    @PostMapping(value = MockSimsConstants.CANCEL_ORDER_ENDPOINT)
    public ReceiveOrderResponse cancelOrder(@RequestBody ReceiveOrderRequest cancelOrderRequest) {
        ReceiveOrderResponse cancelOrderResponse = new ReceiveOrderResponse();

        if (isValidReceiveOrderRequest(cancelOrderRequest)) {
            try {
                cancelOrderResponse = receiveOrderService.cancelOrder(cancelOrderRequest);
                LOG.info("Order {} cancelled successfully", cancelOrderRequest.getOrderId());

            } catch (MockSimsCustomException customException) {
                LOG.info("Failed to cancel order. See details below: ", customException);
                cancelOrderResponse.setResponseCode(customException.getErrorCode());
                cancelOrderResponse.setResponseMessage(customException.getMessage());
            }

        } else {
            cancelOrderResponse.setResponseCode(400);
            cancelOrderResponse.setResponseMessage("Cancel order request has invalid parameters");
            LOG.error("Error: Invalid cancel order request parameters");
        }

        return cancelOrderResponse;
    }

    private boolean isValidReceiveOrderRequest(ReceiveOrderRequest receiveOrderRequest) {
        return receiveOrderRequest != null
                && receiveOrderRequest.getOrderId() != null
                && receiveOrderRequest.getOrderId() > 0
                && ValidationHelper.validateDivisionNumber(receiveOrderRequest.getDivisionNumber())
                && ValidationHelper.validateStoreNumber(receiveOrderRequest.getStoreNumber())
                && ValidationHelper.validateUserEuid(receiveOrderRequest.getUserEuid());
    }

}