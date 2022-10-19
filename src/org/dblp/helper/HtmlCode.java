package org.dblp.helper;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Creates a web page for each type of statistic.<br/>
 * Draws a column chart to illustrate the output of each statistical analysis.
 * @author SergeOliver
 *
 */
public class HtmlCode {
	
	private static String dateAndTime = new SimpleDateFormat("dd/MM/yyyy  -  HH:mm:ss").format(Calendar.getInstance().getTime());
	private static int defaultHeigth = 500;
	private static int defaultWidth = 1250;
	
	public static String getDateAndTime() {
		return dateAndTime;
	}


	public static void setDateAndTime(String dateAndTime) {
		HtmlCode.dateAndTime = dateAndTime;
	}
	
	public static int getDefaultWidth() {
		return defaultWidth;
	}


	public static void setDefaultWidth(int defaultWidth) {
		HtmlCode.defaultWidth = defaultWidth;
	}


	public static int getDefaultHeigth() {
		return defaultHeigth;
	}


	public static void setDefaultHeigth(int defaultHeigth) {
		HtmlCode.defaultHeigth = defaultHeigth;
	}
	
	

	/**
	 * @param htmlOutput: web page in which the char will be drawn.
	 * @param columnChartTitle
	 * @param columnChartyAxis
	 * @param boxPlotTitle
	 * @param boxPlotxAxis
	 * @param mean value of the distribution
	 */
	public static void addHTMLHCode(PrintWriter htmlOutput, String columnChartTitle,
			String columnChartyAxis, String boxPlotTitle, String seriesName, int columnChartWidth){
		htmlOutput.println("<!DOCTYPE html>");
		htmlOutput.println("<html lang=\"en\">");
		htmlOutput.println("<head>");
		htmlOutput.println("<meta charset=\"UTF-8\"/>");
		htmlOutput.println("<title>dblp statistics</title>");
		htmlOutput.println();
		htmlOutput.println();
		htmlOutput.println("<!-- START JAVASCRIPT CODE -->");
		htmlOutput.println();
		htmlOutput.println();
		htmlOutput.println("<!-- ONLINE MODUS -->");
		htmlOutput.println();
//		htmlOutput.println("<!-- ");
		htmlOutput.println("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js\"></script>");
		htmlOutput.println("<script src=\"http://code.highcharts.com/highcharts.js\"></script>");
		htmlOutput.println("<script src=\"http://code.highcharts.com/highcharts-more.js\"></script>");
		htmlOutput.println("<script src=\"http://code.highcharts.com/modules/data.js\"></script>");
		htmlOutput.println("<script src=\"http://code.highcharts.com/modules/exporting.js\"></script>");
//		htmlOutput.println(" -->");
		htmlOutput.println();
		htmlOutput.println();
		htmlOutput.println("<!-- OFFLINE MODUS -->");
		htmlOutput.println();
		htmlOutput.println("<!-- ");
		htmlOutput.println("<script src=\"jquery-1.11.1.min.js\"></script>");
		htmlOutput.println("<script src=\"highcharts.js\"></script>");
		htmlOutput.println("<script src=\"highcharts-more.js\"></script>");
		htmlOutput.println("<script src=\"data.js\"></script>");
		htmlOutput.println("<script src=\"exporting.js\"></script>");
		htmlOutput.println(" -->");
		htmlOutput.println();
		htmlOutput.println();
		htmlOutput.println("<script>");
		drawColumnChart(htmlOutput, columnChartTitle, columnChartyAxis);
		htmlOutput.println("</script>");
		htmlOutput.println();
		htmlOutput.println("<script>");
		drawBoxPlot(htmlOutput, boxPlotTitle, seriesName);
		htmlOutput.println("</script>");
		htmlOutput.println();
		htmlOutput.println("<!-- END JAVASCRIPT CODE -->");
		htmlOutput.println();
		htmlOutput.println();
		htmlOutput.println("</head>");
		
		htmlOutput.println("<body>");
		htmlOutput.println("	<p><h1>DBLP Statistic - University of Trier</h1></p>");
		htmlOutput.println("<p><h3>Column Chart   "+dateAndTime+"</h3></p>");
		htmlOutput.println("<div id=\"containerColumnChart\" style=\"width:"+columnChartWidth+"px; height: "+defaultHeigth+"px; margin: 0 auto\"></div>");
		htmlOutput.println("<pre id=\"csvColumnChart\" style=\"display:none\">");
		
	}
	
	
	/**
	 * HighCharts function which which set the properties of the chart: type, <br/>
	 * background color, zoomType, title, subtitle, xAxis, categories, yAxis, data and series. 
	 * @param htmlOutput: web page in which the char will be drawn.
	 * @param title: title of the column char.
	 * @param yAxis: y-axis of the column char.
	 */
	private static void drawColumnChart(PrintWriter htmlOutput, String title, String yAxis){
		htmlOutput.println("$(function () {");
		htmlOutput.println("console.info(\"%c Start Column Chart Output\", \"color: darkgreen; font-family: arial; font-weight: bold; background-color: gray\");");
		htmlOutput.println("console.time(\"drawColumnChart Run Time\");");
		htmlOutput.println("var count = 0;");
		htmlOutput.println("console.info(count, \". Inside document ready\");");
		htmlOutput.println("var options = {");
		htmlOutput.println("chart:{");
		htmlOutput.println("type: \'column\',");
		htmlOutput.println("backgroundColor: \'#F0F0F8\',");
		htmlOutput.println("shadow: true,");
		htmlOutput.println("zoomType: \'xy\'");
		htmlOutput.println("},");
		
		htmlOutput.println("\n//Defines a title object.");
		htmlOutput.println("title: {");
		htmlOutput.println("text: \'"+title+"\'");
		htmlOutput.println("},");
		
		htmlOutput.println("\n//Defines a subtitle object.");
		htmlOutput.println("subtitle: {");
		htmlOutput.println("text: \'Data input from csv\'");
		htmlOutput.println("},");

		htmlOutput.println("\n//Defines a xAxis object.");
		htmlOutput.println("xAxis:{");
		htmlOutput.println("//Categories are represented by data in the first column of the csv document.");
		htmlOutput.println("categories: []");
		htmlOutput.println("},");

		htmlOutput.println("\n//Defines a yAxis object.");
		htmlOutput.println("yAxis:{");
		htmlOutput.println("title: {");
		htmlOutput.println("text: \'"+yAxis+"\'");
		htmlOutput.println("}");
		htmlOutput.println("},");
		
		htmlOutput.println("\n//Defines a data object and stores the content of the csv document inside.");
		htmlOutput.println("data: {");
		htmlOutput.println("csv: document.getElementById(\'csvColumnChart\').innerHTML");
		htmlOutput.println("},");
		htmlOutput.println("//Series are represented by data from the second column of the csv document.");
		htmlOutput.println("series: []");
		htmlOutput.println("};");
		htmlOutput.println("$(\"#containerColumnChart\").highcharts(options);");
		
		htmlOutput.println("console.info(++count, \". chart type: \", options.chart.type);");
		htmlOutput.println("console.info(++count, \". title: \", options.title.text);");
//		htmlOutput.println("console.info(++count, \". xAxis categories: \", options.xAxis.categories);");
		htmlOutput.println("console.info(++count, \". yAxis title: \", options.yAxis.title);");
//		htmlOutput.println("console.info(++count, \". data: \", options.data);");
//		htmlOutput.println("console.info(++count, \". series: \", options.series);");
		htmlOutput.println("console.timeEnd(\"drawBoxPlot Run Time\");");
		htmlOutput.println("console.info(\"%c End Column Chart Output\", \"color: darkgreen; font-family: arial; font-weight: bold; background-color: gray\");");
		htmlOutput.println("});");		
	}
	
		
		/**
		 * @param htmlOutput: web page in which the char will be drawn.
		 * @param title of the box plot
		 * @param xAxis of the box plot
		 */
			public static void drawBoxPlot(PrintWriter htmlOutput, String title, String seriesName){
			htmlOutput.println("$(document).ready(function(){");
			htmlOutput.println("console.info(\"%c Start Box Plot Output\", \"color: darkred; font-family: arial; font-weight: bold; background-color: gray\");");
			htmlOutput.println("console.time(\"drawBoxPlot Run Time\");");
			htmlOutput.println("//counter for the output on the console");
			htmlOutput.println("var count = 0;");
			htmlOutput.println("console.info(count, \". Inside document ready\");");
			
			htmlOutput.println();
			htmlOutput.println("// creates the options object");
			
			htmlOutput.println("var options = {");
			htmlOutput.println("chart:{");
			htmlOutput.println("type: \'boxplot\',");
			htmlOutput.println("backgroundColor: \'#F0F0F8\'");
			htmlOutput.println("},");
			htmlOutput.println("title:{");
			htmlOutput.println("text: \'"+title+"\'");
			htmlOutput.println("},");
			htmlOutput.println("xAxis: {");
			htmlOutput.println("categories: []");
			htmlOutput.println("},");
			htmlOutput.println("yAxis: {");
			htmlOutput.println("title: {");
			htmlOutput.println("text: \'Frequency Distribution\'");
			htmlOutput.println("},");
			htmlOutput.println("plotLines: [{");
			htmlOutput.println("value: parseFloat(document.getElementById('meanBoxPlot').innerHTML),");
			htmlOutput.println("color: \'green\',");
			htmlOutput.println("width: 2,");
			htmlOutput.println("label: {");
			htmlOutput.println("text: \'Arithmetic Mean: \'+document.getElementById('meanBoxPlot').innerHTML,");
			htmlOutput.println("align: \'left\',");
			htmlOutput.println("style: {");
			htmlOutput.println("color: \'darkgreen\'");
			htmlOutput.println("}");
			htmlOutput.println("}");
			htmlOutput.println("}]");
			htmlOutput.println("},");
			htmlOutput.println("legend: {");
			htmlOutput.println("enable: true");
			htmlOutput.println("},");
			htmlOutput.println("data: {");
			htmlOutput.println("csv : document.getElementById('csvBoxPlot').innerHTML,");
			htmlOutput.println("},");
			htmlOutput.println("series: []");
			htmlOutput.println("};");
			
			htmlOutput.println();
			htmlOutput.println("//stores the content of the csv property and tokenizes it");
			
			htmlOutput.println("var csvData = options.data.csv");
			htmlOutput.println("console.info(++count, \". options.data.csv: \", csvData);");
			htmlOutput.println("var csvItem = csvData.split(\",\");");
			htmlOutput.println("var parsedCsvItem = [];");
			
			htmlOutput.println();
			htmlOutput.println("//creates a series object");
			
			htmlOutput.println("var mySeries = {");
			htmlOutput.println("name: \'"+seriesName+"\',");
			htmlOutput.println("data: []");
			htmlOutput.println("};");
			
			htmlOutput.println();
			htmlOutput.println("// push the data in the series array");
			
			htmlOutput.println("$.each(csvItem,  function(itemNo, item){");
			htmlOutput.println("parsedCsvItem.push(parseFloat(item));");
			htmlOutput.println("});");
			htmlOutput.println("mySeries.data.push(parsedCsvItem);");	
			htmlOutput.println("options.series.push(mySeries);");
			
			htmlOutput.println();
			htmlOutput.println("//draws the box plot on the given container in the html page");
			
			htmlOutput.println("$(\"#containerBoxPlot\").highcharts(options);");
			
			htmlOutput.println();
			htmlOutput.println("//browser console output to check whether everything worked as planned");
			
			htmlOutput.println("console.info(++count, \". chart type: \", options.chart.type);");
			htmlOutput.println("console.info(++count, \". title: \", options.title.text);");
			htmlOutput.println("console.info(++count, \". xAxis categories: \", options.xAxis.categories);");
			htmlOutput.println("console.info(++count, \". yAxis title: \", options.yAxis.title);");
			htmlOutput.println("console.info(++count, \". data: \", options.data);");
			htmlOutput.println("console.info(++count, \". series: \", options.series);");
			htmlOutput.println("console.info(++count, \". mean: \", options.yAxis.plotLines.value);");
			htmlOutput.println("console.info(++count, \". options.yAxis.myPlotLines: \", options.yAxis.plotLines);");
			htmlOutput.println("console.timeEnd(\"drawBoxPlot Run Time\");");
			htmlOutput.println("console.info(\"%c End Box Plot Output\", \"color: darkred; font-family: arial; font-weight: bold; background-color: gray\");");
			htmlOutput.println("});");
		}
		
	
	/**
	 * Closes the pre, body and html tags.
	 * @param htmlOutput
	 */
	public static void closeHTMLTags(PrintWriter htmlOutput){
		htmlOutput.println("</body>");
		htmlOutput.println("</html>");
	}
}
