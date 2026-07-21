package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.AgentRequest;
import mocksims.project.backend.api.domain.AgentResponse;

public interface AgentService {
    AgentResponse handleQuery(AgentRequest request);
}

