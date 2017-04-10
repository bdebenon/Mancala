import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;


public class Server {
	public boolean severMODE = true; //CHANGE THIS FOR SERVER VS LOCAL MODES
	static ServerSocket serverSocket;
	static Socket server;
	public static final int PORT = 43594; //Requires PORT and PORT+1 to run successfully with two-player
	
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
				Game game = new Game(boardQueueIn, boardQueueOut, serverSocket);
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
