package dao;

import dao.custom.impl.*;
import dao.util.DaoType;

public class DaoFactory {
    private static DaoFactory daoFactory;

    private DaoFactory(){}

    public static DaoFactory getInstance(){
        return daoFactory!=null? daoFactory : (daoFactory = new DaoFactory());
    }

    public <T extends SuperDao> T getDao(DaoType type){
        switch(type){
            case USER_AUTHENTICATION: return (T) new UserAuthenticationDaoImpl();
            case CATALOG_ITEM: return (T) new ItemCatalogDaoImpl();
            case CUSTOMER: return (T) new CustomerDaoImpl();
            case ORDER: return (T) new OrderDaoImpl();
            case INVENTORY_ITEM: return (T) new ItemInventoryDaoImpl();
            case PART: return (T) new PartDaoImpl();
        }
        return null;
    }
}
