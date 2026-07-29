package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.AgentRequest;
import mocksims.project.backend.api.domain.AgentResponse;

import java.io.IOException;

public interface AgentService {
    AgentResponse handleQuery(AgentRequest request) throws IOException;
}

