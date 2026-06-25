package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.MarkdownInformationResponse;
import mocksims.project.backend.api.domain.MarkdownItemRequest;

public interface MarkdownItemService {

    public void markdownItem(MarkdownItemRequest markdownItemRequest);

    public MarkdownInformationResponse getMarkdownInfo(String upcNumber, Integer alertId);

}
