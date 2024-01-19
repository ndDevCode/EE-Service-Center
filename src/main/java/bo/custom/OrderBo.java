package bo.custom;

import bo.SuperBo;
import dto.OrderDto;

import java.sql.SQLException;
import java.util.List;

public interface OrderBo extends SuperBo {
    OrderDto saveOrder(OrderDto orderDto) throws SQLException, ClassNotFoundException;
    OrderDto getOrderByID(String orderId) throws SQLException,ClassNotFoundException;
    List<OrderDto> getAllOrder() throws SQLException, ClassNotFoundException;
    boolean updateOrderStatus(OrderDto order) throws SQLException, ClassNotFoundException;
}
