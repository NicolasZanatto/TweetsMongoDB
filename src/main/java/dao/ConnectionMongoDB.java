package dao;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import model.Tweet;

public class ConnectionMongoDB {
	
	public DBCollection ObterTweetCollection() throws UnknownHostException {

		MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
		DB database = mongoClient.getDB("OrganizacaoArquivos");
		return database.getCollection("Tweets");

	}
	
	public DBObject ConverterTweetParaInsercao(Tweet tweet) {
		return new BasicDBObject("_idTwitter", tweet.getIdTwitter())
                .append("mensagem", tweet.getMensagem())
                .append("data", tweet.getData())
                .append("hashtags", tweet.getHashtags());
	}
}
