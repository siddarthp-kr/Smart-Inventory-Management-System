package mocksims.project.backend.controller;

import mocksims.project.backend.domain.task.RunScheduledJobsTask;
import mocksims.project.backend.exception.MockSimsCustomException;
import mocksims.project.backend.service.PdmAlertGenerationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alerts")
public class TESTALERTGENERATIONCONTROLLER {

    private static final Logger LOG = LoggerFactory.getLogger(TESTALERTGENERATIONCONTROLLER.class);

    private final PdmAlertGenerationService service;

    public TESTALERTGENERATIONCONTROLLER (PdmAlertGenerationService service){
        this.service = service;
    }

    @GetMapping(value = "/test")
    public void generateAlerts(){
        try {
            this.service.generatePdmAlerts();
            LOG.info("Succesfully loaded results");
        } catch (MockSimsCustomException e){
            LOG.error("Failed to generate PDM alerts", e);
        }
    }
}
