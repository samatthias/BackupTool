package ch.spilog.mosimann.duplicate.keys;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Start {

	
	public Start() throws IOException{
		
		Path startingDir = Paths.get("Y:\\5_Funktionsbereiche\\Business_Support");
		//Path startingDir = Paths.get("C:\\Users\\mosimannmat\\Documents\\");
		HashFiles hashFiles = new HashFiles();
		Files.walkFileTree(startingDir, hashFiles);
		
	}
	
	
	
	public static void main (String args[]) throws IOException{
		new Start();
	}
}
