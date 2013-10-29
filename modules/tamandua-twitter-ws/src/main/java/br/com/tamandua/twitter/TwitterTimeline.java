/**
 * 
 */
package br.com.tamandua.twitter;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import com.google.gson.Gson;

/**
 * @author erosario
 *
 */
public class TwitterTimeline {

	/**
	 * @param args
	 * @throws TwitterException 
	 */
	public static void main(String[] args) throws TwitterException {
	    // The factory instance is re-useable and thread safe.
	    Twitter twitter = new TwitterFactory().getInstance();

	    // Friends Timeline
	    List<Status> friends = twitter.getFriendsTimeline();
	    Gson gson = new Gson();
	    
	    List<Status> mentions = twitter.getMentions();

        int max = Math.min(mentions.size(), friends.size());
        
        System.out.println("\n\n\n\n");
        List<Status> mixed = new ArrayList<Status>();
        for (int i = 0; i < max; i++) {
            Status mention = mentions.get(i);
            Status friend = friends.get(i);
            mixed.add(mention);
            mixed.add(friend);
        }
        
        System.out.println(gson.toJson(mixed));
        ""
        
	}

}
