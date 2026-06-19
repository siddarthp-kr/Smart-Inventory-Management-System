package mocksims.project.backend.repository;

import mocksims.project.backend.api.domain.BohItem;
import java.util.List;

public interface BohRepository {
    List<BohItem> getBohInfo(String storeNumber, String divisionNumber);
}
