package mocksims.project.backend.api.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PlaceOrderRequest {
    String storeNumber;
    String divisionNumber;
    String userEuid;
    String upcNumber;
    int quantity;
}
