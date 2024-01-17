package dao.custom.impl;

import controller.util.StatusType;
import dao.custom.ItemInventoryDao;
import dao.util.HibernateUtil;
import entity.ItemInventoryEntity;
import entity.OrderEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.List;

public class ItemInventoryDaoImpl implements ItemInventoryDao {
    @Override
    public boolean save(ItemInventoryEntity entity, String orderId) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        entity.setOrder(session.find(OrderEntity.class, orderId));
        session.save(entity);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean save(ItemInventoryEntity entity) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(ItemInventoryEntity entity) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        ItemInventoryEntity entity1 = session.find(ItemInventoryEntity.class, entity.getItemId());

        entity1.setRepairPrice(entity.getRepairPrice());
        entity1.setStatus(entity.getStatus());
        entity1.setZone(entity.getZone());

        session.update(entity1);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean delete(String itemId) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        ItemInventoryEntity item = session.find(ItemInventoryEntity.class, itemId);
        OrderEntity order = session.find(OrderEntity.class, item.getOrder().getOrderId());
        if (order.getItems().size() == 1) {
            order.setStatus(StatusType.CANCELLED.toString());
        }
        session.delete(item);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public List<ItemInventoryEntity> getAll() throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Query query = session.createQuery("From ItemInventoryEntity ");
        List<ItemInventoryEntity> itemList = query.list();
        session.close();
        return itemList;
    }
}
