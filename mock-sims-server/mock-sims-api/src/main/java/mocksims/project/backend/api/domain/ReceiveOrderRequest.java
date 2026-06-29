package mocksims.project.backend.api.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReceiveOrderRequest {
    String storeNumber;
    String divisionNumber;
    String userEuid;
    Long orderId;
}
