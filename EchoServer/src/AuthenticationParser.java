import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class AuthenticationParser {
	
	private static Map<String, Integer> map = new HashMap<String, Integer>();
	private static final String CREDENTIALS = "C:/Users/william/Desktop/ProjectUsers/credentials.txt";
	private String line;
	private static String userName;
	private static int password;
	private static boolean loggedIn = false;
	private static Scanner input;
	

	public Map<String, Integer> parse(Map<String, Integer> map, boolean loggedIn) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(CREDENTIALS)));
		
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
	
	
	public static Map<String, Integer> getMap() {
		return map;
	}

	public static void setMap(Map<String, Integer> map) {
		AuthenticationParser.map = map;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getUserName() {
		return userName;
	}

	public static void setUserName(String userName) {
		AuthenticationParser.userName = userName;
	}

	public int getPassword() {
		return password;
	}

	public static void setPassword(int password) {
		AuthenticationParser.password = password;
	}

	public static boolean isLoggedIn() {
		return loggedIn;
	}

	public static void setLoggedIn(boolean loggedIn) {
		AuthenticationParser.loggedIn = loggedIn;
	}

	public static Scanner getInput() {
		return input;
	}

	public static void setInput(Scanner input) {
		AuthenticationParser.input = input;
	}
}