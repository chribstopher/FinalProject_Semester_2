package puzzles.common.solver;

import java.util.*;

/**
 * common solver class to be used in conjunction with the other puzzles
 * @author Christopher Rose
 */

public class Solver {
    private int configs;
    private int uniqueConfigs;

    /**
     * Solver constructor
     */
    public Solver(){
        this.configs = 0;
        this.uniqueConfigs = 0;
    }

    /**
     * Uses a BFS approach to find the shortest path to the goal of the puzzle currently being solved
     * @param start the starting configuration
     * @return the shortest path to the solution
     */
    public Collection<Configuration> getSolution (Configuration start){

        //creating the visitation queue
        List<Configuration> queue = new LinkedList<>();
        queue.add(start);
        configs += 1;
        uniqueConfigs += 1;

        //creating the predecessor map
        Map<Configuration, Configuration> predecessors = new HashMap<>();
        predecessors.put(start, start);

        while (!queue.isEmpty()){
            Configuration current = queue.remove(0);
            if (current.isSolution()){
                break;
            }
            //looping all the neighbors of the current config
            for (Configuration nbr: current.getNeighbors()){
                configs += 1;
                if (!predecessors.containsKey(nbr)){
                    uniqueConfigs += 1;
                    predecessors.put(nbr, current);
                    queue.add(nbr);
                }
            }
        }
        return constructPath(predecessors, start);
    }

    /**
     * constructs the path of the solution through using the predecessor map passed from the getSolution method
     * @param predecessors predecessor map of the BFS
     * @param start the starting configuration
     * @return the shortest path in string form
     */
    public List<Configuration> constructPath(Map<Configuration, Configuration> predecessors,
                                      Configuration start){
        //Creating the empty linkedlist for the path
        List<Configuration> path = new LinkedList<>();

        //checking if predecessor map contains the solution config
        boolean hasSolution = false;
        Configuration end = null;
        for (Configuration config: predecessors.keySet()){
            if (config.isSolution()){
                hasSolution = true;
                end = config;
            }
        }
        if (hasSolution){
            Configuration currConfig = end;
            while (currConfig != start) {
                path.add(0, currConfig);
                currConfig = predecessors.get(currConfig);
            }
            path.add(0, start);
        }
        return path;
    }

    /**
     * gets the number of configs generated
     * @return config number
     */
    public int getConfigs(){
        return configs;
    }

    /**
     * gets the number of unique configs generated
     * @return unique configs
     */
    public int getUniqueConfigs() {
        return uniqueConfigs;
    }

}
