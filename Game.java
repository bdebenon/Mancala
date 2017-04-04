import java.io.IOException;
import java.util.concurrent.Exchanger;
import java.util.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

		
public class Game implements Runnable {
	//GAME
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
	private final BlockingQueue<int []> boardQueueIn;
	private final BlockingQueue<int []> boardQueueOut;
	
	public Game(BlockingQueue<int[]> _boardQueueIn, BlockingQueue<int[]> _boardQueueOut) {
		boardQueueIn = _boardQueueIn;
		boardQueueOut = _boardQueueOut;
		System.out.println("Game Instance Created");
		}

	String intArrayToString(int [] intArray) {
		String convertedString = "";
		for(int i = 0; i < intArray.length; ++i) {
			if(i != 0) convertedString += "_";
			convertedString += Integer.toString(intArray[i]);
		}
		return convertedString;
	}
	
	int [] stringToIntArray(String _string) {
		String[] tokens = _string.split("_");
		int [] convertedIntArray = new int[tokens.length];
		for(int i = 0; i < tokens.length; ++i) {
			convertedIntArray[i] = Integer.parseInt(tokens[i]);
		}
		return convertedIntArray;
	}
	
	int [] createSendPacket() {
		int [] playerTurn = new int[1];
		if(turn == "p1")
			playerTurn[0] = 1;
		else
			playerTurn[0] = 2;
		int [] info = new int[1 + board.length];
		System.arraycopy(playerTurn, 0, info, 0, 1);
		System.arraycopy(board, 0, info, 1, board.length);
		return info;
	}
	public void run () {
		try {
			//Game game = new Game(); //POTENTIAL ERROR
			//Game game = this;
			//ADDED TO  mancalaClient gameGUI = new GameUI();
			//gameGUI.displayWelcome(game);
			newGame(0,6,4);
			boardQueueOut.put(new int [] {0,0,6,4});
			
			//gameGUI.GUIHandler(game, 0, 6, 4);
			while (true) {
				//ADDED TO  mancalaClient if((house = gameGUI.waitForClick()) >= 0) {
				int [] incoming = boardQueueIn.take();
				if(incoming[2] == 0) {
					System.out.println("Move - " + incoming[1]);
					move(incoming[1]);
					//ADDED TO  mancalaClient gameGUI.updateBoard(game, board);
					boardQueueOut.put(createSendPacket());
					if (isEmpty()){
						lastMove();
						isOver();
						//gameGUI.updateBoard(game, board);
						boardQueueOut.put(createSendPacket());
						break;
					}
				}
			}
		} catch (InterruptedException ex) {
			//TODO
		}
	}
	int produce() {
		return 1;
	}
	
	// initialize the game
	public void newGame (int playMode, int numHouses, int numSeeds) {
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
		MODE = playMode;
		if (MODE == 1) {
			 ai1 = new AIeasy();
		}
		if (MODE == 2) {
			//TODO ai2 = new AImedium(this);
		}
		if (MODE == 3) {
			//TODO ai3 = new AIhard(this);
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
					//TODO ai1.AImove(this,board);
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
}