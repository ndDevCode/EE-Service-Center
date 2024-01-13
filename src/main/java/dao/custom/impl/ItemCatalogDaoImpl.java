package dao.custom.impl;

import dao.custom.ItemCatalogDao;
import dao.util.HibernateUtil;
import dto.CatalogItemDto;
import entity.CatalogEntity;
import entity.StaffEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class ItemCatalogDaoImpl implements ItemCatalogDao {
    @Override
    public boolean save(CatalogItemDto dto) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        CatalogEntity entity = new CatalogEntity(
                null,
                dto.getName(),
                dto.getCategory(),
                dto.getImgLink()
        );
        entity.setStaff(session.find(StaffEntity.class,dto.getStaffId()));
        session.save(entity);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean save(Object entity) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(Object entity) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        CatalogItemDto dto = (CatalogItemDto)entity;
        CatalogEntity item = session.find(CatalogEntity.class,dto.getCatalogItemId());
        item.setName(dto.getName());
        item.setCategory(dto.getCategory());
        item.setImgLink(dto.getImgLink());
        session.save(item);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean delete(String value) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        session.delete(session.find(CatalogEntity.class,value));
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public List<Object> getAll() throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Query query = session.createQuery("From CatalogEntity ", CatalogEntity.class);
        List list = query.list();
        session.close();
        return list;
    }
}
