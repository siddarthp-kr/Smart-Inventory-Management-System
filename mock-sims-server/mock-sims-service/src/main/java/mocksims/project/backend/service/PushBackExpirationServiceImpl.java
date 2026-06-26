package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.PushBackExpirationRequest;
import mocksims.project.backend.controller.ProductsController;
import mocksims.project.backend.repository.PushBackExpirationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    }
}
