package mocksims.project.backend.controller;

import mocksims.project.backend.api.domain.AgentRequest;
import mocksims.project.backend.api.domain.AgentResponse;
import mocksims.project.backend.service.AgentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agent")
@CrossOrigin(origins = "http://localhost:4200")
public class AgentController {
    private static final Logger LOG = LoggerFactory.getLogger(AgentController.class);

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    /**
     * POST request to submit a natural-language query to the AI agent.
     * The agent will use its tools to read from the database and return a summary.
     *
     * @param request contains storeNumber, divisionNumber, and a free-text requestDetails prompt
     * @return response entity containing a natural-language summary of the query results
     */
    @PostMapping("/query")
    public ResponseEntity<AgentResponse> query(@RequestBody AgentRequest request) {
        LOG.info("Agent query received for store {} division {}: {}",
                request.getStoreNumber(), request.getDivisionNumber(), request.getRequestDetails());
        AgentResponse response = agentService.handleQuery(request);
        return ResponseEntity.ok(response);
    }
}

