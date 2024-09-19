package service;

import model.Forest;
import model.Tree;
import repository.TreeRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TreeService {

    private final TreeRepository treeRepository;

    public TreeService(Connection connection) throws SQLException {
        this.treeRepository = new TreeRepository(connection);
    }

    public Tree save(Tree tree) {
        return treeRepository.save(tree);
    }

    public boolean saveAll(List<Tree> trees) {
        boolean allSaved = true;
        for (Tree tree : trees) {
            try {
                treeRepository.save(tree);
            } catch (RuntimeException e) {
                System.err.println("Error saving tree: " + e.getMessage());
                allSaved = false;
            }
        }
        return allSaved;
    }

    public List<Tree> findTreesByForestId(Forest forest) {
        return treeRepository.findByForestId(forest);
    }
}
