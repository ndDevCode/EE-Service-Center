package bo.custom.impl;

import bo.custom.UserAuthenticationBo;
import bo.util.EncriptionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.DaoFactory;
import dao.custom.UserAuthenticationDao;
import dao.util.DaoType;
import dto.StaffDto;
import dto.UserCredentialsDto;
import entity.StaffEntity;

import java.sql.SQLException;

public class UserAuthenticationBoImpl implements UserAuthenticationBo {
    UserAuthenticationDao userAuthenticationDao = DaoFactory.getInstance().getDao(DaoType.USER_AUTHENTICATION);
    ObjectMapper mapper = new ObjectMapper();
    @Override
    public StaffDto verifyUser(UserCredentialsDto userCredentialsDto) {
        UserCredentialsDto userCredentials = new UserCredentialsDto(
                userCredentialsDto.getUserEmail(),
                EncriptionUtil.encrypt(userCredentialsDto.getUserPassword())
        );

        StaffEntity staffEntity = userAuthenticationDao.verifyUser(userCredentialsDto);
        if(userCredentials.getUserEmail().equals(staffEntity.getEmail()) &&
                userCredentials.getUserPassword().equals(staffEntity.getPassword())){
            StaffDto staffDto = mapper.convertValue(staffEntity, StaffDto.class);
            staffDto.setPassword("");
            return  staffDto;
        }

        return null;
    }

    @Override
    public boolean isInitialLogin() {
        return userAuthenticationDao.isInitialLogin();
    }

    @Override
    public boolean saveUser(StaffDto staffDto) throws SQLException, ClassNotFoundException {
        staffDto.setPassword(EncriptionUtil.encrypt(staffDto.getPassword()));
        return userAuthenticationDao.save(mapper.convertValue(staffDto, StaffEntity.class));
    }
}
