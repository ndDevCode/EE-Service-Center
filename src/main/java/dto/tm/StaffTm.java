package dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class StaffTm {
    private String staffId;
    private String firstName;
    private String lastName;
    private String contactNo;
    private String email;
    private String role;
    private String action;
}
