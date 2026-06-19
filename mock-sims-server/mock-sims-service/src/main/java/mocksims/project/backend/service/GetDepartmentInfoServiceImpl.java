package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.GetDepartmentInfoResponse;
import mocksims.project.backend.exception.MockSimsCustomException;
import mocksims.project.backend.repository.GetDepartmentInfoRepository;
import mocksims.project.backend.repository.GetDepartmentInfoRepositoryImpl;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class GetDepartmentInfoServiceImpl implements GetDepartmentInfoService{

    private final GetDepartmentInfoRepository getDepartmentInfoRepository;

    public GetDepartmentInfoServiceImpl(GetDepartmentInfoRepository getDepartmentInfoRepository){
        this.getDepartmentInfoRepository = getDepartmentInfoRepository;
    }

    @Override
    public GetDepartmentInfoResponse getDepartmentInfo(){
        try {
            return new GetDepartmentInfoResponse(getDepartmentInfoRepository.getDepartmentInfo());
        } catch (DataAccessException e){
            throw new MockSimsCustomException(500, e.getMessage());
        }

    }
}
