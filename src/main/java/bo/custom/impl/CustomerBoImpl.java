package bo.custom.impl;

import bo.custom.CustomerBo;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.DaoFactory;
import dao.custom.CustomerDao;
import dao.util.DaoType;
import dto.CustomerDto;
import entity.CustomerEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerBoImpl implements CustomerBo {

    private final CustomerDao customerDao = DaoFactory.getInstance().getDao(DaoType.CUSTOMER);
    ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean verifyCustomer(String email) {
        return customerDao.verifyCustomer(email);
    }

    @Override
    public CustomerDto getCustomer(String email) {
        return mapper.convertValue(customerDao.getCustomer(email),CustomerDto.class);
    }

    @Override
    public boolean saveCustomer(CustomerDto customerDto) throws SQLException, ClassNotFoundException {
        return customerDao.save(mapper.convertValue(customerDto, CustomerEntity.class));
    }

    @Override
    public boolean updateCustomer(CustomerDto customerDto) throws SQLException, ClassNotFoundException {
        return customerDao.update(mapper.convertValue(customerDto, CustomerEntity.class));
    }

    @Override
    public CustomerDto getCustomerData(String email) {
        return null;
    }

    @Override
    public List<CustomerDto> getAllCustomer() throws SQLException, ClassNotFoundException {
        List<CustomerEntity> entityList = customerDao.getAll();
        List<CustomerDto> dtoList = new ArrayList<>();
        for(CustomerEntity entity:entityList){
            dtoList.add(mapper.convertValue(entity, CustomerDto.class));
        }
        return dtoList;
    }
}
