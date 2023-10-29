package db;

import model.bd.CompanyEntity;

import java.sql.*;
import java.util.List;

public class CompanyRepositoryJdbc implements CompanyRepository{
    private final static String SELECT = "select * from company where id = ?";
    private final static String INSERT = "insert into company(name) values(?)";
    private final static String INSERT_WITH_DESC = "insert into company(name, description) values(?, ?)";
    private final static String DELETE = "delete from company where id = ?";
    private Connection connection;

    public CompanyRepositoryJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<CompanyEntity> getAll() throws SQLException {
        return null;
    }

    @Override
    public List<CompanyEntity> getAll(boolean isActive) {
        return null;
    }

    @Override
    public CompanyEntity getLast() throws SQLException {
        return null;
    }

    @Override
    public CompanyEntity getById(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        generatedKeys.next();
        CompanyEntity entity = new CompanyEntity();
        entity.setName(generatedKeys.getString("name"));
        entity.setActive(generatedKeys.getBoolean("isActive"));
        entity.setId(generatedKeys.getInt("id"));
        entity.setDescription(generatedKeys.getString("description"));
        return entity;
    }

    @Override
    public int create(String name) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, name);
        preparedStatement.executeUpdate();
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        generatedKeys.next();
        return generatedKeys.getInt(1);
    }

    @Override
    public int create(String name, String description) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_WITH_DESC, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, description);
        preparedStatement.executeUpdate();
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        generatedKeys.next();
        return generatedKeys.getInt(1);
    }

    @Override
    public void deleteById(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }
}
