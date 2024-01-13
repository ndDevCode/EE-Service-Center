package bo.custom.impl;

import bo.custom.ItemCatalogBo;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.DaoFactory;
import dao.custom.ItemCatalogDao;
import dao.util.DaoType;
import dto.CatalogItemDto;
import entity.CatalogEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemCatalogBoImpl implements ItemCatalogBo {
    private final ItemCatalogDao itemCatalogDao = DaoFactory.getInstance().getDao(DaoType.CATALOG_ITEM);
    ObjectMapper mapper = new ObjectMapper();
    @Override
    public boolean saveItem(CatalogItemDto catalogItemDto) throws SQLException, ClassNotFoundException {
        return itemCatalogDao.save(catalogItemDto);
    }

    @Override
    public boolean updateItem(CatalogItemDto catalogItemDto) throws SQLException, ClassNotFoundException {
        System.out.println(catalogItemDto);
        return itemCatalogDao.update(catalogItemDto);
    }

    @Override
    public boolean deleteItem(String catalogItemId) throws SQLException, ClassNotFoundException {
        return itemCatalogDao.delete(catalogItemId);
    }

    @Override
    public List<CatalogItemDto> getAllItem() throws SQLException, ClassNotFoundException {
        List all = itemCatalogDao.getAll();
        List<CatalogItemDto> dtoList = new ArrayList<>();
        for(Object entity:all){
            CatalogEntity entity1 = (CatalogEntity) entity;
            CatalogItemDto catalogItemDto = mapper.convertValue(entity1, CatalogItemDto.class);
            catalogItemDto.setStaffId(entity1.getStaff().getStaffId());
            dtoList.add(catalogItemDto);
        }
        return dtoList;
    }
}
