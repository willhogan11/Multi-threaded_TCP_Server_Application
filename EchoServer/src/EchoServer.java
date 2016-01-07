import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


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
	  private Map<String, Integer> map = new HashMap<String, Integer>();
	  private AuthenticationParser ap;
	  private boolean loggedIn = false;
	  private int passToString;
	  private String currentDirectory;
	  private String usersURL = "";
  	  private FileClass fileClass = new FileClass();
  	  private Path rootURL = Paths.get("C:\\Users\\william\\Desktop\\ProjectUsers\\");
  	  private String str;
	
	  
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
				System.out.println("Server> " + msg); // Print out to the screen
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	    
	    public void sendLoggedIn(boolean loggedIn){
	    	try {
				out.writeObject(loggedIn);
				out.flush();
				// System.out.println("client>" + loggedIn);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	    
		public void sendLoginDetails(String username, int password){
			try 
			{
				out.writeObject(username);
				out.writeObject(password);
				out.flush();
				// System.out.println("client> " + username + " " + password); // Print out to the screen
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
					ap.parse(map, loggedIn);
					
					// Reading in the Objects that are coming across the internet				
					message = (String)in.readObject();
					username = (String)in.readObject(); 
					password = (String)in.readObject(); 
					
					Integer passwordToInt = Integer.valueOf(password);
					
					for(Map.Entry<String, Integer> entry: map.entrySet()) 
					{
						String key = entry.getKey();
						Integer value = entry.getValue();
						
						if( (key.equals(username)) && (value.equals(passwordToInt) ) )
						{
							sendMessage("Authentication successful for user " + username);
							loggedIn = true;
							
							message = (String)in.readObject();
							
							Integer choiceToInt = Integer.valueOf(message);
							
							String resultOutput = ("\nCurrent Directory is: " + usersURL + "\nContents of User " + username + "'s folder:");
							String file = "";
							
					    	// do{
						    	switch(choiceToInt){
							    	case 1:
							    		
							    	case 2:
							    		fileClass.makeDirectory();
							    		break;
							    	case 3:
							    		if(username.equals("will")){
							    			usersURL = ("Current Directory is: " + rootURL + "\\" + username);
							    			sendMessage(usersURL);
							    		    file = fileClass.listDirectory(username, str);
							    		    sendMessage("List of Files in this directory: " + file);
							    		}
							    		else if(username.equals("mike")){
							    			usersURL = ("Current Directory is: " + rootURL + "\\" + username);
							    			sendMessage(usersURL);
							    		    file = fileClass.listDirectory(username, str);
							    		    sendMessage("\nList of Files in this directory: " + file);
							    		}
							    		else if(username.equals("mark")){
							    			usersURL = ("Current Directory is: " + rootURL + "\\" + username);
							    			sendMessage(usersURL);
							    		    file = fileClass.listDirectory(username, str);
							    		    sendMessage("\nList of Files in this directory: " + file);
							    		}
							    		else if(username.equals("john")){
							    			usersURL = ("Current Directory is: " + rootURL + "\\" + username);
							    			sendMessage(usersURL);
							    		    file = fileClass.listDirectory(username, str);
							    		    sendMessage("\nList of Files in this directory: " + file);
							    		}
							    		break;

							    	case 4:
							    		break;
							    	case 5:
							    		System.exit(0);
							    		break;
									default:
										System.out.println("Enter valid number");
						    	}
					    	// }while(choiceToInt != 5);
							
							message = (String)in.readObject();
							
						}
					}
					if(!loggedIn)
					{
						sendMessage("User not recognised");
					}
					
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


	public void sendMessage(FileClass file) {
		
	}
}
