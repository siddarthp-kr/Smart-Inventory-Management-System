package mocksims.project.backend.controller;

import mocksims.project.backend.api.domain.OrderHistoryRequest;
import mocksims.project.backend.api.domain.OrderHistoryResponse;
import mocksims.project.backend.exception.MockSimsCustomException;
import mocksims.project.backend.service.OrderHistoryService;
import mocksims.project.backend.util.ValidationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import static mocksims.project.backend.domain.MockSimsConstants.ORDER_HISTORY_ENDPOINT;

@RestController
@RequestMapping(value = "/api/order")
@CrossOrigin(origins = "http://localhost:4200/")
public class OrderHistoryController {

    private static final Logger LOG = LoggerFactory.getLogger(OrderHistoryController.class);

    private final OrderHistoryService orderHistoryService;

    public OrderHistoryController(OrderHistoryService orderHistoryService){
        this.orderHistoryService = orderHistoryService;
    }

    @PostMapping(value = ORDER_HISTORY_ENDPOINT)
    public OrderHistoryResponse getOrderHistory(@RequestBody OrderHistoryRequest orderHistoryRequest){

        OrderHistoryResponse orderHistoryResponse = new OrderHistoryResponse();

        if(!ValidationHelper.validateStoreNumber(orderHistoryRequest.getStoreNumber()) || !ValidationHelper.validateDivisionNumber(orderHistoryRequest.getDivisionNumber())){
            LOG.error("Invalid store or division number.", new MockSimsCustomException(400, "Error: Invalid order history request parameters"));
            orderHistoryResponse.setResponseCode(400);
            orderHistoryResponse.setResponseMessage("Failed to get order history due to invalid store or division number.");
        } else {
            try {
                orderHistoryResponse = this.orderHistoryService.getOrderHistory(orderHistoryRequest);
            } catch (MockSimsCustomException e){
                LOG.error("Failed to get order history.", e);
                orderHistoryResponse.setResponseCode(500);
                orderHistoryResponse.setResponseMessage("Failed to get order history");
            }
        }



        return orderHistoryResponse;
    }

}
