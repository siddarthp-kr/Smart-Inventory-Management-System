package mocksims.project.backend.repository;

import mocksims.project.backend.api.domain.MovementInfoRecord;
import java.util.List;

public interface MovementInfoRepository {
    List<MovementInfoRecord> getMovementInfo(String storeNumber, String divisionNumber, String upcNumber);
}
