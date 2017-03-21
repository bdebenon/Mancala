import java.util.*;
import java.io.IOException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game {
	private String turn;   // p1 (player 1) or p2 (player 2)
	private boolean twoPlayer; 
	private boolean AIeasy;
	private boolean AImedium;
	private boolean AIhard;
	private int [] board;
	private int totalPebble1;
	private int totalPebble2;
	private static final int NUMBEANS = 4;
	
	// 2 players mode 
	public Game() {
		newGame(true);
	}
	
	// initialize the game
	public void newGame (boolean twoPlayer) {
		this.twoPlayer = twoPlayer;
		// initialize the board 
		// except for kalahs, which have zero bean at the beginning
		// all the other houses have 4 beans 
		board = new int[14];   
		for (int i = 0; i < 14; ++i) {
			if (i==6 || i == 13){
				board[i] = 0;
			}
			else {
				board[i] = NUMBEANS;
			}
		}
		// initialize turn, player 1 always starts first 
		// player 2 could either be human or AI 
		turn = "p1";
	}
	
	// board getter
	// to help with GUI
	public int boardInfo (int houseNum) {
		if (houseNum > 13 || houseNum < 0) {
			return -1;
		}
		return board[houseNum];
	}
	
	public boolean isPositionValid (int position) {
		if (turn == "p1"){
			if (position < 0 || position > 5 || board[position] == 0) {
				return false;
			}
			else {
				return true;
			}
		}
		if (turn == "p2") {
			if (position < 7 || position > 12 || board[position] == 0) {
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
		if (board[6] > board[13]){
			winner = "p1";
		}
		if (board[6] < board[13]){
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
			// call GUI game over
			// make new game
			System.out.println("The winner is " + result);
			System.out.println("Game Over");
			return true;
		}
		return false;
	}
	
	// check if ALL houses in the player's side are empty
	public boolean isEmpty () {
		totalPebble1 = 0;
		totalPebble2 = 0;
		for (int i = 0; i <=5; ++i){
			totalPebble1 += board[i];
		}
		for (int i = 7; i <= 12; ++i) {
			totalPebble2 += board[i];
		}
		if (totalPebble1 == 0 || totalPebble2 == 0){
			return true;
		}
		return false;
	}
	
	public void lastMove () {
		if (totalPebble1 == 0) {
			board[13] += totalPebble2;
		}
		else if (totalPebble2 == 0) {
			board[6] += totalPebble1;
		}
	}
	
	public void claimPebbles (int position) {
		if (turn == "p1" && position >= 0 && position <= 5 && board[position] == 1){
			board[6] += board[position] + board[12 - position];
			board[position] = 0;
			board[12 - position] = 0;
		}
		if (turn == "p2" && position >= 7 && position <= 12 && board[position] == 1){
			board[13] += board[position] + board[12 - position];
			board[position] = 0;
			board[12 - position] = 0;
		}
		
	}
	
	public void dispersePebbles (int startPosition, int endPosition) {
		for (int i = startPosition; i <= endPosition; ++i){
			board[i] += 1;
		}
	}
	
	public void checkTurn (int position){
		if (twoPlayer){
			if (turn == "p1" && position != 6){
				turn = "p2";
			}
			else if (turn == "p2" && position != 13) {
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
			int endPosition = (position + numPebbles) % 14;
			if (turn == "p1"){
				if (numPebbles + position <= 12){
					dispersePebbles(position+1,position+numPebbles);
				}
				else {
					dispersePebbles(position+1,12);
					numPebbles = numPebbles - (12-position);
					int numLoops = numPebbles/13;
					while (numLoops > 0) {
						dispersePebbles(0,12);
						--numLoops;
					}
					dispersePebbles(0,numPebbles % 13);
				}
			}
			else {
				if (numPebbles + position <= 13){
					dispersePebbles(position+1,position+numPebbles);
				}
				else {
					dispersePebbles(position+1,13);
					numPebbles = numPebbles - (13-position);
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
			}
			board[position] = 0;
			claimPebbles(endPosition);
			/********* FOR DEBUGGING ***********/
			for (int i = 0; i < 14; ++i) {
				System.out.println("number of pebbles in house " + i + " is: " + board[i]);
			}
			System.out.println("End position: " + endPosition);
			/********* FOR DEBUGGING ***********/
			if (twoPlayer) {
				checkTurn(endPosition);
			}
			else { // AI
				if (endPosition != 6) {
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
		
		/********* GUI ***********/
		EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                try {
					new GameUI().displayGUI(game);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
		
		/********* END OF GUI ***********/
		
		/********* FOR DEBUGGING ***********/
		// Scanner sc = new Scanner(System.in);
		// System.out.println("Enter a house number: ");
		// int house;
		// while ((house = sc.nextInt()) >= 0) {
			// game.move(house);
			// if (game.isEmpty()){
				// game.lastMove();
				// game.isOver();
				// break;
			// }
			// System.out.print("Enter a house number: ");
		// }
		/********* END DEBUGGING ***********/
	}
}