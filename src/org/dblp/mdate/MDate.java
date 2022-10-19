/**
 * 
 */
package org.dblp.mdate;

import java.io.PrintWriter;
import java.util.SortedSet;
import java.util.TreeSet;

import org.dblp.helper.HtmlCode;
import org.dblp.helper.MyPath;
import org.dblp.helper.StatisticalDistributionComputation;

/**
 * Collects information about the modification dates per month and per year.<br/>
 * 
 * @author SergeOliver
 *
 */
public class MDate implements MyPath{

	private PrintWriter out, out2;
	
	/**
	 * Initializes the output files
	 */
	public MDate(){
		try {
			out = new PrintWriter(MONTHLYPUBLICATIONMDATEOUTPUT);
			out2 = new PrintWriter(YEARLYPUBLICATIONMDATEOUTPUT);
			HtmlCode.addHTMLHCode(out, "Monthly modification frequence of publications", 
					"Modifications", "Distribution of monthly modifications of publications up to now",
					"Monthly Modifications", HtmlCode.getDefaultWidth());
			HtmlCode.addHTMLHCode(out2, "Yearly modification frequence of publications",
					"Modifications", "Distribution of monthly modifications of publications up to now", 
					"Yearly Modifications", HtmlCode.getDefaultWidth());
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	public void flushAndClose(){
		HtmlCode.closeHTMLTags(out);
		HtmlCode.closeHTMLTags(out2);
		out.flush();
		out.close();
		out2.flush();
		out2.close();
	}
	
	/**
	 * @param dateToBeParsed 
	 * @param key of the publication
	 */
	public void getDate(String dateToBeParsed, String key){
		String[] str = dateToBeParsed.split("-");
		new Date(str[0], Integer.parseInt(str[1]), Integer.parseInt(str[2]), key);
	}
	
	/**
	 * Displays the monthly and yearly modification frequencies of publications
	 */
	public void displayResult(){
		monthlyModificationFrequency();
		yearlyModificationFrequency();
	}
	
	/**
	 * Outputs the monthly modification frequencies
	 */
	private void monthlyModificationFrequency(){
		System.out.println("\nOutput... number of modification per month");
		out.println("Month , Modifications ");
		for(int i=0; i<Date.mMonths.length; i++){
			out.printf("%s , %.0f\n",Month.values()[i].name(),Date.mMonths[i]);
		}	
		if(StatisticalDistributionComputation.computeStatisticalDistribution(Date.mMonths, "Number Of Modification Per Month")){
			out.println("</pre>");
			out.println("<p><h3>Box plot   "+HtmlCode.getDateAndTime()+"</h3></p>");
			out.println("<div id=\"containerBoxPlot\" style=\"width:1280px; height: 500px; margin: 0 auto\"></div>");
			out.println("<pre id=\"csvBoxPlot\" style=\"display:none\">");
			
			out.printf("%.2f, %.2f, %.2f, %.2f, %.2f\n", StatisticalDistributionComputation.getMinimum(), 
					StatisticalDistributionComputation.getLowerQuartile(),
					StatisticalDistributionComputation.getMedian(),
					StatisticalDistributionComputation.getUpperQuartile(),
					StatisticalDistributionComputation.getMaximum());
			out.println("</pre>");
			
			out.println("<pre id=\"meanBoxPlot\" style=\"display:none\">");
			out.printf("%.2f\n", StatisticalDistributionComputation.getMean());
			out.println("</pre>");

			out.println("<div id=\"variance_and_standard_deviation\">");
			out.printf("\n%s %.2f\n<br/>", "Variance: ",StatisticalDistributionComputation.getVariance());
			out.printf("\n%s %.2f\n<br/>", "Standard Deviation: ", StatisticalDistributionComputation.getStandardDeviation());
			out.println("</div>");
		}
	}

	/**
	 * Outputs the yearly modification frequencies
	 */
	private void yearlyModificationFrequency(){
		System.out.println("\nOutput... number of modification per year");
		SortedSet<String> yearSortedSet = new TreeSet<String>(Date.mYears.keySet());
		double[] values = new double[Date.mYears.size()];
		int i = 0;
		double numberOfModification = 0;
		out2.println("Year , Modifications");
		for(String year : yearSortedSet){
			numberOfModification = Date.mYears.get(year).getCounterValue();
			out2.printf("%s , %.0f\n",year,numberOfModification);
			values[i++] = numberOfModification;
		}
		if(StatisticalDistributionComputation.computeStatisticalDistribution(values, "Number Of Modifications Per Year")){
			out2.println("</pre>");
			out2.println("<p><h3>Box plot   "+HtmlCode.getDateAndTime()+"</h3></p>");
			out2.println("<div id=\"containerBoxPlot\" style=\"width:1280px; height: 500px; margin: 0 auto\"></div>");
			out2.println("<pre id=\"csvBoxPlot\" style=\"display:none\">");
			
			out2.printf("%.2f, %.2f, %.2f, %.2f, %.2f\n", StatisticalDistributionComputation.getMinimum(), 
					StatisticalDistributionComputation.getLowerQuartile(),
					StatisticalDistributionComputation.getMedian(),
					StatisticalDistributionComputation.getUpperQuartile(),
					StatisticalDistributionComputation.getMaximum());
			out2.println("</pre>");
			
			out2.println("<pre id=\"meanBoxPlot\" style=\"display:none\">");
			out2.printf("%.2f\n", StatisticalDistributionComputation.getMean());
			out2.println("</pre>");

			out2.println("<div id=\"variance_and_standard_deviation\">");
			out2.printf("\n%s %.2f\n<br/>", "Variance: ",StatisticalDistributionComputation.getVariance());
			out2.printf("\n%s %.2f\n<br/>", "Standard Deviation: ", StatisticalDistributionComputation.getStandardDeviation());
			out2.println("</div>");
		}
	}
}
