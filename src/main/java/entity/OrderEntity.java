package entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@ToString

@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "order-id-generator")
    @GenericGenerator(
            name = "order-id-generator",
            strategy = "dao.util.idgenerators.OrderIdGenerator",
            parameters = @org.hibernate.annotations.Parameter(name="prefix", value = "ODR")
    )
    @Column(name = "order_id", nullable = false, length =10)
    private String orderId;
    private String description;
    private String orderDate;
    private String status;
    private double totalPrice;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private StaffEntity staff;

    @OneToMany(mappedBy = "order")
    private List<ItemInventoryEntity> items = new ArrayList<>();

    public OrderEntity(String description, String orderDate, String status, double totalPrice) {
        this.description = description;
        this.orderDate = orderDate;
        this.status = status;
        this.totalPrice = totalPrice;
    }
}