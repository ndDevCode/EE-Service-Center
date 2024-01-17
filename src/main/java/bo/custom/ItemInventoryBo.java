package bo.custom;

import bo.SuperBo;
import dto.ItemDto;

import java.sql.SQLException;
import java.util.List;

public interface ItemInventoryBo extends SuperBo {
    boolean saveItem(ItemDto item) throws SQLException, ClassNotFoundException;
    boolean updateItem(ItemDto item) throws SQLException, ClassNotFoundException;
    boolean deleteItem(String itemId) throws SQLException, ClassNotFoundException;
    List<ItemDto> getAllItem() throws SQLException, ClassNotFoundException;
}
