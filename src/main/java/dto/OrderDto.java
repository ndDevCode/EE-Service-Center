package dto;

import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString

public class OrderDto {
    private String orderId;
    private String description;
    private String orderDate;
    private String status;
    private double totalPrice;
    private String customer;
    private String staff;
    private List items;
}
