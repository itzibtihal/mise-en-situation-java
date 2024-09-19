package repository;

import Config.DatabaseConnection;
import model.Forest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ForestRepository {
    private final Connection connection;

    public ForestRepository(Connection connection) throws SQLException {
        this.connection = connection;
    }

    public Forest save(Forest forest) {
        String query = "INSERT INTO forests (id, name) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, forest.getId());
            preparedStatement.setString(2, forest.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving forest: " + e.getMessage(), e);
        }
        return forest;
    }

    public List<Forest> findAll() {
        String query = "SELECT * FROM forests";
        List<Forest> forests = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Forest forest = new Forest();
                forest.setId(rs.getString("id"));
                forest.setName(rs.getString("name"));
                forests.add(forest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return forests;
    }

    public Optional<Forest> findById(String id) {
        String query = "SELECT * FROM forests WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Forest forest = new Forest();
                forest.setId(rs.getString("id"));
                forest.setName(rs.getString("name"));
                return Optional.of(forest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
