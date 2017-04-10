import java.util.concurrent.BlockingQueue;

/******* AI - easy level ********/
/*********************************

A simple way to get started on human - AI mode
Next move for AI is randomly decided 

*********************************/

public class AIeasy implements Runnable {
	private final Game game;
	private final BlockingQueue<String> boardQueueIn;
	private final BlockingQueue<String> boardQueueOut;
	private int [] board;
	public AIeasy (Game _game, BlockingQueue<String> _boardQueueIn, BlockingQueue<String> _boardQueueOut) {
		game = _game;
		boardQueueIn = _boardQueueIn;
		boardQueueOut = _boardQueueOut;
		
	}
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
					int NUMHOUSES = game.getNumHouses();
					boolean validMove = false;
					int move = 0;
					while(!validMove) {
						move = (int) (Math.random() * NUMHOUSES + NUMHOUSES + 1);
						if(board[move] != 0)
							validMove = true;
					}
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