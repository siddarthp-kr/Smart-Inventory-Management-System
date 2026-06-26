package mocksims.project.backend.api.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReceiveOrderResponse {
    Integer responseCode;
    String responseMessage;
}

