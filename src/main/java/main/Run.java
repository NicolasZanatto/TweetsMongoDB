package main;

import java.io.IOException;
import java.net.UnknownHostException;
import com.mongodb.DBCollection;
import dao.ConnectionDadosArquivo;
import dao.ConnectionMongoDB;
import model.Tweet;

public class Run {

	public static void main(String[] args) throws IOException {
		InserirDados();
	}
	
	public static void InserirDados() throws IOException {
		ConnectionMongoDB connectionMongo = new ConnectionMongoDB();
		try {
			DBCollection collection = connectionMongo.ObterTweetCollection();
			ConnectionDadosArquivo cad = new ConnectionDadosArquivo();
			for(Tweet tweet : cad.ObterListaTweets()) {
				collection.insert(connectionMongo.ConverterTweetParaInsercao(tweet));
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

}
