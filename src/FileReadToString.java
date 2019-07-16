
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
class FileReadToString
{
 public static  ArrayList<String> File2ArrString(String filename)
  {
	 ArrayList<String> list= new ArrayList<String> ();
  try{
  // Open the file that is the first 
  // command line parameter
  FileInputStream fstream = new FileInputStream(filename);
  // Get the object of DataInputStream
  DataInputStream in = new DataInputStream(fstream);
  BufferedReader br = new BufferedReader(new InputStreamReader(in));
  String strLine=null;
  //Read File Line By Line
  br.skip(1);
  while ((strLine = br.readLine()) != null)   {
	 
	 list.add(strLine);
 
  }
  //Close the input stream
  in.close();
  
    }catch (Exception e){//Catch exception if any
  System.err.println("Error: " + e.getMessage());
  }
  
 
  return list;
  }
}