package bo.custom.impl;

import bo.custom.PartBo;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.DaoFactory;
import dao.custom.PartDao;
import dao.util.DaoType;
import dto.PartDto;
import entity.PartEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PartBoImpl implements PartBo {

    private final PartDao partDao = DaoFactory.getInstance().getDao(DaoType.PART);
    ObjectMapper mapper = new ObjectMapper();
    @Override
    public boolean savePart(PartDto partDto) throws SQLException, ClassNotFoundException {
        return partDao.save(mapper.convertValue(partDto, PartEntity.class));
    }

    @Override
    public boolean updatePart(PartDto partDto) throws SQLException, ClassNotFoundException {
        return partDao.update(mapper.convertValue(partDto, PartEntity.class));
    }

    @Override
    public List<PartDto> getAllPart() throws SQLException, ClassNotFoundException {
        List<PartEntity> all = partDao.getAll();
        List<PartDto> dtoList = new ArrayList<>();
        for (PartEntity entity:all){
            dtoList.add(mapper.convertValue(entity, PartDto.class));
        }
        return dtoList;
    }

    @Override
    public boolean addToOrder(String partId, String orderId, String qtyRequired) {
        return partDao.addToOrder(partId,orderId,qtyRequired);
    }
}
