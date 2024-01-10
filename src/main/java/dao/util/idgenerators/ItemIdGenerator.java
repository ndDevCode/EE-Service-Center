package dao.util.idgenerators;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ItemIdGenerator implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object o) throws HibernateException {
        Connection connection = session.connection();

        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT MAX(CAST(SUBSTRING(item_id, 4) AS SIGNED)) FROM item_inventory");
            rs.next();
            int nextId = rs.getInt(1);

            // Format the ID as ITM0000001
            return "ITM" + String.format("%07d", ++nextId);
        } catch (SQLException e) {
            throw new HibernateException("Error generating custom ID", e);
        }
    }
}
