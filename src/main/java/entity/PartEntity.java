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
@Table(name="parts")
public class PartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "part-id-generator")
    @GenericGenerator(
            name = "part-id-generator",
            strategy = "dao.util.idgenerators.PartIdGenerator",
            parameters = @org.hibernate.annotations.Parameter(name = "prefix", value = "PT")
    )
    @Column(name = "part_id")
    private String partId;
    private String name;
    private double price;
    private int qtyOnHand;

    public PartEntity(String name, double price, int qtyOnHand) {
        this.name = name;
        this.price = price;
        this.qtyOnHand = qtyOnHand;
    }
}
