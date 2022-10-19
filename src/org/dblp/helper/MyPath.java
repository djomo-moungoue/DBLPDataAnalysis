/**
 * 
 */
package org.dblp.helper;

/**
 * Stores the path to all input/output file of the application.
 * Contains the signature of a method to be implemented in order to<br/>
 * flush and close output files when writing operations are performed <br/>
 * completely.
 * @author SergeOliver
 *
 */
public interface MyPath {

	public static final String DBLPDTD = "F:DBLP/dblpXMLDTD/dblp.dtd";
	public static final String DBLPXML = "F:DBLP/dblpXMLDTD/dblp.xml";
	public static final String DBLPXMLMOCKUP = "F:DBLP/dblpXMLDTD/dblpmockup.xml";
	public static final String LOG = "file/log.log";
	public static final String STATISTICALDISTRIBUTIONCOMPUTATIONOUTPUT = "file/statistical_distribution_computation_output.csv";
	public static final String CROSSREFPERBOOKOUTPUT = "file/crossref_per_book_output.html";
	public static final String EEPERYEAROUPTUT = "file/ee_per_year_output.html";
	public static final String FIELDPERPUBLICATIONOUTPUT = "file/field_per_publication.html";
	public static final String MONTHLYPUBLICATIONMDATEOUTPUT = "file/monthly_mdate_output.html";
	public static final String NEWAUTHORPERYEAROUTPUT = "file/new_author_per_year_output.html";
	public static final String NUMBEROFCHARACTERSPERTITLEOUPTUT = "file/number_of_characters_per_title_output.html";
	public static final String NUMBEROFPAGESPERCROSSREFOUTPUT = "file/number_of_pages_per_crosref.html";
	public static final String NUMBEROFWORDSPERTITLEOUPTUT = "file/number_of_words_per_title_output.html";
	public static final String PERSONNAMELENGTHOUPTUT = "file/person_name_length_output.html";
	public static final String YEARLYPUBLICATIONMDATEOUTPUT = "file/yearly_mdata_output.html";

	/**
	 * To be implemented in order to flush and close output files when writing <br/>
	 * operations are performed completely.
	 */
	void flushAndClose();
	
}
