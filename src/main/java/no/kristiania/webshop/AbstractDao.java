package no.kristiania.webshop;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<S> {
    protected DataSource dataSource;

    public AbstractDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public long insert(S product, String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                insertObject(product, stmt);
                stmt.executeUpdate();


                ResultSet generatedKeys = stmt.getGeneratedKeys();
                generatedKeys.next();
                return generatedKeys.getLong(1);
            }

        }
    }

    protected abstract void insertObject(S product, PreparedStatement stmt) throws SQLException;

    public List<S> listAll(String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    List<S> products = new ArrayList<>();
                    while (rs.next()) {
                        products.add(readObject(rs));
                    }
                    return products;
                }
            }
        }
    }

    protected abstract S readObject(ResultSet rs) throws SQLException;

    public abstract void insert(S order) throws SQLException;
}
