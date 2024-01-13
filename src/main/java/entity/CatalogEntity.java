package entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
@ToString

@Entity
@Table(name = "catalog_item")
public class CatalogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "catalog-item-id-generator")
    @GenericGenerator(
            name="catalog-item-id-generator",
            strategy = "dao.util.idgenerators.CatalogItemIdGenerator",
            parameters = @org.hibernate.annotations.Parameter(name = "prefix",value = "CIT")
    )
    @Column(name = "catalog_item_id", nullable = false,length = 6)
    private String catalogItemId;
    private String name;
    private String category;
    private String imgLink;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "staff_id")
    private StaffEntity staff;

    public CatalogEntity(String catalogItemId, String name, String category, String imgLink) {
        this.catalogItemId = catalogItemId;
        this.name = name;
        this.category = category;
        this.imgLink = imgLink;
    }

    public CatalogEntity(String catalogItemId, String name, String category) {
        this.catalogItemId = catalogItemId;
        this.name = name;
        this.category = category;
    }
}
