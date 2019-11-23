package dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Tweet;

public class ConnectionDadosArquivo {
		
	public List<Tweet> ObterListaTweets() throws IOException{
		BufferedReader arq = new BufferedReader(new FileReader("dados.txt"));
		List<Tweet> tweets = new ArrayList<Tweet>();
		String str;
		while (arq.ready()) {
			str = arq.readLine();
			Tweet tweet = new Tweet(str);
			tweets.add(tweet);
		}
		arq.close();
		return tweets;
	}
}
