package model;

public class Dados {
	public String nome;
	public int valor;
	
	public Dados(String nome, int valor)
	{
		this.nome = nome;
		this.valor = valor;
	}
	
	public void setNome(String nome)
	{
		this.nome = nome;
	}
	
	public void setValor(int valor)
	{
		this.valor = valor;
	}
	
	public String getNome()
	{
		return this.nome;
	}
	
	public int getValor()
	{
		return this.valor;
	}
}
