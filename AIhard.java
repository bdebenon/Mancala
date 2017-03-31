import java.util.*;

public class AIhard {
	private Game game;
	private int NUMHOUSES;
	private int NUMSEEDS;
	private int boardsize;
	private int kalah1;
	private int kalah2;

	private int[] Board;
	private int[] mostPreviousBoard;
	private int depth = 10;
	private int endPosition;
	
	public AIhard (Game game) {
		this.game = game;
		NUMHOUSES = game.getNumHouses();
		NUMSEEDS = game.getNumSeeds();
		boardsize = NUMHOUSES*2+2;
		kalah1 = NUMHOUSES;
		kalah2 = boardsize - 1;
		endPosition = -1;
	}
	
	private int[] generatePossibleMoves (int[] board, boolean isComputer) {
		List<Integer> pMoves = new ArrayList<Integer>();
		
		int[] currBoard = board;
		if (!isComputer) {  // if human player 
			for (int i = 0; i < kalah1; ++i) {
				if (currBoard[i] > 0) {
					pMoves.add(i);
				}
			}
		}
		else {
			for (int i = kalah1+1; i < kalah2; ++i) {
				if (currBoard[i] > 0) {
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
	
	// private int evaluateMove (int[] board, int movePosition, boolean isComputer) {
	private int evaluateMove (int[] board, boolean isComputer) {
		int heuristicPoint = 0;
		int[] tempBoard = board;
		int totalSeeds1 = 0;
		int totalSeeds2 = 0;

		// tempBoard = boardMove(tempBoard, movePosition, isComputer);
		for (int i = 0; i < kalah1; ++i) {
			totalSeeds1 += tempBoard[i];
		}
		for (int i = kalah1+1; i < kalah2; ++i) {
			totalSeeds2 += tempBoard[i];
		}
		
		if (!isComputer) {
			// heuristicPoint = tempBoard[6] - tempBoard[13] + (totalSeeds1 - totalSeeds2); 
			heuristicPoint = tempBoard[6] - tempBoard[13];
		}
		else {
			// heuristicPoint = tempBoard[13] - tempBoard[6] + (totalSeeds2 - totalSeeds1);
			heuristicPoint = tempBoard[13] - tempBoard[6];
		}
		return heuristicPoint;
	}
	
	private int[] boardMove (int[] board, int position, boolean isComputer) {
		int[] tempBoard = board;
		int numSeeds = tempBoard[position];
		endPosition = (position + numSeeds) % boardsize;
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
				for (int i = 0; i<= numSeeds % (boardsize-1); ++i) {
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
		
		int oppositePosition = (NUMHOUSES*2) - endPosition;
		if (!isComputer && endPosition >= 0 && endPosition < kalah1 && tempBoard[endPosition] == 1 && tempBoard[oppositePosition] != 0){
			tempBoard[kalah1] += tempBoard[endPosition] + tempBoard[oppositePosition];
			tempBoard[endPosition] = 0;
			tempBoard[oppositePosition] = 0;
		}
		if (isComputer && endPosition >= (kalah1 + 1) && endPosition < kalah2 && tempBoard[endPosition] == 1 && tempBoard[oppositePosition] != 0){
			tempBoard[kalah2] += tempBoard[endPosition] + tempBoard[oppositePosition];
			tempBoard[endPosition] = 0;
			tempBoard[oppositePosition] = 0;
		}
		return tempBoard;
	}
	
	private int minimax (int[] board, int movePosition, int depth, boolean isComputer, int alpha, int beta) {
		int bestValue = 0;
		int currValue;
		
		if (isComputer) {
			bestValue = Integer.MIN_VALUE;
		}
		else {
			bestValue = Integer.MAX_VALUE;
		}
		
		if (generatePossibleMoves(board,isComputer).length == 0 || depth == 0) {
			return evaluateMove(board, isComputer);
		}
		if (isComputer) {
			bestValue = Integer.MIN_VALUE;
            for (int i : generatePossibleMoves(board,true)) {
				int[] tempBoard = board;
				tempBoard = boardMove(tempBoard,movePosition,true);
                currValue = minimax(tempBoard, i, depth-1, false, alpha, beta);
                bestValue = Math.max(bestValue, currValue);
            }
		}
		else {
			bestValue = Integer.MAX_VALUE;
            for (int i : generatePossibleMoves(board,false)) {
				int[] tempBoard = board;
				tempBoard = boardMove(tempBoard,movePosition,false);
                currValue = minimax(tempBoard, i, depth-1, true, alpha, beta);
                bestValue = Math.min(bestValue, currValue);
            }
		}
		return bestValue;
	}
	
	private int generateBestMove (int[] board) {
		int bestMove = -1;
		int currVal;
		int bestValue = Integer.MIN_VALUE;
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		int totalSeeds = NUMHOUSES * NUMSEEDS;
		int[] tempBoard = board;
		int[] pMoves = generatePossibleMoves(board,true);
		for (int i : pMoves) {
			tempBoard = boardMove(tempBoard,i,true);
			if (tempBoard[kalah2] > totalSeeds/2) {
				bestMove = i;
				return bestMove;
			}
			tempBoard = board;
		}
		for (int j : pMoves) {
			tempBoard = boardMove(tempBoard,j,true);
			if (endPosition == kalah2) {
				bestMove = j;
				return bestMove;
			}
			tempBoard = board;
		}
		for (int m : pMoves) {
			currVal = minimax (tempBoard, m, this.depth, true, alpha, beta);
			if (currVal > bestValue) {
				bestValue = currVal;
				bestMove = m;
			}
		}
		return bestMove;
	}
	
	public void AImove (int [] board) {
		int[] tempBoard = board;
		if (!game.isEmpty()) {
			int house = generateBestMove(tempBoard);
			System.out.println("AI house: " + house);
			
			int numSeeds = game.boardInfo(house);
			int endPosition = (house + numSeeds) % boardsize;
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
	}
}