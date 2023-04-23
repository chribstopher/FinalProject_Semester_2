package puzzles.hoppers.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersConfig;

import java.io.IOException;

public class Hoppers {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Hoppers filename");
        } else{
            System.out.println("File: " + args[0]);
            HoppersConfig startConfig = new HoppersConfig(args[0]);
            System.out.println(startConfig.toString());
            Solver puzzleSolver = new Solver();
            Configuration[] path = puzzleSolver.getSolution(startConfig).toArray(new Configuration[0]);
            System.out.println("Total Configs: " + puzzleSolver.getConfigs());
            System.out.println("Unique Configs: " + puzzleSolver.getUniqueConfigs());
            if (path.length == 0){
                System.out.println("No solution");
            } else {
                for (int step = 0; step < path.length; step++){
                    System.out.println("Step " + step + ": ");
                    System.out.println(path[step].toString());
                }
            }
        }
    }
}
