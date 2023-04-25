package puzzles.hoppers.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersConfig;

import java.io.IOException;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HoppersModel {
    /** the collection of observers of this model */
    private final List<Observer<HoppersModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private HoppersConfig currentConfig;
    private String filename;
    private Solver puzzleSolver;

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
        this.gameState = GameState.RESET;
        alertObservers(HoppersModel.STATE_MSGS.get(this.gameState));
    }

    /**
     * getter for the model's gameState
     * @return current gameState
     */
    public GameState getGameState(){
        return gameState;
    }
}
