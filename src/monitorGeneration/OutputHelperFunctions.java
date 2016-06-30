package monitorGeneration;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class OutputHelperFunctions {
	
	/**
	 * Simple method to print in console
	 * @param message The message that is going to be Printed
	 */
	public static void PrintMessage(String message)
	{
		System.out.print(message);		
	}
		
	/**
	 * 
	 * @param functionName
	 * @param lineToWrite
	 * @return
	 */
	public boolean writeToFile(String functionName,String filePath, StringBuilder lineToWrite){		
			
		Path newFile = Paths.get(filePath+"/"+functionName+".aj");
		
		//If file does nto exist... create new one
	    try {
	    	if(!Files.exists(newFile)){
	    		newFile = Files.createFile(newFile);    		
	    	} else{
	    		Files.delete(newFile);
	    		Files.createFile(newFile);
	    	}

	    } catch (IOException ex) {
	    	ex.printStackTrace();
	    }
	    
	    //Writing to file   
	    try(BufferedWriter writer = Files.newBufferedWriter(newFile,Charset.defaultCharset(), StandardOpenOption.CREATE))
	    {
	    	writer.append(lineToWrite);
	    	writer.flush();
	    	return true;
	    }catch(IOException ex){
	    	ex.printStackTrace();
	    }		
		return false;
	}
		
}
