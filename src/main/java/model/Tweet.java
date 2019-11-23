package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Tweet {
	
	private long idTwitter;
	private String mensagem;
	private Date data;
	private List<String> hashtags;
	
	public Tweet(String arquivo) {
		this.setIdTwitter(Long.parseLong(arquivo.substring(0,19)));
		this.setMensagem(arquivo.substring(20,298));
		this.setData(arquivo.substring(299,309));
		this.setHashtags(arquivo.substring(310).trim());
	}
	
	public long getIdTwitter() {
		return idTwitter;
	}
	public void setIdTwitter(long idTwitter) {
		this.idTwitter = idTwitter;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public Date getData() {
		return data;
	}
	public void setData(String data) {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		try {
			this.data = formato.parse(data);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public List<String> getHashtags() {
		return hashtags;
	}
	public void setHashtags(String hashs) {
		this.hashtags = Arrays.asList(hashs.split(","));
	}
	
}
