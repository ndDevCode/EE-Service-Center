package dto.tm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString

public class ItemTm {
    private String itemId;
    private String name;
    private String category;
    private double repairPrice;
    @JsonIgnore
    private String action;
}
