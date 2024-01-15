package dao.custom;

import dao.CrudDao;
import dto.OrderDto;

import java.sql.SQLException;

public interface OrderDao extends CrudDao {
    boolean save(OrderDto dto) throws SQLException, ClassNotFoundException;
}
