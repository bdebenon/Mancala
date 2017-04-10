import java.util.*;
import java.util.concurrent.BlockingQueue;

/******* AI - hard level ********/
/*********************************

Implement AI using the minimax method
The level of depth is 4
AI decides next move after optimizing 2 opposite player's move

*********************************/

public class AIhard implements Runnable {
	
	private final Game game;
	private final BlockingQueue<String> boardQueueIn;
	private final BlockingQueue<String> boardQueueOut;
	private int NUMHOUSES;
	private int NUMSEEDS;
	private int boardsize;
	private int kalah1;
	private int kalah2;
	private int [] board;

	private int depth = 4; 
	private int endP;
	
	public AIhard (Game _game, BlockingQueue<String> _boardQueueIn, BlockingQueue<String> _boardQueueOut) {
		game = _game;
		boardQueueIn = _boardQueueIn;
		boardQueueOut = _boardQueueOut;
		NUMHOUSES = game.getNumHouses();
		NUMSEEDS = game.getNumSeeds();
		boardsize = NUMHOUSES*2+2;
		kalah1 = NUMHOUSES;
		kalah2 = boardsize - 1;
		endP = -1;
	}
	
	/** this function replicates the move function in Game.java
	it is used to calculate the board state after 2 AI and 2 human moves 
	it moves around the board follows kalah rule and claim any appropriate seeds  ***/
	
	private int[] boardMove (int[] board, int position, boolean isComputer) {
		int[] tempBoard = Arrays.copyOf(board,boardsize);
		int numSeeds = tempBoard[position];
		endP = (position + numSeeds) % boardsize;
		if (!isComputer) {
			if (numSeeds + position <= (kalah2 - 1)){
				for (int i = position+1; i<= position+numSeeds; ++i) {
					tempBoard[i] += 1;
				}
			}
			else {
				int numPasses = (numSeeds + position) / (boardsize-1);
				for (int i = position+1; i <= kalah2-1; ++i) {
					tempBoard[i] += 1;
				}
				numSeeds = numSeeds - (kalah2 - 1 - position);
				int numLoops = numSeeds / (boardsize - 1);
				endP = endP + numPasses;
				while (numLoops > 0) {
					for (int i = 0; i<= kalah2-1; ++i) {
						tempBoard[i] += 1;
					}
					--numLoops;
				}
				for (int i = 0; i<= numSeeds % (boardsize-1) - 1; ++i) {
					tempBoard[i] += 1;
				}
			}
		}
		else {
			if (numSeeds + position <= kalah2){
				for (int i = position+1; i<= position+numSeeds; ++i) {
					tempBoard[i] += 1;
				}
			}
			else {
				int numPasses = (numSeeds + position) / (boardsize-1);
				for (int i = position+1; i<= kalah2; ++i) {
					tempBoard[i] += 1;
				}
				numSeeds = numSeeds - (kalah2-position);
				int numLoops = numSeeds/(boardsize-1);
				endP = endP + numPasses;
				while (numLoops > 0) {
					for (int i = 0; i<= kalah1-1; ++i) {
						tempBoard[i] += 1;
					}
					for (int i = kalah1+1; i<= kalah2; ++i) {
						tempBoard[i] += 1;
					}
					numSeeds -= kalah2;
					--numLoops;
				}
				if (numSeeds < kalah1){
					for (int i = 0; i<= numSeeds-1; ++i) {
						tempBoard[i] += 1;
					}
				}
				else {
					for (int i = 0; i<= kalah1-1; ++i) {
						tempBoard[i] += 1;
					}
					for (int i = kalah1+1; i<= numSeeds; ++i) {
						tempBoard[i] += 1;
					}
				}
			}
		}
		tempBoard[position] = 0;
		
		int oppositePosition = (NUMHOUSES*2) - endP;
		if (!isComputer && endP >= 0 && endP < kalah1 && tempBoard[endP] == 1 && tempBoard[oppositePosition] != 0){
			tempBoard[kalah1] += tempBoard[endP] + tempBoard[oppositePosition];
			tempBoard[endP] = 0;
			tempBoard[oppositePosition] = 0;
		}
		if (isComputer && endP >= (kalah1 + 1) && endP < kalah2 && tempBoard[endP] == 1 && tempBoard[oppositePosition] != 0){
			tempBoard[kalah2] += tempBoard[endP] + tempBoard[oppositePosition];
			tempBoard[endP] = 0;
			tempBoard[oppositePosition] = 0;
		}
		return tempBoard;
	}
	/******* DONE ***************/
	
	
	/******* GENERATE POSSIBLE MOVES ***************/
	// find allowed moves at a given level
	private int[] generatePossibleMoves (int[] board, boolean isComputer) {
		List<Integer> pMoves = new ArrayList<Integer>();
		if (!isComputer) {  
			for (int i = 0; i < kalah1; ++i) {
				if (board[i] > 0) {
					pMoves.add(i);
				}
			}
		}
		else {
			for (int i = kalah1+1; i < kalah2; ++i) {
				if (board[i] > 0) {
					pMoves.add(i);
				}
			}
		}
		int[] possibleMoves = new int[pMoves.size()];
		for (int i = 0; i < pMoves.size(); ++i) {
			possibleMoves[i] = pMoves.get(i);
		}
		return possibleMoves;
	}
	/******* DONE ***************/
	
	
	
	/******* EVALUATE MOVES ***************/
	// evaluate the move by finding its heuristic value 
	private int evaluateMove (int[] board, boolean isComputer) {
		int heuristicPoint = 0;
		int [] tempBoard = Arrays.copyOf(board,boardsize);
		
		if (isComputer) {
			heuristicPoint = tempBoard[kalah1] - tempBoard[kalah2];
		
		}
		else {
			heuristicPoint = tempBoard[kalah2] - tempBoard[kalah1];
		}
		return heuristicPoint;
	}
	/******* DONE ***************/
	
	
	/******* MINIMAX RECURSION WITH PRUNNING ***************/
	// Iterate through all possible moves that AI and human can take at depth level 4 
	// Calculate the heuristic value for each move 
	// Goal is to maximize AI's move, minimze human's move
	// Alpha - beta is used to reduced the amount of workload 
	
	private int minimax (int[] board, int movePosition, int depth, boolean isComputer, int alpha, int beta) {
		int currValue;
		int[] tempBoard = Arrays.copyOf(board,boardsize);
		
		if (isComputer) {
		}
		else {
		}
		
		if (depth == 0 || generatePossibleMoves(board,isComputer).length == 0) {
			int temp = evaluateMove(board,isComputer);
			return temp;
		}
		else if (isComputer) {
            for (int i : generatePossibleMoves(board,true)) {
				tempBoard = boardMove(tempBoard,movePosition,true);

				int tempVal = -100;
				int targetMove = -1;
				
				while (endP == kalah2) {
					int[] pos = generatePossibleMoves(tempBoard,true);
					
					for (int j : pos) {
						int[] ttBoard = Arrays.copyOf(tempBoard,boardsize);
						ttBoard = boardMove(ttBoard,j,true);
						if (ttBoard[kalah2] > tempVal) {
							tempVal = ttBoard[kalah2];
							targetMove = j;
						}
					}
					if (targetMove == -1) {
						break;
					}
					if (targetMove != -1) {
						tempBoard = boardMove(tempBoard,targetMove,true);
					}
				}
				
                currValue = minimax(tempBoard, i, depth-1, false, alpha, beta);
                alpha = Math.max(alpha, currValue);
				if (beta <= alpha) {
					break;
				}
            }
			return alpha;
		}
		else {
            for (int i : generatePossibleMoves(board,false)) {
				tempBoard = boardMove(tempBoard,movePosition,false);
				int tempVal = 100;
				int targetMove = -1;
				while (endP == kalah1) {
					int[] pos = generatePossibleMoves(tempBoard,true);
					for (int j : pos) {
						int[] ttBoard = Arrays.copyOf(tempBoard,boardsize);
						ttBoard = boardMove(ttBoard,j,true);
						if (ttBoard[kalah1] < tempVal) {
							tempVal = ttBoard[kalah2];
							targetMove = j;
						}
					}
					if (targetMove == -1) {
						break;
					}
					if (targetMove != -1) {
						tempBoard = boardMove(tempBoard,targetMove,true);
					}
				}
				
				currValue = minimax(tempBoard, i, depth-1, true, alpha, beta);
                beta = Math.min(beta, currValue);
				if (beta <= alpha) {
					break;
				}
            }
			return beta;
		}
	}
	/******* DONE ***************/
	
	
	/********** GENERATE BEST MOVE ***************/
	// Generate the best possible move for AI 
	// First, test to see whether there is a move that is a winning move
	// Winning move = move that make kalah2 has more than half of the amount of total seeds
	// If there is, perform the move 
	// If there is not, run minimax to find move
	
	private int generateBestMove () {
		int bestMove = -1;
		int mmVal;
		int bestValue = Integer.MIN_VALUE;
		int lowestValue = Integer.MAX_VALUE;
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		int totalSeeds = NUMHOUSES * NUMSEEDS;
		int[] pMoves = generatePossibleMoves(board,true);
		int[] bestMoveBoard = Arrays.copyOf(board,boardsize);
		
		for (int i : pMoves) {
			int[] tempBoard = boardMove(bestMoveBoard,i,true);
			if (tempBoard[kalah2] > totalSeeds) {
				bestMove = i;
				return bestMove;
			}
			System.arraycopy(board,0,bestMoveBoard,0,board.length);
		}
		
		for (int m : pMoves) {
			mmVal = minimax (bestMoveBoard, m, this.depth, true, alpha, beta);
			
			if (this.depth%2 == 1) {
				if (mmVal > bestValue) {
					bestValue = mmVal;
					bestMove = m;
				}
			}
			else {
				if (mmVal < lowestValue) {
					lowestValue = mmVal;
					bestMove = m;
				}
			}
		}
		return bestMove;
	}
	/***************** DONE ******************/

	@Override
	public void run() {
		boolean stop = false;
		while (!stop) {
			String input;
			String [] info;
			try {
				input = boardQueueIn.take();
				info = input.split("_");
				System.out.println("AI Command Recieved: " + input);
				switch(info[0]) {
				case "MOVE": //Updated Board Sent With No Turn Change
					String [] _board = new String [info.length-2];
					for(int i = 2; i < info.length; ++i) {
						_board[i-2] = info[i];
					}
					board = new int [_board.length];
					for(int i = 0; i < board.length; ++i) {
						board[i] = Integer.parseInt(_board[i]);
					}
					break;
				case "AITURN":
					int move = generateBestMove();
					boardQueueOut.put("MOVE_2_" + move);
					break;
				}
			} catch (InterruptedException e) {
				stop = true;
				e.printStackTrace();
			}
		}		
	}
}