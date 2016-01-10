import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Scanner;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

public class FileClass {
	
	// Lists the contents of then users folder on the client side and also displays the users Path
	public String listDirectory(String username, String str){
    	String usersURL = "";
    	File usersDirectory = new File(System.getProperty("user.dir") + File.separator + "ProjectUsers\\" + username);
    	Path rootURL = Paths.get("C:/Users/william/Desktop/ProjectUsers/");
    	str = Arrays.toString(usersDirectory.list());
    	usersURL = (rootURL + "\\" + username);
		System.out.println(str);
		
		return str;
    }    
    
	// Makes a New Directory on the Server Side within the users folder
    public File makeDirectory(String username){
    	File file = new File(System.getProperty("user.dir") + File.separator + "ProjectUsers\\" + username + File.separator + username + "NewFolder");
    	if(!file.exists()){
    		file.mkdir();
    		System.out.println("New Directory Created");
    	}
    	else
    		System.err.println("Error, Directory Already Exists");
    	return file; 
    }
}