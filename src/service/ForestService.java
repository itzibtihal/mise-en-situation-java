package service;

import model.Forest;
import repository.ForestRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ForestService {
    private final ForestRepository forestRepository;
    private final TreeService treeService;

    public ForestService(Connection connection) throws SQLException {
        this.forestRepository = new ForestRepository(connection);
        this.treeService = new TreeService(connection);
    }

    public boolean saveAll(List<Forest> forests) {
        boolean allSaved = true;
        for (Forest forest : forests) {
            try {
                forestRepository.save(forest);
            } catch (RuntimeException e) {
                System.err.println("Error saving forest: " + e.getMessage());
                allSaved = false;
            }
        }
        return allSaved;
    }

    public List<Forest> findAll() {
        List<Forest> forests = forestRepository.findAll();
        forests.forEach(forest -> forest.setTrees(treeService.findTreesByForestId(forest)));
        return forests;
    }
}
