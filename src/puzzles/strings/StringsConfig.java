package puzzles.strings;

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * String Configuration Class
 * @author Christopher Rose
 */

public class StringsConfig implements Configuration {
    private String start;
    private String finish;

    /**
     * constructor for StringsConfig
     *
     * @param start  starting string
     * @param finish goal
     */
    public StringsConfig(String start, String finish) {
        this.start = start;
        this.finish = finish;
    }

    /**
     * checks to see if the current string matches the goal
     *
     * @return true or false
     */
    @Override
    public boolean isSolution() {
        return start.equals(finish);
    }

    /**
     * creates all the neighbors for the current configuration
     *
     * @return list of all the neighbors
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        List<Configuration> neighbors = new ArrayList<>();
        for (int c = 0; c < start.length(); c++) {
            StringBuilder nbr1 = new StringBuilder(start);
            StringBuilder nbr2 = new StringBuilder(start);
            if (start.charAt(c) == 'A') {
                char ch = start.charAt(c);
                nbr1.setCharAt(c, 'Z');
                nbr2.setCharAt(c, (char) (ch + 1));
            } else if (start.charAt(c) == 'Z') {
                char ch = start.charAt(c);
                nbr1.setCharAt(c, (char) (ch - 1));
                nbr2.setCharAt(c, 'A');
            } else {
                char ch = start.charAt(c);
                nbr1.setCharAt(c, (char) (ch - 1));
                nbr2.setCharAt(c, (char) (ch + 1));
            }
            neighbors.add(new StringsConfig(nbr1.toString(), finish));
            neighbors.add(new StringsConfig(nbr2.toString(), finish));
        }
        return neighbors;
    }

    /**
     * StringConfig's equals method
     *
     * @param other String config being compared
     * @return whether they have the same string
     */
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof StringsConfig) {
            StringsConfig otherString = (StringsConfig) other;
            result = this.start.equals(otherString.start);
        }
        return result;
    }

    /**
     * makes the hashcode for the StringConfigs
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return start.hashCode();
    }

    /**
     * makes the string form of the StringConfig
     *
     * @return start string
     */
    @Override
    public String toString() {
        return start;
    }
}
