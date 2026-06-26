package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.RfiItemRequest;
import mocksims.project.backend.controller.RfiItemController;
import mocksims.project.backend.exception.MockSimsCustomException;
import mocksims.project.backend.repository.RfiItemRepository;
import mocksims.project.backend.repository.RfiItemRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RfiItemServiceImpl implements RfiItemService {

    private static final Logger LOG = LoggerFactory.getLogger(RfiItemServiceImpl.class);

    private final RfiItemRepository rfiItemRepository;

    public RfiItemServiceImpl (RfiItemRepositoryImpl rfiItemRepository){
        this.rfiItemRepository = rfiItemRepository;
    }

    @Override
    @Transactional
    public void rfiItem(RfiItemRequest rfiItemRequest) {

        LocalDateTime currentTime = LocalDateTime.now();

        Integer qodNumber = rfiItemRepository.getQodNumber(rfiItemRequest.getStoreNumber(), rfiItemRequest.getDivisionNumber(), rfiItemRequest.getUpcNumber());
        Integer qomNumber = rfiItemRepository.getQomNumber(rfiItemRequest.getStoreNumber(), rfiItemRequest.getDivisionNumber(), rfiItemRequest.getUpcNumber());

        if(rfiItemRequest.getQuantity() > qodNumber){
            throw new MockSimsCustomException(400, String.format("Cannot remove items from inventory because user is requesting to remove more items than are in QOD. User: %s. UPC: %s.", rfiItemRequest.getUserEuid(), rfiItemRequest.getUpcNumber()));
        }

        rfiItemRepository.updatePdmAlert(rfiItemRequest.getAlertId(), currentTime, rfiItemRequest.getUserEuid());

        rfiItemRepository.insertRfiTransactionInfo(rfiItemRequest, qodNumber, qomNumber, currentTime);

        /*
        Find original qod and qom (reuse md function)
        Decrement QOD using quantity
            - Check that they are not removing more items than are on the inventory
        Update PDM Alert
        Inserts a row in RFI Transactions:
             - user euid, store, division, upc, quantity comes from request
             - qod and qom found prior in this service layer
             - reason code hard coded to "OD"
             - action time calculated in the service layer
         */

    }
}
