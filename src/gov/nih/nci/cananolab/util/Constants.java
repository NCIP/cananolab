package gov.nih.nci.cananolab.util;

import java.util.HashMap;
import java.util.Map;

public class Constants {
	public static final String DOMAIN_MODEL_NAME = "caNanoLab";

	public static final String SDK_BEAN_JAR = "caNanoLabSDK-beans.jar";

	public static final String CSM_APP_NAME = "caNanoLab";

	public static final String DATE_FORMAT = "MM/dd/yyyy";

	public static final String ACCEPT_DATE_FORMAT = "MM/dd/yy";

	public static final String UNCOMPRESSED_FILE_DIRECTORY = "decompressedFiles";

	public static final String EMPTY = "N/A";

	// caNanoLab property file
	public static final String CANANOLAB_PROPERTY = "caNanoLab.properties";

	public static final String BOOLEAN_YES = "Yes";

	public static final String BOOLEAN_NO = "No";

	public static final String[] BOOLEAN_CHOICES = new String[] { BOOLEAN_YES,
			BOOLEAN_NO };

	public static final String STRING_OPERAND_EQUALS = "equals";
	public static final String STRING_OPERAND_CONTAINS = "contains";

	public static final String DEFAULT_SAMPLE_PREFIX = "NANO-";

	public static final String DEFAULT_APP_OWNER = "NCICBIIT";

	public static final String DEFAULT_GRID_INDEX_SERVER_URL = "http://cagrid-index.nci.nih.gov:8080/wsrf/services/DefaultIndexService";

	public static final String APP_OWNER;
	static {
		String appOwner = PropertyUtils.getProperty(CANANOLAB_PROPERTY,
				"applicationOwner");
		if (appOwner == null || appOwner.length() == 0)
			appOwner = DEFAULT_APP_OWNER;
		APP_OWNER = appOwner.trim();
	}

	public static final String VIEW_COL_DELIMITER = "~~~";

	public static final String VIEW_CLASSNAME_DELIMITER = "!!!";

	public static final String SAMPLE_PREFIX;
	static {
		String samplePrefix = PropertyUtils.getProperty(CANANOLAB_PROPERTY,
				"samplePrefix");
		if (samplePrefix == null || samplePrefix.length() == 0)
			samplePrefix = DEFAULT_SAMPLE_PREFIX;
		SAMPLE_PREFIX = samplePrefix.trim();
	}

	public static final String GRID_INDEX_SERVICE_URL;
	static {
		String gridIndexServiceURL = PropertyUtils.getProperty(
				CANANOLAB_PROPERTY, "gridIndexServiceURL");
		if (gridIndexServiceURL == null || gridIndexServiceURL.length() == 0)
			gridIndexServiceURL = DEFAULT_GRID_INDEX_SERVER_URL;
		GRID_INDEX_SERVICE_URL = gridIndexServiceURL.trim();
	}

	/*
	 * The following Strings are nano specific
	 */
	public static final String[] DEFAULT_CHARACTERIZATION_SOURCES = new String[] { APP_OWNER };

	public static final String ASSOCIATED_FILE = "Other Associated File";

	public static final String PROTOCOL_FILE = "Protocol File";

	public static final String FOLDER_PARTICLE = "particles";

	// public static final String FOLDER_REPORT = "reports";

	public static final String FOLDER_PUBLICATION = "publications";

	public static final String FOLDER_PROTOCOL = "protocols";

	public static final String[] DEFAULT_POLYMER_INITIATORS = new String[] {
			"Free Radicals", "Peroxide" };

	public static final String CHARACTERIZATION_FILE = "characterizationFile";

	public static final int MAX_VIEW_TITLE_LENGTH = 23;

	public static final String CSM_DATA_CURATOR = APP_OWNER + "_DataCurator";

	public static final String CSM_RESEARCHER = APP_OWNER + "_Researcher";

	public static final String CSM_ADMIN = APP_OWNER + "_Administrator";

	public static final String CSM_PUBLIC_GROUP = "Public";

	public static final String[] VISIBLE_GROUPS = new String[] {
			CSM_DATA_CURATOR, CSM_RESEARCHER };

	public static final String AUTO_COPY_ANNOTATION_PREFIX = "COPY";

	public static final String AUTO_COPY_ANNNOTATION_VIEW_COLOR = "red";

	public static final String CSM_READ_ROLE = "R";

	public static final String CSM_DELETE_ROLE = "D";

	public static final String CSM_EXECUTE_ROLE = "E";

	public static final String CSM_CURD_ROLE = "CURD";

	public static final String CSM_CUR_ROLE = "CUR";

	public static final String CSM_READ_PRIVILEGE = "READ";

	public static final String CSM_EXECUTE_PRIVILEGE = "EXECUTE";

	public static final String CSM_DELETE_PRIVILEGE = "DELETE";

	public static final String CSM_CREATE_PRIVILEGE = "CREATE";

	public static final String CSM_PG_PROTOCOL = "protocol";

	public static final String CSM_PG_SAMPLE = "sample";

	public static final String CSM_PG_PUBLICATION = "publication";

	public static final short CHARACTERIZATION_ROOT_DISPLAY_ORDER = 0;

	// This is a hack to querying based on .class to work in case of multi-level
	// inheritance with joined-subclass
	// TODO check the order generated in the hibernate mapping file for each
	// release, the parent class is 1.
	public static final Map<String, Integer> FUNCTIONALIZING_ENTITY_SUBCLASS_ORDER_MAP = new HashMap<String, Integer>();
	static {
		FUNCTIONALIZING_ENTITY_SUBCLASS_ORDER_MAP.put(new String(
				"OtherFunctionalizingEntity"), new Integer(2));
		FUNCTIONALIZING_ENTITY_SUBCLASS_ORDER_MAP.put(new String("Biopolymer"),
				new Integer(3));
		FUNCTIONALIZING_ENTITY_SUBCLASS_ORDER_MAP.put(new String("Antibody"),
				new Integer(4));
		FUNCTIONALIZING_ENTITY_SUBCLASS_ORDER_MAP.put(new String(
				"SmallMolecule"), new Integer(5));
	}

	/* image file name extension */
	public static final String[] IMAGE_FILE_EXTENSIONS = { "AVS", "BMP", "CIN",
			"DCX", "DIB", "DPX", "FITS", "GIF", "ICO", "JFIF", "JIF", "JPE",
			"JPEG", "JPG", "MIFF", "OTB", "P7", "PALM", "PAM", "PBM", "PCD",
			"PCDS", "PCL", "PCX", "PGM", "PICT", "PNG", "PNM", "PPM", "PSD",
			"RAS", "SGI", "SUN", "TGA", "TIF", "TIFF", "WMF", "XBM", "XPM",
			"YUV", "CGM", "DXF", "EMF", "EPS", "MET", "MVG", "ODG", "OTG",
			"STD", "SVG", "SXD", "WMF" };

	public static final String[] PRIVATE_DISPATCHES = { "input", "create",
			"delete", "setupNew", "setupUpdate", "summaryEdit", "add",
			"remove", "save", "resetFinding", "getFinding", "drawMatrix" };

	public static final String PHYSICOCHEMICAL_CHARACTERIZATION = "physico chemical characterization";
	public static final String INVIVO_CHARACTERIZATION = "invivo characterization";
	public static final String INVITRO_CHARACTERIZATION = "invitro characterization";
	public static final String PHYSICOCHEMICAL_ASSAY_PROTOCOL = "physico-chemical assay";
	public static final String INVITRO_ASSAY_PROTOCOL = "in vitro assay";

	public static final String NODE_UNAVAILABLE = "Unable to connect to the grid location that you selected";

	// default discovery internal for grid index server
	public static final int DEFAULT_GRID_DISCOVERY_INTERVAL_IN_MINS = 240;

	// default discovery internal for grid index server
	public static final int DEFAULT_CSM_CLEANUP_INTERVAL_IN_MINS = 1;

	public static final String DOMAIN_MODEL_VERSION = "1.5";

	public static final String GRID_SERVICE_PATH = "wsrf-canano/services/cagrid/CaNanoLabService";

	// Default date format for exported file name.
	public static final String EXPORT_FILE_DATE_FORMAT = "yyyyMMdd_HH-mm-ss-SSS";

	// String for local search.
	public static final String LOCAL_SITE = APP_OWNER;

	// String for file repository entry in property file.
	public static final String FILE_REPOSITORY_DIR = "fileRepositoryDir";

	// String for site name entry in property file.
	public static final String SITE_NAME = "siteName";

	// String for site logo entry in property file.
	public static final String SITE_LOGO = "siteLogo";

	// File name of site logo.
	public static final String SITE_LOGO_FILENAME = "siteLogo.gif";

	// Maximum file size of site logo.
	public static final int MAX_LOGO_SIZE = 65536;

	// LOCATION
	public static final String LOCATION = "location";

	public static final int DISPLAY_TAG_TABLE_SIZE = 25;

	public static final String PLACEHOLDER_DATUM_CONDITION_CREATED_BY = "place holder";

	public static final String DOI_PREFIX = "http://dx.doi.org/";

	public static final String PUBMED_PREFIX = "http://www.ncbi.nlm.nih.gov/pubmed/";

	public static final String PUBMED_XML_PREFIX = "http://www.ncbi.nlm.nih.gov/entrez/utils/pmfetch.fcgi?db=PubMed&report=abstract&mode=xml&id=";

	public static final String ISI_PREFIX = "http://apps.isiknowledge.com/InboundService.do?Func=Frame&product=WOS&action=retrieve&SrcApp=EndNote&Init=Yes&SrcAuth=ResearchSoft&mode=FullRecord&UT=";
}
