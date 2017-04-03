import java.net.*;
import java.io.*;

public class mancalaClient {

   public static void main(String [] args) {
      String serverName = args[0];
      int PORT = 43594;
      try {
         System.out.println("Connecting to " + serverName + " on port " + PORT);
         Socket client = new Socket(serverName, PORT);
         
         System.out.println("Just connected to " + client.getRemoteSocketAddress());
         OutputStream outToServer = client.getOutputStream();
         DataOutputStream out = new DataOutputStream(outToServer);
         
         out.writeUTF("Hello from " + client.getLocalSocketAddress());
         InputStream inFromServer = client.getInputStream();
         DataInputStream in = new DataInputStream(inFromServer);
         
         System.out.println("Server says " + in.readUTF());
         client.close();
      } catch(IOException e) {
         e.printStackTrace();
      }
   }
}