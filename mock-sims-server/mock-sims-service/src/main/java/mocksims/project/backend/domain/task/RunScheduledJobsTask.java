package mocksims.project.backend.domain.task;

import mocksims.project.backend.service.PdmAlertGenerationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RunScheduledJobsTask {

    private final PdmAlertGenerationService pdmAlertGenerationService;

    public RunScheduledJobsTask(PdmAlertGenerationService pdmAlertGenerationService) {
        this.pdmAlertGenerationService = pdmAlertGenerationService;
    }

    @Scheduled(cron = "@midnight", zone = "America/New_York")
    public void generatePdmAlerts(){
        this.pdmAlertGenerationService.generatePdmAlerts();
    }

}
