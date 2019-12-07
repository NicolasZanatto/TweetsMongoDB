package dao;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import model.Dados;

public class Consultas {
	
	public void BuscaArquivoPorID(long chave) throws IOException {
		ConnectionMongoDB connectionMongo = new ConnectionMongoDB();
		try {
			DBCollection collection = connectionMongo.ObterTweetCollection();
			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("_idTwitter", chave);
			DBCursor cursor = collection.find(searchQuery);
			while (cursor.hasNext()) {
				System.out.println(cursor.next());
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public DBCursor ObterTweetsPorData() throws IOException 
	{
		ConnectionMongoDB connectionMongo = new ConnectionMongoDB();
		DBCollection collection = connectionMongo.ObterTweetCollection();
		
		BasicDBObject fields = new BasicDBObject();
		 fields.put("data", "0");

		DBCursor cursor = collection.find(new BasicDBObject(),fields);
		/*
	    while (cursor.hasNext()) 
	    {
	    	BasicDBObject object = (BasicDBObject)cursor.next();
	        System.out.println(object.getString("data"));
	    }*/
		return cursor;
	}
	
	public long ObterQuantidadeTweetsPorHashTag(String hashtag) throws IOException {
		ConnectionMongoDB connectionMongo = new ConnectionMongoDB();
		DBCollection collection = connectionMongo.ObterTweetCollection();
		SecretKey symKey = connectionMongo.ObterSecretKey();
		try {
			Cipher c = Cipher.getInstance(connectionMongo.getAlgorithm());	
			byte[] encryptionBytes = connectionMongo.encryptF(hashtag,symKey,c);
			String hashtagscript = new String(encryptionBytes);
			
			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("hashtags", hashtagscript);
			return collection.count(searchQuery);
			
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
		return 0;
	}
	
	public void MostrarHashtagsEQuantidades() throws IOException {
		for(String hashtag : this.ObterListaHashtagsTimes()) {
			System.out.println(hashtag + ", qntd: " + ObterQuantidadeTweetsPorHashTag(hashtag));
		}
	}
	
	public ArrayList<Dados> BuscaTodasHashtags() throws IOException
	{
		ArrayList<Dados> retorno = new ArrayList<Dados>();
		
		long qntd = 0;
		for(String hashtag : this.ObterListaHashtagsTimes())
		{
			qntd = ObterQuantidadeTweetsPorHashTag(hashtag);
			Dados dados = new Dados(hashtag, (int) qntd);
			retorno.add(dados);

		}
		
		return retorno;
	}
	
	public String ConverteMes(String mes)
	{
		String retorno = new String();
		
		if(mes.equals("Oct"))
		{
			retorno = "10";
		}
		else if(mes.equals("Nov"))
		{
			retorno = "11";
		}
		else
		{
			retorno = "10";
		}
		
		return retorno;
	}
	
	public ArrayList<Dados> BuscaTodasDatas() throws IOException, ParseException
	{
		
		ArrayList<Dados> retorno = new ArrayList<>();

		DBCursor cursor = ObterTweetsPorData();
		
	    while (cursor.hasNext()) 
	    {
	    	BasicDBObject object = (BasicDBObject)cursor.next();
	    	String str = object.getString("data");
		    if(str != null) {
		    	String data = str.substring(8, 10) + "/" + this.ConverteMes(str.substring(4, 7)) + "/"  + str.substring(24, 28);
		    	boolean flag = false;
		    	int indice = 0;
		    	for(Dados linha: retorno)
		    	{			
		    		//Se a linha já existe
		    		if(linha.getNome().equals(data))
		    		{
		    			flag = true; //Marca que existe
		    			Dados dados = new Dados(data, linha.getValor()+1);
		    			retorno.set(indice, dados);//Altera a linha adicionando um valor
		    			
		    		}
		    		indice++;
		    	}
		    	
		    	if(!flag) //Linha ainda nao existe
		    	{
		    		Dados dados = new Dados(data, 1);
		    		retorno.add(dados);
		    	}
		    }
	    }
		
		return retorno;
	}
	
	public void MostrarTimeMaisComentado() throws IOException {
		long maior = 0;
		long qntd = 0;
		String hash = "";
		for(String hashtag : this.ObterListaHashtagsTimes()) {
			qntd = ObterQuantidadeTweetsPorHashTag(hashtag);
			if(maior < qntd) {
				maior =  qntd;
				hash = hashtag;
			}
		}
		System.out.println(hash + ", qntd: " + maior);
	}
	
	private List<String> ObterListaHashtagsTimes() {

		// Fonte:
		// https://ftw.usatoday.com/2019/09/2019-nfl-kickoff-every-nfl-teams-official-twitter-emoji-hashtag

		List<String> retorno = new ArrayList<String>();
		retorno.add("RedSea"); // Arizona Cardinals
		retorno.add("InBrotherhood"); // Atlanta Falcons
		retorno.add("RavensFlock"); // Baltimore Ravens
		retorno.add("GoBills"); // Buffalo Bills
		retorno.add("KeepPounding"); // Carolina Panthers
		retorno.add("Bears100"); // Chicago Bears
		retorno.add("SeizeTheDEY"); // Cincinnati Bengals
		retorno.add("Browns"); // Cleveland Browns
		retorno.add("DallasCowboys"); // Dallas Cowboys
		retorno.add("BroncosCountry");// Denver Broncos
		retorno.add("OnePride"); // Detroit Lions
		retorno.add("GoPackGo"); // Green Bay Packers
		retorno.add("WeAreTexans"); // Houston Texans
		retorno.add("Colts"); // Indianapolis Colts
		retorno.add("DUUUVAL"); // Jacksonville Jaguars
		retorno.add("ChiefsKingdom"); // Kansas City Chiefs
		retorno.add("BoltUp"); // Los Angeles Chargers
		retorno.add("LARams"); // Los Angeles Rams
		retorno.add("FinsUp"); // Miami Dolphins
		retorno.add("Skol"); // Minnesota Vikings
		retorno.add("GoPats"); // New England Patriots
		retorno.add("Saints"); // New Orleans Saints
		retorno.add("GiantsPride"); // New York Giants
		retorno.add("TakeFlight"); // New York Jets
		retorno.add("RaiderNation"); // Oakland Raiders
		retorno.add("FlyEaglesFly"); // Philadelphia Eagles
		retorno.add("HereWeGo"); // Pittsburgh Steelers
		retorno.add("GoNiners"); // San Francisco 49ers
		retorno.add("Seahawks"); // Seattle Seahawks
		retorno.add("GoBucs"); // Tampa Bay Buccaneers
		retorno.add("Titans"); // Tennessee Titans
		retorno.add("HTTR"); // Washington Redskins

		return retorno;
	}
}
