package Game;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;

public class GameLogic {
//    Board board = new Board();
//    ArrayList<Integer> selections = board.selection;    //the selection doesn't save to this Instance of board, but a general one

    public GameLogic() {

    }

    public void gameInit(Board board) {    //get one array position
        board.startNewGame();
        board.setUpPhase = true;
    }


    public void place(Board board) {//get one array position
        //calculates how many troops a player should receive (based on % of board controlled)
        //needs the <colors> and <troops> arrays
        int countRed = 0;
        int countBlue = 0;
        for (Color[] c: board.colors) {
            for (Color c1 : c) {
                if(c1.equals(Color.RED)){
                    countRed++;
                }
                else if(c1.equals(Color.BLUE)){
                    countBlue++;
                }
            }
        }
        System.out.println("Red: " + countRed);
        System.out.println("Blue: " + countBlue);

        double troops = 0;
        if(board.turn == 0){
            troops = countBlue/3; //68 because that is how many possible squares there are.
        }
        else{
            troops = countRed/3;
        }
        if(troops<=2) troops = 2;
//        troops *= Math.random()%6*10;
        System.out.println("Troops: " + troops);
        board.integerTroops[board.selection.get(0)][board.selection.get(1)] += (int)troops;
        board.refresh(board);
    }


    public void move(Board board) {    //get two array positions
        //allows player to move troops between own territories (connected preferably, may add that logic later)
        int row1 = board.selection.get(0);
        int col1 = board.selection.get(1);
        int row2 = board.selection.get(2);
        int col2 = board.selection.get(3);

        board.integerTroops[row2][col2] += (board.integerTroops[row1][col1] - 1);
        board.integerTroops[row1][col1] = 1;
        board.refresh(board);
    }


    public void attack(Board board) {  //get two array positions
        //attack sequence, player selects own block first, then enemy block
        //uses roll() to determine who wins
        int row1 = board.selection.get(0);
        int col1 = board.selection.get(1);
        int row2 = board.selection.get(2);
        int col2 = board.selection.get(3);

        if(board.colors[row1][col1].equals(board.colors[row2][col2])){
            return;
        }else{
            while(board.integerTroops[row1][col1] != 1 && board.integerTroops[row2][col2] != 0){
                boolean battleWinner = roll();
                if(battleWinner){
                    board.integerTroops[row2][col2]--;
                }
                else{
                    board.integerTroops[row1][col1]--;
                }
            }
            if(board.integerTroops[row2][col2] == 0) {
                board.integerTroops[row2][col2] += 1;
                board.integerTroops[row1][col1] -= 1;
                if(board.turn == 0){
                    board.colors[row2][col2] = Color.valueOf(String.valueOf(Color.BLUE));
                }else{
                    board.colors[row2][col2] = Color.valueOf(String.valueOf(Color.RED));
                }
            }
            else{
                return;
            }
        }
        board.refresh(board);
    }

    public void endTurn(Board board){
        board.turn = 1 - board.turn;
        board.refresh(board);
    }

    public boolean roll() {
        //one is attacker, two  is defender
        double one = Math.random()*10%6;
        double two = Math.random()*10%6;
        return one > two;   //return true if attacker wins, false if attacker loses
    }
}