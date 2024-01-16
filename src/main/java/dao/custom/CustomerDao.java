package dao.custom;

import dao.CrudDao;
import entity.CustomerEntity;

public interface CustomerDao extends CrudDao<CustomerEntity> {
    boolean verifyCustomer(String email);
    CustomerEntity getCustomer(String email);
}
