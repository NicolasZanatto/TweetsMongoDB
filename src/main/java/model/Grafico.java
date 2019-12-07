package model;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.orsoncharts.Chart3D;

import dao.ConnectionMongoDB;
import dao.Consultas;

public class Grafico extends JFrame{
	
	public void BuscaDadosGraficoBarra() throws IOException, ParseException
	{	
		Consultas consulta = new Consultas();
		GraficoBarra(consulta.BuscaTodasDatas());
		this.setVisible(true);
	}
	
	public void BuscaDadosGraficoPizza() throws IOException
	{	
		Consultas consulta = new Consultas();
		GraficoPizza(consulta.BuscaTodasHashtags());
		this.setVisible(true);
	}
	
	public void GraficoPizza(ArrayList<Dados> listaDados)
	{
		DefaultPieDataset dataPie = new DefaultPieDataset();
		
		for(Dados linha: listaDados)
		{
			dataPie.setValue(linha.getNome(), linha.getValor());
		}
		
		JFreeChart grafico = ChartFactory.createPieChart(
				"Gráfico",  // chart title
		        dataPie,         // data
		        true,         // include legend
		        true,
		        false
				);
		
		this.add( new ChartPanel(grafico) );
		
		this.pack();
	}
	
	
	public void GraficoBarra( ArrayList<Dados> listaDados )
	{	

		CategoryDataset dataBar = AddDadosGraficoBarra(listaDados); 
		
		JFreeChart chart = ChartFactory.createBarChart(
		        "Gráfico", //Chart Title
		        "Times", // Category axis
		        "Quantidade de Hashtags", // Value axis
		        dataBar,
		        PlotOrientation.VERTICAL,
		        true,true,false
		       );
		
		this.add( new ChartPanel(chart) );
		
		this.pack();

	}
	
	private CategoryDataset AddDadosGraficoBarra( ArrayList<Dados> listaDados ) 
	{	
	    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	    
	    for(Dados linha: listaDados)
	    {
	    	dataset.addValue(linha.getValor(), linha.getNome(), linha.getNome());
	    }

	    return dataset;
	  }
	/*
	public DefaultPieDataset AddDadosGraficoPizza(String texto, int quant)
	{
		DefaultPieDataset dataPie = new DefaultPieDataset();
		dataPie.setValue(texto, quant);
		
		return dataPie;
	}
*/
/*
	public static void main(String[] args)
	{
		ArrayList<Dados> lista = new ArrayList<>();
		String[] nomes = {"Saints", "Giants", "Falcons"};;
		
		System.out.println(nomes.length);
		
		for(int i = 0; i<nomes.length;i++)
		{
			Dados dados = new Dados(nomes[i], i+4);
			lista.add(dados);
		}
		
		Grafico grafico = new Grafico();

		//grafico.GraficoBarra(lista);
		grafico.GraficoPizza(lista);
		
		grafico.setVisible(true);
	}*/
}
