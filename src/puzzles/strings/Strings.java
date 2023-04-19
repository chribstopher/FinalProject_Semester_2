package puzzles.strings;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

/**
 * Main class for the Strings puzzle. Runs the puzzle solver using a starting StringConfig
 * @author Christopher Rose
 */

public class Strings {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Strings start finish"));
        } else {
            System.out.println("Start: " + args[0] + ", End: " + args[1]);
            Configuration startString = new StringsConfig(args[0], args[1]);
            Solver puzzleSolver = new Solver();
            String[] path = puzzleSolver.getSolution(startString).toArray(new String[0]);
            System.out.println("Total Configs: " + puzzleSolver.getConfigs());
            System.out.println("Unique Configs: " + puzzleSolver.getUniqueConfigs());
            if (path.length == 0){
                System.out.println("No solution");
            } else {
                for (int step = 0; step < path.length; step++){
                    System.out.println("Step " + step + ": " + path[step]);
                }
            }
        }
    }
}

