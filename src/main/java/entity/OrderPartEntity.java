package entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString

@Entity
@Table(name = "order_parts")
public class OrderPartEntity {
    @EmbeddedId
    private OrderPartKey id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne
    @MapsId("partId")
    @JoinColumn(name = "part_id")
    private PartEntity part;

    private int qtyRequired;
}
