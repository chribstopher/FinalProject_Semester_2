package puzzles.clock;

import puzzles.common.solver.Configuration;

import java.util.*;

/**
 * Clock Config class that holds the methods needed to be used with the common solver.
 * @author Christopher Rose
 */

public class ClockConfig implements Configuration {
    private int start;
    private int end;
    private int current;
    private int hours;

    /**
     * ClockConfig constructor
     * @param start start time
     * @param end goal time
     * @param current current time of the clock
     * @param hours total hours on the clock
     */
    public ClockConfig(int start, int end, int current, int hours){
        this.start = start;
        this.end = end;
        this.current = current;
        this.hours = hours;
    }

    /**
     * checks if the clock is at its goal
     * @return true or false
     */
    @Override
    public boolean isSolution() {
        return this.current == this.end;
    }

    /**
     * Creates the neighbors for the current clock config
     * @return list of neighbor configs
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        List<Configuration> neighbors = new ArrayList<>();
        if (this.current == 1){
            neighbors.add(new ClockConfig(start, end, hours, hours));
            neighbors.add(new ClockConfig(start, end, current+1, hours));
        } else if (this.current == hours){
            neighbors.add(new ClockConfig(start, end, current-1, hours));
            neighbors.add(new ClockConfig(start, end, 1, hours));
        } else {
            neighbors.add(new ClockConfig(start, end, current-1, hours));
            neighbors.add(new ClockConfig(start, end, current+1, hours));
        }
        return neighbors;
    }

    /**
     * checks if a clock config is equal to another
     * @param other clock config that is being compared
     * @return true or false
     */
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof ClockConfig){
            ClockConfig otherClock = (ClockConfig) other;
            result = this.current == otherClock.current;
        }
        return result;
    }

    /**
     * creates the hashcode for the clock config
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return this.current;
    }

    /**
     * makes the string version of the clock config
     * @return current time
     */
    @Override
    public String toString() {
        return "" + this.current;
    }
}
