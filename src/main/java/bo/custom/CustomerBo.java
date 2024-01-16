package bo.custom;

import bo.SuperBo;
import dto.CustomerDto;
import dto.StaffDto;

import java.sql.SQLException;
import java.util.List;

public interface CustomerBo extends SuperBo {
    boolean verifyCustomer(String email);
    CustomerDto getCustomer(String email);
    boolean saveCustomer(CustomerDto customerDto) throws SQLException, ClassNotFoundException;
    boolean updateCustomer(CustomerDto customerDto) throws SQLException, ClassNotFoundException;
    CustomerDto getCustomerData(String email);
    List<CustomerDto> getAllCustomer() throws SQLException, ClassNotFoundException;
}
