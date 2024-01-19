package bo.custom.impl;

import bo.custom.OrderBo;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.DaoFactory;
import dao.custom.OrderDao;
import dao.util.DaoType;
import dto.ItemDto;
import dto.OrderDto;
import entity.ItemInventoryEntity;
import entity.OrderEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderBoImpl implements OrderBo {

    private final OrderDao orderDao = DaoFactory.getInstance().getDao(DaoType.ORDER);

    @Override
    public OrderDto saveOrder(OrderDto orderDto) throws SQLException, ClassNotFoundException {
        OrderEntity save = orderDao.save(orderDto);
        List<ItemDto> itemList = new ArrayList<>();

        for (ItemInventoryEntity entity: save.getItems()){
            itemList.add(new ItemDto(
                    entity.getItemId(),
                    entity.getName(),
                    entity.getCategory(),
                    entity.getRepairPrice()
            ));
        }

        return new OrderDto(
                save.getOrderId(),
                save.getDescription(),
                save.getOrderDate(),
                save.getStatus(),
                save.getCustomer().getCustomerId(),
                save.getStaff().getStaffId(),
                itemList
        );
    }

    @Override
    public OrderDto getOrderByID(String orderId) throws SQLException, ClassNotFoundException {
        return orderDao.getOrderById(orderId);
    }

    @Override
    public List<OrderDto> getAllOrder() throws SQLException, ClassNotFoundException {
        List<OrderEntity> entityList = orderDao.getAll();
        List<OrderDto> dtoList = new ArrayList<>();

        for (OrderEntity entity : entityList){
            dtoList.add(new OrderDto(
                    entity.getOrderId(),
                    entity.getDescription(),
                    entity.getOrderDate(),
                    entity.getStatus(),
                    entity.getCustomer().getCustomerId(),
                    entity.getStaff().getStaffId(),
                    entity.getItems()
            ));
        }

        return dtoList;
    }

    @Override
    public boolean updateOrderStatus(OrderDto order) throws SQLException, ClassNotFoundException {
        OrderEntity entity = new OrderEntity(
                order.getOrderId(),
                order.getDescription(),
                order.getOrderDate(),
                order.getStatus()
        );
        return orderDao.update(entity);
    }
}
