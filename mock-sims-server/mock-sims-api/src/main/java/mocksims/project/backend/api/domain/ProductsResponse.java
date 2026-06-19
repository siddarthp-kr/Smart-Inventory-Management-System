package mocksims.project.backend.api.domain;

import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ProductsResponse {
    private List<ProductItem> products;
}
