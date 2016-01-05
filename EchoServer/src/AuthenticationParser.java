import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class AuthenticationParser {
	
	private static Map<String, Integer> map = new HashMap<String, Integer>();
	private static final String FILE = "C:/Users/william/Desktop/ProjectUsers/credentials.txt";
	private String line;
	private static String userName;
	private static int password;
	private static boolean loggedIn = false;
	private static Scanner input;
	
	public static void main(String[] args) throws Exception {
		
		AuthenticationParser ap = new AuthenticationParser();
		ap.parse(map, loggedIn);
		
		input = new Scanner(System.in);
		System.out.println("Enter your Username");
		userName = input.nextLine();
		
		System.out.println("Enter your password");
		password = input.nextInt();
		
		for(Map.Entry<String, Integer> entry: map.entrySet()) 
		{
			String key = entry.getKey();
			Integer value = entry.getValue();
			
			if( (key.equals(userName)) && (value == password) )
			{
				System.out.println("Welcome " + userName);
				loggedIn = true;
			}
		}
		if(!loggedIn)
		{
			System.out.println("User not recognised");
		}
	}
	
	/*public static void main(String[] args) throws Exception {
		
		AuthenticationParser ap = new AuthenticationParser();
		ap.parse(loggedIn);
		
		try {
			input = new Scanner(System.in);
			System.out.println("Enter your Username");
			userName = input.nextLine();
		} catch (Exception e) {
			System.out.println("String Required");
		}
		
		try {
			System.out.println("Enter your password");
			password = input.nextLine();
		} catch (Exception e) {
			System.out.println("Integer Required");
		}
		
		int passToNum = Integer.parseInt(password);
		
		for(Map.Entry<String, Integer> entry: map.entrySet()){
			String key = entry.getKey();
			Integer value = entry.getValue();
			
			if(key.equals(userName) && value == passToNum){
				loggedIn = true;
				System.out.println("Welcome " + userName);
			}
		}
		if(!loggedIn)
			System.out.println("User not recognised");
		
		input.close();
	}*/
	

	public Map<String, Integer> parse(Map<String, Integer> map, boolean loggedIn) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(FILE)));
		
		while((line = br.readLine())!= null){
			String[] stuff = line.split(" ");
			map.put(stuff[0], Integer.parseInt(stuff[1]));
		}
		// Test that map was successfully populated with Key value pairs from credentials.txt
		// ** UNCOMMENT THE BELOW TO DEBUG **
		// System.out.println(map);
		
		br.close();
		return map;
		
	} // End FileParser
}