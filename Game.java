import java.util.*;
import java.io.IOException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game {
	private String turn;   // p1 (player 1) or p2 (player 2)
	private boolean twoPlayer; 
	private static int [] board;
	private int boardSize;
	private int totalPebble1;
	private int totalPebble2;
	// if mode == 0 => 2 players
	// if mode == 1 => AI easy
	// if mode == 2 => AI medium
	// if mode == 3 => AI hard
	private int MODE = 0;
	private int NUMPEBBLES;
	private int NUMHOUSES;
	private int kalahPosition1 = 0;
	private int kalahPosition2 = 0;
	private static GameUI gameGUI;
	 
	public Game() {
		newGame(0,6,4);
	}
	
	// initialize the game
	public void newGame (int playMode, int numHouses, int numPebbles) {
		MODE = playMode;
		NUMPEBBLES = numPebbles;
		NUMHOUSES = numHouses;
		// boardSize = number of pits * 2 (for 2 players) + 2 kalahs
		boardSize = NUMHOUSES * 2 + 2; 
		board = new int[boardSize];
		kalahPosition1 = NUMHOUSES;
		kalahPosition2 = boardSize - 1;
		for (int i = 0; i < boardSize; ++i) {
			if (i== kalahPosition1 || i == kalahPosition2){
				board[i] = 0;
			}
			else {
				board[i] = NUMPEBBLES;
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
				// board[i] = NUMPEBBLES;
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
		totalPebble1 = 0;
		totalPebble2 = 0;
		for (int i = 0; i < kalahPosition1; ++i){
			totalPebble1 += board[i];
		}
		for (int i = (kalahPosition1 + 1); i < kalahPosition2; ++i) {
			totalPebble2 += board[i];
		}
		if (totalPebble1 == 0 || totalPebble2 == 0){
			return true;
		}
		return false;
	}
	
	public void lastMove () {
		if (totalPebble1 == 0) {
			board[kalahPosition2] += totalPebble2;
		}
		else if (totalPebble2 == 0) {
			board[kalahPosition1] += totalPebble1;
		}
		for (int i = 0; i < boardSize; ++i) {
			if (i != kalahPosition1 && i != kalahPosition2) {
				board[i] = 0;
			}
		}
		for (int i = 0; i < boardSize; ++i) {
			System.out.println("number of pebbles in house " + i + " is: " + board[i]);
		}
		System.out.println("Kalah 1: " + board[kalahPosition1]);
		System.out.println("Kalah 2: " + board[kalahPosition2]);
	}
	
	public void claimPebbles (int position) {
		int oppositePosition = (NUMHOUSES*2) - position;
		System.out.println("The oppositePosition is: " + oppositePosition);
		System.out.println("The position in claimPebbles is: " + position);
		System.out.println("The numPebbles is: " + NUMHOUSES);
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
	
	public void dispersePebbles (int startPosition, int endPosition) {
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
			System.out.println(turn);
		}
		else { // AI mode 
			turn = "p2";
			int endPosition = 0;
			do {
			if (!isEmpty()) {
				int house;
				do {
					house = (int) (Math.random() * 6 + 7);
				} while (boardInfo(house) == 0); 
				System.out.println("AI house: " + house);
			
				int numPebbles = board[house];
				endPosition = (house + numPebbles) % 14;
				if (numPebbles + house <= 13){
					dispersePebbles(house+1,house+numPebbles);
				}
				else {
					dispersePebbles(house+1,13);
					numPebbles = numPebbles - (13-house);
					int numLoops = numPebbles/13;
					while (numLoops > 0) {
						dispersePebbles(0,5);
						dispersePebbles(7,13);
						numPebbles -= 13;
						--numLoops;
					}
					if (numPebbles < 6){
						dispersePebbles(0,numPebbles-1);
					}
					else {
						dispersePebbles(0,5);
						dispersePebbles(7,numPebbles);
					}
				}
				board[house] = 0;
				claimPebbles(endPosition);
				for (int i = 0; i < 14; ++i) {
					System.out.println("number of pebbles in house " + i + " is: " + board[i]);
				}
				System.out.println("End position: " + endPosition);
				}
			} while (endPosition == 13);
			turn = "p1";
		}
	}
	
	public void move (int position) {
		if (isPositionValid(position)){
			int numPebbles = board[position];
			int endPosition = (position + numPebbles) % boardSize;
			if (turn == "p1"){
				if (numPebbles + position <= (kalahPosition2 - 1)){
					dispersePebbles(position+1,position+numPebbles);
				}
				else {
					dispersePebbles(position+1,kalahPosition2-1);
					numPebbles = numPebbles - (kalahPosition2 - 1 - position);
					int numLoops = numPebbles / (boardSize - 1);
					while (numLoops > 0) {
						dispersePebbles(0,kalahPosition2-1);
						--numLoops;
					}
					dispersePebbles(0,numPebbles % (boardSize-1));
				}
			}
			else {
				if (numPebbles + position <= kalahPosition2){
					dispersePebbles(position+1,position+numPebbles);
				}
				else {
					dispersePebbles(position+1,kalahPosition2);
					numPebbles = numPebbles - (kalahPosition2-position);
					int numLoops = numPebbles/(boardSize-1);
					while (numLoops > 0) {
						dispersePebbles(0,kalahPosition1-1);
						dispersePebbles(kalahPosition1+1,kalahPosition2);
						numPebbles -= kalahPosition2;
						--numLoops;
					}
					if (numPebbles < kalahPosition1){
						dispersePebbles(0,numPebbles-1);
					}
					else {
						dispersePebbles(0,kalahPosition1-1);
						dispersePebbles(kalahPosition1 + 1,numPebbles);
					}
				}
			}
			board[position] = 0;
			claimPebbles(endPosition);
			/********* FOR DEBUGGING ***********/
			for (int i = 0; i < 14; ++i) {
				System.out.println("number of pebbles in house " + i + " is: " + board[i]);
			}
			System.out.println("End position: " + endPosition);
			/********* FOR DEBUGGING ***********/
			if (MODE == 0) {
				checkTurn(endPosition);
			}
			else { // AI
				if (endPosition != kalahPosition1) {
					checkTurn(endPosition);
				}
			}
		}
		else {
			// GUI show "invalid position" message
			System.out.println("Invalid position");
		}
	}
	
	public static void main (String [] args) {
		Game game = new Game();
		gameGUI = new GameUI();
		try {
			gameGUI.displayGUI(game);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int house;
		while (true) {
			if((house = gameGUI.waitForClick()) >= 0) {
				game.move(house);
				gameGUI.updateBoard(board);
				if (game.isEmpty()){
					game.lastMove();
					game.isOver();
					gameGUI.updateBoard(board);
					break;
				}
			}
		}
	}
}
