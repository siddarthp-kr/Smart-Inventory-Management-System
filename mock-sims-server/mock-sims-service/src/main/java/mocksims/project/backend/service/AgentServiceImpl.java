package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.AgentRequest;
import mocksims.project.backend.api.domain.AgentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class AgentServiceImpl implements AgentService {
    private static final Logger LOG = LoggerFactory.getLogger(AgentServiceImpl.class);

    private final ChatClient chatClient;
    private final AgentTools agentTools;
    //private final ChatMemory chatMemory;

    public AgentServiceImpl(ChatClient chatClient, AgentTools agentTools, ChatMemory chatMemory) {
        this.chatClient = chatClient;
        this.agentTools = agentTools;
        //this.chatMemory = chatMemory;
    }

    /**
     * Handles a natural-language query by passing it to the AI agent along with database tools.
     * The agent will call tools as needed and return a plain-language summary of results.
     *
     * @param request contains storeNumber, divisionNumber, conversationId, and the user's natural-language requestDetails
     * @return AgentResponse containing the AI-generated summary and the conversationId
     */
    @Override
    public AgentResponse handleQuery(AgentRequest request) throws IOException {
        String conversationId = (request.getConversationId() != null && !request.getConversationId().isBlank())
                ? request.getConversationId()
                : UUID.randomUUID().toString();

        LOG.info("AgentServiceImpl handling query for store {} division {} conversation {}",
                request.getStoreNumber(), request.getDivisionNumber(), conversationId);

        String systemPrompt = new String(
                new ClassPathResource("prompts/MockSimsAgentSystemPrompt.md").getInputStream().readAllBytes()
        ).formatted(request.getStoreNumber(), request.getDivisionNumber());

        String summary = chatClient.prompt()
                .system(systemPrompt)
                .user(request.getRequestDetails())
                .tools(agentTools)
                .call()
                .content();

        AgentResponse response = new AgentResponse();
        response.setSummary(summary);
        response.setConversationId(conversationId);
        return response;
    }
}

