package dao.custom.impl;

import dao.custom.UserAuthenticationDao;
import dao.util.HibernateUtil;
import dto.UserCredentialsDto;
import entity.StaffEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.List;

public class UserAuthenticationDaoImpl implements UserAuthenticationDao {

    @Override
    public StaffEntity verifyUser(UserCredentialsDto userCredentialsDto) {
        Session session = HibernateUtil.getSession();
        Query<StaffEntity> query = session.createQuery("FROM StaffEntity where email =:email",StaffEntity.class);
        query.setParameter("email",userCredentialsDto.getUserEmail());
        StaffEntity staff = query.getSingleResult();
        session.close();
        return staff;
    }

    @Override
    public boolean isInitialLogin() {
        Session session = HibernateUtil.getSession();
        Query<Long> query = session.createQuery("SELECT COUNT(*) FROM StaffEntity", Long.class);
        Long rowCount = query.uniqueResult();
        session.close();
        return rowCount<=0;
    }

    @Override
    public boolean save(StaffEntity entity) throws SQLException, ClassNotFoundException {
        Session session =HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        session.save(entity);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(StaffEntity entity) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String value) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public List getAll() throws SQLException, ClassNotFoundException {
        return null;
    }
}
