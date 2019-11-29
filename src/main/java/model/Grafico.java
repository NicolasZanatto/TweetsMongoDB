package model;

import java.net.UnknownHostException;

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

public class Grafico extends JFrame{
	
	public void GraficoPizza()
	{
		DefaultPieDataset dataPie = new DefaultPieDataset();
		dataPie.setValue("Giants", 7);
		dataPie.setValue("Seahawks", 8);
		dataPie.setValue("Saints", 5);
		
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
	public void GraficoBarra()
	{	

		CategoryDataset dataBar = AddDadosGraficoBarra(); 
		
		JFreeChart chart = ChartFactory.createBarChart(
		        "Gráfico", //Chart Title
		        "Year", // Category axis
		        "Hashtags", // Value axis
		        dataBar,
		        PlotOrientation.VERTICAL,
		        true,true,false
		       );
		
		this.add( new ChartPanel(chart) );
		
		this.pack();
	}
	
	private CategoryDataset AddDadosGraficoBarra() 
	{	
	    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(5, "Seahawks", "Seahawks");
		//
		dataset.addValue(10, "Giants", "Giants");
		dataset.addValue(8, "Saints", "Saints");
		//
	    return dataset;
	  }
	
	public DefaultPieDataset AddDadosGraficoPizza(String texto, int quant)
	{
		DefaultPieDataset dataPie = new DefaultPieDataset();
		dataPie.setValue(texto, quant);
		
		return dataPie;
	}


	public static void main(String[] args)
	{

		Grafico grafico = new Grafico();
		/* Descomentar a linha para exibir*/
		//grafico.GraficoBarra();
		grafico.GraficoPizza();
		
		grafico.setVisible(true);
	}
}
