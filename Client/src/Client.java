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
			// System.out.println("Please Enter your IP Address");
			ipaddress = "localhost"; // stdin.next();
			requestSocket = new Socket(ipaddress, 9999);
			System.out.println("Connected to " + ipaddress + " in port 9999"); // Port number and ip address must match server ip address and port
			
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
									
									if(choice == 1)
									{
										requestFileFromServer();
									}
									// Choice 3 Will Display a Directory listing for that User & The Actual Directory Path
									else if(choice == 3)
									{
										message = (String) in.readObject();
										System.out.println(message);
										message = (String) in.readObject();
										System.out.println(message);
									}
									else if(choice == 5)
									{
										/*System.out.println(message);
										message = (String) in.readObject();
										System.out.println(message);*/
									}
									/*message = "exit";
									System.out.println("\nLogged Out");
									System.exit(0);*/
									input.close();
									
								} while (choice != 6);
								
								in.close();
							}
							else{
								System.out.println("User Not recognised\n");
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
	
	
	public void sendLoginDetails(String username, String password){
		try 
		{
			out.writeObject(username);
			out.writeObject(password);
			out.flush();
			// System.out.println("client>" + username + " " + password);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	private boolean requestFileFromServer() {
		File file = new File(System.getProperty("user.dir") + File.separator + "Test.txt");
		// File file = new File("C:\\Users\\william\\workspace\\EchoServer\\Test.txt");
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
					System.out.println("File Request Success, See below directory");
					listDirectory();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		else
			System.err.println("File Already Exists");
		return false;
	}
	
	
	public File listDirectory(){
     	File usersDirectory = new File(System.getProperty("user.dir"));
     	String[] list = (usersDirectory.list());
     	for(String s: list)
     		System.out.println(s);
     	return usersDirectory;
    }    

}