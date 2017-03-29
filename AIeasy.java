public class AIeasy {
	private Game game;
	void AImove (Game game, int [] board) {
		this.game = game;
		int endPosition = 0;
		int NUMHOUSES = game.getNumHouses();
		int NUMPEBBLES = game.getNumPebbles();
		int boardsize = NUMHOUSES*2+2;
		int kalah1 = NUMHOUSES;
		int kalah2 = boardsize - 1;
		
		do {
			if (!game.isEmpty()) {
				int house;
				do {
					house = (int) (Math.random() * NUMHOUSES + kalah1 + 1);
				} while (game.boardInfo(house) == 0); 
				System.out.println("AI house: " + house);
			
				int numPebbles = game.boardInfo(house);
				endPosition = (house + numPebbles) % boardsize;
				if (numPebbles + house <= kalah2){
					game.dispersePebbles(house+1,house+numPebbles);
				}
				else {
					game.dispersePebbles(house+1,kalah2);
					numPebbles = numPebbles - (kalah2-house);
					int numLoops = numPebbles/ (boardsize - 1);
					while (numLoops > 0) {
						game.dispersePebbles(0,kalah1-1);
						game.dispersePebbles(kalah1+1,kalah2);
						numPebbles -= kalah2;
						--numLoops;
					}
					if (numPebbles < kalah1){
						game.dispersePebbles(0,numPebbles-1);
					}
					else {
						game.dispersePebbles(0,kalah1-1);
						game.dispersePebbles(kalah1 + 1,numPebbles);
					}
				}
				board[house] = 0;
				game.claimPebbles(endPosition);
				for (int i = 0; i < boardsize; ++i) {
					System.out.println("number of pebbles in house " + i + " is: " + board[i]);
				}
				}
			} while (endPosition == kalah2);
	}
}