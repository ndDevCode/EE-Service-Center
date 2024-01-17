package bo.custom;

import bo.SuperBo;
import dto.PartDto;

import java.sql.SQLException;
import java.util.List;

public interface PartBo extends SuperBo {
    boolean savePart(PartDto partDto) throws SQLException, ClassNotFoundException;
    boolean updatePart(PartDto partDto) throws SQLException, ClassNotFoundException;
    List<PartDto> getAllPart() throws SQLException, ClassNotFoundException;
    boolean addToOrder(String partId, String orderId, String qtyRequired);
}
