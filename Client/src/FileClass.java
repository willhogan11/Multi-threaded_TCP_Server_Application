import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class FileClass {
 
    public static void main(String[] args) {
    	
    	FileClass fileClass = new FileClass();
    	Scanner input = new Scanner(System.in);
    	System.out.println("1:List Directory\n2:Make Directory\n3:Copy File\n4:Exit");
    	int choice = input.nextInt();

    	boolean running = true;
    	
    	do{
	    	switch(choice){
	    	case 1:
	    		fileClass.listDirectory();
	    		break;
	    	case 2:
	    		fileClass.makeDirectory();
	    		break;
	    	case 3:
	    		fileClass.copyFile();
	    		break;
	    	case 4:
	    		running = false;
	    		System.exit(0);
	    		break;
			default:
				System.out.println("Enter valid number");
	    	}
    	}while(!running);
    	
    	input.close();
    }
    
    public File listDirectory(){
     	File file = new File("C:/Users/william/Desktop/ProjectUsers/will");
    	String[] str = file.list();
    	
    	for (String string : str) {
			System.out.println(string);
		}
		return file;
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