package entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "customer")
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "customer-id-generator")
    @GenericGenerator(
            name = "customer-id-generator",
            strategy = "dao.util.idgenerators.CustomerIdGenerator",
            parameters = @org.hibernate.annotations.Parameter(name = "prefix",value = "CTR")
    )
    @Column(name = "customer_id",nullable = false,length = 10)
    private String customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String contactNo;

    @JsonIgnore
    @OneToMany(mappedBy = "customer")
    private List<OrderEntity> orders = new ArrayList<>();

    public CustomerEntity(String firstName, String lastName, String email, String contactNo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.contactNo = contactNo;
    }

    @Override
    public String toString() {
        return "CustomerEntity{" +
                "customerId='" + customerId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", contactNo='" + contactNo + '\'' +
                '}';
    }
}
