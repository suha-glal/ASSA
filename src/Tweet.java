

public class Tweet {
	String orgtxt;
	String prossesedtxt;
	String normalizedtxt;
	int Annotation;
	String []Tweetwords;
	String[]proTweetWords;
	
	Tweet(String org,String pro)
	{
		orgtxt=org;
		prossesedtxt=pro;
		Annotation=-1;
		
	}
	Tweet (String TW[], String PTW[], String org)
	{
		Tweetwords=TW;	
		proTweetWords=PTW;
		orgtxt=org;
	}
	

}
