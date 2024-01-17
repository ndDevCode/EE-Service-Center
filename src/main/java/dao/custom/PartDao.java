package dao.custom;

import dao.CrudDao;
import entity.PartEntity;

public interface PartDao extends CrudDao<PartEntity> {
    boolean addToOrder(String partId, String orderId, String qtyRequired);
}
