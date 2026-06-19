package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.BohItem;
import mocksims.project.backend.api.domain.BohRequest;
import mocksims.project.backend.api.domain.BohResponse;
import mocksims.project.backend.exception.MockSimsCustomException;
import mocksims.project.backend.repository.BohRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BohServiceImpl implements BohService{
    private static final Logger LOG = LoggerFactory.getLogger(BohServiceImpl.class);
    private final BohRepository bohRepository;

    public BohServiceImpl(BohRepository bohRepository){
        this.bohRepository = bohRepository;
    }

    /**
     * Retrieve BOH information for specific store and division and wrap into a response object
     * @param bohRequest request fore store number and division number
     * @return list of BOH product details
     */
    @Override
    public BohResponse getBohInfo(BohRequest bohRequest){
        BohResponse response = new BohResponse();

        try {
            List<BohItem> products = bohRepository.getBohInfo(
                    bohRequest.getStoreNumber(),
                    bohRequest.getDivisionNumber()
            );
            response.setProducts(products);
        } catch(DataAccessException error) {
            LOG.error("Failed to retrieve BOH information.", error);
            throw new MockSimsCustomException(500, "Error: Failed to retrieve BOH information");
        }
        return response;
    }
}
