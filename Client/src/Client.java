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
						
					while(!loggedIn){
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
						} catch (Exception e) {
							System.out.println("Integer required");
						}
						
						sendLoginDetails(username, password);
						sendMessage(message);
						
						message = (String)in.readObject();
						
						if(message.contains("successful")){
							loggedIn = true;
							System.out.println("User: " + username + " now logged in");
							userMenu();
						}
						else{
							System.out.println("User Not recognised\n");
							loggedIn = false;
						}
					}
				}
				catch(ClassNotFoundException classNot)
				{
					System.err.println("data received in unknown format");
				}
			}while(!message.equals("bye"));
			
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
				+ "3:List all Files in the Directory");
		
		Scanner input = new Scanner(System.in);
		int choice = input.nextInt();
		
		switch(choice){
			case 1:
				System.out.println("Case 1");
				break;
			case 2:
				System.out.println("Case 1");
				break;
			case 3:
				System.out.println("List Files in the Directory");
				break;
			case 4:
				System.out.println("Case 1");
				break;
			default:
				System.out.println("Please enter a valid choice");
				break;
		}		
		input.close();
		return choice;
	}
}