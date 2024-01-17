package dao.custom.impl;

import dao.custom.PartDao;
import dao.util.HibernateUtil;
import entity.OrderEntity;
import entity.OrderPartEntity;
import entity.OrderPartKey;
import entity.PartEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.List;

public class PartDaoImpl implements PartDao {

    @Override
    public boolean save(PartEntity entity) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        session.save(entity);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(PartEntity entity) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        PartEntity entity1 = session.find(PartEntity.class, entity.getPartId());
        entity1.setName(entity.getName());
        entity1.setPrice(entity.getPrice());
        entity1.setQtyOnHand(entity.getQtyOnHand());
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
    public List<PartEntity> getAll() throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Query query = session.createQuery("FROM PartEntity ");
        return query.list();
    }

    @Override
    public boolean addToOrder(String partId, String orderId, String qtyRequired) {

        System.out.println(partId+" "+orderId+" "+qtyRequired);

        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        PartEntity part = session.find(PartEntity.class, partId);
        OrderPartKey orderPartKey = new OrderPartKey(orderId, partId);
        OrderPartEntity orderPartEntity1 = session.find(OrderPartEntity.class, orderPartKey);
        if(orderPartEntity1!=null){
            orderPartEntity1.setQtyRequired(orderPartEntity1.getQtyRequired()+Integer.parseInt(qtyRequired));
            orderPartEntity1.setPrice(orderPartEntity1.getQtyRequired()* part.getPrice());
            session.update(orderPartEntity1);
            transaction.commit();
            session.close();
            return true;
        }

        OrderPartEntity orderPartEntity = new OrderPartEntity(
                orderPartKey,
                session.find(OrderEntity.class,orderId),
                part,
                Integer.parseInt(qtyRequired),
                part.getPrice()*Integer.parseInt(qtyRequired)
        );
        session.save(orderPartEntity);
        transaction.commit();
        session.close();
        return true;
    }
}
