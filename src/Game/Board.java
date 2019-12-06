package Game;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Arrays;

public class Board extends GameLogic {
    BorderPane bp = new BorderPane();
    GridPane gpColors = new GridPane();
    GridPane gpTroops = new GridPane();
    GridPane gpSelection = new GridPane();
    Rectangle[][] boardColors = new Rectangle[10][10];
    Rectangle[][] boardSelections = new Rectangle[10][10];
    Label[][] boardTroops = new Label[10][10];
    StackPane sp = new StackPane();
    Integer[][] integerTroops;
    ArrayList<Integer> selection;
    int turn;
    public boolean setUpPhase = false;
    Color[][] colors;
    Button place = new Button("Place");
    Button attack = new Button("Attack");
    Button move = new Button("Move");
    Button endTurn = new Button("End Turn");
    Button clearSelections = new Button("Clear Selections");
    Button newGame = new Button("New Game");
    TextField input = new TextField("Troop #");

    public Board(){
        selection = new ArrayList<>();
        startNewGame();
    }

    public void startNewGame(){
        turn = 0;
        initTroops();   //initializes the board troops to '0'
        resetColors();
        initSelections();
        setActions();   //sets the actions of the buttons (from the GameLogic)
//        refresh(Board.this);  //draws the GUI
        refresh(Board.this);
    }

    public void refresh(Board board) {
        this.turn = board.turn;
//        this.selection = board.selection;
        this.colors = board.colors;
        this.integerTroops = board.integerTroops;
        clearPanes();
        clearSelection();
        drawTitle();
        drawBoard();
        drawButtons();
    }


    public void drawTitle(){
        System.out.println("drawTitle");
        String who = turn == 0 ? "Blue" : "Red";
        Label lb = new Label(who + " player's turn");
        lb.setFont(Font.font("SansSerif", 30));
        bp.setTop(lb);
        bp.setAlignment(lb, Pos.CENTER);
//        System.out.println("Got here1");
    }

    public void drawBoard() {
        System.out.println("drawBoard");
        convertColors(colors);
        convertTroops();
//        convertSelections();

        for (int i = 0; i < boardColors.length; i++) {
            for (int j = 0; j < boardColors[i].length; j++) {
//                StackPane tmpStack = new StackPane();
//                tmpStack.getChildren().addAll(boardColors[i][j], boardTroops[i][j], boardSelections[i][j]);
//                gpColors.add(tmpStack, j, i);
                gpColors.add(boardColors[i][j], j, i);
                gpTroops.add(boardTroops[i][j], j, i);
                gpSelection.add(boardSelections[i][j], j, i);
            }
        }

        gpTroops.setAlignment(Pos.CENTER);
        gpColors.setAlignment(Pos.CENTER);
        gpSelection.setAlignment(Pos.CENTER);
        gpTroops.setVgap(36);
        gpTroops.setHgap(41);
        sp.setAlignment(Pos.CENTER);
        sp.getChildren().addAll(gpColors, gpTroops, gpSelection);
        bp.setCenter(sp);
    }

    public void drawButtons(){
        System.out.println("drawButtons\n");
        HBox pn = new HBox();
        pn.getChildren().addAll(place, attack, move, endTurn, clearSelections, newGame);
        pn.setAlignment(Pos.CENTER);
        bp.setBottom(pn);
    }

    private void TextField(String s) {
    }

    private EventHandler<MouseEvent> clickedBox = e -> {
        if (setUpPhase) {
            Rectangle r = (Rectangle)e.getSource();
            int row = gpColors.getRowIndex(r);
            int col = gpColors.getColumnIndex(r);
            //give clicked result to something
            System.out.println("This box was clicked: " + row + ", " + col + "\nWith color Value: " + boardColors[row][col]);    //for testing purposes
            highlightSelection(row, col);

            if (this.turn == 0) {
                if(this.colors[row][col] == (Color.WHITE)){
                    this.colors[row][col] = (Color.BLUE);
                    this.integerTroops[row][col] = 2;
                    this.endTurn(this);
                }
            } else {
                if(this.colors[row][col] == (Color.WHITE)) {
                    this.colors[row][col] = (Color.RED);
                    this.integerTroops[row][col] = 2;
                    this.endTurn(this);
                }
            }
            this.refresh(this);

            ArrayList<Color> colorList = new ArrayList<>();
            for (Color[] c : this.colors) {
                colorList.addAll(Arrays.asList(c));
            }
            if(!colorList.contains(Color.WHITE)){
                this.setUpPhase = false;
            }
        } else {
            Rectangle r = (Rectangle)e.getSource();
            int row = gpColors.getRowIndex(r);
            int col = gpColors.getColumnIndex(r);
            //give clicked result to something
            System.out.println("This box was clicked: " + row + ", " + col + "\nWith color Value: " + boardColors[row][col]);    //for testing purposes
            highlightSelection(row, col);
            this.selection.add(row);
            this.selection.add(col);
        }
    };

    private void convertColors(Color[][] colors) {      //makes regions clickable
        for (int i=0; i < colors.length; i++) {
            for (int j=0; j < colors[i].length; j++) {
                Rectangle r = new Rectangle(50, 50);
                r.setFill(colors[i][j]);
                if (colors[i][j] != Color.BLACK) r.setStroke(Color.ORANGE);
                else r.setStroke(Color.BLACK);
                boardColors[i][j] = r;
            }
        }
    }

    private void convertTroops() {     //makes Integers Node objects to put in gpTroops to stack on gpColors
        for (int i=0; i<integerTroops.length; i++) {
            for (int j=0; j<integerTroops[i].length; j++) {
                boardTroops[i][j] = new Label(Integer.toString(integerTroops[i][j]));
            }
        }
    }

    private void highlightSelection(int i, int j) {
        boardSelections[i][j].setStroke(Color.GREEN);
        boardSelections[i][j].setFill(Color.GREY);
        boardSelections[i][j].setOpacity(0.4);
    }

    public void clearSelection() {
        System.out.println(selection);
        for (int i=0; i<boardSelections.length; i++) {
            for (int j=0; j<boardSelections[i].length; j++) {
                boardSelections[i][j].setStroke(Color.TRANSPARENT);
                boardSelections[i][j].setFill(Color.TRANSPARENT);
            }
        }
        selection = new ArrayList<>();
    }

    private void clearPanes() {
        gpColors = new GridPane();
        gpTroops = new GridPane();
        gpSelection = new GridPane();
    }

    public void initTroops() {
        this.integerTroops =  new Integer[10][10];
        for (Integer[] troop : integerTroops) {
            Arrays.fill(troop, 0);
        }
    }   //integer troops initializer to 0;

    public void setActions() {
        place.setOnAction(e -> place(Board.this));
        attack.setOnAction(e -> attack(Board.this));
        move.setOnAction(e -> move(Board.this));
        endTurn.setOnAction(e -> endTurn(Board.this));
        clearSelections.setOnAction(e -> clearSelection()); //How to
        newGame.setOnAction(e -> gameInit(Board.this));
    }

    public void initSelections() {
        for(int i=0; i<boardSelections.length; i++) {
            for(int j=0; j< boardSelections[i].length; j++) {
                boardSelections[i][j] = new Rectangle(50,50);
                boardSelections[i][j].setStroke(Color.TRANSPARENT);
                boardSelections[i][j].setFill(Color.TRANSPARENT);
                if (colors[i][j] != Color.BLACK) boardSelections[i][j].setOnMouseClicked(clickedBox);
            }
        }
    }

    private void resetColors() {
        this.colors = new Color[][]{   //10x10 color grid BLACK SPACES ARE EMPTY-REGIONS
                {Color.BLACK, Color.BLACK, Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK, Color.BLACK, Color.BLACK},
                {Color.BLACK, Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK, Color.BLACK},
                {Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK},
                {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK},
                {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK},
                {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE},
                {Color.BLACK, Color.BLACK, Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE,},
                {Color.BLACK, Color.BLACK, Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK, Color.WHITE,},
                {Color.BLACK, Color.BLACK, Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK, Color.BLACK},
                {Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK, Color.BLACK, Color.BLACK},
        };
    }

//    public static void gameInit() { //this is my problem
//        GameLogic game = new GameLogic();
//        game.gameInit();
//    }

    public ArrayList<Integer> getSelection() {
        return this.selection;
    }

    public int getTurn() {
        return this.turn;
    }

    public void setTurn(int who) {
        this.turn = who;
    }
}
