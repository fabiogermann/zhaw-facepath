package ch.zhaw.seps;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User;

public class RestApiFB {
	public static void main(String[] args) throws ClientProtocolException, IOException {
		// See documentation at http://restfb.com/#initialization
		
		final String FABIO_ACCESS_TOKEN = "CAACEdEose0cBAEHxe6ZB6NWxSI1KbZCyF2nq76zqrD5OygkZBFH9ksN5nm4Y77sD4qBCSBj2FjDyVKrgVZANpW8Nwyd2s9ZBC6kUUFcvxhJ3Jo0ECumxCrOLZCa8ro26KiONgKZCAigfgmEMuAURZANahhuiAmhmjGXhhAPllLPRVmABUG7YCYDul3RrWg7vHiEZD";
	
		FacebookClient facebookClient = new DefaultFacebookClient(FABIO_ACCESS_TOKEN);
		
		//FacebookClient publicOnlyFacebookClient = new DefaultFacebookClient();
		
		User user = facebookClient.fetchObject("me", User.class);
		System.out.println(user.getLink());
		System.out.println("User name: " + user.getName());
		System.out.println("----------------------------");
		Connection<User> myFriends = facebookClient.fetchConnection("me/friends", User.class);
		List<User> friendsList = myFriends.getData();
		
		for(Iterator<User> i = friendsList.iterator(); i.hasNext(); ) {
		    User item = i.next();
		    System.out.println(item.getName());
		}
	}
}
