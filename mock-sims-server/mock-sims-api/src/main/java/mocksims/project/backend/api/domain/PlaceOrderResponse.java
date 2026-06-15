package mocksims.project.backend.api.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderResponse {
    int responseCode;
    String responseMessage;
}
