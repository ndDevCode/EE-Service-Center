package dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private String status;
    @JsonIgnore
    private String zone;
    private double repairPrice;
    @JsonIgnore
    private String orderId;

    public ItemDto(String itemId, String name, String category, double repairPrice) {
        this.itemId = itemId;
        this.name = name;
        this.category = category;
        this.repairPrice = repairPrice;
    }
}
