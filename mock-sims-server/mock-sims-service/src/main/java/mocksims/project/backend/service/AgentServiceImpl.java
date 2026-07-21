package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.AgentRequest;
import mocksims.project.backend.api.domain.AgentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AgentServiceImpl implements AgentService {
    private static final Logger LOG = LoggerFactory.getLogger(AgentServiceImpl.class);

    private final ChatClient chatClient;
    private final AgentTools agentTools;

    public AgentServiceImpl(ChatClient.Builder chatClientBuilder, AgentTools agentTools) {
        this.chatClient = chatClientBuilder.build();
        this.agentTools = agentTools;
    }

    /**
     * Handles a natural-language query by passing it to the AI agent along with database tools.
     * The agent will call tools as needed and return a plain-language summary of results.
     *
     * @param request contains storeNumber, divisionNumber, and the user's natural-language requestDetails
     * @return AgentResponse containing the AI-generated summary
     */
    @Override
    public AgentResponse handleQuery(AgentRequest request) {
        LOG.info("AgentServiceImpl handling query for store {} division {}",
                request.getStoreNumber(), request.getDivisionNumber());

        String systemPrompt = """
                You are a retail inventory assistant for a store management system.
                You have access to tools that query the store's inventory database.
                Use the tools to answer the user's question accurately, then provide a clear, concise summary.
                Always use storeNumber "%s" and divisionNumber "%s" when calling tools.
                Do not ask for clarification — use the tools to find the answer.
                """.formatted(request.getStoreNumber(), request.getDivisionNumber());

        String summary = chatClient.prompt()
                .system(systemPrompt)
                .user(request.getRequestDetails())
                .tools(agentTools)
                .call()
                .content();

        AgentResponse response = new AgentResponse();
        response.setSummary(summary);
        return response;
    }
}

