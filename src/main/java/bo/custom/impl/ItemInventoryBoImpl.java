package bo.custom.impl;

import bo.custom.ItemInventoryBo;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.DaoFactory;
import dao.custom.ItemInventoryDao;
import dao.util.DaoType;
import dto.ItemDto;
import entity.ItemInventoryEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemInventoryBoImpl implements ItemInventoryBo {

    ItemInventoryDao inventoryDao = DaoFactory.getInstance().getDao(DaoType.INVENTORY_ITEM);
    @Override
    public boolean saveItem(ItemDto item) throws SQLException, ClassNotFoundException {
        ItemInventoryEntity entity = new ItemInventoryEntity(
                item.getName(),
                item.getCategory(),
                item.getStatus(),
                item.getZone(),
                item.getRepairPrice()
        );
        return inventoryDao.save(entity,item.getOrderId());
    }

    @Override
    public boolean updateItem(ItemDto item) throws SQLException, ClassNotFoundException {
        return inventoryDao.update(new ItemInventoryEntity(
                item.getItemId(),
                item.getName(),
                item.getCategory(),
                item.getStatus(),
                item.getZone(),
                item.getRepairPrice()));
    }

    @Override
    public boolean deleteItem(String itemId) throws SQLException, ClassNotFoundException {
        return inventoryDao.delete(itemId);
    }

    @Override
    public List<ItemDto> getAllItem() throws SQLException, ClassNotFoundException {
        List<ItemInventoryEntity> inventoryEntities = inventoryDao.getAll();
        List<ItemDto> dtoList = new ArrayList<>();

        for (ItemInventoryEntity entity:inventoryEntities){
            dtoList.add(new ItemDto(
                    entity.getItemId(),
                    entity.getName(),
                    entity.getCategory(),
                    entity.getStatus(),
                    entity.getZone(),
                    entity.getRepairPrice(),
                    entity.getOrder().getOrderId()
            ));
        }

        return dtoList;
    }
}
