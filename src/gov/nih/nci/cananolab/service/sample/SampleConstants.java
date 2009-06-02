package gov.nih.nci.cananolab.service.sample;


public class SampleConstants {
	// PubChem data source - Compound.
	public static final String COMPOUND  = "Compound";
	
	// PubChem data source - Substance.
	public static final String SUBSTANCE = "Substance";
	
	// PubChem data source - BioAssay.
	public static final String BIOASSAY = "BioAssay";

	// PubChem data source id - cid.
	public static final String COMPOUND_ID  = "cid";
	
	// PubChem data source id - sid.
	public static final String SUBSTANCE_ID = "sid";
	
	// PubChem data source id - bid.
	public static final String BIOASSAY_ID = "bid";

	// PubChem data source array.
	public static final String[] PUBCHEM_DS_LIST = {COMPOUND, SUBSTANCE, BIOASSAY};
	
	// PubChem URL.
	public static final String PUBCHEM_URL = "http://pubchem.ncbi.nlm.nih.gov/summary/summary.cgi?";

}
