package dao.util.idgenerators;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StaffIdGenerator implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object o) throws HibernateException {
        Connection connection = session.connection();

        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT MAX(CAST(SUBSTRING(staff_id, 3) AS SIGNED)) FROM staff");
            rs.next();
            int nextId = rs.getInt(1);

            // Format the ID as ST001
            return "ST" + String.format("%03d", ++nextId);
        } catch (SQLException e) {
            throw new HibernateException("Error generating custom ID", e);
        }
    }
}
