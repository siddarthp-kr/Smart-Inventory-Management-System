package mocksims.project.backend.api.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductsRequest {
    private String storeNumber;
    private String divisionNumber;
}