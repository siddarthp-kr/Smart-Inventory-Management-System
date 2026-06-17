package mocksims.project.backend.api.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderResponse {
    Integer responseCode;
    String responseMessage;
    Long orderId;
}
