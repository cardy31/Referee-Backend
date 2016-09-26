package org.cardy.spring.dao;

import org.cardy.spring.model.Contact;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * An implementation of the ContactDAO interface.
 * @author Rob Cardy
 */
public class ContactDAOImpl implements ContactDAO{

    private JdbcTemplate jdbcTemplate;

    public ContactDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void saveOrUpdate(Contact contact) {
        if (contact.getId() > 0) {
            // update
            String sql = "UPDATE contact SET name=?. email=?, address=?. "
                        + "telephone=? WHERE contact_id=?";
            jdbcTemplate.update(sql, contact.getName(), contact.getEmail(),
                    contact.getAddress(), contact.getTelephone(), contact.getId());
        }
        else {
            // insert
            String sql = "INSERT INTO contact (name, email, address, telephone)"
                        + "VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql, contact.getName(), contact.getEmail(),
                    contact.getAddress(), contact.getTelephone());
        }
    }

    public void delete(int contactId) {
        String sql = "DELETE FROM contact WHERE contact_id=?";
        jdbcTemplate.update(sql, contactId);
    }

    public List<Contact> list() {
        String sql = "SELECT * FROM contact";
        List<Contact> listContact = jdbcTemplate.query(sql, new RowMapper<Contact>() {

            public Contact mapRow(ResultSet rs, int rowNum) throws SQLException {
                return fillContract(rs);
            }

        });

        return listContact;
    }

    public Contact get(int contactId) {
        String sql = "SELECT * FROM contact WHERE contact_id=" + contactId;
        return jdbcTemplate.query(sql, new ResultSetExtractor<Contact>() {
            public Contact extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    return fillContract(rs);
                }

                return null;
            }
        });
    }

    private Contact fillContract(ResultSet rs) throws SQLException, DataAccessException{
        Contact contact = new Contact();
        contact.setId(rs.getInt("contact_id"));
        contact.setName(rs.getString("name"));
        contact.setEmail(rs.getString("email"));
        contact.setAddress(rs.getString("address"));
        contact.setTelephone(rs.getString("telephone"));
        return contact;
    }
}
