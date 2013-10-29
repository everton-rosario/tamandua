/**
 * 
 */
package br.com.tamandua.twitter;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Registrar a aplicação sob o usuário do twitter RadioUOL em http://dev.twitter.com/pages/auth#register
 * Depois pegar as chaves: 
 *   oauth.consumerKey=XXXXXXXXXXXXXXXXXXXXXX
 *   oauth.consumerSecret=yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy
 * Preencher estas chaves no arquivo twitter4j.properties
 * Ao preencher, executar esta classe (RegistryOnce)
 * Seguir o pedido pelo script
 * Preencher os 2 campos restantes no twitter4j.properties e a partir de entao nao eh mais necessario fazer isso
 *
 * @author Everton Rosario
 */
public class RegistryOnce {
	public static void main(String args[]) throws Exception {
		// The factory instance is re-useable and thread safe.
		Twitter twitter = new TwitterFactory().getInstance();
		RequestToken requestToken = twitter.getOAuthRequestToken();
		AccessToken accessToken = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (null == accessToken) {
			System.out.println("Open the following URL and grant access to your account:");
			System.out.println(requestToken.getAuthorizationURL());
			System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
			String pin = br.readLine();
			try {
				if (pin.length() > 0) {
					accessToken = twitter.getOAuthAccessToken(requestToken, pin);
				} else {
					accessToken = twitter.getOAuthAccessToken();
				}
			} catch (TwitterException te) {
				if (401 == te.getStatusCode()) {
					System.out.println("Unable to get the access token.");
				} else {
					te.printStackTrace();
				}
			}
		}
		// persist to the accessToken for future reference.
		storeAccessToken((int)twitter.verifyCredentials().getId(), accessToken);
		System.exit(0);
	}

	private static void storeAccessToken(int useId, AccessToken accessToken) {
		System.out.println("oauth.accessToken="+accessToken.getToken());
		System.out.println("oauth.accessTokenSecret="+accessToken.getTokenSecret());
		
	}
}
