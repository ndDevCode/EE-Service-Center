package dao.custom;

import dao.CrudDao;
import dto.CatalogItemDto;
import entity.CatalogEntity;

import java.sql.SQLException;

public interface ItemCatalogDao extends CrudDao {
    boolean save(CatalogItemDto dto) throws SQLException, ClassNotFoundException;
}
