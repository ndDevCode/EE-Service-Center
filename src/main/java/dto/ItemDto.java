package dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ItemDto{
    private String itemId;
    private String name;
    private String category;
    private String status;
    private String zone;
    private double repairPrice;
    private String orderId;
}
