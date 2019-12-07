package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import dao.Consultas;
import model.Dados;

public class ConsultasTest {
	private Consultas consultas = new Consultas();
	@Test
    public void deveriaBuscarTodasHashtags() throws Exception {
		ArrayList<Dados> dados = consultas.BuscaTodasHashtags();;
        assertTrue("Não existem dados de hashtags", dados.size() > 0);
    }
	
	@Test
	public void deveriaObterQuantidadeTweetsPorHashTag() throws Exception {
		int qntdBrowns = 384;
		assertEquals(consultas.ObterQuantidadeTweetsPorHashTag("Browns"), qntdBrowns);
	}
	@Test
	public void deveriaBuscarTodasDatas() throws Exception {
		ArrayList<Dados> dados = consultas.BuscaTodasDatas();
        assertTrue("Não existem dados de datas", dados.size() > 0);
	}
}
