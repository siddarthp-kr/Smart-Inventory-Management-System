package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.MarkdownRulesRecord;
import mocksims.project.backend.api.domain.PushBackExpirationRequest;
import mocksims.project.backend.controller.ProductsController;
import mocksims.project.backend.exception.MockSimsCustomException;
import mocksims.project.backend.repository.PushBackExpirationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class PushBackExpirationServiceImpl implements PushBackExpirationService {

    private static final Logger LOG = LoggerFactory.getLogger(PushBackExpirationServiceImpl.class);

    private final PushBackExpirationRepository pushBackExpirationRepository;

    public PushBackExpirationServiceImpl (PushBackExpirationRepository pushBackExpirationRepository){
        this.pushBackExpirationRepository = pushBackExpirationRepository;
    }

    @Override
    @Transactional
    public void pushBackExpirationDate(PushBackExpirationRequest pushBackExpirationRequest) {
        /*
        get subcommodity numbers from the alertid
        get new markdown rules from the subcommodity number
        set dates based on the new day
        insert new row into the table
        deactivate the old row
         */

        boolean isActive = pushBackExpirationRepository.getAlertActiveStatus(pushBackExpirationRequest.getAlertId());

        if(!isActive){
            throw new MockSimsCustomException(400, String.format("Failed to push back expiration date for alert %d. An inactive alert cannot be pushed back.", pushBackExpirationRequest.getAlertId()));
        }

        LocalDate originalExpiration = pushBackExpirationRepository.getOriginalExpirationDate(pushBackExpirationRequest.getAlertId());

        if(originalExpiration.isAfter(pushBackExpirationRequest.getNewExpirationDate()) || originalExpiration.isEqual(pushBackExpirationRequest.getNewExpirationDate())){
            throw new MockSimsCustomException(400, String.format("Failed to push back expiration for alert %d. New expiration date must be after original expiration date.", pushBackExpirationRequest.getAlertId()));
        }

        String subcommodityNumber = pushBackExpirationRepository.getSubcommodityNumber(pushBackExpirationRequest.getAlertId());
        MarkdownRulesRecord markdownRulesRecord = pushBackExpirationRepository.getMarkdownRules(subcommodityNumber);
        LocalDate newMdDate = pushBackExpirationRequest.getNewExpirationDate().minusDays(markdownRulesRecord.getDaysBeforeExpToMd());
        LocalDate newRfiDate = pushBackExpirationRequest.getNewExpirationDate().minusDays(markdownRulesRecord.getDaysBeforeExpToRfi());

        LocalDateTime currentTime = LocalDateTime.now();

        pushBackExpirationRepository.insertNewAlert(pushBackExpirationRequest.getNewExpirationDate(), newRfiDate, newMdDate, pushBackExpirationRequest.getAlertId());

        pushBackExpirationRepository.updatePdmAlert(pushBackExpirationRequest.getAlertId(), currentTime, pushBackExpirationRequest.getUserEuid());

        pushBackExpirationRepository.deactivateActiveDuplicateAlerts(pushBackExpirationRequest.getStoreNumber(), pushBackExpirationRequest.getDivisionNumber(), pushBackExpirationRequest.getUpcNumber(), currentTime, pushBackExpirationRequest.getUserEuid());

    }
}
