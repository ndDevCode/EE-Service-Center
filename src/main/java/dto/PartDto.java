package dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class PartDto {
    private String partId;
    private String name;
    private double price;
    private int qtyOnHand;
}
