package bo.custom;

import bo.SuperBo;
import dto.CatalogItemDto;

import java.sql.SQLException;
import java.util.List;

public interface ItemCatalogBo extends SuperBo {
    boolean saveItem(CatalogItemDto catalogItemDto) throws SQLException, ClassNotFoundException;
    boolean updateItem(CatalogItemDto catalogItemDto) throws SQLException, ClassNotFoundException;
    boolean deleteItem(String catalogItemId) throws SQLException, ClassNotFoundException;
    List<CatalogItemDto> getAllItem() throws SQLException, ClassNotFoundException;
}
