package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.MarkdownInformationResponse;
import mocksims.project.backend.api.domain.MarkdownItemRequest;
import mocksims.project.backend.exception.MockSimsCustomException;
import mocksims.project.backend.repository.MarkdownItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class MarkdownItemServiceImpl implements MarkdownItemService {

    private final MarkdownItemRepository markdownItemRepository;

    public MarkdownItemServiceImpl (MarkdownItemRepository markdownItemRepository){
        this.markdownItemRepository = markdownItemRepository;
    }

    @Override
    @Transactional
    public void markdownItem(MarkdownItemRequest markdownItemRequest) {

        LocalDateTime currentTime = LocalDateTime.now();

        Integer qodNumber = markdownItemRepository.getQodNumber(markdownItemRequest.getStoreNumber(), markdownItemRequest.getDivisionNumber(), markdownItemRequest.getUpcNumber());
        Integer qomNumber = markdownItemRepository.getQomNumber(markdownItemRequest.getStoreNumber(), markdownItemRequest.getDivisionNumber(), markdownItemRequest.getUpcNumber());

        if(markdownItemRequest.getQuantity() > qodNumber){
            throw new MockSimsCustomException(400, "Failed to markdown item: cannot markdown more items than are in QOD");
        }

        Double originalPrice = markdownItemRepository.getStandardPrice(markdownItemRequest.getUpcNumber());
        Integer firstMarkdownPercent = markdownItemRepository.getFirstMarkdownPercent(markdownItemRequest.getAlertId());
        Double newPrice = originalPrice * (1.0 - firstMarkdownPercent/100.0);

        markdownItemRepository.updateQodAndQom(markdownItemRequest.getStoreNumber(), markdownItemRequest.getDivisionNumber(), markdownItemRequest.getUpcNumber(), markdownItemRequest.getQuantity());

        markdownItemRepository.updatePdmAlert(markdownItemRequest.getAlertId(),currentTime, markdownItemRequest.getUserEuid());

        markdownItemRepository.insertMarkdownTransactionInfo(markdownItemRequest, originalPrice, newPrice, currentTime, qodNumber, qomNumber);

        /*
         - Get the QOD and QOM before the transaction - DONE
            If quantity is greater than QOD, throw an error in service layer
         - Get the original price - DONE
         - Get the first markdown percent - DONE
         - Increment QOM and decrement QOD - DONE
             - do a check to ensure that the number of rows updated is 1. If not, throw an error
         - Update is_active, alert_actioned_time, alert_actioned_euid, alert_actioned_code for the given alert_id - DONE
         - Insert a row in MD_TRANSACTIONS with all of the information - DONE
            - Gets euid, store, division, upc from request
            - Already gets the original price
            - Calculates the new price in the service layer
                - Gets first markdown percent from PDM_ALERTS
            - Gets actioned_time from service layer
            - Already gets QOD and QOM

         */
    }

    @Override
    public MarkdownInformationResponse getMarkdownInfo(String upcNumber, Integer alertId) {
        MarkdownInformationResponse markdownInformationResponse = new MarkdownInformationResponse();

        Double originalPrice = markdownItemRepository.getStandardPrice(upcNumber);
        markdownInformationResponse.setOriginalPrice(originalPrice);

        Integer firstMarkdownPercent = markdownItemRepository.getFirstMarkdownPercent(alertId);
        Double newPrice = originalPrice * (1.0 - firstMarkdownPercent/100.0);

        markdownInformationResponse.setNewPrice(Math.round(newPrice * 100) / 100.);

        return markdownInformationResponse;
    }
}
