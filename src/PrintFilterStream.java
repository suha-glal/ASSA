
import twitter4j.*;

import java.io.IOException;
import java.util.ArrayList;


public final class PrintFilterStream {
	
    public static void main(String[] args) throws TwitterException, IOException {
    	
   	final Preprocesser Preproc;
		Preproc= new Preprocesser();
		Preproc.preparefiles();
		System.out.println("File Prepared!");
    	final String target="الجمعه";
    	new tweetToinstance ("!أنا أحب يوم الجمعه كثيرا",target, Preproc,"1").start();
    	new tweetToinstance (" تافه حقير واحقد عليه   أنا أكره يوم الجمعه كثيرا",target, Preproc,"2").start();
    	new tweetToinstance ("الجمعه #السودان #عمان #الامارات #الكويت !!! http://uae.ae",target, Preproc,"2").start();
    	
      /*StatusListener listener = new StatusListener() {
            
            public void onStatus(Status status) {
                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
           // if(status.isRetweet()==false)
            	//new tweetToinstance (status.getText(),target, Preproc,""+status.getId()).start();
            
            }

            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                //System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
             //   System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            
            public void onScrubGeo(long userId, long upToStatusId) {
               // System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

           
            public void onStallWarning(StallWarning warning) {
              //  System.out.println("Got stall warning:" + warning);
            }

            
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };

        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(listener);
        ArrayList<String> word=new  ArrayList<String>();
        word.add(target);
   
        String[] trackArray=new String [word.size()];
        trackArray= word.toArray(trackArray);
        

        // filter() method internally creates a thread which manipulates TwitterStream and calls these adequate listener methods continuously.
        twitterStream.filter(new FilterQuery(0, null, trackArray));
        */
    }
   
}