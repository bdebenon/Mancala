public class AIeasy {
	private Game game;
	void AImove (Game game, int [] board) {
		this.game = game;
		int endPosition = 0;
		int NUMHOUSES = game.getNumHouses();
		int NUMSEEDS = game.getNumSeeds();
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
			} while (endPosition == kalah2);
	}
}