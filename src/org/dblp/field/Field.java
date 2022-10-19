package org.dblp.field;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.dblp.helper.Counter;
import org.dblp.helper.HtmlCode;
import org.dblp.helper.IntKey;
import org.dblp.helper.MyPath;
import org.dblp.helper.StatisticalDistributionComputation;
import org.dblp.parser.MyParser;

/**
 * Contains the data structures and methods which are necessary<br/>
 * to analyze an generate statistical data on the elements of the dblp <br/>
 * XML document.<br/>
 * It is the second most important class of the application.
 * Ignores www publications because they are not relevant for our analysis.
 * @author SergeOliver
 *
 */
public class Field implements MyPath{

	/**
	 * Stores the number of fields of the current publication.
	 */
	private static int fieldCounter = 0;
	
	/**
	 * List of output files which contain the statistical data.
	 */
	private ArrayList<PrintWriter> out= new ArrayList<>();
	
	/**
	 * List of (publication type -> (number of fields -> number of publications<br/>
	 * which have the same number of fields) pairs) pairs.
	 */
	private Map<String, Map<IntKey, Counter>> fieldDistributionList = new HashMap<>();

	
	/**
	 * List of (year -> set of author/editor which made their first publication<br/>
	 * in the same year) pairs.
	 */
	private Map<IntKey, Set<Person>> personList = new HashMap<>();
	
	/**
	 * List of (number of characters -> number of author/editor names <br/>
	 * which have the same length) pairs.
	 */
	private Map<IntKey, Counter> charactersInPersonNames = new HashMap<>();
	
	/**
	 * List of (number of words -> number of titles which have the same <br/>
	 * number of words) pairs.
	 */
	private Map<IntKey, Counter> wordsPerTitles = new HashMap<>();
	
	/**
	 * List of (number of characters -> number of titles which have the same <br/>
	 * number of characters) pairs.
	 */
	private Map<IntKey, Counter> charactersPerTitles = new HashMap<>();
	
	/**
	 * List of (year -> number of electronic versions produced in the same year) pairs.
	 */
	private Map<IntKey, Counter> electronicVersionsPerYear = new HashMap<>();

	/**
	 * List of (number of cross references -> number of books which have the<br/>
	 * same number of cross references) pairs.
	 */
	private Map<String, Counter> crossReferencesPerBooks = new HashMap<>();

	/**
	 * List of (numbers of pages -> numbers of cross references which have <br/>
	 * the same numbers of pages) pairs.
	 */
	private Map<IntKey, Counter> pagesPerCrossReferences = new HashMap<>();
	
	/**
	 * List of titles of the column chars.
	 */
	private String[] chartTitle = {
			"Number of publication which have the same number of fields from 1 to 50",
			"Number of authors/editors which made their first publication in the same year",
			"Number of author/editror names which have the same number of characters",
			"Number of titles which have the same number of words from 1 to 50",
			"Number of electronic versions made the same year",
			"Number of books which have the same number of cross references from 1 to 150",
			"Number of cross references which have the same number of pages from 1 to 100",
			"Number of titles which have the same number of characters from 1 to 150"
	};
	
	/**
	 * List of names of the Y-Axis of the column char.
	 */
	private String[] yAxisName = {
			"Publications", "Authors / Editors", "Person Names", "Titles", 
			"Electronic Versions", "Books", "Cross References", "Titles"
	};
	
	private int defaultWidth = HtmlCode.getDefaultWidth();
	
	private int[] columnChartWidths = {
			defaultWidth, defaultWidth, defaultWidth, defaultWidth,
			defaultWidth, defaultWidth, defaultWidth, defaultWidth,
	};
	
	/**
	 * Stores the key of publications which have too many fields compared <br/>
	 * to the median value of the distribution.
	 */
	private Set<String> setOfPubKeyWithUnreliableNbrOfFields = new TreeSet<String>();
	
	/**
	 * Stores the key of book which have too many fields compared to the <br/>
	 * median value of the distribution.
	 */
	private Set<String> setOfBookKeyWithUnreliableNbrOfCrossrefs = new TreeSet<String>();
	/**
	 * List of titles of the column chars.
	 */
	private String[] columnChartTitles = {
			"Number of publication which have the same number of fields from 1 to 50",
			"Number of authors/editors which made their first publication in the same year",
			"Number of author/editror names which have the same number of characters",
			"Number of titles which have the same number of words from 1 to 50",
			"Number of electronic versions made the same year",
			"Number of cross reference numbers which have the same number of cross references from 1 to 150",
			"Number of cross references which have the same number of pages from 1 to 100",
			"Number of titles which have the same number of characters from 1 to 150"
	};
	
	private String[] boxPlotTitles = {
			"Frequency distribution of fields inside publications",
			"Frequency distribution of new authors/editors up to now",
			"Frequency distribution of characters inside person names",
			"Frequency distribution of words inside titles",
			"Frequency distribution of electronic versions un to now",
			"Frequency distribution of cross references on books",
			"Frequency distribution of pages inside cross references",
			"Frequency distribution of characters inside titles"
	};
	
	/**
	 * List of names of the Y-Axis of the column char.
	 */
	private String[] columnChartyAxisNames = {
			"Publications", "Authors / Editors", "Person Names", "Titles", 
			"Electronic Versions", "Books", "Cross References", "Titles"
	};
	
	private String[] seriesNames = {
			"Fields", "Authors / Editors", "Characters in Person Names", "Words in Titles", 
			"Electronic Versions", "Cross References", "Pages", "Characters in Titles"
	};
	
	/**
	 * Creates the output files and stores in the list of outputs.<br/>
	 * Invokes the addHTMLCode method for each output file.
	 */
	public Field(){
		try {
			out.add(0, new PrintWriter(FIELDPERPUBLICATIONOUTPUT));
			out.add(1, new PrintWriter(NEWAUTHORPERYEAROUTPUT));
			out.add(2, new PrintWriter(PERSONNAMELENGTHOUPTUT));
			out.add(3, new PrintWriter(NUMBEROFWORDSPERTITLEOUPTUT));
			out.add(4, new PrintWriter(EEPERYEAROUPTUT));
			out.add(5, new PrintWriter(CROSSREFPERBOOKOUTPUT));
			out.add(6, new PrintWriter(NUMBEROFPAGESPERCROSSREFOUTPUT));
			out.add(7, new PrintWriter(NUMBEROFCHARACTERSPERTITLEOUPTUT));
			if(out.size() == chartTitle.length && out.size() == yAxisName.length){
				for(int i=0; i<out.size(); i++){
					HtmlCode.addHTMLHCode(out.get(i), columnChartTitles[i], 
							columnChartyAxisNames[i], boxPlotTitles[i], seriesNames[i], columnChartWidths[i]);
				}
			}else{
				System.out.println("Verify the list of Title, output file ...");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
//	----------------------------------------NOTIFICATION---------------------------------------
	/**
	 * @param name
	 * @return True if name is an author/editor and false else.
	 */
	public boolean isPerson(String name){
		return (name.equals("author") || name.equals("editor")); 
	}
	
	/**
	 * @param name
	 * @return True if name is a year and false else.
	 */
	public boolean isYear(String name){
		return name.equals("year"); 
	}
	
	/**
	 * @param name
	 * @return True if name is a title and false else.
	 */
	public boolean isTitle(String name){
		return name.equals("title");
	}

	/**
	 * @param name
	 * @return True if name is an electronic version and false else.
	 */
	public boolean isEe(String name){
		return name.equals("ee");
	}
	
	/**
	 * @param name
	 * @return True if name is a cross reference and false else.
	 */
	public boolean isCrossref(String name){
		return name.equals("crossref");
	}
	
	/**
	 * @param name
	 * @return True if name is a page and false else.
	 */
	public boolean isPages(String name){
		return name.equals("pages");
	}
	
	public static void incrementFieldCounter(String key){
		++fieldCounter;
	}
	
	/**
	 * Reinitialize the counter of fields to zero.
	 */
	public static void resetFieldCounter(){
		fieldCounter = 0;
	}

//	---------------------------------------UPDATE-------------------------------------------------------
	int tmpCount = 0;
	/**
	 * @return Number of fields inside the publication.
	 */
	public static int getNumberOfFields(){
		return fieldCounter;
	}

	/**
	 * Ignores www publications because they are not relevant for our analysis.<br/>
	 * Inserts a new publication type in the list. Associates it to a new <br/>
	 * fields -> publications pair in which fields = fieldCounter and publication = 1.<br/> 
	 * Associates a new fields -> publications pair to publication types that are <br/>
	 * already in the list.<br/>
	 * Increments the number of publications when a existing number of fields<br/>
	 * is found inside the current publication. 
	 * @param qName: name of the current publication.
	 * @param key: key of the current publication.
	 */
	public void updateFieldDistributionList(String qName, String key){
			if(qName.equals("www")) return;
			if(!fieldDistributionList.containsKey(qName)){
				fieldDistributionList.put(qName, new HashMap<IntKey, Counter>());
				fieldDistributionList.get(qName).put(new IntKey(fieldCounter), new Counter());
			}else{
				if(!fieldDistributionList.get(qName).containsKey(new IntKey(fieldCounter)))
					fieldDistributionList.get(qName).put(new IntKey(fieldCounter), new Counter());
				else
					fieldDistributionList.get(qName).get(new IntKey(fieldCounter)).increment();
			}
			
//			add the key of publications, which too many fields, into log.log 
			if(tmpCount < MyParser.getMAX() && qName.equals("article") && 
					fieldCounter > 260){
				++tmpCount;
				setOfPubKeyWithUnreliableNbrOfFields.add("Article key: "+key+
						" --> # fields = "+fieldCounter);
			}
			else if(tmpCount < MyParser.getMAX() && qName.equals("book") &&
					fieldCounter > 400){
				++tmpCount;
				setOfPubKeyWithUnreliableNbrOfFields.add("Book key: "+key+
						" --> # fields = "+fieldCounter);
			}
			else if(tmpCount < MyParser.getMAX() && qName.equals("proceedings") &&
					fieldCounter > 200){
				++tmpCount;
				setOfPubKeyWithUnreliableNbrOfFields.add("Proceedings key: "+key+
						" --> # fields = "+fieldCounter);
			}
	}
	
	

	/**
	 * Do nothing for empty list or invalid year format
	 * Creates a new entry (year -> list of persons) for a new year.
	 * Or adds new persons to an existing year.
	 * @param currentList
	 * @param currentYear
	 */
	public void updatePersonList(List<Person> currentList, IntKey currentYear){
		if(currentList.size() == 0 || currentYear.getIntKeyValue() == -1) return;
		if(!personList.containsKey(currentYear)){
			personList.put(currentYear, new  HashSet<Person>());
			personList.get(currentYear).addAll(currentList);
		}else{
			personList.get(currentYear).addAll(currentList);
		}
	}
	
	/**
	 * Sorts the keys of the map in an ascending order.<br/>
	 * Each new year, removes all authors/editors that have already published before.<br/>
	 * e.g. 1960 -> a1, a2, a3 1961 -> a3, a5 => a3 should be removed in 1961.<br/>
	 * Counts the number of authors/editors whose name have the same length
	 * @param map: Map of (year -> set of persons) pairs
	 */
	private void filterPersonList(Map<IntKey, Set<Person>> map){
		Set<Person> tmpSet = new HashSet<>();
		SortedSet<IntKey> yearSortedSet = new TreeSet<IntKey>(map.keySet());
		for(IntKey year : yearSortedSet){
			map.get(year).removeAll(tmpSet); //difference
			tmpSet.addAll(map.get(year));//union
		}
		for(Person person : tmpSet){
			if(!charactersInPersonNames.containsKey(person.getNameLength()))
				charactersInPersonNames.put(person.getNameLength(), new Counter());
			else
				charactersInPersonNames.get(person.getNameLength()).increment();
		}
	}
	
	/**
	 * Does nothing for invalid number of characters or words.<br/>
	 * Creates a new entry (numbers of words -> one title) for a new number of words.
	 * Or increment the number of titles of an existing number of words.<br/>
	 * Similar procedure for the number of characters.
	 * @param title
	 */
	public void updateTitleStat(Title title){
			if(title.getCharacters().getIntKeyValue() < 0 || title.getWords().getIntKeyValue() < 0)
				return;
			if(!wordsPerTitles.containsKey(title.getWords()))
				wordsPerTitles.put(title.getWords(), new Counter());
			else
				wordsPerTitles.get(title.getWords()).increment();
	
			if(!charactersPerTitles.containsKey(title.getCharacters()))
				charactersPerTitles.put(title.getCharacters(), new Counter());
			else
				charactersPerTitles.get(title.getCharacters()).increment();
	}
	
	/**
	 * Creates a new entry (year -> number of electronic versions) for a new year.
	 * Or increment the number of electronic versions for an existing year.<br/>
	 * @param year
	 */
	public void updateEeStat(IntKey year){
		if(!electronicVersionsPerYear.containsKey(year))
			electronicVersionsPerYear.put(year, new Counter());
		else
			electronicVersionsPerYear.get(year).increment();
	}
	
	int tmpCount2 = 0;

	/**
	 * Creates a new entry (cross reference -> number of books) for a new cross reference.<br/>
	 * Or increments the number of books for an existing cross reference.<br/>
	 * Stores the key of books which have more than 3000 cross references for a late analysis.
	 * @param crossref
	 */
	public void updateCrossrefStat(String crossref){
		if(!crossReferencesPerBooks.containsKey(crossref))
			crossReferencesPerBooks.put(crossref, new Counter());
		else{
			crossReferencesPerBooks.get(crossref).increment();

			if(tmpCount2 < MyParser.getMAX() && crossReferencesPerBooks.get(crossref).getCounterValue() > 3000){
				++tmpCount2;
				setOfBookKeyWithUnreliableNbrOfCrossrefs.add(crossref);
			}
		}
	}
	
	/**
	 * Does nothing for invalid number of pages
	 * Creates a new entry (number of pages -> number of cross references) for a new number of pages.
	 * Or increment the number of cross references of an existing number of pages.<br/>
	 * @param page
	 */
	public void updatePagesStat(Page page){
		if(page.getPages().getIntKeyValue() < 0) return;
		if(!pagesPerCrossReferences.containsKey(page.getPages()))
			pagesPerCrossReferences.put(page.getPages(), new Counter());
		else{
			pagesPerCrossReferences.get(page.getPages()).increment();
		}
	}

//	---------------------------------------DISPLAY-------------------------------------------------------
	/**
	 * output the result of each statistical evaluation in a html file
	 */
	public void displayResult(){
		
//		Output: (fields, publications)
		numberOfFieldPerPublicationOutput();
		
//		Output: (year, new authors/editors)
		newAuthorOrEditorPerYearOutput();
		
//		Output: (characters, person names)
		personNameLengthOutput();
		
//		Output: (words, titles)
		numberOfWordsPerTitleOutput();	
		
//		Output: (characters, titles)
		numberOfCharactersPerTitleOutput();
		
//		Output: (year, electronic versions)
		numberOfElectronicVersionPerYearOutput();
		
//		Output: (cross references, books)
		numberOfCrossreferencesPerBookOutput();
		
//		Output: (pages, cross references)
		numberOfPagesPerCrossreferenceOutput();
	}
	
	/**
	 * number of fields -> [articles, inproceedings, other]
	 */
	Map<IntKey, int[]> map = new HashMap<>();
	/**
	 * Collects the number of fields per masterthesis, phdthesis, book, incollection, 
	 * proceedings and store them in a map called others.<br/>
	 * Concatenates the number of publications of existing number of fields<br/>
	 * Or creates an entry for new number of fields.<br/>
	 * @return List of aggregate number of publications.
	 */
	private double[] unionOfMaps(){
		Map<IntKey, Counter> others = new HashMap<>();
		SortedSet<String> publicationTypeSet = new TreeSet<String>(fieldDistributionList.keySet());
		ArrayList<Counter> listOfPublications = new ArrayList<>();
		for(String publicationType: publicationTypeSet){
			//masterthesis, phdthesis, book, incollection, proceedings
			if(!publicationType.equals("article") && !publicationType.equals("inproceedings")){
				if(!others.isEmpty()){
					//number of fields -> number of publications
					for(Entry<IntKey, Counter> field : fieldDistributionList.get(publicationType).entrySet()){
						if(others.containsKey(field.getKey())){//number of fields
								Counter counter = new Counter();
								int valueInOthers = others.get(field.getKey()).getCounterValue();
								int newValue = field.getValue().getCounterValue();
								counter.setCounterValue(valueInOthers+newValue);
								others.put(field.getKey(), counter);
						}else
							others.put(field.getKey(), field.getValue());
					}
				}else
					others.putAll(fieldDistributionList.get(publicationType));
			}
		}
		for(Entry<IntKey, Counter> o : others.entrySet()){
			listOfPublications.add(o.getValue());//number fo publications
		}
		double[] numberOfpublicationsArray = new double[listOfPublications.size()];
		int length = numberOfpublicationsArray.length;
		for(int j=0;j<length; j++)
			numberOfpublicationsArray[j] = listOfPublications.get(j).getCounterValue();
		SortedSet<IntKey> numberOfFieldsSet = new TreeSet<IntKey>(others.keySet());	
		for(IntKey numberOfField : numberOfFieldsSet){
			if(!map.containsKey(numberOfField)){
				map.put(numberOfField, new int[3]);
				map.get(numberOfField)[2] = others.get(numberOfField).getCounterValue();
			}else
				map.get(numberOfField)[2] = others.get(numberOfField).getCounterValue();
		}
		return numberOfpublicationsArray;
	}

	
	/**
	 * Outputs the number of fields per articles, inproceedings and other (masterthesis, <br/>
	 * phdthesis, book, incollection, proceedings)<br/>
	 * Only the number of publications which have 1 to 50 fields.
	 */
	private void numberOfFieldPerPublicationOutput() {
		System.out.println("\nOutput... number of field per publication");
		SortedSet<String> publicationTypeSet = new TreeSet<String>(fieldDistributionList.keySet()); 
		double[] numberOfpublicationsArray = unionOfMaps();
		double[] fiveNumbers = new double[5];
		double mean = 0;
		double variance = 0;
		double standardDeviation = 0;
		int count = 0;
		if(StatisticalDistributionComputation.computeStatisticalDistribution(numberOfpublicationsArray, 
				"Number Of Fields Per Other (incollection+proceedings+book+phdthesis+masterthesis)")){
		
			fiveNumbers[0] = StatisticalDistributionComputation.getMinimum();
			fiveNumbers[1] = StatisticalDistributionComputation.getLowerQuartile();
			fiveNumbers[2] = StatisticalDistributionComputation.getMedian();
			fiveNumbers[3] = StatisticalDistributionComputation.getUpperQuartile();
			fiveNumbers[4] = StatisticalDistributionComputation.getMaximum();
			mean = StatisticalDistributionComputation.getMean();
			variance = StatisticalDistributionComputation.getVariance();
			standardDeviation = StatisticalDistributionComputation.getStandardDeviation();
			System.out.println(++count+". M = "+mean+" V = "+variance+" SD = "+standardDeviation);
		}
		for(String publicationType: publicationTypeSet){//publication type
			SortedSet<IntKey> numberOfFieldSet = new TreeSet<IntKey>(fieldDistributionList.get(publicationType).keySet());//sorted set of number of fields
			double[] values = new double[fieldDistributionList.get(publicationType).size()]; //
			int i = 0;
			int numberOfPublication = 0;
			if(publicationType.equals("article")){
				for(IntKey numberOfField : numberOfFieldSet){
					if(!map.containsKey(numberOfField)){
						map.put(numberOfField, new int[3]);
						map.get(numberOfField)[0] = fieldDistributionList.get(publicationType).get(numberOfField).getCounterValue();
					}else{
						map.get(numberOfField)[0] = fieldDistributionList.get(publicationType).get(numberOfField).getCounterValue();
					}
					numberOfPublication = fieldDistributionList.get(publicationType).get(numberOfField).getCounterValue();
					values[i++] = numberOfPublication;
				}
				if(StatisticalDistributionComputation.computeStatisticalDistribution(values, "Number Of Fields Per "+publicationType)){
				
					fiveNumbers[0] += StatisticalDistributionComputation.getMinimum();
					fiveNumbers[1] += StatisticalDistributionComputation.getLowerQuartile();
					fiveNumbers[2] += StatisticalDistributionComputation.getMedian();
					fiveNumbers[3] += StatisticalDistributionComputation.getUpperQuartile();
					fiveNumbers[4] += StatisticalDistributionComputation.getMaximum();
					mean = Math.max(mean, StatisticalDistributionComputation.getMean());
					variance = Math.max(variance, StatisticalDistributionComputation.getVariance());
					standardDeviation = Math.max(standardDeviation, StatisticalDistributionComputation.getStandardDeviation());
					System.out.println(++count+". M = "+mean+" V = "+variance+" SD = "+standardDeviation);
				}
			}
			if(publicationType.equals("inproceedings")){
				for(IntKey numberOfField : numberOfFieldSet){
					if(!map.containsKey(numberOfField)){
						map.put(numberOfField, new int[3]);
						map.get(numberOfField)[1] = fieldDistributionList.get(publicationType).get(numberOfField).getCounterValue();
					}else{
						map.get(numberOfField)[1] = fieldDistributionList.get(publicationType).get(numberOfField).getCounterValue();
					}
					numberOfPublication = fieldDistributionList.get(publicationType).get(numberOfField).getCounterValue();
					values[i++] = numberOfPublication;
				}
				if(StatisticalDistributionComputation.computeStatisticalDistribution(values, "Number Of Fields Per"+publicationType)){

					fiveNumbers[0] += StatisticalDistributionComputation.getMinimum();
					fiveNumbers[1] += StatisticalDistributionComputation.getLowerQuartile();
					fiveNumbers[2] += StatisticalDistributionComputation.getMedian();
					fiveNumbers[3] += StatisticalDistributionComputation.getUpperQuartile();
					fiveNumbers[4] += StatisticalDistributionComputation.getMaximum();
					mean = Math.max(mean, StatisticalDistributionComputation.getMean());
					variance = Math.max(variance, StatisticalDistributionComputation.getVariance());
					standardDeviation = Math.max(standardDeviation, StatisticalDistributionComputation.getStandardDeviation());
					System.out.println(++count+". M = "+mean+" V = "+variance+" SD = "+standardDeviation);
				}
			}
			
		}
		SortedSet<IntKey> sortedSet = new TreeSet<IntKey>(map.keySet());
		out.get(0).println("Fields , Articles , Inproceedings , Other");
		int maxNumberOfFields = 50;
		for(IntKey field : sortedSet){
			if(--maxNumberOfFields == 0) return;
			out.get(0).printf("%s , %d, %d , %d\n", field.getIntKeyValue(), map.get(field)[0], map.get(field)[1], map.get(field)[2]);
		}
		out.get(0).println("</pre>");
		out.get(0).println("<p><h3>Box plot   "+HtmlCode.getDateAndTime()+"</h3></p>");
		out.get(0).println("<div id=\"containerBoxPlot\" style=\"width:1280px; height: 500px; margin: 0 auto\"></div>");
		out.get(0).println("<pre id=\"csvBoxPlot\" style=\"display:none\">");
		out.get(0).printf("%.2f, %.2f, %.2f, %.2f, %.2f\n", fiveNumbers[0], 
				fiveNumbers[1],
				fiveNumbers[2],
				fiveNumbers[3],
				fiveNumbers[4]);

		out.get(0).println("</pre>");
		
		
		out.get(0).println("<pre id=\"meanBoxPlot\" style=\"display:none\">");
		out.get(0).printf("%.2f\n", mean);
		System.out.printf("mean = %.2f\n ",mean);
		out.get(0).println("</pre>");

		out.get(0).println("<div id=\"variance_and_standard_deviation\">");
		out.get(0).printf("\n%s %.2f\n<br/>", "Max Variance: ",variance);
		out.get(0).printf("\n%s %.2f\n<br/>", "Max Standard Deviation: ", standardDeviation);
		out.get(0).println("</div>");
		MyParser.getLog().println("\n\nKeys of publication which have a number of fields very large compared to other");
		for(String publicationKey : setOfPubKeyWithUnreliableNbrOfFields){
			MyParser.getLog().println(publicationKey);
		}
		MyParser.getLog().println("\n______________________________________________________________\n");
	}
	
	/**
	 * Output format year , new authors/editors
	 */
	private void newAuthorOrEditorPerYearOutput() {
		out.get(1).printf("%s ,  %s\n ", "Year","New Authors / Editors");
		System.out.println("\nOutput... number of new authors per year");
		filterPersonList(personList);
		SortedSet<IntKey> yearSortedSet = new TreeSet<IntKey>(personList.keySet());
		double[] values = new double[personList.size()];
		int i = 0;
		for(IntKey year: yearSortedSet){
			SortedSet<Person> newPersonSet = new TreeSet<Person>(personList.get(year));
			out.get(1).printf("%d , %d\n", year.getIntKeyValue(), newPersonSet.size());
			values[i++] += newPersonSet.size();
		}
		if(StatisticalDistributionComputation.computeStatisticalDistribution(values, "New Authors Or Editors Per Year")){
			out.get(1).println("</pre>");
			out.get(1).println("<p><h3>Box plot   "+HtmlCode.getDateAndTime()+"</h3></p>");
			out.get(1).println("<div id=\"containerBoxPlot\" style=\"width:1280px; height: 500px; margin: 0 auto\"></div>");
			out.get(1).println("<pre id=\"csvBoxPlot\" style=\"display:none\">");
			out.get(1).printf("%.2f, %.2f, %.2f, %.2f, %.2f\n", StatisticalDistributionComputation.getMinimum(), 
					StatisticalDistributionComputation.getLowerQuartile(),
					StatisticalDistributionComputation.getMedian(),
					StatisticalDistributionComputation.getUpperQuartile(),
					StatisticalDistributionComputation.getMaximum());
			out.get(1).println("</pre>");
			
			
			out.get(1).println("<pre id=\"meanBoxPlot\" style=\"display:none\">");
			out.get(1).printf("%.2f\n", StatisticalDistributionComputation.getMean());
			out.get(1).println("</pre>");
			
			out.get(1).println("<div id=\"variance_and_standard_deviation\">");
			out.get(1).printf("\n%s %.2f\n<br/>", "Variance: ",StatisticalDistributionComputation.getVariance());
			out.get(1).printf("\n%s %.2f\n<br/>", "Standard Deviation: ", StatisticalDistributionComputation.getStandardDeviation());
			out.get(1).println("</div>");
		}
	}

	/**
	 * Output format length, person names
	 */
	private void personNameLengthOutput() {
		System.out.println("\nOutput... person name length");

		out.get(2).printf("%s ,  %s\n ", "Characters","Person names");
		SortedSet<IntKey> lengthSet = new TreeSet<IntKey>(charactersInPersonNames.keySet());
		double[] values = new double[charactersInPersonNames.size()];
		int i = 0;
		int numberOfName = 0;
		for(IntKey length : lengthSet){
			numberOfName = charactersInPersonNames.get(length).getCounterValue();
			out.get(2).printf("%d , %d\n", length.getIntKeyValue(), numberOfName);
			values[i++] = numberOfName; 
		}
		if(StatisticalDistributionComputation.computeStatisticalDistribution(values, "Person Names Length")){
			out.get(2).println("</pre>");
			out.get(2).println("<p><h3>Box plot   "+HtmlCode.getDateAndTime()+"</h3></p>");
			out.get(2).println("<div id=\"containerBoxPlot\" style=\"width:1280px; height: 500px; margin: 0 auto\"></div>");
			out.get(2).println("<pre id=\"csvBoxPlot\" style=\"display:none\">");
			out.get(2).printf("%.2f, %.2f, %.2f, %.2f, %.2f\n", StatisticalDistributionComputation.getMinimum(), 
					StatisticalDistributionComputation.getLowerQuartile(),
					StatisticalDistributionComputation.getMedian(),
					StatisticalDistributionComputation.getUpperQuartile(),
					StatisticalDistributionComputation.getMaximum());
			out.get(2).println("</pre>");
			
			out.get(2).println("<pre id=\"meanBoxPlot\" style=\"display:none\">");
			out.get(2).printf("%.2f\n", StatisticalDistributionComputation.getMean());
			out.get(2).println("</pre>");
			
			out.get(2).println("<div id=\"variance_and_standard_deviation\">");
			out.get(2).printf("\n%s %.2f\n<br/>", "Variance: ",StatisticalDistributionComputation.getVariance());
			out.get(2).printf("\n%s %.2f\n<br/>", "Standard Deviation: ", StatisticalDistributionComputation.getStandardDeviation());
			out.get(2).println("</div>");
		}
		MyParser.getLog().println("\n\nList of person names which have an unreliable number of characters");
		for(String unreliableName : Person.getSetOfUnreliableName()){
			MyParser.getLog().println(unreliableName);
		}
		MyParser.getLog().println("\n______________________________________________________________\n");
	}

	/**
	 * Output format number of words , number of titles.<br/>
	 * Only the number of titles which have 1 to 50 words.
	 */
	private void numberOfWordsPerTitleOutput() {
		System.out.println("\nOutput... number of words per title");
		out.get(3).printf("%s ,  %s\n ", "Words","Titles");
		SortedSet<IntKey> numberOfWordSet = new TreeSet<IntKey>(wordsPerTitles.keySet());
		double[] values = new double[wordsPerTitles.size()];
		int i = 0;
		int numberOfTitle = 0;
		int maxNumberOfWords = 50;
		for(IntKey numberOfWord : numberOfWordSet){
			numberOfTitle = wordsPerTitles.get(numberOfWord).getCounterValue();
			if(--maxNumberOfWords >= 0)
				out.get(3).printf("%d , %d\n", numberOfWord.getIntKeyValue(), numberOfTitle);
			values[i++] = numberOfTitle;
		}
		if(StatisticalDistributionComputation.computeStatisticalDistribution(values, "Number Of Words Per Title")){
			out.get(3).println("</pre>");
			out.get(3).println("<p><h3>Box plot   "+HtmlCode.getDateAndTime()+"</h3></p>");
			out.get(3).println("<div id=\"containerBoxPlot\" style=\"width:1280px; height: 500px; margin: 0 auto\"></div>");
			out.get(3).println("<pre id=\"csvBoxPlot\" style=\"display:none\">");
			out.get(3).printf("%.2f, %.2f, %.2f, %.2f, %.2f\n", StatisticalDistributionComputation.getMinimum(), 
					StatisticalDistributionComputation.getLowerQuartile(),
					StatisticalDistributionComputation.getMedian(),
					StatisticalDistributionComputation.getUpperQuartile(),
					StatisticalDistributionComputation.getMaximum());
			out.get(3).println("</pre>");
			
			out.get(3).println("<pre id=\"meanBoxPlot\" style=\"display:none\">");
			out.get(3).printf("%.2f\n", StatisticalDistributionComputation.getMean());
			out.get(3).println("</pre>");
			
			out.get(3).println("<div id=\"variance_and_standard_deviation\">");
			out.get(3).printf("\n%s %.2f\n<br/>", "Variance: ",StatisticalDistributionComputation.getVariance());
			out.get(3).printf("\n%s %.2f\n<br/>", "Standard Deviation: ", StatisticalDistributionComputation.getStandardDeviation());
			out.get(3).println("</div>");
		}
	}

	/**
	 * Output format year, electronic versions
	 */
	private void numberOfElectronicVersionPerYearOutput() {
		System.out.println("\nOutput... number of electronic versions per year");
		out.get(4).printf("%s ,  %s\n ", "Year","Electronic versions");
		SortedSet<IntKey> eeSet = new TreeSet<IntKey>(electronicVersionsPerYear.keySet());
		double[] values = new double[electronicVersionsPerYear.size()];
		int i = 0;
		int numberOfEe = 0;
		for(IntKey year : eeSet){
			numberOfEe = electronicVersionsPerYear.get(year).getCounterValue();
			out.get(4).printf("%d , %d\n", year.getIntKeyValue(), numberOfEe);
			values[i++] = numberOfEe;
		}
		if(StatisticalDistributionComputation.computeStatisticalDistribution(values, "Number Of Electronic Versions Per Year")){
			out.get(4).println("</pre>");
			out.get(4).println("<p><h3>Box plot   "+HtmlCode.getDateAndTime()+"</h3></p>");
			out.get(4).println("<div id=\"containerBoxPlot\" style=\"width:1280px; height: 500px; margin: 0 auto\"></div>");
			out.get(4).println("<pre id=\"csvBoxPlot\" style=\"display:none\">");
			out.get(4).printf("%.2f, %.2f, %.2f, %.2f, %.2f\n", StatisticalDistributionComputation.getMinimum(), 
					StatisticalDistributionComputation.getLowerQuartile(),
					StatisticalDistributionComputation.getMedian(),
					StatisticalDistributionComputation.getUpperQuartile(),
					StatisticalDistributionComputation.getMaximum());
			out.get(4).println("</pre>");
			
			out.get(4).println("<pre id=\"meanBoxPlot\" style=\"display:none\">");
			out.get(4).printf("%.2f\n", StatisticalDistributionComputation.getMean());
			out.get(4).println("</pre>");
			
			out.get(4).println("<div id=\"variance_and_standard_deviation\">");
			out.get(4).printf("\n%s %.2f\n<br/>", "Variance: ",StatisticalDistributionComputation.getVariance());
			out.get(4).printf("\n%s %.2f\n<br/>", "Standard Deviation: ", StatisticalDistributionComputation.getStandardDeviation());
			out.get(4).println("</div>");
		}
	}

	/**
	 * Output format cross references , books.<br/>
	 * Only the numbers of books which have 1 to 150 cross references.
	 */
	private void numberOfCrossreferencesPerBookOutput() {
		System.out.println("\nOutput... number of cross references per book");
		out.get(5).printf("%s ,  %s\n ", "Cross References", "Books");
		Map<Counter, SortedSet<String>> map = new HashMap<>();
		for(Entry<String, Counter> e : crossReferencesPerBooks.entrySet()){
			if(!map.containsKey(e.getValue())){
				map.put(e.getValue(), new TreeSet<String>());
				map.get(e.getValue()).add(e.getKey());
			}else
				map.get(e.getValue()).add(e.getKey());
		}
		SortedSet<Counter> numberOfCrossrefSet = new TreeSet<Counter>(map.keySet());
		double[] values = new double[map.size()];
		int i = 0;
		int numberOfBook = 0;
		int maxNumberOfCrossReferences = 150;
		for(Counter numberOfCrossref : numberOfCrossrefSet){
			 numberOfBook = map.get(numberOfCrossref).size();
			 if(--maxNumberOfCrossReferences >= 0)
				 out.get(5).printf("%d , %d\n", numberOfCrossref.getCounterValue(), numberOfBook);
			values[i++] = numberOfBook;
		}
		if(StatisticalDistributionComputation.computeStatisticalDistribution(values, "Number Of Cross References Per Book")){
			out.get(5).println("</pre>");
			out.get(5).println("<p><h3>Box plot   "+HtmlCode.getDateAndTime()+"</h3></p>");
			out.get(5).println("<div id=\"containerBoxPlot\" style=\"width:1280px; height: 500px; margin: 0 auto\"></div>");
			out.get(5).println("<pre id=\"csvBoxPlot\" style=\"display:none\">");
			out.get(5).printf("%.2f, %.2f, %.2f, %.2f, %.2f\n", StatisticalDistributionComputation.getMinimum(), 
					StatisticalDistributionComputation.getLowerQuartile(),
					StatisticalDistributionComputation.getMedian(),
					StatisticalDistributionComputation.getUpperQuartile(),
					StatisticalDistributionComputation.getMaximum());
			out.get(5).println("</pre>");
			
			out.get(5).println("<pre id=\"meanBoxPlot\" style=\"display:none\">");
			out.get(5).printf("%.2f\n", StatisticalDistributionComputation.getMean());
			out.get(5).println("</pre>");
			
			out.get(5).println("<div id=\"variance_and_standard_deviation\">");
			out.get(5).printf("\n%s %.2f\n<br/>", "Variance: ",StatisticalDistributionComputation.getVariance());
			out.get(5).printf("\n%s %.2f\n<br/>", "Standard Deviation: ", StatisticalDistributionComputation.getStandardDeviation());
			out.get(5).println("</div>");
		}
		MyParser.getLog().println("\n\nKeys of books which have a number of cross references very large compared to other");
		for(String bookKey : setOfBookKeyWithUnreliableNbrOfCrossrefs){
			MyParser.getLog().println(bookKey);
		}
		MyParser.getLog().println("\n______________________________________________________________\n");
	}

	/**
	 * Output format Pages, Cross References.<br/>
	 * Only the numbers of Cross References which have 1 to 100 pages.
	 */
	private void numberOfPagesPerCrossreferenceOutput() {
		System.out.println("\nOutput... number of pages per cross reference");
		out.get(6).printf("%s , %s\n ","Pages", "Cross References");
		SortedSet<IntKey> numberOfpageSet = new TreeSet<IntKey>(pagesPerCrossReferences.keySet());
		double[] values = new double[pagesPerCrossReferences.size()];
		int i = 0;
		int numberOfCrossReferences = 0;
		int maxNumberOfPages = 100;
		for(IntKey numberOfPage : numberOfpageSet){
			numberOfCrossReferences = pagesPerCrossReferences.get(numberOfPage).getCounterValue();
			if(--maxNumberOfPages >= 0)
				out.get(6).printf("%d , %d\n", numberOfPage.getIntKeyValue(), numberOfCrossReferences);
			values[i++] = numberOfCrossReferences;
		}
		if(StatisticalDistributionComputation.computeStatisticalDistribution(values, "Number Of Pages Per Cross references")){
			out.get(6).println("</pre>");
			out.get(6).println("<p><h3>Box plot   "+HtmlCode.getDateAndTime()+"</h3></p>");
			out.get(6).println("<div id=\"containerBoxPlot\" style=\"width:1280px; height: 500px; margin: 0 auto\"></div>");
			out.get(6).println("<pre id=\"csvBoxPlot\" style=\"display:none\">");
			out.get(6).printf("%.2f, %.2f, %.2f, %.2f, %.2f\n", StatisticalDistributionComputation.getMinimum(), 
					StatisticalDistributionComputation.getLowerQuartile(),
					StatisticalDistributionComputation.getMedian(),
					StatisticalDistributionComputation.getUpperQuartile(),
					StatisticalDistributionComputation.getMaximum());
			out.get(6).println("</pre>");
			
			out.get(6).println("<pre id=\"meanBoxPlot\" style=\"display:none\">");
			out.get(6).printf("%.2f\n", StatisticalDistributionComputation.getMean());
			out.get(6).println("</pre>");
			
			out.get(6).println("<div id=\"variance_and_standard_deviation\">");
			out.get(6).printf("\n%s %.2f\n<br/>", "Variance: ",StatisticalDistributionComputation.getVariance());
			out.get(6).printf("\n%s %.2f\n<br/>", "Standard Deviation: ", StatisticalDistributionComputation.getStandardDeviation());
			out.get(6).println("</div>");
		}
		MyParser.getLog().println("\n\nKeys of cross references which have a number of pages with an invalid format");
		for(String crossrefKey : Page.getSetOfUnreliablePage()){
			MyParser.getLog().println(crossrefKey);
		}
		MyParser.getLog().println("\n______________________________________________________________\n");
	}
	

	/**
	 * Output format Characters, Titles.<br/>
	 * Only the numbers of titles which have 1 to 150 characters.
	 */
	private void numberOfCharactersPerTitleOutput() {
		System.out.println("\nOutput... number of characters per title");
		out.get(7).printf("%s , %s\n ", "Characters", "Titles");
		SortedSet<IntKey> characterSet = new TreeSet<IntKey>(charactersPerTitles.keySet());
		double[] values = new double[charactersPerTitles.size()];
		int i = 0;
		int numberOfTitle = 0;
		int maxNumberOfCharacters = 150;
		for(IntKey numberOfCharacter : characterSet){
			numberOfTitle = charactersPerTitles.get(numberOfCharacter).getCounterValue();
			if(--maxNumberOfCharacters >= 0)
				out.get(7).printf("%d , %d\n", numberOfCharacter.getIntKeyValue(), numberOfTitle);
			values[i++] = numberOfTitle;
		}
		if(StatisticalDistributionComputation.computeStatisticalDistribution(values, "Number Of Characters Per Title")){
			out.get(7).println("</pre>");
			out.get(7).println("<p><h3>Box plot   "+HtmlCode.getDateAndTime()+"</h3></p>");
			out.get(7).println("<div id=\"containerBoxPlot\" style=\"width:1280px; height: 500px; margin: 0 auto\"></div>");
			out.get(7).println("<pre id=\"csvBoxPlot\" style=\"display:none\">");
			out.get(7).printf("%.2f, %.2f, %.2f, %.2f, %.2f\n", StatisticalDistributionComputation.getMinimum(), 
					StatisticalDistributionComputation.getLowerQuartile(),
					StatisticalDistributionComputation.getMedian(),
					StatisticalDistributionComputation.getUpperQuartile(),
					StatisticalDistributionComputation.getMaximum());

			out.get(7).println("</pre>");
			
			out.get(7).println("<pre id=\"meanBoxPlot\" style=\"display:none\">");
			out.get(7).printf("%.2f\n", StatisticalDistributionComputation.getMean());
			out.get(7).println("</pre>");
			
			out.get(7).println("<div id=\"variance_and_standard_deviation\">");
			out.get(7).printf("\n%s %.2f\n<br/>", "Variance: ",StatisticalDistributionComputation.getVariance());
			out.get(7).printf("\n%s %.2f\n<br/>", "Standard Deviation: ", StatisticalDistributionComputation.getStandardDeviation());
			out.get(7).println("</div>");
		}
		MyParser.getLog().println("\n\nList of title which have an unreliable number of characters");
		for(String unreliableTitle : Title.getSetOfUnreliableTitle()){
			MyParser.getLog().println(unreliableTitle);
		}
		MyParser.getLog().println("\n______________________________________________________________\n");
	}

	//	---------------------------------------RELEASE RESSOURCES-------------------------------------------------------
	@Override
	public void flushAndClose() {
		for(PrintWriter pw : out){
			HtmlCode.closeHTMLTags(pw);
			pw.flush();
			pw.close();
		}
	}
	
	
}
