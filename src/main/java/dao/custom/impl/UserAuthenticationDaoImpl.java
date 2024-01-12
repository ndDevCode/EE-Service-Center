package dao.custom.impl;

import dao.custom.UserAuthenticationDao;
import dao.util.HibernateUtil;
import dto.UserCredentialsDto;
import entity.StaffEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import java.sql.SQLException;
import java.util.List;

public class UserAuthenticationDaoImpl implements UserAuthenticationDao {

    @Override
    public StaffEntity verifyUser(UserCredentialsDto userCredentialsDto) {
        Session session = HibernateUtil.getSession();
        Query<StaffEntity> query = session.createQuery("FROM StaffEntity where email =:email", StaffEntity.class);
        query.setParameter("email", userCredentialsDto.getUserEmail());
        StaffEntity staff = query.getSingleResult();
        session.close();
        return staff;
    }

    @Override
    public StaffEntity verifyUser(String email) {
        Session session = HibernateUtil.getSession();
        Query query = session.createQuery("FROM StaffEntity s WHERE email =:email");
        query.setParameter("email", email);
        try {
            StaffEntity singleResult = (StaffEntity) query.getSingleResult();
            session.close();
            return singleResult;
        } catch (NoResultException nre) {
            System.out.println("No Result");
            return null;
        }
    }

    @Override
    public boolean isInitialLogin() {
        Session session = HibernateUtil.getSession();
        Query<Long> query = session.createQuery("SELECT COUNT(*) FROM StaffEntity", Long.class);
        Long rowCount = query.uniqueResult();
        session.close();
        return rowCount <= 0;
    }

    @Override
    public boolean updatePassword(String email, String newPassword) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("UPDATE StaffEntity SET password=:newPassword WHERE email =:email");
        query.setParameter("email", email);
        query.setParameter("newPassword",newPassword);
        int rsl = query.executeUpdate();
        transaction.commit();
        session.close();
        return rsl>0;
    }

    @Override
    public boolean save(StaffEntity entity) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        session.save(entity);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(StaffEntity entity) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        StaffEntity staffEntity = session.find(StaffEntity.class, entity.getStaffId());

        if (entity.getPassword() == null || entity.getPassword().isEmpty()) {
            staffEntity.setFirstName(entity.getFirstName());
            staffEntity.setLastName(entity.getLastName());
            staffEntity.setEmail(entity.getEmail());
            staffEntity.setContactNo(entity.getContactNo());
            staffEntity.setRole(entity.getRole());
        } else {
            staffEntity.setFirstName(entity.getFirstName());
            staffEntity.setLastName(entity.getLastName());
            staffEntity.setEmail(entity.getEmail());
            staffEntity.setPassword(entity.getPassword());
            staffEntity.setContactNo(entity.getContactNo());
            staffEntity.setRole(entity.getRole());
        }

        session.save(staffEntity);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean delete(String value) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        session.delete(session.find(StaffEntity.class, value));
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public List<StaffEntity> getAll() throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Query query = session.createQuery("FROM StaffEntity");
        List<StaffEntity> list = query.list();
        session.close();
        return list;
    }
}
