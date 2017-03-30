import java.util.*;
import java.io.IOException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Game {
	public String turn;   // p1 (player 1) or p2 (player 2)
	private boolean twoPlayer; 
	private static int [] board;
	private int boardSize;
	private int totalSeed1;
	private int totalSeed2;
	// if mode == 0 => 2 players
	// if mode == 1 => AI easy
	// if mode == 2 => AI medium
	// if mode == 3 => AI hard
	private int MODE = 0;
	private int NUMSEEDS;
	private int NUMHOUSES;
	private int kalahPosition1 = 0;
	private int kalahPosition2 = 0;
	private static GameUI gameGUI;
	private static AIeasy ai1;
	 
	public Game() {
		newGame(0,6,4);
	}
	public Game(int playMode, int numHouses, int numSeeds) {
		newGame(playMode,numHouses,numSeeds);
	}
	
	// initialize the game
	public void newGame (int playMode, int numHouses, int numSeeds) {
		MODE = playMode;
		if (MODE == 1) {
			 ai1 = new AIeasy();
		}
		NUMSEEDS = numSeeds;
		NUMHOUSES = numHouses;
		// boardSize = number of Houses * 2 (for 2 players) + 2 kalahs
		boardSize = NUMHOUSES * 2 + 2; 
		board = new int[boardSize];
		kalahPosition1 = NUMHOUSES;
		kalahPosition2 = boardSize - 1;
		for (int i = 0; i < boardSize; ++i) {
			if (i== kalahPosition1 || i == kalahPosition2){
				board[i] = 0;
			}
			else {
				board[i] = NUMSEEDS;
			}
		}
		turn = "p1";
	}
	
	public void newGame () {
		// board = new int[14];   
		// for (int i = 0; i < 14; ++i) {
			// if (i==6 || i == 13){
				// board[i] = 0;
			// }
			// else {
				// board[i] = NUMSEEDS;
			// }
		// }
		// turn = "p1";
		// gameGUI.updateBoard(board);
	}
	
	public int boardInfo (int houseNum) {
		if (houseNum > (boardSize - 1) || houseNum < 0) {
			return -1;
		}
		return board[houseNum];
	}
	
	public int getNumHouses () {
		return NUMHOUSES;
	}
	
	public int getNumSeeds () {
		return NUMSEEDS;
	}
	
	public boolean isPositionValid (int position) {
		if (turn == "p1"){
			if (position < 0 || position > (kalahPosition1 - 1) || board[position] == 0) {
				return false;
			}
			else {
				return true;
			}
		}
		if (turn == "p2") {
			if (position < (kalahPosition1 + 1) || position > (kalahPosition2 - 1) || board[position] == 0) {
				return false;
			}
			else {
				return true;
			}
		}
		return false;
	}
	
	public String checkWinner () {
		String winner = "none";
		if (board[kalahPosition1] > board[kalahPosition2]){
			winner = "p1";
		}
		else if (board[kalahPosition1] < board[kalahPosition2]){
			winner = "p2";
		}
		else {
			winner = "tie";
		}
		return winner;
	}
	
	public boolean isOver () {
		String result = checkWinner();
		if (!result.equals("none")) {
			if (result.equals("tie")){
				System.out.println("TIE");
				System.out.println("Game Over");
			}
			else {
				System.out.println("The winner is " + result);
				System.out.println("Game Over");
			}
			return true;
		}
		return false;
	}
	
	// check if ALL houses in the player's side are empty
	public boolean isEmpty () {
		totalSeed1 = 0;
		totalSeed2 = 0;
		for (int i = 0; i < kalahPosition1; ++i){
			totalSeed1 += board[i];
		}
		for (int i = (kalahPosition1 + 1); i < kalahPosition2; ++i) {
			totalSeed2 += board[i];
		}
		if (totalSeed1 == 0 || totalSeed2 == 0){
			return true;
		}
		return false;
	}
	
	public void lastMove () {
		if (totalSeed1 == 0) {
			board[kalahPosition2] += totalSeed2;
		}
		else if (totalSeed2 == 0) {
			board[kalahPosition1] += totalSeed1;
		}
		for (int i = 0; i < boardSize; ++i) {
			if (i != kalahPosition1 && i != kalahPosition2) {
				board[i] = 0;
			}
		}
		for (int i = 0; i < boardSize; ++i) {
			System.out.println("number of seeds in house " + i + " is: " + board[i]);
		}
		System.out.println("Kalah 1: " + board[kalahPosition1]);
		System.out.println("Kalah 2: " + board[kalahPosition2]);
	}
	
	public void claimSeeds (int position) {
		int oppositePosition = (NUMHOUSES*2) - position;
		if (turn == "p1" && position >= 0 && position < kalahPosition1 && board[position] == 1 && board[oppositePosition] != 0){
			board[kalahPosition1] += board[position] + board[oppositePosition];
			board[position] = 0;
			board[oppositePosition] = 0;
		}
		if (turn == "p2" && position >= (kalahPosition1 + 1) && position < kalahPosition2 && board[position] == 1 && board[oppositePosition] != 0){
			board[kalahPosition2] += board[position] + board[oppositePosition];
			board[position] = 0;
			board[oppositePosition] = 0;
		}
		
	}
	
	public void disperseSeeds (int startPosition, int endPosition) {
		for (int i = startPosition; i <= endPosition; ++i){
			board[i] += 1;
		}
	}
	
	public void checkTurn (int position){
		if (MODE == 0){
			if (turn == "p1" && position != kalahPosition1){
				turn = "p2";
			}
			else if (turn == "p2" && position != kalahPosition2) {
				turn = "p1";
			}
			System.out.println("TURN: " + turn);
		}
		else { // AI mode 
			if (position != kalahPosition1) {
				turn = "p2";
				ai1.AImove(this,board);
				gameGUI.updateBoard(this, board);
				turn = "p1";
			}
		}
	}
	
	public void move (int position) {
		if (isPositionValid(position)){
			int numSeeds = board[position];
			int endPosition = (position + numSeeds) % boardSize;
			if (turn == "p1"){
				if (numSeeds + position <= (kalahPosition2 - 1)){
					disperseSeeds(position+1,position+numSeeds);
				}
				else {
					disperseSeeds(position+1,kalahPosition2-1);
					numSeeds = numSeeds - (kalahPosition2 - 1 - position);
					int numLoops = numSeeds / (boardSize - 1);
					while (numLoops > 0) {
						disperseSeeds(0,kalahPosition2-1);
						--numLoops;
					}
					disperseSeeds(0,numSeeds % (boardSize-1));
				}
			}
			else {
				if (numSeeds + position <= kalahPosition2){
					disperseSeeds(position+1,position+numSeeds);
				}
				else {
					disperseSeeds(position+1,kalahPosition2);
					numSeeds = numSeeds - (kalahPosition2-position);
					int numLoops = numSeeds/(boardSize-1);
					while (numLoops > 0) {
						disperseSeeds(0,kalahPosition1-1);
						disperseSeeds(kalahPosition1+1,kalahPosition2);
						numSeeds -= kalahPosition2;
						--numLoops;
					}
					if (numSeeds < kalahPosition1){
						disperseSeeds(0,numSeeds-1);
					}
					else {
						disperseSeeds(0,kalahPosition1-1);
						disperseSeeds(kalahPosition1 + 1,numSeeds);
					}
				}
			}
			board[position] = 0;
			claimSeeds(endPosition);
			/********* FOR DEBUGGING ***********/
			for (int i = 0; i < boardSize; ++i) {
				System.out.println("number of seeds in house " + i + " is: " + board[i]);
			}
			/********* FOR DEBUGGING ***********/
			checkTurn(endPosition);
		}
		else {
			System.out.println("Invalid position");
		}
	}
	
	public static void main (String [] args) {
		Game game = new Game();
		gameGUI = new GameUI();
		try {
			//gameGUI.displayWelcome(game);
			game.newGame(0,6,4);
			gameGUI.GUIHandler(game, 0, 6, 4);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int house;
		while (true) {
			if((house = gameGUI.waitForClick()) >= 0) {
				game.move(house);
				gameGUI.updateBoard(game, board);
				if (game.isEmpty()){
					game.lastMove();
					game.isOver();
					gameGUI.updateBoard(game, board);
					break;
				}
			}
		}
	}
}
