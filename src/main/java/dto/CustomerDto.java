package dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString

public class CustomerDto {
    private String customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String contactNo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomerDto that = (CustomerDto) o;

        return contactNo.equals(that.contactNo);
    }

    @Override
    public int hashCode() {
        return contactNo.hashCode();
    }
}
