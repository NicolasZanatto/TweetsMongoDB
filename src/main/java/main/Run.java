package main;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import javax.crypto.SecretKey;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import dao.ConnectionDadosArquivo;
import dao.ConnectionMongoDB;
import dao.Consultas;
import model.Grafico;
import model.Tweet;

public class Run {

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException, ParseException {
		//InserirDados(); //RODAR SOMENTE UMA VEZ
		Consultas consultas = new Consultas();
		consultas.MostrarTimeMaisComentado();
		
		//Gráficos
		Grafico grafico = new Grafico();
		grafico.BuscaDadosGraficoBarra();
		//grafico.BuscaDadosGraficoPizza();
	}
	
	public static void InserirDados() throws IOException, NoSuchAlgorithmException {
		ConnectionMongoDB connectionMongo = new ConnectionMongoDB();
		try {
			DBCollection senhaCollection = connectionMongo.ObterSenhaCollection();
			SecretKey senha = connectionMongo.GerarSymKey();
			senhaCollection.insert(connectionMongo.ObterDBOjectParaInsercaoSenha(senha.getEncoded()));
			
			
			
			
			ConnectionDadosArquivo cad = new ConnectionDadosArquivo();
			DBCollection collection = connectionMongo.ObterTweetCollection();
			for(Tweet tweet : cad.ObterListaTweets()) {
				collection.insert(connectionMongo.ConverterTweetParaInsercao(tweet,senha));
			}
			
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

}
