package dao.custom.impl;

import dao.custom.CustomerDao;
import dao.util.HibernateUtil;
import entity.CustomerEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import java.sql.SQLException;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {
    @Override
    public boolean save(CustomerEntity entity) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        session.save(entity);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(CustomerEntity entity) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        CustomerEntity customer = session.find(CustomerEntity.class, entity.getCustomerId());
        customer.setFirstName(entity.getFirstName());
        customer.setLastName(entity.getLastName());
        customer.setEmail(entity.getEmail());
        customer.setContactNo(entity.getContactNo());
        session.update(customer);
        return false;
    }

    @Override
    public boolean delete(String value) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public List<CustomerEntity> getAll() throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Query query = session.createQuery("From CustomerEntity");
        List<CustomerEntity> list = query.list();
        session.close();
        return list;
    }

    @Override
    public boolean verifyCustomer(String email) {
        Session session = HibernateUtil.getSession();
        Query query = session.createQuery("FROM CustomerEntity c WHERE email =:email");
        query.setParameter("email", email);
        try {
            int size = query.getResultList().size();
            session.close();
            return size==1;
        } catch (NoResultException nre) {
            session.close();
            return false;
        }
    }

    @Override
    public CustomerEntity getCustomer(String email) {
        Session session = HibernateUtil.getSession();
        Query query = session.createQuery("FROM CustomerEntity c WHERE email =:email");
        query.setParameter("email", email);
        try {
            CustomerEntity entity  = (CustomerEntity) query.getSingleResult();
            session.close();
            return entity;
        } catch (NoResultException nre) {
            session.close();
            return null;
        }
    }
}
