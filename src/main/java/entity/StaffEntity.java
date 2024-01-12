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
@Table(name = "staff")
public class StaffEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "staff-id-generator")
    @GenericGenerator(
            name = "staff-id-generator",
            strategy = "dao.util.idgenerators.StaffIdGenerator",
            parameters = @org.hibernate.annotations.Parameter(name = "prefix" , value = "ST")
    )
    @Column(name = "staff_id", nullable = false, length =5)
    private String staffId;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false,unique = true)
    private String contactNo;
    @Column(nullable = false,unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String role;

    @JsonIgnore
    @OneToMany(mappedBy = "staff", fetch = FetchType.EAGER)
    private List<OrderEntity> orders = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "staff")
    private List<CatalogEntity> catalogItems = new ArrayList<>();

    public StaffEntity(String staffId, String firstName, String lastName, String contactNo, String email, String password, String role) {
        this.staffId = staffId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNo = contactNo;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @Override
    public String toString() {
        return "StaffEntity{" +
                "staffId='" + staffId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}

