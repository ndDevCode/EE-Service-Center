package dao.custom.impl;

import dao.custom.OrderDao;
import dao.util.HibernateUtil;
import dto.ItemDto;
import dto.OrderDto;
import entity.CustomerEntity;
import entity.ItemInventoryEntity;
import entity.OrderEntity;
import entity.StaffEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.List;

public class OrderDaoImpl implements OrderDao {

    @Override
    public OrderEntity save(OrderDto dto) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        OrderEntity order = new OrderEntity(
                null,
                dto.getDescription(),
                dto.getOrderDate(),
                dto.getStatus()
        );

        order.setStaff(session.find(StaffEntity.class,dto.getStaff()));
        order.setCustomer(session.find(CustomerEntity.class,dto.getCustomer()));
        session.save(order);
        transaction.commit();

        List<ItemDto> itemList = dto.getItems();
        for(ItemDto item:itemList){
            transaction.begin();
            ItemInventoryEntity itemEntity = new ItemInventoryEntity(
                    item.getName(),
                    item.getCategory(),
                    item.getStatus(),
                    item.getZone(),
                    item.getRepairPrice()
            );
            itemEntity.setOrder(session.find(OrderEntity.class,order.getOrderId()));
            session.save(itemEntity);
            transaction.commit();
        }

        OrderEntity entity = session.find(OrderEntity.class, order.getOrderId());
        Query query = session.createQuery("From ItemInventoryEntity i WHERE i.order.id =:orderId");
        query.setParameter("orderId",entity.getOrderId());
        entity.setItems(query.list());
        session.close();
        return entity;
    }

    @Override
    public OrderDto getOrderById(String orderId) {
        Session session = HibernateUtil.getSession();
        Query query = session.createQuery("From OrderEntity o where orderId=:orderId");
        query.setParameter("orderId",orderId);
        OrderEntity result = (OrderEntity) query.getSingleResult();
        return new OrderDto(
                result.getOrderId(),
                result.getDescription(),
                result.getOrderDate(),
                result.getStatus(),
                result.getCustomer().getCustomerId(),
                result.getStaff().getStaffId(),
                result.getItems()
        );
    }

    @Override
    public boolean save(Object entity) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(Object entity) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        OrderEntity entity1 =(OrderEntity) entity;
        entity1 = session.find(OrderEntity.class, entity1.getOrderId());
        entity1.setStatus(((OrderEntity) entity).getStatus());
        session.update(entity1);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean delete(String value) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public List getAll() throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Query query = session.createQuery("From OrderEntity");
        return query.list();
    }
}
