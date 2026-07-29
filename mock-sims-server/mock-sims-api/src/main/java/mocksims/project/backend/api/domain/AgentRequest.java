package mocksims.project.backend.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgentRequest {
    private String storeNumber;
    private String divisionNumber;
    private String requestDetails;
    private String conversationId;
}

