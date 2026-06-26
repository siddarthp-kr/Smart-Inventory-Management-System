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
import java.util.List;

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
            throw new MockSimsCustomException(
                    404,
                    "No ordered items found for order " + receiveOrderRequest.getOrderId()
            );
        }

        for (ReceiveOrderItemRecord item : orderItems) {
            Integer qodBeforeTransaction = receiveOrderRepository.getQodNumber(
                    receiveOrderRequest.getStoreNumber(),
                    receiveOrderRequest.getDivisionNumber(),
                    item.getUpcNumber()
            );

            receiveOrderRepository.incrementQod(
                    receiveOrderRequest.getStoreNumber(),
                    receiveOrderRequest.getDivisionNumber(),
                    item.getUpcNumber(),
                    item.getQuantity()
            );

            receiveOrderRepository.updateQodBeforeTransaction(
                    receiveOrderRequest.getOrderId(),
                    item.getUpcNumber(),
                    qodBeforeTransaction
            );

            String subcommodityNumber = receiveOrderRepository.getSubcommodityNumber(item.getUpcNumber());

            Integer daysAfterOrderToSetExp = receiveOrderRepository.getNumberOfDaysBeforeExpiration(subcommodityNumber);

            LocalDate expirationDate = null;

            if (daysAfterOrderToSetExp != null) {
                expirationDate = orderReceivedTime.toLocalDate().plusDays(daysAfterOrderToSetExp);
            }

            receiveOrderRepository.insertProductInventoryInfo(
                    receiveOrderRequest.getOrderId(),
                    item.getUpcNumber(),
                    item.getQuantity(),
                    expirationDate,
                    orderReceivedTime.toLocalDate(),
                    true
            );
        }

        receiveOrderRepository.updateOrderReceived(
                receiveOrderRequest.getStoreNumber(),
                receiveOrderRequest.getDivisionNumber(),
                receiveOrderRequest.getOrderId(),
                receiveOrderRequest.getUserEuid(),
                orderReceivedTime
        );

        receiveOrderResponse.setResponseCode(200);
        receiveOrderResponse.setResponseMessage("Order received successfully");

        LOG.info("Order {} received successfully by user {}",
                receiveOrderRequest.getOrderId(),
                receiveOrderRequest.getUserEuid()
        );

        return receiveOrderResponse;
    }
}