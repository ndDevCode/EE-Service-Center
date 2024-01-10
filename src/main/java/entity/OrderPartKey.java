package entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Embeddable

public class OrderPartKey implements Serializable {
    @Column(name = "order_id")
    private String orderId;

    @Column(name = "part_id")
    private String partId;
}
