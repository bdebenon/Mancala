import java.net.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.io.*;


class clientNetwork implements Runnable {
	private final BlockingQueue<String> boardQueue;
	final Socket server;
	int mode;
	
	clientNetwork(BlockingQueue<String> _boardQueue, Socket _server, int _mode) {
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

public class mancalaClient {

	static String intArrayToString(int [] intArray) {
		String convertedString = "";
		for(int i = 0; i < intArray.length; ++i) {
			if(i != 0) convertedString += "_";
			convertedString += Integer.toString(intArray[i]);
		}
		return convertedString;
	}
	
	static int [] stringToIntArray(String _string) {
		String[] tokens = _string.split("_");
		int [] convertedIntArray = new int[tokens.length];
		for(int i = 0; i < tokens.length; ++i) {
			convertedIntArray[i] = Integer.parseInt(tokens[i]);
		}
		return convertedIntArray;
	}
	
   public static void main(String [] args) {
	   
	  BlockingQueue<String> informationQueueIn = new SynchronousQueue<String>();
	  BlockingQueue<String> informationQueueOut = new SynchronousQueue<String>();
	  String serverName = "0.0.0.0";
	  boolean end = false;
	  int PORT = 43594;
      try {
          GameUI gameGUI = new GameUI(informationQueueIn, informationQueueOut);
    	  System.out.println("Connecting to " + serverName + " on port " + PORT);
    	  Socket client = new Socket(serverName, PORT);
	      System.out.println("Just connected to " + client.getRemoteSocketAddress());
	      clientNetwork clientNetworkInput = new clientNetwork(informationQueueIn, client, 0);
	      clientNetwork clientNetworkOutput = new clientNetwork(informationQueueOut, client, 1);
	      new Thread(gameGUI).start();
	 	  new Thread(clientNetworkInput).start();
	 	  new Thread(clientNetworkOutput).start();
	 	  end = true;
      } catch(IOException e) {
         e.printStackTrace();
      }
   }
}
	