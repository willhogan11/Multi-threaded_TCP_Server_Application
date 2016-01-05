import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;


public class EchoServer {
	
	public static void main(String[] args) throws Exception {
		ServerSocket m_ServerSocket = new ServerSocket(9999); // Open port on 2004, waiting for connections
		int id = 0;
		while (true) { // Runs while true
			Socket clientSocket = m_ServerSocket.accept();
			ClientServiceThread cliThread = new ClientServiceThread(clientSocket, id++); // This allows the socket to be passed to another thread, relieving the Server Socket Listener
			cliThread.start();
		}
	}
} // End EchoServer



//////////////////////////////////////////////////////////////////////////////////////////////////////////



class ClientServiceThread extends Thread 
{
	  private Socket clientSocket;
	  private String message;
	  private String username;
	  private String password;
	  private int clientID = -1;
	  private boolean running = true;
	  private ObjectOutputStream out;
	  private ObjectInputStream in;
	  private Map<String, Integer> map;
	  private AuthenticationParser ap;
	  private boolean loggedIn;
	  int passToString;
	
	  
	  // Constructor
	  public ClientServiceThread(Socket s, int i) {
	    clientSocket = s;
	    clientID = i;
	  }
	  
	
	    // Sends the stream to the client
	    public void sendMessage(String msg)
		{
			try{
				out.writeObject(msg); // 
				out.flush();
				System.out.println("client> " + msg); // Print out to the screen
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	    
		public void sendLoginDetails(String userName, String password){
			try 
			{
				out.writeObject(userName);
				out.writeObject(password);
				out.flush();
				System.out.println("client> " + userName + " " + password); // Print out to the screen
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	    
	    
	  public void run() {
	    System.out.println("Accepted Client : ID - " + clientID + " : Address - "
	        + clientSocket.getInetAddress().getHostName()); // Gets Name and IP address of that domain
	    try 
	    {
	    	out = new ObjectOutputStream(clientSocket.getOutputStream()); // Writes back out
			out.flush();
			in = new ObjectInputStream(clientSocket.getInputStream()); // Gets stuff in
	/*		System.out.println("Accepted Client : ID - " + clientID + " : Address - " // Message to inform client that message successful
			        + clientSocket.getInetAddress().getHostName());*/
			
			sendMessage("Connection successful");
			do{
				try
				{
					ap = new AuthenticationParser();
					ap.parse(loggedIn);
					
					message = (String)in.readObject();
					username = (String)in.readObject(); // Reading in the message thats coming across the internet
					password = (String)in.readObject(); // Reading in the message thats coming across the internet
					
					int passToString = Integer.parseInt(password);
					
					for(Map.Entry<String, Integer> entry: map.entrySet()) {
						String key = entry.getKey();
						Integer value = entry.getValue();
						
						if(key.equals(username) && value == passToString){
							System.out.println("Welcome " + username);
							sendMessage("Authentication successful, Welcome " + username);
							loggedIn = true;
						}
					}
					if(!loggedIn)
						System.out.println("User not recognised");
						sendMessage("User not recognised");
					
					
					
					// System.out.println("client>"+clientID+"  " + message);
					
					
					
					// sendLoginDetails(userName, password);
					// sendMessage(message);
					
					// System.out.println("\n");
					
				    // System.out.println("client>" + userName + " " +  password);
					
				}
				catch(ClassNotFoundException classnot){
					System.err.println("Data received in unknown format");
				}
	    	}while(!message.equals("bye"));
	      
			System.out.println("Ending Client : ID - " + clientID + " : Address - "
			        + clientSocket.getInetAddress().getHostName());
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
}
