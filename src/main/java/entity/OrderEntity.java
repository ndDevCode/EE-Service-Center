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

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private StaffEntity staff;

    @OneToMany(mappedBy = "order", orphanRemoval = true)
    private List<ItemInventoryEntity> items = new ArrayList<>();

    public OrderEntity(String orderId,String description, String orderDate, String status) {
        this.orderId = orderId;
        this.description = description;
        this.orderDate = orderDate;
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "orderId='" + orderId + '\'' +
                ", description='" + description + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}