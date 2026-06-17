package mocksims.project.backend.api.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderRequest {
    String storeNumber;
    String divisionNumber;
    String userEuid;
    String upcNumber;
    int quantity;
}
