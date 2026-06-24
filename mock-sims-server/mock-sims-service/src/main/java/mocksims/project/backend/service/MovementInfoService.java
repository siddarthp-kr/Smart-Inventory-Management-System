package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.MovementInfoRequest;
import mocksims.project.backend.api.domain.MovementInfoResponse;

public interface MovementInfoService {
    MovementInfoResponse getMovementInfo(MovementInfoRequest movementInfoRequest);
}
