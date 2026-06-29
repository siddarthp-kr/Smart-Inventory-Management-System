package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.ReceiveOrderItemRecord;
import mocksims.project.backend.api.domain.ReceiveOrderRequest;
import mocksims.project.backend.api.domain.ReceiveOrderResponse;
import mocksims.project.backend.exception.MockSimsCustomException;
import mocksims.project.backend.repository.ReceiveOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReceiveOrderServiceImpl implements ReceiveOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(ReceiveOrderServiceImpl.class);

    private final ReceiveOrderRepository receiveOrderRepository;

    public ReceiveOrderServiceImpl(ReceiveOrderRepository receiveOrderRepository) {
        this.receiveOrderRepository = receiveOrderRepository;
    }

    @Override
    @Transactional
    public ReceiveOrderResponse receiveOrder(ReceiveOrderRequest receiveOrderRequest) {
        ReceiveOrderResponse receiveOrderResponse = new ReceiveOrderResponse();

        LocalDateTime orderReceivedTime = LocalDateTime.now();

        Boolean orderReceived = receiveOrderRepository.getOrderReceivedStatus(
                receiveOrderRequest.getStoreNumber(),
                receiveOrderRequest.getDivisionNumber(),
                receiveOrderRequest.getOrderId()
        );

        if (Boolean.TRUE.equals(orderReceived)) {
            throw new MockSimsCustomException(409, "Order has already been received.");
        }

        List<ReceiveOrderItemRecord> orderItems = receiveOrderRepository.getOrderItems(receiveOrderRequest.getOrderId());

        if (orderItems.isEmpty()) {
            throw new MockSimsCustomException(404, "No ordered items found for order " + receiveOrderRequest.getOrderId());
        }

        List<String> upcNumbers = orderItems.stream().map(ReceiveOrderItemRecord::getUpcNumber).distinct().toList();

        Map<String, Integer> qodBeforeTransactionByUpc = receiveOrderRepository.getQodNumbersByUpc(receiveOrderRequest.getStoreNumber(), receiveOrderRequest.getDivisionNumber(), upcNumbers);
        validateAllItemsHaveBohRecords(orderItems, qodBeforeTransactionByUpc);
        Map<String, Integer> daysAfterOrderToSetExpByUpc = receiveOrderRepository.getDaysAfterOrderToSetExpByUpc(upcNumbers);
        Map<String, LocalDate> expirationDateByUpc = buildExpirationDateByUpc(orderItems, daysAfterOrderToSetExpByUpc, orderReceivedTime.toLocalDate());
        receiveOrderRepository.batchUpdateQodBeforeTransaction(receiveOrderRequest.getOrderId(), qodBeforeTransactionByUpc);
        receiveOrderRepository.batchIncrementQod(receiveOrderRequest.getStoreNumber(), receiveOrderRequest.getDivisionNumber(), orderItems);
        receiveOrderRepository.batchInsertProductInventoryInfo(
                receiveOrderRequest.getOrderId(),
                orderItems,
                expirationDateByUpc,
                orderReceivedTime.toLocalDate()
        );

        receiveOrderRepository.updateOrderReceived(
                receiveOrderRequest.getStoreNumber(),
                receiveOrderRequest.getDivisionNumber(),
                receiveOrderRequest.getOrderId(),
                receiveOrderRequest.getUserEuid(),
                orderReceivedTime
        );

        receiveOrderResponse.setResponseCode(200);
        receiveOrderResponse.setResponseMessage("Order received successfully");

        LOG.info("Order {} received successfully by user {}", receiveOrderRequest.getOrderId(), receiveOrderRequest.getUserEuid());

        return receiveOrderResponse;
    }

    private void validateAllItemsHaveBohRecords(List<ReceiveOrderItemRecord> orderItems, Map<String, Integer> qodBeforeTransactionByUpc) {
        for (ReceiveOrderItemRecord item : orderItems) {
            if (!qodBeforeTransactionByUpc.containsKey(item.getUpcNumber())) {
                throw new MockSimsCustomException(404, "BOH record not found for UPC " + item.getUpcNumber());
            }
        }
    }

    private Map<String, LocalDate> buildExpirationDateByUpc(
            List<ReceiveOrderItemRecord> orderItems,
            Map<String, Integer> daysAfterOrderToSetExpByUpc,
            LocalDate orderReceivedDate
    ) {
        Map<String, LocalDate> expirationDateByUpc = new HashMap<>();

        for (ReceiveOrderItemRecord item : orderItems) {
            Integer daysAfterOrderToSetExp = daysAfterOrderToSetExpByUpc.get(item.getUpcNumber());

            LocalDate expirationDate = null;

            if (daysAfterOrderToSetExp != null) {
                expirationDate = orderReceivedDate.plusDays(daysAfterOrderToSetExp);
            }

            expirationDateByUpc.put(item.getUpcNumber(), expirationDate);
        }

        return expirationDateByUpc;
    }
}