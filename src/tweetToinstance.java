import java.io.IOException;

import weka.core.Instance;
import weka.core.Instances;


public class tweetToinstance extends Thread
{
	Preprocesser  Preproc;
	String Tweettxt;
	String Target;
	static long startTime=System.nanoTime();
	tweetToinstance ()
	   {
	      // The compiler creates the byte code equivalent of super ();
	   }
	tweetToinstance (String tweet,String target, Preprocesser  preproc,String name)
	   {
	     super (name); // Pass name to Thread superclass
		Tweettxt=tweet;
		Target=target;
		Preproc=preproc;
	   }
	   public void run ()
	   {
		  
		   Tweet tweet= Filtering();
		   
		   tweet=DetectPosNegInTweet(tweet);
		   tweet=DetectingPosNegExpressions(tweet);
		   SubjAttrFreq subatt= SubjectivityAttFreqExProcess(tweet);
		  
		 Instances targetInstance= Preproc.subjInst.createSubjInstance(subatt);
          int s=Preproc.PredictSubjectivity(targetInstance);
		   if(s==1)   
		   {  
			   
			   PolarAttrFreq polatt=PolarityAttFreqExProcess(tweet);
			   Instances subjInstance=Preproc.polerInst.createPolarInstance(polatt);
			   int p=Preproc.PredictPolarity(subjInstance);
			   if(p==1)
			   {
				   System.out.println(this.getName()+"--"+Tweettxt+"is positive   "+p);
			   }
			   else
			   {
				   System.out.println(this.getName()+"--"+Tweettxt+"is negative  "+p);
			   }
		   }
		   else
		   {
			   System.out.println(this.getName()+"--"+Tweettxt+"is " +s);
		   }
		   TimeElapsed();
			  
	   }//run
	   Tweet Filtering()
	   {
		  // System.out.println ("My name is: " + getName ());
		      String msen= Preproc.TargetTag(Tweettxt, Target);
			  msen=Preproc.DetectNeutralPosNegExpression(msen);
			  msen=Preproc.DetectNamedEntity(msen);
			  msen=msen.replaceAll("o O", "استنكار");
			  String TW[]=msen.split("\\s");
			  String proTW[]=null;
			try {
				proTW = Preproc.preprocessTweetWord(TW);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Tweet tweetobj=null;
			
				if(proTW.length>1)
				{
	  		 tweetobj= new Tweet(TW,proTW,Tweettxt);
	  		
		        }
			return 	tweetobj;
	   }//modification
	   Tweet DetectPosNegInTweet(Tweet twet)
	   {
		   String pntw[]= Preproc.DetectingPosNegTweetWords(twet.proTweetWords);
		   
	        for(int j=0;j<pntw.length-1;j++)
			{
				if(Preproc.garLetters.contains(pntw[j]))
				{
					if(!pntw[j+1].contains("target")&& !pntw[j+1].contains("PosW") && !pntw[j+1].contains("NegW"))
					{
						pntw[j]=pntw[j]+"_"+pntw[j+1];
						pntw[j+1]="";
					}//if
				}//if
			}//for
	        
	      if(pntw.length>0)
		        {
		        twet.proTweetWords=pntw;
		        StringBuilder builder = new StringBuilder();
				for(String s : pntw) {
					if (!(s.equals(" ")||s.equals("")))
				    builder.append(s+" ");
				}//for
				
		        twet.prossesedtxt=builder.toString();
		        twet.normalizedtxt=twet.prossesedtxt.replaceAll("[^ء-غ ف-ي]+", " ");
		        
		        
	      	}//if
	      
	      return twet;
	      
	   }//fun
	   Tweet DetectingPosNegExpressions(Tweet twet)
	    {  
	    	
	    		String pnExp= Preproc.DetectingPosNegExpressions(twet.prossesedtxt);
	    		 twet.prossesedtxt=pnExp;
	       	
	    	
	    	 return  twet;
	    }
	   SubjAttrFreq SubjectivityAttFreqExProcess(Tweet twet) //throws IOException
	    {

			
				SubjAttrFreq att= new SubjAttrFreq();
				Preproc.SubjectiveAttFreq(twet.prossesedtxt,twet.orgtxt ,att);
				att.tweet=twet.normalizedtxt;
				att.orgtweet=twet.orgtxt;
				att.Tweetwords=twet.Tweetwords;
				att.proTweetWords=twet.proTweetWords;
				att.classfication=twet.Annotation;
				
			
			
		  return   att;
			
	    }
	   PolarAttrFreq PolarityAttFreqExProcess(Tweet twet) //throws IOException
	    {

			
				PolarAttrFreq att= new PolarAttrFreq();
				Preproc.PolarityAttFreq(twet.prossesedtxt,twet.orgtxt ,att);
				att.tweet=twet.normalizedtxt;
				att.orgtweet=twet.orgtxt;
				att.Tweetwords=twet.Tweetwords;
				att.proTweetWords=twet.proTweetWords;
				att.classfication=twet.Annotation;
				
			
			
		  return   att;
			
	    }
	    void TimeElapsed()
		{
	    	long endTime=System.nanoTime();
	    	System.out.println("Duration:"+((endTime-startTime)/1000000000)+" Sec");
		}
	   
}//class
	