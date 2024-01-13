package bo;

import bo.custom.impl.ItemCatalogBoImpl;
import bo.custom.impl.UserAuthenticationBoImpl;
import bo.util.BoType;

public class BoFactory {
    private static BoFactory boFactory;

    private BoFactory(){}

    public static BoFactory getInstance(){
        return boFactory!=null ? boFactory : (boFactory = new BoFactory());
    }

    public <T extends SuperBo>T getBo(BoType boType){
        switch (boType){
            case USER_AUTHENTICATION: return (T) new UserAuthenticationBoImpl();
            case CATALOG_ITEM: return (T) new ItemCatalogBoImpl();
        }

        return null;
    }
}
