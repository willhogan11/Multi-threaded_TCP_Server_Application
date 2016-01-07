import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

public class FileClass {
 
    public static void main(String[] args) {
    	
    	String username = "";
    	String usersURL = "";
    	FileClass fileClass = new FileClass();
    	
    	// **DEBUG: Shows Root Directory for Users**
    	// fileClass.listDirectory(username);
    	
    	Path url = Paths.get("C:\\Users\\william\\Desktop\\ProjectUsers\\");
    	
    	Scanner input = new Scanner(System.in);
    	System.out.println("1:List Directory\n2:Make Directory\n3:Copy File\n4:Exit");
    	int choice = input.nextInt();
    	
    	do{
	    	switch(choice){
		    	case 1:
		    		System.out.println("Enter your Username");
		    		username = input.next();
		    		
		    		if(username.equals("will")){
		    			usersURL = (url + "\\" + username);
		    			System.out.println("Current Directory is: " + usersURL + "\nContents of User " + username + "'s folder:");
		    			fileClass.listDirectory(username);
		    		}
		    		else if(username.equals("mike")){
		    			usersURL = (url + "\\" + username);
		    			System.out.println("Current Directory is: " + usersURL + "\nContents of User " + username + "'s folder:");
		    			fileClass.listDirectory(username);
		    		}
		    		else if(username.equals("mark")){
		    			usersURL = (url + "\\" + username);
		    			System.out.println("Current Directory is: " + usersURL + "\nContents of User " + username + "'s folder:");
		    			fileClass.listDirectory(username);
		    		}
		    		else if(username.equals("john")){
		    			usersURL = (url + "\\" + username);
		    			System.out.println("Current Directory is: " + usersURL + "\nContents of User " + username + "'s folder:");
		    			fileClass.listDirectory(username);
		    		}
		    		break;
		    	case 2:
		    		fileClass.makeDirectory();
		    		break;
		    	case 3:
		    		fileClass.copyFile();
		    		break;
		    	case 4:
		    		System.exit(0);
		    		break;
				default:
					System.out.println("Enter valid number");
	    	}
    	}while(!username.equals("exit"));
    	
    	System.out.println("User Logged Out");
    	input.close();
    }
    
    public File listDirectory(String username){

    	String usersURL = "";
     	File usersDirectory = new File("C:/Users/william/Desktop/ProjectUsers/" + username);
     	Path url = Paths.get("C:/Users/william/Desktop/ProjectUsers/");
    	String[] str = usersDirectory.list();
    	usersURL = (url + "\\" + username);
    	
    	System.out.println("\nCurrent Directory is: " + usersURL + "\nContents of User " + username + "'s folder:");
    	
    	for (String string : str) {
			System.out.println(string);
		}
		return usersDirectory;
    }    
    
    public File makeDirectory(){
    	File file = new File("C:/Users/william/Desktop/ProjectUsers/will/NewFolder");
    	if(!file.exists()){
    		file.mkdir();
    		System.out.println("New Directory Created");
    	}
    	else
    		System.err.println("Error, Directory Already Exists");
    	return file;
    }
    
    public File copyFile(){
    	File sourceFile = new File("C:/Users/william/Desktop/ProjectUsers/will/will_textFile.txt");
    	File destinationFile = new File("C:/Users/william/Desktop/ProjectUsers/will/CopyOfWillTextFile.txt");
    	
    	try {
			Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			System.out.println("File Copied Successful");
			return sourceFile;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return destinationFile;
    }
}