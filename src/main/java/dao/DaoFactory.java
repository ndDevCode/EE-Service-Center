package dao;

import dao.custom.impl.UserAuthenticationDaoImpl;
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
        }

        return null;
    }
}
