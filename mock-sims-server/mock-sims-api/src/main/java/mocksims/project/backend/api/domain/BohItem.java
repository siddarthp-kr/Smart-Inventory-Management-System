package mocksims.project.backend.api.domain;

import lombok.*;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BohItem {
    private String upcNumber;
    private Integer qodNumber;
    private Integer qomNumber;
    private String departmentNumber;
    private String departmentName;
    private String productName;
}
