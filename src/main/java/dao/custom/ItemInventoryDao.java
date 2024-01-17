package dao.custom;

import dao.CrudDao;
import entity.ItemInventoryEntity;

import java.sql.SQLException;

public interface ItemInventoryDao extends CrudDao<ItemInventoryEntity> {
    public boolean save(ItemInventoryEntity entity, String orderId) throws SQLException, ClassNotFoundException;
}