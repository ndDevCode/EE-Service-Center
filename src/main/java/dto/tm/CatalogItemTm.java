package dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class CatalogItemTm {
    private String catalogItemId;
    private String itemName;
    private String category;
    private String imgLink;
    private String addedByUser;
    private String action;
}
