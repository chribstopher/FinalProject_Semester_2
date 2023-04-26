package puzzles.hoppers.ptui;

import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersModel;
import puzzles.hoppers.ptui.HoppersPTUI;
import puzzles.hoppers.model.HoppersConfig;

import java.io.IOException;
import java.util.Scanner;

/**
 * PTUI for the Hoppers game
 * @author Christopher Rose
 */
public class HoppersPTUI implements Observer<HoppersModel, String> {
    private HoppersModel model;
    private boolean initialized;

    public void init(String filename) throws IOException {
        this.initialized = false;
        this.model = new HoppersModel(filename);
        this.model.addObserver(this);
        System.out.println(model.getDisplay());
        displayHelp();
    }

    @Override
    public void update(HoppersModel model, String data) {
        if (!this.initialized) return;

        System.out.println(model.getDisplay());
        HoppersModel.GameState gameState = model.getGameState();
        System.out.println(data);
    }

    private void displayHelp() {
        System.out.println( "h(int)              -- hint next move" );
        System.out.println( "l(oad) filename     -- load new puzzle file" );
        System.out.println( "s(elect) r c        -- select cell at r, c" );
        System.out.println( "q(uit)              -- quit the game" );
        System.out.println( "r(eset)             -- reset the current game" );
    }

    public void run() throws IOException {
        this.initialized = true;
        Scanner in = new Scanner( System.in );
        for ( ; ; ) {
            System.out.print( "> " );
            String line = in.nextLine();
            String[] words = line.split( "\\s+" );
            if (words.length > 0) {
                if (words[0].startsWith( "q" )) {
                    break;
                } else if (words[0].startsWith("h")){
                    model.hint();
                } else if (words[0].startsWith("l")){
                    model.load(words[1]);
                } else if (words[0].startsWith("s")){
                    select(Integer.parseInt(words[1]), Integer.parseInt(words[2]));
                } else if (words[0].startsWith("r")){
                    model.reset();
                }
                else {
                    displayHelp();
                }
            }
        }
    }
    public void select(int r, int c){
        if (r >= 0 && r < model.getCurrentConfig().getRows() && c >= 0 && c < model.getCurrentConfig().getColumns()){
            if(model.getCurrentConfig().isFrog(r,c)){
                Scanner in = new Scanner(System.in);
                System.out.print("Where do you wish to move > ");
                String line = in.nextLine();
                String[] coords = line.split("\\s+");
                model.move(r, c, Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
            } else{
                System.out.println("The selected coordinates do not contain a frog.");
            }
        } else{
            System.out.println("Coordinates given were out of bounds.");
        }
    }
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            try {
                HoppersPTUI ptui = new HoppersPTUI();
                ptui.init(args[0]);
                ptui.run();
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }
}
