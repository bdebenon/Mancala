import java.util.concurrent.BlockingQueue;

		
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
	private static AIeasy ai1;
	private static AImedium ai2;
	private static AIhard ai3;
	private final BlockingQueue<String> boardQueueIn;
	private final BlockingQueue<String> boardQueueOut;
	
	public Game(BlockingQueue<String> _boardQueueIn, BlockingQueue<String> _boardQueueOut) {
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
	
	String createSendPacket() {
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
		while (true) {
			try {
				String [] incoming = (boardQueueIn.take()).split("_");
				System.out.println("Command Received: " + incoming[0]);
				switch(incoming[0]) {
				case "ACK":
					System.out.println("SubCommand Received: " + incoming[1]);
					switch(incoming[1]) {
					case "READY":
						boardQueueOut.put("ACK_BEGIN");
						System.out.println("Check if sent");
						break;
					case "OK":
						boardQueueOut.put("ACK_WELCOME");
						break;
					case "GAMEINFO":
						newGame(Integer.parseInt(incoming[2]), Integer.parseInt(incoming[3]), Integer.parseInt(incoming[4]));
						break;
					}
					break;
				case "MOVE":
					move(Integer.parseInt(incoming[2]));
					if (isEmpty()){
						lastMove();
						isOver();
						boardQueueOut.put(createSendPacket());
						break;
					}
					break;
				case "SELECTION":
					//TODO
					break;
				case "OPTIONS":
					break;
				case "EXIT:":
					System.out.println("Client Terminated Game.");
					findNewConnection();
					break;
				}
			} catch (InterruptedException ex) {
			//TODO
			}
		}
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
		System.out.println("Play Mode: " + playMode);
		//System.out.println("seeds " + NUMSEEDS + ". Houses: " + NUMHOUSES + ". KalahPosition2: " + kalahPosition2);
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
	
	public void checkTurn (int position) throws InterruptedException{
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
		boardQueueOut.put(createSendPacket());
	}
	
	public void move (int position) throws InterruptedException {
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
			//for (int i = 0; i < boardSize; ++i) {
			//	System.out.println("number of seeds in house " + i + " is: " + board[i]);
			//}
			/********* FOR DEBUGGING ***********/
			checkTurn(endPosition);
		}
		else {
			System.out.println("Invalid position");
			boardQueueOut.put("ACK_ILLEGAL");
		}
	}
	
	void findNewConnection() {
		//TODO
	}
}