package dao.custom;

import dao.CrudDao;
import dto.UserCredentialsDto;
import entity.StaffEntity;

public interface UserAuthenticationDao extends CrudDao<StaffEntity> {
    StaffEntity verifyUser(UserCredentialsDto userCredentialsDto);
    boolean isInitialLogin();
}
