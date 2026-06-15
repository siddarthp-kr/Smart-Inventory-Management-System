package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.AddItemRequest;
import mocksims.project.backend.api.domain.AddItemResponse;

public interface AddItemService {
    public AddItemResponse addItem(AddItemRequest addItemRequest);
}
