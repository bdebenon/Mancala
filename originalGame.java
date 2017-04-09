import java.util.*;
import java.io.IOException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.concurrent.*;

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
	private static AImedium ai2;
	private static AIhard ai3;
	
	
	
	/**** NEW VARIABLES FOR PIE RULE: Part 1/4 ****/  
	private boolean Pie = false;
	private int endP = 0;
	/**** END ****/
	
	
	
	
	public Game() {
		newGame(0,6,4);
	}
	public Game(int playMode, int numHouses, int numSeeds) {
		newGame(playMode,numHouses,numSeeds);
	}
	
	// initialize the game
	public void newGame (int playMode, int numHouses, int numSeeds) {
		NUMSEEDS = numSeeds;
		NUMHOUSES = numHouses;
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
		MODE = playMode;
		if (MODE == 1) {
			 ai1 = new AIeasy();
		}
		if (MODE == 2) {
			ai2 = new AImedium(this);
		}
		if (MODE == 3) {
			ai3 = new AIhard(this);
		}
		turn = "p1";
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
	
	public int[] getBoard () {
		return board;
	}
	
	
	
	
	/*********** HELPER FUNCTIONS FOR PIE RULE: Part 2/4 **************/
	public boolean getPie () {
		return Pie;
	}
	
	public void setPie (boolean pie) {
		Pie = pie;
	}
	
	public void setEndP (int end) {
		endP = end;
	}
	
	public void pieRule () {
		System.out.println("You want to switch position with player 1. True or false");
		Scanner sc = new Scanner(System.in);
		boolean isPie = Boolean.parseBoolean(sc.nextLine());
		setPie(isPie);
		if (Pie) {
			int [] tempBoard = Arrays.copyOf(board,boardSize);
			for (int i = 0; i < NUMHOUSES; ++i) {
				board[i] = tempBoard[kalahPosition1+1+i];
				board[kalahPosition1+1+i] = tempBoard[i];
			}
			board[kalahPosition1] = tempBoard[kalahPosition2];
			board[kalahPosition2] = tempBoard[kalahPosition1];
			turn = "p1";
			System.out.println("Board position has been switched");
			System.out.println("Player 2 has Player 1 board side now");
			System.out.println("Player 1 continues to play with the new board");
		}
	}
	
	/*********** END OF HELPER FUNCTIONS FOR PIE RULE **************/

	
	
	
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
				if (MODE == 1) {
					ai1.AImove(this,board);
				}
				if (MODE == 2) {
					ai2.AImove(board);
				}
				if (MODE == 3) {
					ai3.AImove(board);
				}
				turn = "p1";
			}
		}
	}
	
	public void move (int position) {
		if (isPositionValid(position)){
			int numSeeds = board[position];
			board[position] = 0;
			int endPosition = (position + numSeeds) % boardSize;
			
			if (turn == "p1"){
				if (numSeeds + position <= (kalahPosition2 - 1)){
					disperseSeeds(position+1,position+numSeeds);
				}
				else {
					int numPasses = (numSeeds + position) / (boardSize-1);
					disperseSeeds(position+1,kalahPosition2-1);
					numSeeds = numSeeds - (kalahPosition2 - 1 - position);
					int numLoops = numSeeds / (boardSize - 1);
					endPosition = endPosition + numPasses;
					while (numLoops > 0) {
						disperseSeeds(0,kalahPosition2-1);
						--numLoops;
						numSeeds -= kalahPosition2;
					}
					disperseSeeds(0,numSeeds - 1);
				}
			}
			else {
				if (numSeeds + position <= kalahPosition2){
					disperseSeeds(position+1,position+numSeeds);
				}
				else {
					int numPasses = (numSeeds + position - NUMHOUSES - 1) / (boardSize - 1);
					disperseSeeds(position+1,kalahPosition2);
					numSeeds = numSeeds - (kalahPosition2-position);
					int numLoops = numSeeds/(boardSize-1);
					endPosition = endPosition + numPasses;
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
			setEndP (endPosition);
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
			game.newGame(0,6,4);
			gameGUI.GUIHandler(game, 0, 6, 4);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
		}
		
		int house;
		
		
		
		// Variable for Pie rule : Part 3/4
		int count = 0;
		
		

		while (true) {
			if((house = gameGUI.waitForClick()) >= 0) {
				// ExecutorService executor = Executors.newSingleThreadExecutor();
				// Future<?> future = executor.submit(new Task());
				// try {
					// System.out.println("Started..");
					// System.out.println(future.get(5, TimeUnit.SECONDS));
					// System.out.println("Finished!");
				// } catch (TimeoutException e) {
					// future.cancel(true);
					// System.out.println("Terminated!");
				// }
				
				
				
				
				/****************MODIFIED MAIN FOR PIE RULE: Part 4/4 *********/
				if (count == 0) {
					game.move(house);
					gameGUI.updateBoard(game, board);
					while (game.endP == 6) {
						if ((house = gameGUI.waitForClick()) >= 0) {
							game.move(house);
							gameGUI.updateBoard(game, board);
						}
					}
					game.pieRule();
					gameGUI.updateBoard(game,board);
					count += 1; 
				}
				else {
					game.move(house);
					gameGUI.updateBoard(game, board);
					if (game.isEmpty()){
						game.lastMove();
						game.isOver();
						gameGUI.updateBoard(game, board);
						break;
					}
				}
				/**************** END *********/
				
				
				
				
				//executor.shutdownNow();
			}
		}
	}
}


// class Task implements Callable<Void> {
    // @Override
    // public Void call() throws Exception {
        // Thread.sleep(4000); // Just to demo a long running task of 4 seconds.
		// System.out.println("Ready!");
		// return null;
    // }
// }
