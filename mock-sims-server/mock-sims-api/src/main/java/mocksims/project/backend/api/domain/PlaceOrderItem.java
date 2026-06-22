package mocksims.project.backend.api.domain;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceOrderItem {
    String upcNumber;
    Integer quantity;

    //these are populated in the service layer

    //add a jackson annotation at the top of the class for only including non null
    String subcommodityNumber;
    LocalDate expirationDate;
    Boolean isActive;
}
