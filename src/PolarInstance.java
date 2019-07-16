
import java.util.ArrayList;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;



public class PolarInstance {
	 String[] attributesNames={"noPosW","noNegW","question","shortPos","shortNeg","nearPos","nearNeg"};
	 ArrayList<String> wngrams=FileReadToString.File2ArrString("C:/Users/Suha/workspace/WekaTest/lexicalAtt/polarity.txt");
	 double[]        vals;
	 ArrayList<Attribute> atts;	
	 ArrayList<String> attClassification;
	 ArrayList<String> trufal;
     int size=5;
     String clss[]={"Negative","Positive"};
     String tf[]={"False","True"};
     
     PolarInstance() {
		
	     // 1. set up attributes
	    atts = new ArrayList<Attribute>();
	 
	   
	   trufal= new ArrayList<String>();
	      trufal.add("False");
	      trufal.add("True");
	     
	    
	   
	    attClassification = new  ArrayList<String>();
	    
 	     attClassification.add("Negative");
	     attClassification.add("Positive");
	     
	    
	 	   
	 	    for(int x=0;x<size;x++)
			{
				atts.add(new Attribute(attributesNames[x]));	
			}
	 	     atts.add(new Attribute(attributesNames[size] ,trufal));//near to positive?
	         atts.add(new Attribute(attributesNames[size+1] ,trufal));//near to negative?
	        
	         for(String word:wngrams)
	         {
			  atts.add(new Attribute(word,trufal));
	         }
	         
	         atts.add(new Attribute("Class", attClassification));
			 	  		     
     }
     
Instances createPolarInstance(PolarAttrFreq polatt)
	{
	Instances dataUnlabeled = new Instances("TestInstances", atts, 0);
	
    
    vals = new double[dataUnlabeled.numAttributes()];
       
   for(int j=0;j<size;j++)
   {
   	
   	
   	
   		vals[j] = polatt.AttributeValues[j];
   
   //j	
   }
   vals[size]=trufal.indexOf(tf[(int) polatt.AttributeValues[size]]);
   vals[size+1]=trufal.indexOf(tf[(int) polatt.AttributeValues[size+1]]);
  
   int u=size+2;
   
   for (int q=u;q<u+wngrams.size();q++)
   {
   	
   		vals[q]=trufal.indexOf(tf[0]);
   
   }//j
 
String[] tweetNoEng=polatt.tweet.split("\\s+");

	    for(String subword:tweetNoEng)
	    {   		
	    			
	    	int indx=wngrams.indexOf(subword);
    		
		    if(indx>-1)
		    {
		    	
		    	vals[u+indx]=trufal.indexOf(tf[1]);
		    }
		   
	    }//for string
   
   
	//classification att	
   vals[dataUnlabeled.numAttributes()-1] = -1;
    //add
   DenseInstance newInst = new DenseInstance(1.0,vals);
   dataUnlabeled.add(newInst);
   dataUnlabeled.setClassIndex(dataUnlabeled.numAttributes() - 1); 
	

			return dataUnlabeled;	
	 }
	  
}//class
