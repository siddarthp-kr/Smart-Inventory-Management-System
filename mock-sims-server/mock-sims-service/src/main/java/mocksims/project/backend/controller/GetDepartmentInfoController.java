package mocksims.project.backend.controller;

import mocksims.project.backend.api.domain.GetDepartmentInfoResponse;
import mocksims.project.backend.exception.MockSimsCustomException;
import mocksims.project.backend.service.GetDepartmentInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static mocksims.project.backend.domain.MockSimsConstants.GET_DEPARTMENT_INFO_ENDPOINT;

@RestController
@RequestMapping(value = "/api/boh")
@CrossOrigin(origins = "http://localhost:4200/")
public class GetDepartmentInfoController {

    private static final Logger LOG = LoggerFactory.getLogger(GetDepartmentInfoController.class);

    private final GetDepartmentInfoService getDepartmentInfoService;

    public GetDepartmentInfoController(GetDepartmentInfoService getDepartmentInfoService){
        this.getDepartmentInfoService = getDepartmentInfoService;
    }

    @GetMapping(value = GET_DEPARTMENT_INFO_ENDPOINT)
    public ResponseEntity<GetDepartmentInfoResponse> getDepartmentInfo(){
        GetDepartmentInfoResponse getDepartmentInfoResponse = new GetDepartmentInfoResponse();
        try {
            getDepartmentInfoResponse = getDepartmentInfoService.getDepartmentInfo();
        } catch (MockSimsCustomException e){
            LOG.error("Failed to get department information", e);
            return ResponseEntity.status(e.getErrorCode()).body(getDepartmentInfoResponse);
        }
        return ResponseEntity.ok(getDepartmentInfoResponse);
    }
}
