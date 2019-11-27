package dao;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

import model.Tweet;

public class ConnectionMongoDB {
	
	static String algorithm = "DESede";
	
	/*public static void main(String[] args) throws Exception{
		  Key symKey = KeyGenerator.getInstance(algorithm).generateKey();
		  Cipher c = Cipher.getInstance(algorithm);
		  byte[] encryptionBytes = encryptF("Browns",symKey,c);
		  System.out.println("Encrypted: " + new String(encryptionBytes));
		  System.out.println("Decrypted: " + decryptF(encryptionBytes,symKey,c));
	}*/

	public DBCollection ObterTweetCollection() throws UnknownHostException {

		MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
		DB database = mongoClient.getDB("OrganizacaoArquivos");
		return database.getCollection("Tweets");

	}

	public DBObject ConverterTweetParaInsercao(Tweet tweet) {
		List<String> hashtagsCriptografadas = ObterHashsCriptografadas(tweet.getHashtags());
		return new BasicDBObject("_idTwitter", tweet.getIdTwitter()).append("mensagem", tweet.getMensagem())
				.append("data", tweet.getData()).append("hashtags", hashtagsCriptografadas);
	}
	
	public List<String> ObterHashsCriptografadas(List<String> hashtags) {
		Key symKey;
		List<String> retorno =  new ArrayList<String>();
		try {
			symKey = KeyGenerator.getInstance(algorithm).generateKey();
			Cipher c = Cipher.getInstance(algorithm);	
			for(String hashtag : hashtags) {
				  byte[] encryptionBytes = encryptF(hashtag,symKey,c);
				  retorno.add(new String(encryptionBytes));
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retorno;
	}

	private static byte[] encryptF(String input, Key pkey, Cipher c) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

		c.init(Cipher.ENCRYPT_MODE, pkey);
		byte[] inputBytes = input.getBytes();
		return c.doFinal(inputBytes);
	}

	private static String decryptF(byte[] encryptionBytes, Key pkey, Cipher c) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

		c.init(Cipher.DECRYPT_MODE, pkey);

		byte[] decrypt = c.doFinal(encryptionBytes);

		String decrypted = new String(decrypt);

		return decrypted;
	}
}
