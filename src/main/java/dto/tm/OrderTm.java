package dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class OrderTm {
    private String orderId;
    private String description;
    private String orderDate;
    private String status;
    private String customer;
}
