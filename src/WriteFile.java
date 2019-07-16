import java.io.BufferedWriter;
import java.io.FileWriter;
public class WriteFile {
	
	BufferedWriter out;
	
	 public WriteFile(String filename, boolean append)
	  {
	  try{
	  // Create file 
	FileWriter fstream = new FileWriter(filename, append);
	   out = new BufferedWriter(fstream);
	 
	  }catch (Exception e){//Catch exception if any
	  System.err.println("Error: " + e.getMessage());
	  }//catch
	  
	  }
	 void writeln(String args)
	 {
		 try {
			out.write(args+"\n");
			out.flush();
		} catch (Exception e){//Catch exception if any
	  System.err.println("Error: " + e.getMessage());
	  }//catch
		 
	 }
	 void write(String args)
	 {
		 try {
			out.write(args);
			out.flush();
		} catch (Exception e){//Catch exception if any
	  System.err.println("Error: " + e.getMessage());
	  }//catch
		 
	 }
	 void close()
	 {//Close the output stream
	  try {
		out.close();
	} catch (Exception e){//Catch exception if any
	  System.err.println("Error: " + e.getMessage());
	  }//catch
	 }
	
}
