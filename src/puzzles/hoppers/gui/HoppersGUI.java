package puzzles.hoppers.gui;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersModel;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class HoppersGUI extends Application implements Observer<HoppersModel, String> {
    /** The size of all icons, in square dimension */
    private final static int ICON_SIZE = 75;
    /** the font size for labels and buttons */
    private final static int FONT_SIZE = 12;

    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";
    private HoppersModel model;
    private boolean initialized;
    private Label statuslabel;
    private BorderPane mainPane;
    private Button[][] board;
    private Image redFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"red_frog.png"));
    private Image greenFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"green_frog.png"));
    private Image lilyPad = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"lily_pad.png"));
    private Image water = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"water.png"));
    private Stage stage;

    public void init() throws IOException {
        String filename = getParameters().getRaw().get(0);
        this.initialized = false;
        this.model = new HoppersModel(filename);
        this.board = new Button[model.getModelRows()][model.getModelColumns()];
        this.model.addObserver(this);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        //creating main borderpane
        BorderPane mainPane = new BorderPane();
        this.mainPane = mainPane;
        //label that displays status of the game
        Label statusLabel = new Label("Loaded: " + model.getFilename());
        this.statuslabel = statusLabel;
        mainPane.setTop(statusLabel);
        //creating the board
        GridPane board = createBoard();
        mainPane.setCenter(board);
        //creating hbox for bottom of pane
        mainPane.setBottom(createHBox());
        //configuring scene
        Scene scene = new Scene(mainPane);
        stage.setScene(scene);
        stage.setTitle("Hoppers GUI");
        stage.show();
        this.initialized = true;
    }

    /**
     * method that creates the gridpane of buttons to represent the board through using
     * the models 2D array
     * @return gridpane representation of model
     */
    public GridPane createBoard(){
        GridPane boardGrid = new GridPane();
        for (int r = 0; r < model.getModelRows(); r++){
            for (int c = 0; c < model.getModelColumns(); c++){
                Image buttonImage = null;
                if(model.getGraph()[r][c] == 'R'){
                    buttonImage = redFrog;
                } else if(model.getGraph()[r][c] == 'G'){
                    buttonImage = greenFrog;
                } else if(model.getGraph()[r][c] == '.'){
                    buttonImage = lilyPad;
                } else{
                    buttonImage = water;
                }
                Button boardTile = new Button();
                boardTile.setGraphic(new ImageView(buttonImage));
                boardTile.setMinSize(ICON_SIZE, ICON_SIZE);
                boardTile.setMaxSize(ICON_SIZE, ICON_SIZE);
                this.board[r][c] = boardTile;
                boardGrid.add(boardTile, c, r);
            }
        }
        return boardGrid;
    }
    public HBox createHBox (){
        HBox bottom = new HBox();
        //implementation for the load button
        Button load = new Button("Load");
        FileChooser fileChooser = new FileChooser(); // creating the file chooser and setting initial directory
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        currentPath += File.separator + "data" + File.separator + "hoppers";
        fileChooser.setInitialDirectory(new File(currentPath));
        load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File file = fileChooser.showOpenDialog(stage);
                model.load(String.valueOf(file));
                }
            }
        );

        //implementation for reset button
        Button reset = new Button("Reset");
        reset.setOnAction(event -> {
            try {
                model.reset();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        //implementation for hint button
        Button hint = new Button("Hint");
        hint.setOnAction(event -> model.hint());
        //adding them all to the hbox
        bottom.getChildren().addAll(load, reset, hint);

        return bottom;
    }
    @Override
    public void update(HoppersModel hoppersModel, String msg) {
        if (!this.initialized) return;
        statuslabel.setText(msg);
        this.board = new Button[model.getModelRows()][model.getModelColumns()];
        mainPane.setCenter(createBoard());

        this.stage.sizeToScene();  // when a different sized puzzle is loaded
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            Application.launch(args);
        }
    }
}
