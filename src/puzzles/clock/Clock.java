package puzzles.clock;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

public class Clock {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Clock start stop"));
        } else {
            System.out.println("Hours: " + args[0] + ", Start: " + args[1] + ", End: " + args[2]);
            Configuration startClock = new ClockConfig(Integer.parseInt(args[1]), Integer.parseInt(args[2]),
                    Integer.parseInt(args[1]), Integer.parseInt(args[0]));
            Solver puzzleSolver = new Solver();
            Configuration[] path = puzzleSolver.getSolution(startClock).toArray(new Configuration[0]);
            System.out.println("Total Configs: " + puzzleSolver.getConfigs());
            System.out.println("Unique Configs: " + puzzleSolver.getUniqueConfigs());
            if (path.length == 0){
                System.out.println("No solution");
            } else {
                for (int step = 0; step < path.length; step++){
                    System.out.println("Step " + step + ": " + path[step].toString());
                }
            }
        }
    }
}