package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.MovementInfoRecord;
import mocksims.project.backend.api.domain.MovementInfoRequest;
import mocksims.project.backend.api.domain.MovementInfoResponse;
import mocksims.project.backend.exception.MockSimsCustomException;
import mocksims.project.backend.repository.MovementInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MovementInfoServiceImpl implements MovementInfoService{
    private static final Logger LOG = LoggerFactory.getLogger(MovementInfoServiceImpl.class);
    private final MovementInfoRepository movementInfoRepository;

    public MovementInfoServiceImpl(MovementInfoRepository movementInfoRepository) {
        this.movementInfoRepository = movementInfoRepository;
    }

    @Override
    public MovementInfoResponse getMovementInfo(MovementInfoRequest movementInfoRequest){
        MovementInfoResponse response = new MovementInfoResponse();

        try {
            List<MovementInfoRecord> movements = movementInfoRepository.getMovementInfo(
                    movementInfoRequest.getStoreNumber(),
                    movementInfoRequest.getDivisionNumber(),
                    movementInfoRequest.getUpcNumber()
            );

            response.setMovements(movements);
            response.setResponseCode(200);
            response.setResponseMessage("Successfully retrieved movement info");
        } catch (DataAccessException error){
            LOG.error("Failed to retrieve movement info.", error);
            throw new MockSimsCustomException(500, "Error: Failed to retrieve movement info");
        }
        return response;
    }
}
