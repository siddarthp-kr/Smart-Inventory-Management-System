package mocksims.project.backend.api.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReceiveOrderItemRecord {
    private String upcNumber;
    private Integer quantity;
}