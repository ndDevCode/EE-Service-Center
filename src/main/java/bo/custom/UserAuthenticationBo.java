package bo.custom;

import bo.SuperBo;
import dto.StaffDto;
import dto.UserCredentialsDto;

import java.sql.SQLException;

public interface UserAuthenticationBo extends SuperBo {
     StaffDto verifyUser(UserCredentialsDto userCredentialsDto);
     boolean isInitialLogin();
     boolean saveUser(StaffDto staffDto) throws SQLException, ClassNotFoundException;
}
