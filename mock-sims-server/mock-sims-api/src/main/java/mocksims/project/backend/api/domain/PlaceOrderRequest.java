package mocksims.project.backend.api.domain;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PlaceOrderRequest {
    String storeNumber;
    String divisionNumber;
    String userEuid;
    List<PlaceOrderItem> items;
}
