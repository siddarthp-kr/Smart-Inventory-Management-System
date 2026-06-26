package mocksims.project.backend.api.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MarkdownInformationResponse {
    String responseMessage;
    Double originalPrice;
    Double newPrice;
}
