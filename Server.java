import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

class Network implements Runnable {
	private final BlockingQueue<String> boardQueue;
	final Socket server;
	int mode;
	
	Network(BlockingQueue<String> _boardQueue, Socket _server, int _mode) {
		boardQueue = _boardQueue;
		server = _server;
		mode = _mode;
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
	
	void handleInput() throws InterruptedException, IOException {
	        DataInputStream in = new DataInputStream(server.getInputStream());
	        boardQueue.put(in.readUTF());
	}
	
	void handleOutput() throws IOException, InterruptedException {
	        DataOutputStream out = new DataOutputStream(server.getOutputStream());
	        out.writeUTF(boardQueue.take());
	}
	
	public void run () {
		boolean stop = false;
		while(!stop) {
			try {
				if(mode == 0) {
					handleInput();
				} else {
					handleOutput();
				}
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				stop = true;
				e.printStackTrace();
			}
		}
	}
}

public class Server {
	public boolean severMODE = true; //CHANGE THIS FOR SERVER VS LOCAL MODES
	static ServerSocket serverSocket;
	static Socket server;
	public static final int PORT = 43594;
	
	public static void main(String[] args) throws IOException {
		BlockingQueue<String> boardQueueIn = new SynchronousQueue<String>();
		BlockingQueue<String> boardQueueOut = new SynchronousQueue<String>();
		serverSocket = new ServerSocket(PORT);
		Thread _game = null, _networkInput = null, _networkOutput = null;
		while(true) {
			try {
		        System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
				server = serverSocket.accept();
				System.out.println("Just connected to " + server.getRemoteSocketAddress());
				Game game = new Game(boardQueueIn, boardQueueOut);
				Network networkInput = new Network(boardQueueIn, server, 0);
				Network networkOutput = new Network(boardQueueOut, server, 1);
				_game = new Thread(game);
				_networkInput = new Thread(networkInput);
				_networkOutput = new Thread(networkOutput);
				_game.start();
				_networkInput.start();
				_networkOutput.start();
				while(_game.isAlive() && _networkInput.isAlive() && _networkOutput.isAlive()){
					//DO NOTHING
				}
				_game.interrupt();
				_networkInput.interrupt();
				_networkOutput.interrupt();
				server.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
