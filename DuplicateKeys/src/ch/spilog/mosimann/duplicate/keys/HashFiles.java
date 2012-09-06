package ch.spilog.mosimann.duplicate.keys;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



// Hi that's a comment!

public class HashFiles extends SimpleFileVisitor<Path> {
	
	
	
	public HashFiles (){
		this.openConnectionToDatabase();		
	}
	
	Connection conn = null;

    // Print information about
    // each type of file.
    @Override
    public FileVisitResult visitFile(Path file,
                                   BasicFileAttributes attr) {
        if (attr.isSymbolicLink()) {
           // System.out.format("Symbolic link: %s ", file);
        } else if (attr.isRegularFile()) {
        	
            try {
				System.out.format(this.calcHash(file) + " + Regular file: %s ", file);
				this.writeToDatabase(file.toString(), this.calcHash(file));
			} catch (NoSuchAlgorithmException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        } else {
            //System.out.format("Other: %s ", file);
        }
        
        System.out.println("(" + attr.size() + "bytes)");
        return FileVisitResult.CONTINUE;
    }
    
    private void openConnectionToDatabase(){
    
    	
    	try {
			conn = DriverManager.getConnection("jdbc:mysql://h036xg/dupclicate?" +
				                                   "user=anlieferstat&password=lilit");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    
    
    private void writeToDatabase(String path, String hash){
    	Statement stmt = null;
    	ResultSet rs = null;
    	
    	try {
			stmt = conn.createStatement();
		
			
			 stmt.execute("Insert into filebase (Path, HashSHA256) Values ('" + path +"','"+hash+"')");

   
        
        
        
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
		}
    }
    
    private String calcHash(Path path) throws NoSuchAlgorithmException, IOException{
    	MessageDigest md = MessageDigest.getInstance("SHA-256");
        FileInputStream fis = new FileInputStream(path.toString());
 
        byte[] dataBytes = new byte[1024];
 
        int nread = 0; 
        while ((nread = fis.read(dataBytes)) != -1) {
          md.update(dataBytes, 0, nread);
        };
        byte[] mdbytes = md.digest();
 
        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
          sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
 
        //System.out.println("Hex format : " + sb.toString());
        
        return sb.toString();
    }

    // Print each directory visited.
    @Override
    public FileVisitResult postVisitDirectory(Path dir,
                                          IOException exc) {
       // System.out.format("Directory: %s%n", dir);
        return FileVisitResult.CONTINUE;
    }

    // If there is some error accessing
    // the file, let the user know.
    // If you don't override this method
    // and an error occurs, an IOException 
    // is thrown.
    @Override
    public FileVisitResult visitFileFailed(Path file,
                                       IOException exc) {
        System.err.println(exc);
        return FileVisitResult.CONTINUE;
    }

}
