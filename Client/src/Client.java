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
					System.out.println("\nWelcome\nTo connect to the server please enter your credentials\n");
					
					try {
						System.out.println("USERNAME:");
						username = stdin.next();
					} catch (Exception e) {
						System.out.println("String required");
					}
					
					try {
						System.out.println("PASSWORD");
						password = stdin.next();
					} catch (Exception e) {
						System.out.println("Integer required");
					}
					
					sendLoginDetails(username, password);
					sendMessage(message);
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
	
	
	public void sendLoginDetails(String userName, String password){
		try 
		{
			out.writeObject(userName);
			out.writeObject(password);
			out.flush();
			System.out.println("client>" + userName + " " + password);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
}