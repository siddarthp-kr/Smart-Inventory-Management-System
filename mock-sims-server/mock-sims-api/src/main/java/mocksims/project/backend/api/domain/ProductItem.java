package mocksims.project.backend.api.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductItem {
    private String upcNumber;
    private String productName;
}
