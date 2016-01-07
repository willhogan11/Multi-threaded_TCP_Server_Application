import java.io.*;
import java.net.*;
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
					message = (String)in.readObject();
					sendMessage(message);
					System.out.println("\nWelcome\nPlease Enter your credentials\n");	
						
					do
					{
						try {
							System.out.println("|----------|");
							System.out.println("| USERNAME |");
							System.out.println("|----------|");
							username = stdin.next();
						} catch (Exception e) {
							System.out.println("String required");
						}
						
						try {
							System.out.println("|----------|");
							System.out.println("| PASSWORD |");
							System.out.println("|----------|");
							password = stdin.next();
							
							sendLoginDetails(username, password);
							message = (String)in.readObject();
							
							if(message.contains("successful")){
								loggedIn = true;
								System.out.println("\nUser [" + username + "] now logged in\n");
								
								do {
									System.out.println(
											"\nMenu options:\n1:Copy File from Server\n2:Move a File to the Server\n"
										  + "3:List all Files in the Directory\n4:Move Directory\n5:Make a new Directory\n6:Exit");
									
									Scanner input = new Scanner(System.in);
									choice = input.nextInt();
									String choiceTxt = Integer.toString(choice);
									sendMessage(choiceTxt);
									message = (String) in.readObject();
									System.out.println(message);
									message = (String) in.readObject();
									System.out.println(message);
									
									
									
									message = "exit";
									System.out.println("Logged Out");
									System.exit(0);
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
	
	public void sendLoggedIn(boolean loggedIn){
    	try {
			out.writeObject(loggedIn);
			out.flush();
			// System.out.println("Server>" + loggedIn);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	public int userMenu(){
		System.out.println("Menu options:\n1:Copy File from Server\n2:Move a File to the Server\n"
				+ "3:List all Files in the Directory\n4:Move Directory\n5:Exit");
		
		Scanner input = new Scanner(System.in);
		choice = input.nextInt();
		
		switch(choice){
			case 1:
				System.out.println("Case 1");
				break;
			case 2:
				System.out.println("Case 2");
				break;
			case 3:
				System.out.println("Users Directory");
				break;
			case 4:
				System.out.println("Case 4");
				break;
			case 5:
				System.out.println("User Logged Out");
				System.exit(0);
				break;
			default:
				System.out.println("Please enter a valid choice");
				break;
		}		
		input.close();
		return choice;
	}
}