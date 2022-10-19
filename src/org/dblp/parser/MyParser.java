package org.dblp.parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.dblp.field.Field;
import org.dblp.field.Page;
import org.dblp.field.Person;
import org.dblp.field.Title;
import org.dblp.helper.IntKey;
import org.dblp.helper.MyPath;
import org.dblp.helper.StatisticalDistributionComputation;
import org.dblp.mdate.MDate;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Most important class of the application.<br/>
 * Contains the main method.
 * Constructs a SAXParser object that parses the dblp XML document.<br/>
 * Calls the methods of the classes Field, MDate or Operation when the input<br/>
 * data that they need are ready.<br/>
 * Contains utilities methods to keep track on the SAX parser.<br/>
 * Releases the resources on the log file when logging operations are <br/>
 * are completed.<br/>
 * Display the run time of the application.
 * @author SergeOliver
 * 
 */
public class MyParser extends DefaultHandler implements MyPath {

	/**
	 * Stores the system time in milliseconds when the SAX parser is at the <br/> 
	 * beginning of the XML document.
	 */
	private long start = 0;

	/**
	 * Stores the run time of the application in milliseconds.
	 */
	private int difference = 0;
	
	/**
	 * Stores the run time of the application in h:min:sec, min:sec or sec.
	 */
	private String runtime = "Application runtime = ";
	
	/**
	 * Stores the current position of the parser on the dbp.xml tree.<br/>
	 * level 1: &lt;dblp&gt; or &lt;/dblp&gt;<br/>
	 * level 2:		&lt;publication&gt; or &lt;/publication&gt; e.g. &lt;article&gt;...&lt;/article&gt;<br/>
	 * level 3: 			&lt;field&gt;	or &lt;/field&gt; e.g. &lt;author&gt;...&lt;/author&gt;
	 */
	private int level = 0;
	
	/**
	 * File which contains untrusted data from the XML document. These data should<br/>
	 *	be analyzed later.
	 */
	private static PrintWriter log;
	
	private Field field = new Field();
	
	private MDate mDate = new MDate();
	
	
	private StatisticalDistributionComputation sdComputation = new StatisticalDistributionComputation();
	
	/**
	 * Stores the name of the publication that is being parsed.
	 */
	private String tagName = "";
	
	/**
	 * Stores the key of the publication that is being parsed.
	 */
	private String key = "";
	
	/**
	 * Builds a string from characters inside the cross reference element that is being parsed.
	 */
	private StringBuilder crossrefBuilder = new StringBuilder();
	
	/**
	 * Builds a string from characters inside the page element that is being parsed.
	 */
	private StringBuilder pageBuilder = new StringBuilder();
	
	/**
	 * Builds a string from characters inside the author/editor element that is being parsed.
	 */
	private StringBuilder personNameBuilder = new StringBuilder();
	
	/**
	 * Builds a string from characters inside the title element that is being parsed.
	 */
	private StringBuilder titleBuilder = new StringBuilder();
	
	/**
	 * Is the SAX parser inside an electronic version element?
	 */
	private boolean insideEe = false;
	
	/**
	 * Is the SAX parser inside a title element?
	 */
	private boolean insideTitle = false;
	
	/**
	 * Stores the the name of authors/editors of the publication that is being parsed
	 */
	private List<Person> tmpPersonList = new ArrayList<>();
	
	/**
	 * Stores the publication year of the publication that is being parsed.
	 */
	private IntKey tmpYear = new IntKey(-1);
	
	
	/**
	 * Maximal number of untrusted data that should be printed in the log file<br/>
	 * for each type of analysis.
	 */
	private static final int MAX = 100;
	
	/**
	 * Defines a factory API that enables a MyParser instance to configure and 
	 * obtain a SAX based parser to parse the XML document.
	 * Creates a new instance of a SAXParser using the currently configured
	 * factory parameters.
	 * Returns the XMLReader that is encapsulated by the implementation of 
	 * this class.
	 * Allow MyParser to register a content event handler.
	 * Parse the XML document from a system identifier (IRU). It's equivalent to
	 * xmlR.parse(new InputSource(XML document)). 
	 * @param dblpxml: XML document to be parsed.
	 * @throws ParserConfigurationException if a parser which satisfies the 
	 * requested configuration cannot be created.
	 * @throws SAXException if any SAX errors occur during processing.
	 * @throws IOException  if any IO error occurs interacting with the
     * InputStream.
	 */
	public MyParser(String dblpxml) throws ParserConfigurationException, SAXException, IOException{
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xmlR = sp.getXMLReader();
		xmlR.setContentHandler(this);
		xmlR.parse(dblpxml);
	}

	/**
	 * Create a new MyParser instance to parse the short version of dblp.xml<br/>
	 * (DBLPXMLMOCKUP) in order to test new functionalities of the software.<br/>
	 * Or create a new MyParser instance to parse the dblp.xml (DBLPXML) in oder<br/>
	 * to generate statistical data.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
//			new MyParser(MyPath.DBLPXMLMOCKUP);
			new MyParser(MyPath.DBLPXML);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
	 * Receive notification of the beginning of the document.<br/>
	 * Stores the system current time in milliseconds.<br/>
	 * Display in the log file when it was generated. The date and time. 
	 */
	@Override
	public void startDocument(){
		System.out.println("Parsing started...");
		start = System.currentTimeMillis();
		try {
			log = new PrintWriter(LOG);
			log.println(new SimpleDateFormat("dd.MM.yyyy  -  HH:mm:ss").format(Calendar.getInstance().getTime()));
			log.println();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, 
	 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 * Receive notification of the start of an element.<br/>
	 * Stores the key of the publication that is being parsed and reset the<br/> 
	 * counter of fields to zero.<br/>
	 * Stores the name of the current publication in order to use it later<br/>
	 * in the methods characters.<br/>
	 * Counts the fields inside the current publication.<br/>
	 * Ignore the title of homepages because they are not relevant for the analysis.
	 */
	@Override
	public void startElement(String url, String localName, String qName, Attributes atts){
		
		incrementLevel();
		tagName = qName;
		
		if(level == 2){
			key = atts.getValue("key");
			Field.resetFieldCounter();
			mDate.getDate(atts.getValue("mdate"), key);
		}
		
		if(level == 3)
			Field.incrementFieldCounter(key);
		if(field.isEe(tagName))
			insideEe = true;
		if(field.isTitle(tagName) && !key.startsWith("homepages")){
			insideTitle = true;
		}
	}
	
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 * Receive notification of character data inside an element.<br/>
	 * Builds the appropriate string from the characters inside the element <br/>
	 * that is being parsed.
	 */
	@Override
	public void characters(char[] ch, int start, int length){
		
		if(field.isPerson(tagName)){
			personNameBuilder.append(ch, start, length);
		}
		if(field.isYear(tagName)){
			tmpYear = new IntKey(Integer.parseInt(new String(ch, start, length))); 
		}
		if(insideTitle){
			titleBuilder.append(ch, start, length);
		}
		if(field.isCrossref(tagName)){
			crossrefBuilder.append(ch, start, length);
		}
		if(field.isPages(tagName)){
			pageBuilder.append(ch, start, length);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, 
	 * java.lang.String, java.lang.String)
	 * Receive notification of the end of an element.
	 * Stores  a person object in the list, when the parser goes out of a author/editor<br/>
	 * element.
	 * Invokes the update methods of the class fields when the parse go out<br/>
	 * goes out of a publication element.
	 */
	@Override
	public void endElement(String url, String localName, String qName){
		
		decrementLevel();

		if(field.isPerson(qName)){//parser is outside a author/editor element.
			if(!personNameBuilder.toString().isEmpty()){
				tmpPersonList.add(new Person(personNameBuilder.toString(), key));
				personNameBuilder.setLength(0);
			}
		}
		
		if(field.isTitle(qName))//parser is outside a title element.
			insideTitle = false;
		
		
		if(level == 1){//parser is outside a publication element.
			
			field.updateFieldDistributionList(qName, key);
			field.updatePersonList(tmpPersonList, tmpYear);
			if(!titleBuilder.toString().isEmpty())
				field.updateTitleStat(new Title(titleBuilder.toString(), key));
			if(insideEe){
				if(tmpYear.getIntKeyValue() != -1)
					field.updateEeStat(tmpYear);
			}
			if(!crossrefBuilder.toString().isEmpty() && !crossrefBuilder.toString().startsWith("homepages"))
				field.updateCrossrefStat(crossrefBuilder.toString());
			if(!pageBuilder.toString().isEmpty())
				field.updatePagesStat(new Page(pageBuilder.toString(), key));
			
			tmpPersonList = new ArrayList<>();
			tmpYear = new IntKey(-1);
			insideEe = false;
			titleBuilder.setLength(0);
			crossrefBuilder.setLength(0);
			pageBuilder.setLength(0);
			key = "";
		}
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endDocument()
	 * Invokes the display methods of the classes field and mDate to print <br/>
	 * the statistical data out.<br/>
	 * Calculates the run time of the application and display it on the console.<br/>
	 * Flushes an closes all output files.<br/>
	 */
	@Override
	public void endDocument(){
		
		mDate.displayResult();
		field.displayResult();
//		calculate the run-time and display it
		difference = (int) (System.currentTimeMillis() - start);
		runtime += (difference >= 60000) ? difference/60000+"min:"+(difference%60000)/1000+"sec" : difference/1000+"sec";
		System.out.println(runtime);
		
//		release ressources
		mDate.flushAndClose();
		field.flushAndClose();
		sdComputation.flushAndClose();
		flushAndClose();
		System.out.println("Parsing ended...");
	}

	@Override
	public void flushAndClose() {
		log.flush();
		log.close();
	}

	public static PrintWriter getLog() {
		return log;
	}

	public int getLevel (){ 
		return level;
	}

	/**
	 * Increments the level when the SAX parser get in an XML element.<br/>
	 * ++level
	 */
	public void incrementLevel(){
		++level;
	}

	/**
	 * Decrements the level when the SAX parser get out of an XML element.<br/>
	 * --level 
	 */
	public void decrementLevel(){
		--level;
	}

	public static int getMAX(){
		return MAX;
	}

}
