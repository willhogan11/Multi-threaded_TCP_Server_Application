import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Scanner;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

public class FileClass {
	
	public String listDirectory(String username, String str){
    	String usersURL = "";
     	File usersDirectory = new File("C:/Users/william/Desktop/ProjectUsers/" + username);
     	Path rootURL = Paths.get("C:/Users/william/Desktop/ProjectUsers/");
    	str = Arrays.toString(usersDirectory.list());
    	usersURL = (rootURL + "\\" + username);
		System.out.println(str);
		
		return str;
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