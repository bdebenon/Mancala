import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
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
		try {
			while(true) {
				if(mode == 0) {
					handleInput();
				} else {
					handleOutput();
				}
			}
	     } catch(SocketTimeoutException s) {
		        System.out.println("Socket timed out!");
		     } catch(IOException e) {
		        e.printStackTrace();
		     } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}

public class Server {
	public boolean severMODE = true; //CHANGE THIS FOR SERVER VS LOCAL MODES
	static ServerSocket serverSocket;
	static Socket server;
	public static final int PORT = 43594;
	
	public static void main(String[] args) {
		BlockingQueue<String> boardQueueIn = new SynchronousQueue<String>();
		BlockingQueue<String> boardQueueOut = new SynchronousQueue<String>();
		try {
			serverSocket = new ServerSocket(PORT);
	        System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
			server = serverSocket.accept();
			System.out.println("Just connected to " + server.getRemoteSocketAddress());
			Game game = new Game(boardQueueIn, boardQueueOut);
			Network networkInput = new Network(boardQueueIn, server, 0);
			Network networkOutput = new Network(boardQueueOut, server, 1);
			new Thread(game).start();
			new Thread(networkInput).start();
			new Thread(networkOutput).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
