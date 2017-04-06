import java.util.*;

public class AIhard {
	private Game game;
	private int NUMHOUSES;
	private int NUMSEEDS;
	private int boardsize;
	private int kalah1;
	private int kalah2;

	private int depth = 4; 
	private int endP;
	
	public AIhard (Game game) {
		this.game = game;
		NUMHOUSES = game.getNumHouses();
		NUMSEEDS = game.getNumSeeds();
		boardsize = NUMHOUSES*2+2;
		kalah1 = NUMHOUSES;
		kalah2 = boardsize - 1;
		endP = -1;
	}
	
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
				for (int i = position+1; i <= kalah2-1; ++i) {
					tempBoard[i] += 1;
				}
				numSeeds = numSeeds - (kalah2 - 1 - position);
				int numLoops = numSeeds / (boardsize - 1);
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
				for (int i = position+1; i<= kalah2; ++i) {
					tempBoard[i] += 1;
				}
				numSeeds = numSeeds - (kalah2-position);
				int numLoops = numSeeds/(boardsize-1);
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
	
	
	
	/******* GENERATE POSSIBLE MOVES ***************/
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
	private int evaluateMove (int[] board, boolean isComputer) {
		int heuristicPoint = 0;
		int [] tempBoard = Arrays.copyOf(board,boardsize);
		
		if (isComputer) {
			heuristicPoint = tempBoard[6] - tempBoard[13];
		
		}
		else {
			heuristicPoint = tempBoard[13] - tempBoard[6];
		}
		return heuristicPoint;
	}
	/******* DONE ***************/
	
	
	/******* MINIMAX RECURSION WITH PRUNNING ***************/
	private int minimax (int[] board, int movePosition, int depth, boolean isComputer, int alpha, int beta) {
		// int bestValue = 0;
		int currValue;
		int[] tempBoard = Arrays.copyOf(board,boardsize);
		
		// if (isComputer) {
			// bestValue = Integer.MIN_VALUE;
		// }
		// else {
			// bestValue = Integer.MAX_VALUE;
		// }
		
		if (depth == 0 || generatePossibleMoves(board,isComputer).length == 0) {
			int temp = evaluateMove(board,isComputer);
			return temp;
		}
		else if (isComputer) {
            for (int i : generatePossibleMoves(board,true)) {
				tempBoard = boardMove(tempBoard,movePosition,true);
                currValue = minimax(tempBoard, i, depth-1, false, alpha, beta);
                // bestValue = Math.max(bestValue, currValue);
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
                currValue = minimax(tempBoard, i, depth-1, true, alpha, beta);
                // bestValue = Math.min(bestValue, currValue);
                beta = Math.min(beta, currValue);
				if (beta <= alpha) {
					break;
				}
            }
			return beta;
		}
		// return bestValue;
	}
	/******* DONE ***************/
	
	
	
	private int generateBestMove (int[] board) {
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
	
	public void AImove (int [] board) {
		int endPosition;
		
		do {
			if (!game.isEmpty()) {
				int house = generateBestMove(board);
				System.out.println("AI house: " + house);
			
				int numSeeds = game.boardInfo(house);
				endPosition = (house + numSeeds) % boardsize;
				if (numSeeds + house <= kalah2){
					game.disperseSeeds(house+1,house+numSeeds);
				}
				else {
					game.disperseSeeds(house+1,kalah2);
					numSeeds = numSeeds - (kalah2-house);
					int numLoops = numSeeds/ (boardsize - 1);
					while (numLoops > 0) {
						game.disperseSeeds(0,kalah1-1);
						game.disperseSeeds(kalah1+1,kalah2);
						numSeeds -= kalah2;
						--numLoops;
					}
					if (numSeeds < kalah1){
						game.disperseSeeds(0,numSeeds-1);
					}
					else {
						game.disperseSeeds(0,kalah1-1);
						game.disperseSeeds(kalah1 + 1,numSeeds);
					}
				}
				board[house] = 0;
				game.claimSeeds(endPosition);
				for (int i = 0; i < boardsize; ++i) {
					System.out.println("number of Seeds in house " + i + " is: " + board[i]);
				}
			}
			else {
				break;
			}
		} while (endPosition == kalah2);
	}
}