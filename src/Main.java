import Config.DatabaseConnection;
import model.Forest;
import model.Tree;
import service.ForestService;
import service.TreeService;
import util.FactoryUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws SQLException {

        List<String> forestNames = new ArrayList<>(
                Arrays.asList(
                        "Amazon Rainforest",
                        "Black Forest",
                        "Daintree Rainforest",
                        "Tongass National",
                        "Monteverde Cloud"));

        List<String> TreeTypes = new ArrayList<>(
                Arrays.asList(
                        "Oak",
                        "Redwood",
                        "Cypress",
                        "Pine",
                        "Elm",
                        "Sequoia",
                        "Fir",
                        "Banyan",
                        "Cedar"));

        List<Forest> forests = FactoryUtil.GenerateObjects(Forest.class,2);
        forests.forEach(forest -> forest.setName(forestNames.get(ThreadLocalRandom.current().nextInt(forestNames.size()))));
        forests.forEach(System.out::println);

        System.out.println("------------------------------------------");

        List<Tree> trees = FactoryUtil.GenerateObjects(Tree.class,20);

        trees.forEach(tree -> tree.setType(TreeTypes.get(ThreadLocalRandom.current().nextInt(TreeTypes.size()))));
        trees.forEach(System.out::println);



        // Call services here !

        Connection connection = DatabaseConnection.getInstance().getConnection();

        ForestService forestService = new ForestService(connection);
        TreeService treeService = new TreeService(connection);

        // save forests and trees to DB
        /*
        boolean allForestsSaved = forestService.saveAll(forests);
        if (allForestsSaved) {
            System.out.println("All forests saved successfully.");
        } else {
            System.out.println("Some forests could not be saved.");
        }
        for (Forest forest : forests) {
            for (int i = 0; i < 20; i++) {
                Tree tree = new Tree();
                tree.setType(TreeTypes.get(ThreadLocalRandom.current().nextInt(TreeTypes.size())));
                tree.setForest(forest);
                treeService.save(tree);
            }
        }

         */


        // get List of forests From DB each with associated trees
        List<Forest> forestsWithTrees = forestService.findAll();
        System.out.println("------------------------------------------");
        for (Forest forest : forestsWithTrees) {
            System.out.println("Forest Name: " + forest.getName());
            System.out.println("Trees in this forest:");
            for (Tree tree : forest.getTrees()) {
                System.out.println("- " + tree.getType());
            }
            System.out.println("------------------------------------------");
        }


        // Filter forests by number of trees
        Integer treeCount = 3;
        List<Forest> filtredForests = forestsWithTrees
                .stream()
                .filter(forest -> forest.getTrees().size() > treeCount)
                .collect(Collectors.toList());

        System.out.println("Forests with more than " + treeCount + " trees :");
        filtredForests.forEach(System.out::println);


        // For each forest extract the name of each tree and how many samples exists in the forest
        System.out.println("------------------------------------------");

        for (Forest forest : forestsWithTrees) {
            System.out.println("Forest Name: " + forest.getName());

            Map<String, Integer> treeCountMap = new HashMap<>();

            for (Tree tree : forest.getTrees()) {
                String treeType = tree.getType();
                treeCountMap.put(treeType, treeCountMap.getOrDefault(treeType, 0) + 1);
            }

            System.out.println("Tree Samples in this forest:");
            for (String treeType : treeCountMap.keySet()) {
                System.out.println("- " + treeType + ": " + treeCountMap.get(treeType) + " samples");
            }
            System.out.println("------------------------------------------");
        }

        // Print the most dominant Tree (All forests combined)
        Map<String, Integer> overallTreeCountMap = new HashMap<>();
        for (Forest forest : forestsWithTrees) {
            for (Tree tree : forest.getTrees()) {
                overallTreeCountMap.put(tree.getType(), overallTreeCountMap
                        .getOrDefault(tree.getType(), 0) + 1);
            }
        }
        String dominantTree = overallTreeCountMap.entrySet().stream()
                .max(Map.Entry.comparingByValue()).map(Map.Entry::getKey)
                .orElse(null);
        int maxCount = overallTreeCountMap.getOrDefault(dominantTree, 0);

        if (dominantTree != null) {
            System.out.println("Most Dominant Tree Type: " + dominantTree + " with " + maxCount + " samples.");
        } else {
            System.out.println("No trees found.");
        }





    }
}