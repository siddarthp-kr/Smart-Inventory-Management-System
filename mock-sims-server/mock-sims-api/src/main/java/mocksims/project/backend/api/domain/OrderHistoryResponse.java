package mocksims.project.backend.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistoryResponse {
    Integer orderHistoryResponseCode;
    String orderHistoryResponseMessage;
    List<OrderHistoryRecord> orders;
}
