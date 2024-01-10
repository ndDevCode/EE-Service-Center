package dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class StaffDto {
    private String staffId;
    private String firstName;
    private String lastName;
    private String contactNo;
    private String email;
    private String password;
    private String role;

    public StaffDto(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
