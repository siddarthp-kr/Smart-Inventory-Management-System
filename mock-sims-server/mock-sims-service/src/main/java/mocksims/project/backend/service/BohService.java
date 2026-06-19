package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.BohRequest;
import mocksims.project.backend.api.domain.BohResponse;


public interface BohService {
    BohResponse getBohInfo(BohRequest bohRequest);
}
