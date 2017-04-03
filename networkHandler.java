import java.net.*;
import java.io.*;

public class networkHandler extends Thread {
	ServerSocket serverSocket;
	public static final int PORT = 43594;

	public networkHandler () throws IOException {
		serverSocket = new ServerSocket(PORT);
		serverSocket.setSoTimeout(10000);
	}

	public void run() {
	  while(true) {
	     try {
	        System.out.println("Waiting for client on port " + 
	           serverSocket.getLocalPort() + "...");
	        Socket server = serverSocket.accept();
	        
	        System.out.println("Just connected to " + server.getRemoteSocketAddress());
	        DataInputStream in = new DataInputStream(server.getInputStream());
	        
	        System.out.println(in.readUTF());
	        DataOutputStream out = new DataOutputStream(server.getOutputStream());
	        out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress()
	           + "\nGoodbye!");
	        server.close();
	        
	     } catch(SocketTimeoutException s) {
	        System.out.println("Socket timed out!");
	        break;
	     } catch(IOException e) {
	        e.printStackTrace();
	        break;
	     }
	  }
	}

	public static void main (String [] args) {
		boolean exit = false;
		try {
			Thread t = new networkHandler();
			t.start();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}