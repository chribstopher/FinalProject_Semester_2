package puzzles.hoppers.model;

import puzzles.common.Coordinates;
import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersConfig;

import java.io.IOException;
import java.util.*;

public class HoppersModel {
    /** the collection of observers of this model */
    private final List<Observer<HoppersModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private HoppersConfig currentConfig;
    private String filename;
    private Solver puzzleSolver;
    private int initialR;
    private int initialC;
    private int secondR;
    private int secondC;

    /*** enum of the different states the game can be in at a time*/
    public enum GameState{
        ONGOING, WON, LOST, ILLEGAL_MOVE, LOAD, INVALID_FILE, RESET
    }

    /*** The current state of the game*/
    private GameState gameState;
    private static final EnumMap<GameState, String> STATE_MSGS =
            new EnumMap<>(Map.of(
                    GameState.WON, "You won!",
                    GameState.LOST, "You lost.",
                    GameState.ONGOING, "Make your next move!",
                    GameState.ILLEGAL_MOVE, "Illegal Move.",
                    GameState.LOAD, "File loaded successfully.",
                    GameState.INVALID_FILE, "File could not be read.",
                    GameState.RESET, "Puzzle has been reset."
            ));

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<HoppersModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String msg) {
        for (var observer : observers) {
            observer.update(this, msg);
        }
    }

    /**
     * Hopper's model constructor
     * @param filename which file to make the config on
     * @throws IOException
     */
    public HoppersModel(String filename) throws IOException {
        currentConfig = new HoppersConfig(filename);
        puzzleSolver = new Solver();
        this.initialR = -1;
        this.initialC = -1;
        this.secondR = -1;
        this.secondC = -1;
        this.filename = filename;
    }

    /**
     * Hint method. Shows the next step in the puzzle if there is a possible solution. If not,
     * no change is made and the user is notified.
     */
    public void hint(){
        Configuration[] path = puzzleSolver.getSolution(currentConfig).toArray(new Configuration[0]);
        if (path.length == 0){
            this.gameState = GameState.LOST;
        } else{
            Configuration nextStep = path[1];
            currentConfig = (HoppersConfig) nextStep;
            if (currentConfig.isSolution()){
                this.gameState = GameState.WON;
            } else{
                this.gameState = GameState.ONGOING;
            }
        }
        alertObservers(HoppersModel.STATE_MSGS.get(this.gameState));
    }

    /**
     * Load method. Takes in the user given filename and path and sets the current config to be the newly
     * generated config.
     * @param filename path and name for new hoppers game
     */
    public void load(String filename){
        try{
            currentConfig = new HoppersConfig(filename);
            this.filename = filename;
            this.gameState = GameState.LOAD;
            alertObservers(HoppersModel.STATE_MSGS.get(this.gameState));
        } catch (IOException e) {
            this.gameState = GameState.INVALID_FILE;
            alertObservers(HoppersModel.STATE_MSGS.get(this.gameState));
        }
    }

    /**
     * Reset method. Previously loaded file is reloaded onto the current config causing the puzzle to be
     * reverted to its initial state.
     * @throws IOException
     */
    public void reset() throws IOException {
        currentConfig = new HoppersConfig(this.filename);
        this.initialR = -1;
        this.initialC = -1;
        this.gameState = GameState.RESET;
        alertObservers(HoppersModel.STATE_MSGS.get(this.gameState));
    }

    /**
     * second part of the select method. Finds what possible moves there are for the initial
     * set of coordinates. If where the user wishes to miss the frog is part of these moves, the piece
     * is moved and the configuration is updated.
     * @param r original row
     * @param c original column
     * @param r2 row to be moved to
     * @param c2 column to be moved to
     */
    public void move(int r, int c, int r2, int c2){
        List<Coordinates> possibleMoves = currentConfig.possibleMoves(r,c);
        Coordinates newMove = new Coordinates(r2, c2);
        if (possibleMoves.contains(newMove)){
            if (r2 < r){
                if (c2 < c){
                    currentConfig.getGraph()[r-1][c-1] = '.';
                    currentConfig.getGraph()[r2][c2] = currentConfig.getGraph()[r][c];
                    currentConfig.getGraph()[r][c] = '.';
                } else if (c2 == c){
                    currentConfig.getGraph()[r-2][c] = '.';
                    currentConfig.getGraph()[r2][c2] = currentConfig.getGraph()[r][c];
                    currentConfig.getGraph()[r][c] = '.';
                } else{
                    currentConfig.getGraph()[r-1][c+1] = '.';
                    currentConfig.getGraph()[r2][c2] = currentConfig.getGraph()[r][c];
                    currentConfig.getGraph()[r][c] = '.';
                }
            } else if (r2 == r){
                if (c2 < c){
                    currentConfig.getGraph()[r][c-2] = '.';
                    currentConfig.getGraph()[r2][c2] = currentConfig.getGraph()[r][c];
                    currentConfig.getGraph()[r][c] = '.';
                } else{
                    currentConfig.getGraph()[r][c+2] = '.';
                    currentConfig.getGraph()[r2][c2] = currentConfig.getGraph()[r][c];
                    currentConfig.getGraph()[r][c] = '.';
                }
            } else {
                if (c2 < c){
                    currentConfig.getGraph()[r+1][c-1] = '.';
                    currentConfig.getGraph()[r2][c2] = currentConfig.getGraph()[r][c];
                    currentConfig.getGraph()[r][c] = '.';
                } else if (c2 == c){
                    currentConfig.getGraph()[r+2][c] = '.';
                    currentConfig.getGraph()[r2][c2] = currentConfig.getGraph()[r][c];
                    currentConfig.getGraph()[r][c] = '.';
                } else{
                    currentConfig.getGraph()[r+1][c+1] = '.';
                    currentConfig.getGraph()[r2][c2] = currentConfig.getGraph()[r][c];
                    currentConfig.getGraph()[r][c] = '.';
                }

            }
            if (currentConfig.isSolution()){
                gameState = GameState.WON;
            } else{
                Configuration[] path = puzzleSolver.getSolution(currentConfig).toArray(new Configuration[0]);
                if (path.length == 0){
                    gameState = GameState.LOST;
                } else{
                    gameState = GameState.ONGOING;
                }
            }
            initialR = -1;
            initialC = -1;
            alertObservers(HoppersModel.STATE_MSGS.get(gameState));
        } else{
            initialR = -1;
            initialC = -1;
            alertObservers("Invalid move. Pick a frog and try again.");
        }
    }
    public void select(int r, int c){
        if (this.initialR != -1 && this.initialC != -1){
            secondR = r;
            secondC = c;
            move(initialR, initialC, secondR, secondC);
        }else{
            if (r >= 0 && r < currentConfig.getRows() && c >= 0 && c < currentConfig.getColumns()){
                if (currentConfig.isFrog(r,c)){
                    this.initialR = r;
                    this.initialC = c;
                    alertObservers("Selected (" + initialR + "," + initialC + ")");
                }
            } else{
                alertObservers("Invalid selection");
            }
        }
    }


    /**
     * getter for the model's gameState
     * @return current gameState
     */
    public GameState getGameState(){
        return gameState;
    }

    /**
     * getter for rows
     * @return row dimensions of configuration
     */
    public int getModelRows(){
        return currentConfig.getRows();
    }
    /**
     * getter for columns
     * @return column dimensions of configuration
     */
    public int getModelColumns(){
        return currentConfig.getColumns();
    }
    public String getFilename(){
        return filename;
    }
    public char[][] getGraph(){
        return currentConfig.getGraph();
    }
    public HoppersConfig getCurrentConfig(){
        return currentConfig;
    }
    /**
     * generates the display version of the configuration with added row and column
     * numbers along the top and bottom
     * @return string representation of configuration
     */
    public String getDisplay(){
        StringBuilder display = new StringBuilder("");
        for (int c = 0; c < currentConfig.getColumns(); c++){
            if (c == currentConfig.getColumns()-1){
                display.append(c).append(System.lineSeparator());
            } else {
                display.append(c).append(" ");
            }
        }
        for (int c = 0; c < currentConfig.getColumns(); c++){
            if (c == currentConfig.getColumns()-1){
                display.append("-").append(System.lineSeparator());
            } else {
                display.append("-").append(" ");
            }
        }
        for (int r = 0; r < currentConfig.getRows(); r++){
            for (int c = 0; c < currentConfig.getColumns(); c++){
                if (c == currentConfig.getColumns()-1){
                    display.append(currentConfig.getGraph()[r][c]).append("| ").append(r).append(System.lineSeparator());
                } else{
                    display.append(currentConfig.getGraph()[r][c]).append(" ");
                }
            }
        }

        return display.toString();
    }
}
