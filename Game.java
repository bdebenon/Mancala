import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;

		
public class Game implements Runnable {
	//GAME
	public String turn;   // p1 (player 1) or p2 (player 2)
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
	private boolean isRandom;
	private boolean Pie = false;
	private boolean LOCALGAME;
	private boolean trueTwoPlayer;
	private boolean firstPlayerTwoTurn;
	final private ServerSocket serverSocket;
	private int PORT;
	private Socket p2Server;
	private Thread _secondPlayer = null, _networkInput = null, _networkOutput = null;
	
	private final BlockingQueue<String> boardQueueIn;
	private final BlockingQueue<String> boardQueueOut;
	private BlockingQueue<String> boardQueueIn2;
	private BlockingQueue<String> boardQueueOut2;
	
	public Game(BlockingQueue<String> _boardQueueIn, BlockingQueue<String> _boardQueueOut, ServerSocket _serverSocket) {
		boardQueueIn = _boardQueueIn;
		boardQueueOut = _boardQueueOut;
		boardQueueIn2 = new SynchronousQueue<String>();
		boardQueueOut2 = new SynchronousQueue<String>();
		serverSocket = _serverSocket;
		System.out.println("Game Instance Created");
		trueTwoPlayer = false;
		turn = "p1";
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
	
	String createUpdatePacket() {
		String turnInformation = new String("");
		turnInformation += "MOVE";
		if(turn == "p1")
			turnInformation += "_p1";
		else
			turnInformation += "_p2";
		for(int i = 0; i < board.length; ++i) {
			turnInformation += "_";
			turnInformation += Integer.toString(board[i]);
		}
		return turnInformation;
	}
	public void run () {
		boolean stop = false;
		while (!stop) {
			try {
				if(turn.equals("p2") && firstPlayerTwoTurn == true && trueTwoPlayer == true) {
					firstPlayerTwoTurn = false;
					boardQueueOut2.put("ACK_PIE");
				}
				String [] incoming;
				String input;
				if(turn == "p1") 
					input = boardQueueIn.take();
				else
					input = boardQueueIn2.take();
				incoming = input.split("_");
				System.out.println("Command Received: " + input);
				switch(incoming[0]) {
				case "ACK":
					switch(incoming[1]) {
					case "READY":
						boardQueueOut.put("ACK_BEGIN_" + turn);
						break;
					case "OKie":
						boardQueueOut.put("ACK_WELCOME");
						break;
					case "OK":
						break;
					case "GAMEINFO":
						newGame(Integer.parseInt(incoming[2]), Integer.parseInt(incoming[3]), Integer.parseInt(incoming[4]), Integer.parseInt(incoming[5]), Integer.parseInt(incoming[6]));
						boardQueueOut.put(createUpdatePacket());
						break;
					}
					break;
				case "MOVE":
						move(Integer.parseInt(incoming[2]));
						if (isEmpty()){
							lastMove();
							isOver();
						}
					break;
				case "SELECTION":
					//TODO
					//boardQueueOut2.put("QUIT");
					//_secondPlayer.interrupt();
					//_networkInput.interrupt();
					//_networkOutput.interrupt();
					//serverSocket.close();
					break;
				case "PIE":
					if(incoming[1].equals("ACCEPT"))
						pieRule();
					boardQueueOut.put(createUpdatePacket());
					boardQueueOut2.put(createUpdatePacket());
					break;
				case "OPTIONS":
					break;
				case "EXIT":
					System.out.println("Client Terminated Game.");
					throw new InterruptedException("Client Disconnected");
				}
			} catch (InterruptedException | NumberFormatException | IOException ex) {
			stop = true;
			try {
				serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
	}
	
	
	
	/********************CONSTRUCTOR ********************/
	// initialize the game with the play mode, number of houses, number of seeds, is randomized or not, and is local or not
	public void newGame (int playMode, int numHouses, int numSeeds, int isRand, int isLOCAL) throws IOException, InterruptedException {
		NUMSEEDS = numSeeds;
		NUMHOUSES = numHouses;
		boardSize = NUMHOUSES * 2 + 2;
		firstPlayerTwoTurn = true;
		boardQueueIn.clear();
		boardQueueOut.clear();
		boardQueueIn2.clear();
		boardQueueOut2.clear();
		if (isRand == 0) 
			isRandom = false;
		else
			isRandom = true;
		
		if(isLOCAL == 0)
			LOCALGAME = false;
		else 
			LOCALGAME = true;
		board = new int[boardSize];
		kalahPosition1 = NUMHOUSES;
		kalahPosition2 = boardSize - 1;
		if (!isRandom) {
			for (int i = 0; i < boardSize; ++i) {
				if (i== kalahPosition1 || i == kalahPosition2){
					board[i] = 0;
				}
				else {
					board[i] = NUMSEEDS;
				}
			}
		}
		else {
			Random rand = new Random();
			int seedsLeft = NUMSEEDS * NUMHOUSES;
			for (int i = 0; i < NUMHOUSES; i++) {
				int current = rand.nextInt(seedsLeft);
				board[i] = current;
				board[i+NUMHOUSES+1] = current;
				seedsLeft -= current;
			}
			board[kalahPosition1] = 0;
			board[kalahPosition2] = 0;
		}
		MODE = playMode;
		if(MODE == 0) {
			if(LOCALGAME == true) {
				//TODO
			} else {
				System.out.println("Waiting for Player 2 Connection...");
				p2Server = serverSocket.accept();
				System.out.println("Just connected to " + p2Server.getRemoteSocketAddress());
				_networkInput = new Thread(new Network(boardQueueIn2, p2Server, 0));
				_networkOutput = new Thread(new Network(boardQueueOut2, p2Server, 1));
				_networkInput.start();
				_networkOutput.start();
				boardQueueOut2.put("INFO_" + MODE + "_" + NUMHOUSES + "_" + NUMSEEDS);
				boardQueueOut2.put(createUpdatePacket());
				trueTwoPlayer = true;
			}
		}
		if (MODE == 1) {
			 _secondPlayer = new Thread(new AIeasy(this, boardQueueOut2, boardQueueIn2));
			 _secondPlayer.start();
		}
		if (MODE == 2) {
			_secondPlayer = new Thread(new AImedium(this, boardQueueOut2, boardQueueIn2));
			_secondPlayer.start();
		}
		if (MODE == 3) {
			_secondPlayer = new Thread(new AIhard(this, boardQueueOut2, boardQueueIn2));
			_secondPlayer.start();
		}
		turn = "p1";
	}
	/******************** DONE ********************/
	
	
	
	/******************* HELPER GET/SET FUNCTIONS **********/
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
	
	public boolean getPie () {
		return Pie;
	}
	
	public void setPie (boolean pie) {
		Pie = pie;
	}
	
	public void pieRule () {
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
	
	/*********** END OF HELPER FUNCTIONS **************/
	
	
	
	/********** CHECK MOVE VALIDATION ***************/
	// player 1 can only chooses the bottom row
	// player 2 can only chooses the top row 
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
	/************* DONE ********************/
	
	
	//********** CHECK AND DECLARE WINNER **********/
	// if kalah 2 has more seeds than kalah 1 => player 2 wins
	// if kalah 1 has more seeds than kalah 2 => player 1 wins 
	// otherwise, it is a tie
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
	/**************** DONE **********************/
	
	
	
	/************* Game Over *******************/
	// Check to see whether the game is over 
	public boolean isOver () throws InterruptedException {
		String result = checkWinner();
		boardQueueOut.put(createUpdatePacket());
		if(trueTwoPlayer == true)
			boardQueueOut2.put(createUpdatePacket());
		if (!result.equals("none")) {
			if (result.equals("tie")){
				boardQueueOut.put("ACK_TIE");
			}
			else {
				if(result.equals("p1")) {
				boardQueueOut.put("ACK_WINNER");	
				} else {
				boardQueueOut.put("ACK_LOSER");
				}
			}
			return true;
		}
		return false;
	}
	/**************** DONE **********************/
	
	
	
	
	/**************** EMPTY CHECKING **********************/
	// check if ALL houses in either board side are empty
	// this functions help determine whether the game is over or not 
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
	/**************** DONE **********************/
	
	
	/**************** LAST MOVE **********************/
	// if the game is over, the lastMove() function collects the seeds into appropriate kalahs and clean up the board
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
	}
	/**************** DONE **********************/
	
	
	/**************** CLAIM SEEDS **********************/
	// if the last seed falls into an empty house in the current player side 
	// and if the opposite house has seeds 
	// this function collect seeds in those 2 houses into the current player's kalah 
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
	/**************** DONE **********************/
	
	
	/**************** DISPERSE SEEDS **********************/
	// disperse 1 seed into each house from the start position to the end position 
	public void disperseSeeds (int startPosition, int endPosition) {
		for (int i = startPosition; i <= endPosition; ++i){
			board[i] += 1;
		}
	}
	/**************** DONE **********************/
	
	
	/**************** CHECK TURN  **********************/
	// check to see whose turn is it 
	public void checkTurn (int position) throws InterruptedException{
			if (turn == "p1" && position != kalahPosition1){
				turn = "p2";
			}
			else if (turn == "p2" && position != kalahPosition2) {
				turn = "p1";
			}
			boardQueueOut.put(createUpdatePacket());
			boardQueueOut2.put(createUpdatePacket());
			if(turn == "p2" && !trueTwoPlayer)
				boardQueueOut2.put("AITURN");
	}
	/**************** DONE **********************/
	
	
	/**************** MOVE FUNCTION **********************/
	// this function first checks whether a position is valid for the current player 
	// if it is, it calls disperseSeeds() function to disperse seeds 
	// then it collects seeds (if any), check turn, and switch player 
	public void move (int position) throws InterruptedException {
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
			claimSeeds(endPosition);
			checkTurn(endPosition);
			boardQueueOut.put(createUpdatePacket());
			if(trueTwoPlayer == true)
				boardQueueOut2.put(createUpdatePacket());
		}
		else {
			System.out.println("Invalid position");
			if(turn.equals("p2") && !trueTwoPlayer) {
				boardQueueOut2.put(createUpdatePacket());
				boardQueueOut2.put("AITURN");
			}
			boardQueueOut.put("ACK_ILLEGAL");
		}
	}
	/**************** DONE **********************/
	
	void findNewConnection() {
		//TODO
	}
}
