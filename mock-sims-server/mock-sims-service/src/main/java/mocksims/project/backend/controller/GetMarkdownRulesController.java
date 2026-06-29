package mocksims.project.backend.controller;

import mocksims.project.backend.api.domain.GetMarkdownRulesResponse;
import mocksims.project.backend.domain.MockSimsConstants;
import mocksims.project.backend.exception.MockSimsCustomException;
import mocksims.project.backend.service.GetMarkdownRulesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/order")
@CrossOrigin(origins = "http://localhost:4200")
public class GetMarkdownRulesController {

    private static final Logger LOG = LoggerFactory.getLogger(GetMarkdownRulesController.class);

    private final GetMarkdownRulesService getMarkdownRulesService;

    public GetMarkdownRulesController(GetMarkdownRulesService getMarkdownRulesService) {
        this.getMarkdownRulesService = getMarkdownRulesService;
    }

    @GetMapping(value = MockSimsConstants.GET_MARKDOWN_RULES_ENDPOINT)
    public ResponseEntity<GetMarkdownRulesResponse> getMarkdownRules() {
        GetMarkdownRulesResponse getMarkdownRulesResponse = new GetMarkdownRulesResponse();

        try {
            getMarkdownRulesResponse.setMarkdownRules(getMarkdownRulesService.getMarkdownRules());
            getMarkdownRulesResponse.setResponseMessage("Successfully retrieved " + getMarkdownRulesResponse.getMarkdownRules().size() + " markdown rules.");

            LOG.info("Successfully retrieved markdown rules.");

            return ResponseEntity.ok(getMarkdownRulesResponse);

        } catch (MockSimsCustomException error) {
            LOG.error("Failed to retrieve markdown rules.", error);

            getMarkdownRulesResponse.setResponseMessage("Failed to retrieve markdown rules.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(getMarkdownRulesResponse);
        }
    }
}
