package mocksims.project.backend.api.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddItemResponse {
    int responseCode;
    String responseMessage;
}
