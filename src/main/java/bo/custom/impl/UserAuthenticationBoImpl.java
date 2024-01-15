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
import java.util.ArrayList;
import java.util.List;

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
    public boolean verifyUser(String email) {
        StaffEntity entity = userAuthenticationDao.verifyUser(email);
        return entity != null && entity.getEmail().equals(email);
    }

    @Override
    public boolean resetPassword(String email, String newPassword) {
        return userAuthenticationDao.updatePassword(email,EncriptionUtil.encrypt(newPassword));
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

    @Override
    public boolean updateUser(StaffDto staffDto) throws SQLException, ClassNotFoundException {
        if(staffDto.getPassword()!=null && staffDto.getPassword().isEmpty()){
            staffDto.setPassword(EncriptionUtil.encrypt(staffDto.getPassword()));
        }
        return userAuthenticationDao.update(mapper.convertValue(staffDto, StaffEntity.class));
    }

    @Override
    public boolean deleteUser(String staffId) throws SQLException, ClassNotFoundException {
        return userAuthenticationDao.delete(staffId);
    }

    @Override
    public StaffDto getUserData(String email) {
        StaffEntity entity = userAuthenticationDao.verifyUser(email);
        return mapper.convertValue(entity, StaffDto.class);
    }

    @Override
    public List<StaffDto> getAllStaff() throws SQLException, ClassNotFoundException {
        List<StaffEntity> entityList = userAuthenticationDao.getAll();
        List<StaffDto> dtoList = new ArrayList<>();

        for(StaffEntity entity : entityList){
            StaffDto staffDto = mapper.convertValue(entity, StaffDto.class);
            staffDto.setPassword("");
            dtoList.add(staffDto);
        }

        return dtoList;
    }
}
