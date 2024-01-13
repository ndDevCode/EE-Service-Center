package dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class CatalogItemDto {
    private String catalogItemId;
    private String name;
    private String category;
    private String imgLink;
    private String staffId;
}
