package mocksims.project.backend.controller;

import mocksims.project.backend.api.domain.OrderHistoryRequest;
import mocksims.project.backend.api.domain.OrderHistoryResponse;
import mocksims.project.backend.exception.MockSimsCustomException;
import mocksims.project.backend.service.OrderHistoryService;
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

    @GetMapping(value = ORDER_HISTORY_ENDPOINT)
    public OrderHistoryResponse getOrderHistory(@RequestBody OrderHistoryRequest orderHistoryRequest){

        //validate request

        OrderHistoryResponse orderHistoryResponse = new OrderHistoryResponse();
        try {
            orderHistoryResponse = this.orderHistoryService.getOrderHistory(orderHistoryRequest);
        } catch (MockSimsCustomException e){
            LOG.error("Failed to get order history.", e);
            orderHistoryResponse.setOrderHistoryResponseCode(500);
            orderHistoryResponse.setOrderHistoryResponseMessage("Failed to get order history");
        }

        return orderHistoryResponse;
    }

}
