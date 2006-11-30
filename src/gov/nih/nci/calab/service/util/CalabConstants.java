package gov.nih.nci.calab.service.util;

public class CalabConstants {
	public static final String CSM_APP_NAME = "calab";

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
			"Tube", "Vial", "Other" };

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

	// caLAB property file
	public static final String CALAB_PROPERTY = "calab.properties";

	public static final String PARTICLE_PROPERTY = "particle.properties";

	public static final String PHYSICAL_CHARACTERIZATION = "Physical";
	
	public static final String COMPOSITION_CHARACTERIZATION = "Composition";

	public static final String INVITRO_CHARACTERIZATION = "In Vitro";

	public static final String INVIVO_CHARACTERIZATION = "In Vivo";

	public static final String TOXICITY_CHARACTERIZATION = "Toxicity";

	public static final String CYTOXICITY_CHARACTERIZATION = "Cytoxicity";

	public static final String BLOOD_CONTACT_IMMUNOTOXICITY_CHARACTERIZATION = "Blood Contact";

	public static final String IMMUNE_CELL_FUNCTION_IMMUNOTOXICITY_CHARACTERIZATION = "Immune Cell Function";

	public static final String METABOLIC_STABILITY_TOXICITY_CHARACTERIZATION = "Metabolic Stability";
	
	public static final int MAX_VIEW_TITLE_LENGTH=25;
}
