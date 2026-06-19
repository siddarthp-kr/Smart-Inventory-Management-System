package mocksims.project.backend.api.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BohRequest {
    private String divisionNumber;
    private String storeNumber;
}
