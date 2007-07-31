package gov.nih.nci.calab.service.util;

import java.util.HashMap;
import java.util.Map;

public class CaNanoLabConstants {
	public static final String DOMAIN_MODEL_NAME = "caNanoLab";

	public static final String CSM_APP_NAME = "caNanoLab";

	public static final String DATE_FORMAT = "MM/dd/yyyy";

	public static final String ACCEPT_DATE_FORMAT = "MM/dd/yy";

	// Storage element
	public static final String STORAGE_BOX = "Box";

	public static final String STORAGE_SHELF = "Shelf";

	public static final String STORAGE_RACK = "Rack";

	public static final String STORAGE_FREEZER = "Freezer";

	public static final String STORAGE_ROOM = "Room";

	public static final String STORAGE_LAB = "Lab";

	// DataStatus
	public static final String MASK_STATUS = "Masked";

	public static final String ACTIVE_STATUS = "Active";

	// for Container type
	public static final String OTHER = "Other";

	public static final String[] DEFAULT_CONTAINER_TYPES = new String[] {
			"Tube", "Vial" };

	// Sample Container type
	public static final String ALIQUOT = "Aliquot";

	public static final String SAMPLE_CONTAINER = "Sample_container";

	// Run Name
	public static final String RUN = "Run";

	// File upload
	public static final String FILEUPLOAD_PROPERTY = "fileupload.properties";

	public static final String UNCOMPRESSED_FILE_DIRECTORY = "decompressedFiles";

	public static final String EMPTY = "N/A";

	// File input/output type
	public static final String INPUT = "Input";

	public static final String OUTPUT = "Output";

	// zip file name
	public static final String ALL_FILES = "ALL_FILES";

	public static final String URI_SEPERATOR = "/";

	// caNanoLab property file
	public static final String CANANOLAB_PROPERTY = "caNanoLab.properties";

	public static final String CSM_READ_ROLE = "R";

	public static final String CSM_READ_PRIVILEGE = "READ";

	public static final String CSM_EXECUTE_PRIVILEGE = "EXECUTE";

	public static final String CSM_DELETE_PRIVILEGE = "DELETE";

	// caLAB Submission property file
	public static final String SUBMISSION_PROPERTY = "exception.properties";

	public static final String BOOLEAN_YES = "Yes";

	public static final String BOOLEAN_NO = "No";

	public static final String[] BOOLEAN_CHOICES = new String[] { BOOLEAN_YES,
			BOOLEAN_NO };

	public static final String DEFAULT_SAMPLE_PREFIX = "NANO-";

	public static final String DEFAULT_APP_OWNER = "NCICB";

	public static final String APP_OWNER;
	static {
		String appOwner = PropertyReader.getProperty(CANANOLAB_PROPERTY,
				"applicationOwner");
		if (appOwner == null || appOwner.length() == 0)
			appOwner = DEFAULT_APP_OWNER;
		APP_OWNER = appOwner;
	}

	public static final String SAMPLE_PREFIX;
	static {
		String samplePrefix = PropertyReader.getProperty(CANANOLAB_PROPERTY,
				"samplePrefix");
		if (samplePrefix == null || samplePrefix.length() == 0)
			samplePrefix = DEFAULT_SAMPLE_PREFIX;
		SAMPLE_PREFIX = samplePrefix;
	}

	public static final String GRID_INDEX_SERVICE_URL;
	static {
		String gridIndexServiceURL = PropertyReader.getProperty(
				CANANOLAB_PROPERTY, "gridIndexServiceURL");
		GRID_INDEX_SERVICE_URL = gridIndexServiceURL;
	}

	/*
	 * The following Strings are nano specific
	 * 
	 */
	public static final String DENDRIMER_TYPE = "Dendrimer";

	public static final String POLYMER_TYPE = "Polymer";

	public static final String LIPOSOME_TYPE = "Liposome";

	public static final String CARBON_NANOTUBE_TYPE = "Carbon Nanotube";

	public static final String FULLERENE_TYPE = "Fullerene";

	public static final String QUANTUM_DOT_TYPE = "Quantum Dot";

	public static final String METAL_PARTICLE_TYPE = "Metal Particle";

	public static final String EMULSION_TYPE = "Emulsion";

	public static final String COMPLEX_PARTICLE_TYPE = "Complex Particle";

	public static final String CORE = "core";

	public static final String SHELL = "shell";

	public static final String COATING = "coating";

	public static final String[] DEFAULT_CHARACTERIZATION_SOURCES = new String[] { APP_OWNER };

	public static final String[] CARBON_NANOTUBE_WALLTYPES = new String[] {
			"Single (SWNT)", "Double (DWMT)", "Multiple (MWNT)" };

	public static final String REPORT = "Report";

	public static final String ASSOCIATED_FILE = "Other Associated File";

	public static final String PROTOCOL_FILE = "Protocol File";

	public static final String FOLDER_WORKFLOW_DATA = "workflow_data";

	public static final String FOLDER_PARTICLE = "particles";

	public static final String FOLDER_REPORT = "reports";

	public static final String FOLDER_PROTOCOL = "protocols";

	public static final String[] DEFAULT_POLYMER_INITIATORS = new String[] {
			"Free Radicals", "Peroxide" };

	public static final String[] DEFAULT_DENDRIMER_BRANCHES = new String[] {
			"1-2", "1-3" };

	public static final String[] DEFAULT_DENDRIMER_GENERATIONS = new String[] {
			"0", "0.5", "1.0", "1.5", "2.0", "2.5", "3.0", "3.5", "4.0", "4.5",
			"5.0", "5.5", "6.0", "6.5", "7.0", "7.5", "8.0", "8.5", "9.0",
			"9.5", "10.0" };

	public static final String CHARACTERIZATION_FILE = "characterizationFile";

	public static final String DNA = "DNA";

	public static final String PEPTIDE = "Peptide";

	public static final String SMALL_MOLECULE = "Small Molecule";

	public static final String PROBE = "Probe";

	public static final String ANTIBODY = "Antibody";

	public static final String IMAGE_CONTRAST_AGENT = "Image Contrast Agent";

	public static final String ATTACHMENT = "Attachment";

	public static final String ENCAPSULATION = "Encapsulation";

	public static final String[] FUNCTION_AGENT_TYPES = new String[] { DNA,
			PEPTIDE, SMALL_MOLECULE, PROBE, ANTIBODY, IMAGE_CONTRAST_AGENT };

	public static final String[] FUNCTION_LINKAGE_TYPES = new String[] {
			ATTACHMENT, ENCAPSULATION };

	public static final String RECEPTOR = "Receptor";

	public static final String ANTIGEN = "Antigen";

	public static final int MAX_VIEW_TITLE_LENGTH = 23;

	public static final String[] SPECIES_SCIENTIFIC = { "Mus musculus",
			"Homo sapiens", "Rattus rattus", "Sus scrofa",
			"Meriones unguiculatus", "Mesocricetus auratus", "Cavia porcellus",
			"Bos taurus", "Canis familiaris", "Capra hircus", "Equus Caballus",
			"Ovis aries", "Felis catus", "Saccharomyces cerevisiae",
			"Danio rerio" };

	public static final String[] SPECIES_COMMON = { "Mouse", "Human", "Rat",
			"Pig", "Mongolian Gerbil", "Hamster", "Guinea pig", "Cattle",
			"Dog", "Goat", "Horse", "Sheep", "Cat", "Yeast", "Zebrafish" };

	public static final String UNIT_PERCENT = "%";

	public static final String UNIT_CFU = "CFU";

	public static final String UNIT_RFU = "RFU";

	public static final String UNIT_SECOND = "SECOND";

	public static final String UNIT_MG_ML = "mg/ml";

	public static final String UNIT_FOLD = "Fold";

	public static final String ORGANIC_HYDROCARBON = "organic:hydrocarbon";

	public static final String ORGANIC_CARBON = "organic:carbon";

	public static final String ORGANIC = "organic";

	public static final String INORGANIC = "inorganic";

	public static final String COMPLEX = "complex";

	public static final Map<String, String> PARTICLE_CLASSIFICATION_MAP;

	static {
		PARTICLE_CLASSIFICATION_MAP = new HashMap<String, String>();
		PARTICLE_CLASSIFICATION_MAP.put(DENDRIMER_TYPE, ORGANIC_HYDROCARBON);
		PARTICLE_CLASSIFICATION_MAP.put(POLYMER_TYPE, ORGANIC_HYDROCARBON);
		PARTICLE_CLASSIFICATION_MAP.put(FULLERENE_TYPE, ORGANIC_CARBON);
		PARTICLE_CLASSIFICATION_MAP.put(CARBON_NANOTUBE_TYPE, ORGANIC_CARBON);
		PARTICLE_CLASSIFICATION_MAP.put(LIPOSOME_TYPE, ORGANIC);
		PARTICLE_CLASSIFICATION_MAP.put(EMULSION_TYPE, ORGANIC);
		PARTICLE_CLASSIFICATION_MAP.put(METAL_PARTICLE_TYPE, INORGANIC);
		PARTICLE_CLASSIFICATION_MAP.put(QUANTUM_DOT_TYPE, INORGANIC);
		PARTICLE_CLASSIFICATION_MAP.put(COMPLEX_PARTICLE_TYPE, COMPLEX);
	}

	
	public static final String CSM_PI = APP_OWNER + "_PI";

	public static final String CSM_RESEARCHER = APP_OWNER + "_Researcher";

	public static final String CSM_ADMIN = APP_OWNER + "_Administrator";

	public static final String CSM_PUBLIC_GROUP = "Public";

	public static final String[] VISIBLE_GROUPS = new String[] { CSM_PI,
			CSM_RESEARCHER };

	public static final String AUTO_COPY_CHARACTERIZATION_VIEW_TITLE_PREFIX = "copy_";

	public static final String AUTO_COPY_CHARACTERIZATION_VIEW_COLOR = "red";
	
	public static final String UNIT_TYPE_CONCENTRATION = "Concentration";
	
	public static final String UNIT_TYPE_CHARGE	= "Charge";
	
	public static final String UNIT_TYPE_QUANTITY = "Quantity";
	
	public static final String UNIT_TYPE_AREA = "Area";
	
	public static final String UNIT_TYPE_SIZE = "Size";
	
	public static final String UNIT_TYPE_VOLUME = "Volume";
	
	public static final String UNIT_TYPE_MOLECULAR_WEIGHT = "Molecular Weight";
	
	public static final String UNIT_TYPE_ZETA_POTENTIAL = "Zeta Potential";
	
}
