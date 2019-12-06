package dao;

import java.io.IOException;
import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
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
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import model.Tweet;

public class ConnectionMongoDB {
	
	public static void main(String[] args) throws Exception{
		
		SecretKey symKey = KeyGenerator.getInstance(algorithm).generateKey();
	    Cipher c = Cipher.getInstance(algorithm);
		byte[] encryptionBytes = encryptF("Browns",symKey,c);
		System.out.println("Encrypted: " + new String(encryptionBytes));		
		
		
		byte[] encoded = symKey.getEncoded(); 
		System.out.println("key: "+encoded);// key: [B@52b2a2d8
		
		SecretKey secretKey = new SecretKeySpec(encoded, "DESede");

		
		System.out.println("Decrypted: " + decryptF(encryptionBytes,secretKey,c));

	}
	
	public String algorithm = "DESede";

	public DBCollection ObterTweetCollection() throws UnknownHostException {
		
		MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
		DB database = mongoClient.getDB("OrganizacaoArquivos");
		return database.getCollection("Tweets");

	}

	public DBObject ConverterTweetParaInsercao(Tweet tweet, SecretKey senha) {
		List<String> hashtagsCriptografadas = ObterHashsCriptografadas(tweet.getHashtags(), senha);
		return new BasicDBObject("_idTwitter", tweet.getIdTwitter()).append("mensagem", tweet.getMensagem())
				.append("data", tweet.getData()).append("hashtags", hashtagsCriptografadas);
	}
	
	public List<String> ObterHashsCriptografadas(List<String> hashtags, SecretKey symkey) {
		List<String> retorno =  new ArrayList<String>();
		try {
			
			Cipher c = Cipher.getInstance(algorithm);	
			for(String hashtag : hashtags) {
				  byte[] encryptionBytes = encryptF(hashtag,symkey,c);
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
	
	public byte[] encryptF(String input, Key pkey, Cipher c) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

		c.init(Cipher.ENCRYPT_MODE, pkey);
		byte[] inputBytes = input.getBytes();
		return c.doFinal(inputBytes);
	}

	public String decryptF(byte[] encryptionBytes, Key pkey, Cipher c) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

		c.init(Cipher.DECRYPT_MODE, pkey);

		byte[] decrypt = c.doFinal(encryptionBytes);

		String decrypted = new String(decrypt);

		return decrypted;
	}
	
	
	
	
	public String getAlgorithm() {
		return algorithm;
	}

	public SecretKey GerarSymKey() throws NoSuchAlgorithmException{
		return KeyGenerator.getInstance(algorithm).generateKey();
	}
	
	public DBCollection ObterSenhaCollection() throws UnknownHostException {
		MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
		DB database = mongoClient.getDB("OrganizacaoArquivos");
		return database.getCollection("Senha");
	}
	
	public DBObject ObterDBOjectParaInsercaoSenha(byte[] symKeyEncoded) {
		return new BasicDBObject("_senhaCriptografia", symKeyEncoded);
	}
	
	public SecretKey ObterSecretKey() throws UnknownHostException {
		DBCollection senhaCollection = this.ObterSenhaCollection();
		BasicDBObject searchQuery = new BasicDBObject();
		DBCursor cursor = senhaCollection.find(searchQuery);
		while (cursor.hasNext()) {
			DBObject obj = cursor.next();
			byte[] encoded = (byte[]) obj.get("_senhaCriptografia");
			return new SecretKeySpec(encoded, algorithm);
		}
		return null;
		
		
	}
}
