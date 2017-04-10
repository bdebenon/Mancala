import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class Network implements Runnable {
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
				stop = true;
				e.printStackTrace();
			}
		}
	}
}