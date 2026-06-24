package mocksims.project.backend.domain.task;

import mocksims.project.backend.exception.MockSimsCustomException;
import mocksims.project.backend.repository.PdmAlertGenerationRepositoryImpl;
import mocksims.project.backend.service.PdmAlertGenerationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RunScheduledJobsTask {

    private static final Logger LOG = LoggerFactory.getLogger(RunScheduledJobsTask.class);

    private final PdmAlertGenerationService pdmAlertGenerationService;

    public RunScheduledJobsTask(PdmAlertGenerationService pdmAlertGenerationService) {
        this.pdmAlertGenerationService = pdmAlertGenerationService;
    }

    @Scheduled(cron = "@midnight", zone = "America/New_York")
    public void generatePdmAlerts(){
        try {
            this.pdmAlertGenerationService.generatePdmAlerts();
        } catch (MockSimsCustomException e){
            LOG.error("Failed to generate PDM alerts", e);
        }
    }

}
