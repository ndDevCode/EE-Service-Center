package entity;

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
@Table(name="item_inventory")
public class ItemInventoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "item-id-generator")
    @GenericGenerator(
            name = "item-id-generator",
            strategy = "dao.util.idgenerators.ItemIdGenerator",
            parameters = @org.hibernate.annotations.Parameter(name = "prefix", value = "ITM")
    )
    @Column(name = "item_id")
    private String itemId;
    private String name;
    private String category;
    private String status;
    private String zone;
    private double repairPrice;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    public ItemInventoryEntity(String name, String category, String status, String zone, double repairPrice) {
        this.name = name;
        this.category = category;
        this.status = status;
        this.zone = zone;
        this.repairPrice = repairPrice;
    }
}
