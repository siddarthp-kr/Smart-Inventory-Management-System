package mocksims.project.backend.controller;

import mocksims.project.backend.api.domain.MarkdownInformationResponse;
import mocksims.project.backend.api.domain.MarkdownItemRequest;
import mocksims.project.backend.api.domain.MarkdownItemResponse;
import mocksims.project.backend.domain.MockSimsConstants;
import mocksims.project.backend.exception.MockSimsCustomException;
import mocksims.project.backend.service.MarkdownItemService;
import mocksims.project.backend.util.ValidationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/pdm")
@CrossOrigin(origins = "http://localhost:4200")
public class MarkdownItemController {

    private static final Logger LOG = LoggerFactory.getLogger(MarkdownItemController.class);

    private final MarkdownItemService markdownItemService;

    public MarkdownItemController (MarkdownItemService markdownItemService){
        this.markdownItemService = markdownItemService;
    }

    @PostMapping(value = MockSimsConstants.MARKDOWN_ITEM_ENDPOINT)
    public ResponseEntity<MarkdownItemResponse> markdownItem(@RequestBody MarkdownItemRequest markdownItemRequest){
        MarkdownItemResponse markdownItemResponse = new MarkdownItemResponse();

        if(markdownItemRequest.getQuantity() > 0
            && ValidationHelper.validateUserEuid(markdownItemRequest.getUserEuid())
            && ValidationHelper.validateStoreNumber(markdownItemRequest.getStoreNumber())
            && ValidationHelper.validateDivisionNumber(markdownItemRequest.getDivisionNumber())
            && ValidationHelper.validateUpcNumber(markdownItemRequest.getUpcNumber()))
        {
            try {
                markdownItemService.markdownItem(markdownItemRequest);
                markdownItemResponse.setResponseMessage("Successfully marked down item " + markdownItemRequest.getUpcNumber() + ".");
                return ResponseEntity.ok(markdownItemResponse);
            } catch (MockSimsCustomException e){
                LOG.error("Failed to markdown item.", e);
                markdownItemResponse.setResponseMessage("Failed to markdown item due to internal server error.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(markdownItemResponse);
            }
        } else {
            LOG.error("Failed to markdown item. Invalid request parameters.");
            markdownItemResponse.setResponseMessage("Failed to markdown item: invalid request parameters");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(markdownItemResponse);
        }

    }

    @GetMapping(value = MockSimsConstants.MARKDOWN_ITEM_INFORMATION_ENDPOINT)
    public ResponseEntity<MarkdownInformationResponse> getMarkdownInformation(@RequestParam String upcNumber, @RequestParam Integer alertId){

        MarkdownInformationResponse markdownInformationResponse = new MarkdownInformationResponse();

        try {
            markdownInformationResponse = markdownItemService.getMarkdownInfo(upcNumber, alertId);
            markdownInformationResponse.setResponseMessage("Successfully retrieved markdown information for alert " + upcNumber + ".");
            return ResponseEntity.ok(markdownInformationResponse);
        } catch (MockSimsCustomException e){
            markdownInformationResponse.setResponseMessage("Failed to get markdown information for alert " + alertId + ".");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(markdownInformationResponse);
        }
    }



}
