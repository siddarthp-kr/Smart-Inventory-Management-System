package mocksims.project.backend.repository;

import mocksims.project.backend.api.domain.DepartmentInfoRecord;

import java.util.List;

public interface GetDepartmentInfoRepository {

    public List<DepartmentInfoRecord> getDepartmentInfo();

}
