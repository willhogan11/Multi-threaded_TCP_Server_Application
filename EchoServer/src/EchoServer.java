import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
	  // Instance Variables
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
  	  private final StringBuffer responseBuffer;
  	  private final StringBuffer requestBuffer;
	
	  
	  // Constructor
	  public ClientServiceThread(Socket s, int i) {
	    clientSocket = s;
	    clientID = i;
	    responseBuffer = new StringBuffer(); //  new buffer object declaration
	    requestBuffer = new StringBuffer(); // new buffer object declaration
	  }
	  
	
	    // Sends the stream to the client
	    public void sendMessage(String msg)
		{
			try{
				out.writeObject(msg); //(responseBuffer.toString()); responseBuffer.delete(0, responseBuffer.length()); 
				out.flush();
				System.out.println("Server> " + msg); // Print out to the screen
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	    
	    // Sends the stream to the client
	    public void sendMessage(byte[] data, int length)
		{
			try{
				out.write(data, 0, length); //(responseBuffer.toString()); responseBuffer.delete(0, responseBuffer.length()); 
				out.flush(); // Print out to the screen
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	    
	    private void receiveObject(){
	    	try {
	    		requestBuffer.delete(0, requestBuffer.length());// requestBuffer.toString().getBytes();
				requestBuffer.append((String) in.readObject());
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
	    }
	    
		public void sendLoginDetails(String username, int password){
			try 
			{
				out.writeObject(username);
				out.writeObject(password);
				out.flush();
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
					// Populate the HashMap with credentials in Key Valuie pair format
					ap = new AuthenticationParser();
					ap.parse(map, loggedIn);
					
					// Reading in the Objects that are coming across the internet				
					message = (String)in.readObject();
					username = (String)in.readObject(); 
					password = (String)in.readObject(); 
					
					Integer passwordToInt = Integer.valueOf(password);
					
					// Checks the HashMap against the users input, client side and either logs the user in, or blocks the request
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
							String file = "";
					    	// do{
						    	switch(choiceToInt){
							    	case 1: // Copy File from Server
							    		sendRequestedFileToClient();
							    		break;
							    	case 2: // Move a File to the Server
							    		receiveFileFromClient();
							    		break;
							    	case 3: // List all Files in the Directory & Display Root path directory, depending on the user that's logged in
							    		if(choiceToInt == 3){
								    		if(username.equals("will")){
								    			usersURL = ("\nCurrent Directory is: " + rootURL + "\\" + username);
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
							    		}
							    		break;

							    	case 4: // Move Directory
							    		// Not Implemented
							    		break;
							    	case 5: // Make a new Directory
							    		if(choiceToInt == 5){
							    			fileClass.makeDirectory(username);	
							    			sendMessage("Directory successfully created");
							    		}
							    		break;
							    	case 6:
							    		System.exit(0); // Exit
							    		break;
									default:
										System.out.println("Enter valid number");
						    	}
						}
					}
					if(!loggedIn){
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

	  // Receives request from the Client for a file and send the user the file, named Test.txt, again statically declared
	  private synchronized boolean sendRequestedFileToClient() {
			File file = new File(System.getProperty("user.dir") + File.separator + "Test.txt");
			if(file.exists() &&  file.isFile()){
				try(FileInputStream fis = new FileInputStream(file)){
					int readBits;
					byte[] buffer = new byte[4096]; 
					while(true){
						readBits = fis.read(buffer, 0, buffer.length);
						if(readBits == -1){
							break;
						}
						sendMessage(buffer, readBits);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}	
			return false;
	  }
	  
	  // Receives a file sent by the client and places the file into the specified user's folder
	  private boolean receiveFileFromClient() {
			File file = new File(System.getProperty("user.dir") + File.separator + "ProjectUsers\\" + username + File.separator + "FileToUploadToServer.txt");
			if(!file.exists())
			{
				try(FileOutputStream fos = new FileOutputStream(file))
				{
					int readBits;
					byte[] buffer = new byte[4096]; 
					while(true)
					{
						readBits = in.read(buffer, 0, buffer.length);
						if(readBits == -1){
							break;
						}
						fos.write(buffer, 0, readBits);
						String fileList = fileClass.listDirectory(username, str);
		    		    sendMessage("\n\nFile Request Success, See below directory:\n " + fileList);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}
			else
				sendMessage("File Already Exists");
			return false;
		}
}
