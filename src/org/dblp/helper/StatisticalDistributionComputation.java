package org.dblp.helper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Utility class which contains methods to determine the sample maximum, <br/>
 * and minimum, the mean, the upper and lower quartile, the sum, the <br/>
 * the arithmetic mean, the variance and the standard deviation on a dataset.   
 * @author SergeOliver
 *
 */
public class StatisticalDistributionComputation implements MyPath{
	
	private static PrintWriter statisticalDistributionOutput;
	private static double maximum = 0;
	private static double upperQuartile = 0;
	private static double median = 0;
	private static double lowerQuartile = 0;
	private static double minimum = 0;
	private static double mean = 0;
	private static double variance = 0;
	private static double standardDeviation = 0;
	private static double total = 0;
	
	static{
		try {
			statisticalDistributionOutput = new PrintWriter(STATISTICALDISTRIBUTIONCOMPUTATIONOUTPUT);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String str = new SimpleDateFormat("dd/MM/yyyy  -  HH:mm:ss").
				format(Calendar.getInstance().getTime());
		statisticalDistributionOutput.println("DBLP UNIVERSITY OF TRIER   "+ str+
				"\n\nSTATISTICAL DISTRIBUTION COMPUTATION OUTPUT SUMMARY");
	}
	
	/**
	 * @param 
	 * @return Minimum of the list.
	 */
	public static double minimum(double[] dataset){
		Arrays.sort(dataset);
		return dataset[0];
	}

	/**
	 * @param 
	 * @return Lower quartile: the median of the first half of the list.
	 */
	public static double lowerQuartile(double[] dataset){
		int l = dataset.length;
		if(l < 4) return -1;
		Arrays.sort(dataset);
		return ((dataset.length/2)%2 != 0)? dataset[l/4] : 
			(dataset[l/4-1]+dataset[l/4])/2 ;
	}

	/**
	 * @param
	 * @return Median of the list.
	 */
	public static double median(double[] listOfStatisticalData) {
		int l = listOfStatisticalData.length;
		if(l < 2) return -1;
		Arrays.sort(listOfStatisticalData);
		return (listOfStatisticalData.length%2!=0)? listOfStatisticalData[l/2] : 
			(listOfStatisticalData[l/2-1]+listOfStatisticalData[l/2])/2 ;
	}

	/**
	 * @param
	 * @return Upper quartile: the median of the second half of the list.
	 */
	public static double upperQuartile(double[] dataset){
		double result;
		int l = dataset.length;
		if(l < 4) return -1;
		Arrays.sort(dataset);
		if((dataset.length/2)%2 != 0)
			 result = dataset[l*3/4];
		else if(dataset.length%2 != 0)
			result = (dataset[l*3/4+1]+dataset[l*3/4])/2;
		else
			result = (dataset[l*3/4-1]+dataset[l*3/4])/2;
		return result;
	}

	/**
	 * @param 
	 * @return Maximum of the list also called mode.
	 */
	public static double maximum(double[] dataset){
		Arrays.sort(dataset);
		return dataset[dataset.length-1];
	}

	/**
	 * @param 
	 * @return Sum of the list elements.
	 */
	public static double sum(double[] dataset){
		double sum = 0;
		for(double d : dataset)
			sum +=d;
		return sum;
	}

	/**
	 * @param
	 * @return Arithmetic mean: the average of the list of statistical data.
	 */
	public static double arithmeticMean(double[] dataset){
		int l = dataset.length;
		if(l < 2) return -1;
		double sum = 0;
		for(double n: dataset)
			sum += n;
		return sum/l;
	}
	
	/**
	 * @param
	 * @return Variance: measures how spread out the statistical data are.
	 * e.i. the average location of all data according to the mean value.
	 */
	public static double variance(double[] listOfStatisticalData){
		double mean = arithmeticMean(listOfStatisticalData);
		double var = 0;
		for(double n: listOfStatisticalData){
			var += Math.pow(n-mean, 2); 
		}
		return var/listOfStatisticalData.length;
	}
	
	/**
	 * @param
	 * @return Standard Deviation: measures how spread out the statistical data are.
	 * 	e.i. the average location of all data according to the mean value.
	 */
	public static double standardDeviation(double[] listOfStatisticalData){
		return Math.sqrt(variance(listOfStatisticalData));
	}
	
	static int count = 0;
	/**
	 * Adds new operation results to operation_result_output.csv
	 * @param 
	 * @param 
	 */
	public static boolean computeStatisticalDistribution(double[] arrayOfValues, String outputTitle){
		maximum = StatisticalDistributionComputation.maximum(arrayOfValues);
		upperQuartile = StatisticalDistributionComputation.upperQuartile(arrayOfValues);
		median = StatisticalDistributionComputation.median(arrayOfValues);
		lowerQuartile = StatisticalDistributionComputation.lowerQuartile(arrayOfValues);
		minimum = StatisticalDistributionComputation.minimum(arrayOfValues);
		mean = StatisticalDistributionComputation.arithmeticMean(arrayOfValues);
		variance = StatisticalDistributionComputation.variance(arrayOfValues);
		standardDeviation = StatisticalDistributionComputation.standardDeviation(arrayOfValues);
		total = StatisticalDistributionComputation.sum(arrayOfValues);
		statisticalDistributionOutput.println();
		statisticalDistributionOutput.println("____________________________");
		statisticalDistributionOutput.println();
		statisticalDistributionOutput.println((++count)+". "+outputTitle+"\n");
		statisticalDistributionOutput.println("Sample minimum = "+minimum);
		statisticalDistributionOutput.printf("%s %.2f\n", "Lower quartile = ",lowerQuartile);
		statisticalDistributionOutput.printf("%s %.2f\n", "Median = ",median);
		statisticalDistributionOutput.printf("%s %.2f\n", "Upper quartile = ",upperQuartile);
		statisticalDistributionOutput.println("Sample maximum = "+maximum);
		
		statisticalDistributionOutput.printf("\n%s %.2f", "Arithmetic Mean = ",
				mean);
		statisticalDistributionOutput.printf("\n%s %.2f", "Variance = ",variance);
		statisticalDistributionOutput.printf("\n%s %.2f\n\n", "Standard Deviation = ",
				standardDeviation);
		statisticalDistributionOutput.println("Total = "+total);
		return true;
	}
	

	@Override
	public void flushAndClose() {
		statisticalDistributionOutput.flush();
		statisticalDistributionOutput.close();
	}

	public static PrintWriter getStatisticalDistributionOutput() {
		return statisticalDistributionOutput;
	}

	public static void setStatisticalDistributionOutput(
			PrintWriter statisticalDistributionOutput) {
		StatisticalDistributionComputation.statisticalDistributionOutput = statisticalDistributionOutput;
	}

	public static double getMaximum() {
		return maximum;
	}

	public static void setMaximum(double maximum) {
		StatisticalDistributionComputation.maximum = maximum;
	}

	public static double getUpperQuartile() {
		return upperQuartile;
	}

	public static void setUpperQuartile(double upperQuartile) {
		StatisticalDistributionComputation.upperQuartile = upperQuartile;
	}

	public static double getMedian() {
		return median;
	}

	public static void setMedian(double median) {
		StatisticalDistributionComputation.median = median;
	}

	public static double getLowerQuartile() {
		return lowerQuartile;
	}

	public static void setLowerQuartile(double lowerQuartile) {
		StatisticalDistributionComputation.lowerQuartile = lowerQuartile;
	}

	public static double getMinimum() {
		return minimum;
	}

	public static void setMinimum(double minimum) {
		StatisticalDistributionComputation.minimum = minimum;
	}

	public static double getMean() {
		return mean;
	}

	public static void setMean(double mean) {
		StatisticalDistributionComputation.mean = mean;
	}

	public static double getVariance() {
		return variance;
	}

	public static void setVariance(double variance) {
		StatisticalDistributionComputation.variance = variance;
	}

	public static double getStandardDeviation() {
		return standardDeviation;
	}

	public static void setStandardDeviation(double standardDeviation) {
		StatisticalDistributionComputation.standardDeviation = standardDeviation;
	}

	public static double getTotal() {
		return total;
	}

	public static void setTotal(double total) {
		StatisticalDistributionComputation.total = total;
	}

	/**
	 * To test the reliability of all the methods implemented .
	 * @param
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		double[] eight = {1, 2, 3, 4, 5, 6, 7, 8};
		double[] nine = {1, 2, 3, 4, 5, 6, 7, 8, 9};
		double[] ten = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		System.out.println("Eight: the list and its half have an even number of elements");
		System.out.println("Sample maximum [8]: "+maximum(eight));
		System.out.println("Upper quartile [6.5]: "+upperQuartile(eight));
		System.out.println("Median [4.5]: "+median(eight));
		System.out.println("Lower quartile [2.5]: "+lowerQuartile(eight));
		System.out.println("Sample minimum [1]: "+minimum(eight));
		System.out.println("Total [36]: "+sum(eight));
		System.out.println("Arithmetic mean [4.5]: "+arithmeticMean(eight));
		System.out.printf("Varianz  [5.25]: %.2f\n",variance(eight));
		System.out.printf("Standardabweichung [2.29]: %.2f\n",standardDeviation(eight));
		
		System.out.println("\nnine: the list has an odd number of elements" +
				" but its half has an even one");
		System.out.println("Sample maximum [9]: "+maximum(nine));
		System.out.println("Upper quartile [7.5]: "+upperQuartile(nine));
		System.out.println("Median [5]: "+median(nine));
		System.out.println("Lower quartile [2.5]: "+lowerQuartile(nine));
		System.out.println("Sample minimum [1]: "+minimum(nine));
		System.out.println("Total [45]: "+sum(nine));
		System.out.println("Arithmetisches Mittel [5]: "+arithmeticMean(nine));
		System.out.printf("Varianz [6.67]: %.2f\n",variance(nine));
		System.out.printf("Standardabweichung [2.58]: %.2f\n",standardDeviation(nine));
		
		System.out.println("\nten: the list has an even number of elements" +
				" but its half has an odd one");
		System.out.println("Sample maximum [10]: "+maximum(ten));
		System.out.println("Upper quartile [8]: "+upperQuartile(ten));
		System.out.println("Median [5.5]: "+median(ten));
		System.out.println("Lower quartile [3]: "+lowerQuartile(ten));
		System.out.println("Sample minimum [1]: "+minimum(ten));
		System.out.println("Total [55]: "+sum(ten));
		System.out.println("Arithmetisches Mittel [5.5]: "+arithmeticMean(ten));
		System.out.printf("Varianz [8.25]: %.2f\n",variance(ten));
		System.out.printf("Standardabweichung [2.87]: %.2f\n",standardDeviation(ten));
	}

}
