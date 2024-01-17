package bo;

import bo.custom.impl.*;
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
            case CUSTOMER: return (T) new CustomerBoImpl();
            case ORDER: return (T) new OrderBoImpl();
            case INVENTORY_ITEM: return (T) new ItemInventoryBoImpl();
        }

        return null;
    }
}
