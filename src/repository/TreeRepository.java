package repository;

import Config.DatabaseConnection;
import model.Forest;
import model.Tree;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TreeRepository {
    private final Connection connection;

    public TreeRepository(Connection connection) throws SQLException {
        this.connection = connection;
    }

    public Tree save(Tree tree) {
        String query = "INSERT INTO trees (id, type, forest_id) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, tree.getId());
            preparedStatement.setString(2, tree.getType());
            preparedStatement.setString(3, tree.getForest().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving tree: " + e.getMessage(), e);
        }
        return tree;
    }

    public Optional<Tree> findById(String id) {
        String query = "SELECT * FROM trees WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                Tree tree = new Tree();
                tree.setId(rs.getString("id"));
                tree.setType(rs.getString("type"));
                Optional<Forest> forestOptional = new ForestRepository(connection).findById(rs.getString("forest_id"));
                forestOptional.ifPresent(tree::setForest);
                return Optional.of(tree);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Tree> findByForestId(Forest forest) {
        List<Tree> trees = new ArrayList<>();
        String query = "SELECT * FROM trees WHERE forest_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, forest.getId());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Tree tree = new Tree();
                tree.setId(rs.getString("id"));
                tree.setType(rs.getString("type"));
                tree.setForest(forest);
                trees.add(tree);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trees;
    }
}
