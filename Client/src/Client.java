import java.io.*;
import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class Client {
	
	private Socket requestSocket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String message = "";
	private String ipaddress; // 40.117.147.147
	private int portNumber;
	private Scanner stdin;
	private String username;
	private String password;
	private boolean loggedIn = false;
	private String userLoggedIn;
	private int choice;
	
	public Client(){} // Null Constructor
	
	
	public static void main(String args[])
	{
		Client client = new Client();
		client.run();
	}
	
	
	public void run()
	{
		stdin = new Scanner(System.in);
		try
		{	
			//1. creating a socket to connect to the server
			System.out.println("Please Enter your IP Address"); 
			ipaddress = stdin.next(); // Gets the IP Address
			System.out.println("Please enter the port number");
			portNumber = stdin.nextInt(); // Gets the Port Number
			requestSocket = new Socket(ipaddress, portNumber);
			System.out.println("Connected to " + ipaddress + " in port " + portNumber); // Port number and ip address must match server ip address and port
			
			//2. get Input and Output streams, needed every time a connection happens to send or receive data between client and server
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			
			//3: Communicating with the server
			do
			{
				try
				{	
					message = (String)in.readObject();// Receive incoming Object and cast to a String
					sendMessage(message); // Sent the received message to the Server
					System.out.println("\nWelcome\nPlease Enter your credentials\n"); // Authentication	
					
					do
					{
						try {
							System.out.println("|----------|");
							System.out.println("| USERNAME |");
							System.out.println("|----------|");
							username = stdin.next(); // Takes in Username
							
						} catch (Exception e) {
							System.out.println("String required"); // Catches any exception
						}
						
						try {
							System.out.println("|----------|");
							System.out.println("| PASSWORD |");
							System.out.println("|----------|");
							password = stdin.next(); // Takes in Password
							
							sendLoginDetails(username, password); // Send both username & password to the Server for Authentication 
							message = (String)in.readObject();
							
							// Logs the User in upon Successful Authentication
							if(message.contains("successful")){
								loggedIn = true;
								System.out.println("\nUser [" + username + "] now logged in");
								
								// Provide a Menu for the Client side to choose from
								do {
									System.out.println(
											"\nMenu options:\n1:Copy File from Server\n2:Move a File to the Server\n"
										  + "3:List all Files in the Directory\n4:Move Directory\n5:Make a new Directory\n6:Exit");
									
									Scanner input = new Scanner(System.in);
									choice = input.nextInt(); // Saves the choice as an Integer value
									String choiceTxt = Integer.toString(choice); // Convert to String to send to the Server
									sendMessage(choiceTxt); // Send the choice to the server
									
									/* Requests a Static file from the server called Test.txt */
									if(choice == 1){
										requestFileFromServer(); 
									}
									else if(choice == 2){
										sendFileToServer();
										System.out.println("File Successfully sent to server");
									}
									// Choice 3 Will Display a Directory listing for that User & The Actual Directory Path
									else if(choice == 3){
										message = (String) in.readObject();
										System.out.println(message);
										message = (String) in.readObject();
										System.out.println(message);
									}
									// Choice 5 will Make a new directory within the users folder
									else if(choice == 5){
										System.out.println("Making new Directory....");
										message = (String) in.readObject();
										System.out.println(message);
									}
									else{
										System.out.println("\nLogged Out"); //  Logs user out
										System.exit(0);
									}
									input.close(); // Housekeeping
									
								} while (choice != 6); //  Do this will user doen't enter 6(exit)
								
								in.close();
							}
							else{
								System.out.println("User Not recognised\n"); // If id challenge returns false, blocks user
								loggedIn = false;
							}
							
						} catch (Exception e) {
							System.out.println("Integer required");
						}
						
					}while(!loggedIn); // End do while
					
				}
				catch(ClassNotFoundException classNot)
				{
					System.err.println("data received in unknown format");
				}
			}while(!message.equals("exit"));
			
		}
		catch(UnknownHostException unknownHost)
		{
			System.err.println("You are trying to connect to an unknown host!");
		}
		catch(IOException ioException)
		{
			ioException.printStackTrace();
		}
		finally
		{
			//4: Closing connection
			try
			{
				in.close();
				out.close();
				requestSocket.close();
			}
			catch(IOException ioException)
			{
				ioException.printStackTrace();
			}
		}
	}
	
	public void sendMessage(String msg)
	{
		try
		{
			out.writeObject(msg);
			out.flush();
			System.out.println("client>" + msg);
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
	
	// Sends the users credentials entered to the server for authentication
	public void sendLoginDetails(String username, String password){
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
	
	/* This method reads the requested file, breaks it into bytes and send to the client */
	private boolean requestFileFromServer() {
		File file = new File(System.getProperty("user.dir") + File.separator + "Test.txt"); // Path for file to be put
		if(!file.exists()) // If it isn't there already, do the below
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
					fos.write(buffer, 0, readBits); // Write out the bytes from the FileOutputStream to create the requested file
					System.out.println("File Request Success, See below directory");
					listDirectory(); // Display to the user the contents of THIS, the client side directory
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
			System.err.println("File Already Exists"); // If the file exists, display error and return false
		return false;
	}
	
	
	// Client side ONLY, requests a list of contents
	public File listDirectory(){
	 	File usersDirectory = new File(System.getProperty("user.dir"));
	 	String[] list = (usersDirectory.list());
	 	for(String s: list)
	 		System.out.println(s);
	 	return usersDirectory;
	}    
	
	  /* Sends a file to the server. The file is can be anything, but i have it statically initialised in the path File string
	   * Similar to the above method, the information is output and sent in byte format. except this is sending an actual file */
	  private synchronized boolean sendFileToServer() {
			File file = new File(System.getProperty("user.dir") + File.separator + "FileToUploadToServer.txt");
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
						System.out.println("Successfully Sent to Server");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}	
			return false;
	  }

}