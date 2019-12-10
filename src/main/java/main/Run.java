package main;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

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
				
		Menu();
	}
	
	public static void Menu() throws IOException, ParseException {
		boolean continua = true;
		Consultas consultas = new Consultas();
		Scanner leitor = new Scanner(System.in);
		Grafico grafico = new Grafico();
		while(continua) {
			System.out.println("-----Menu-----");
			System.out.println("1 - Buscar por IdTwitter");
			System.out.println("2 - Buscar por Hashtag");
			System.out.println("3 - Buscar por Data");
			System.out.println("4 - Exibir resultado de todos os times");
			System.out.println("5 - Exibir time mais comentado");
			System.out.println("6 - Gráfico de tweets inseridos por data");
			System.out.println("7 - Gráfico de tweets por hashtag");
			System.out.println("8 - Sair");
			
			switch(leitor.nextInt()) {
			case 1:
				System.out.println("Digite o indice que deseja:");
				leitor.nextLine();
				String tw_id = leitor.nextLine();
				Long twid = Long.parseLong(tw_id);
				consultas.BuscaArquivoPorID(twid);
				break;
			case 2: 
				System.out.println("Digite a hashtag desejada:");
				leitor.nextLine();
				String hashtag = leitor.nextLine();
				
				consultas.ObterTweetsPorHashTag(hashtag);
				break;
				
			case 3:
				System.out.println("Digite a data desejada no formato dd/mm/yyyy");
				leitor.nextLine();
				String data = leitor.nextLine();
				try {
					SimpleDateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy");  
					java.util.Date date1=formatter1.parse(data);  

					consultas.ObterTweetsPorDataParaConsulta(date1);

				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
				
			case 4:
				consultas.MostrarHashtagsEQuantidades();
				break;
			case 5:
				consultas.MostrarTimeMaisComentado();
				break;
			case 6:
				grafico.BuscaDadosGraficoBarra();
				break;
			case 7:
				grafico.BuscaDadosGraficoPizza();
				break;
			case 8:
				continua = false;
				break;
			}
			
		}
		leitor.hasNextLine();
		leitor.close();	
		

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
