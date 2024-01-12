package bo.custom;

import bo.SuperBo;
import dto.StaffDto;
import dto.UserCredentialsDto;

import java.sql.SQLException;
import java.util.List;

public interface UserAuthenticationBo extends SuperBo {
     StaffDto verifyUser(UserCredentialsDto userCredentialsDto);
     boolean verifyUser(String email);
     boolean resetPassword(String email,String newPassword);
     boolean isInitialLogin();
     boolean saveUser(StaffDto staffDto) throws SQLException, ClassNotFoundException;
     boolean updateUser(StaffDto staffDto) throws SQLException, ClassNotFoundException;
     boolean deleteUser(String staffId) throws SQLException,ClassNotFoundException;
     StaffDto getUserData(String email);
     List<StaffDto> getAllStaff() throws SQLException, ClassNotFoundException;
}
