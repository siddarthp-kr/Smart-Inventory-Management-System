package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.PushBackExpirationRequest;

public interface PushBackExpirationService {
    public void pushBackExpirationDate(PushBackExpirationRequest pushBackExpirationRequest);
}
