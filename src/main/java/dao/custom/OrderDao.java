package dao.custom;

import dao.CrudDao;
import dto.OrderDto;
import entity.OrderEntity;

import java.sql.SQLException;

public interface OrderDao extends CrudDao {
    OrderEntity save(OrderDto dto) throws SQLException, ClassNotFoundException;
}
