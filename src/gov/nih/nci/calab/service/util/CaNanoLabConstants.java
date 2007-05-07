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
			"Tube", "Vial"};

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

	public static final String PHYSICAL_CHARACTERIZATION = "Physical";

	public static final String COMPOSITION_CHARACTERIZATION = "Composition";

	public static final String INVITRO_CHARACTERIZATION = "In Vitro";

	public static final String INVIVO_CHARACTERIZATION = "In Vivo";

	public static final String TOXICITY_CHARACTERIZATION = "Toxicity";

	public static final String CYTOXICITY_CHARACTERIZATION = "Cytoxicity";

	public static final String APOPTOSIS_CELL_DEATH_METHOD_CYTOXICITY_CHARACTERIZATION = "apoptosis";

	public static final String NECROSIS_CELL_DEATH_METHOD_CYTOXICITY_CHARACTERIZATION = "necrosis";

	public static final String BLOOD_CONTACT_IMMUNOTOXICITY_CHARACTERIZATION = "Blood Contact";

	public static final String IMMUNE_CELL_FUNCTION_IMMUNOTOXICITY_CHARACTERIZATION = "Immune Cell Function";

	public static final String METABOLIC_STABILITY_TOXICITY_CHARACTERIZATION = "Metabolic Stability";

	public static final String PHYSICAL_SIZE = "Size";

	public static final String PHYSICAL_SHAPE = "Shape";

	public static final String PHYSICAL_MOLECULAR_WEIGHT = "Molecular Weight";

	public static final String PHYSICAL_SOLUBILITY = "Solubility";

	public static final String PHYSICAL_SURFACE = "Surface";

	public static final String PHYSICAL_STABILITY = "Stability";

	public static final String PHYSICAL_PURITY = "Purity";

	public static final String PHYSICAL_FUNCTIONAL = "Functional";

	public static final String PHYSICAL_MORPHOLOGY = "Morphology";

	public static final String PHYSICAL_COMPOSITION = "Composition";

	public static final String TOXICITY_OXIDATIVE_STRESS = "Oxidative Stress";

	public static final String TOXICITY_OXIDATIVE_STRESS_DATA_TYPE = "Percent Oxidative Stress";

	public static final String TOXICITY_ENZYME_FUNCTION = "Enzyme Function";

	public static final String TOXICITY_ENZYME_FUNCTION_DATA_TYPE = "Percent Enzyme Induction";

	public static final String CYTOTOXICITY_CELL_VIABILITY = "Cell Viability";

	public static final String CYTOTOXICITY_CELL_VIABILITY_DATA_TYPE = "Percent Cell Viability";

	public static final String CYTOTOXICITY_CASPASE3_ACTIVIATION = "Caspase 3 Activation";

	public static final String CYTOTOXICITY_CASPASE3_ACTIVIATION_DATA_TYPE = "Percent Caspase 3 Activation";

	public static final String BLOODCONTACTTOX_PLATE_AGGREGATION = "Platelet Aggregation";

	public static final String BLOODCONTACTTOX_PLATELET_AGGREGATION_DATA_TYPE = "Percent Platelet Aggregation";

	public static final String BLOODCONTACTTOX_HEMOLYSIS = "Hemolysis";

	public static final String BLOODCONTACTTOX_HEMOLYSIS_DATA_TYPE = "Percent Hemolysis";

	public static final String BLOODCONTACTTOX_COAGULATION = "Coagulation";

	public static final String BLOODCONTACTTOX_COAGULATION_DATA_TYPE = "Coagulation Time";

	public static final String BLOODCONTACTTOX_PLASMA_PROTEIN_BINDING = "Plasma Protein Binding";

	public static final String BLOODCONTACTTOX_PLASMA_PROTEIN_BINDING_DATA_TYPE = "Percent Plasma Protein Binding";

	public static final String IMMUNOCELLFUNCTOX_PHAGOCYTOSIS = "Phagocytosis";

	public static final String IMMUNOCELLFUNCTOX_PHAGOCYTOSIS_DATA_TYPE = "Fold Induction";

	public static final String IMMUNOCELLFUNCTOX_OXIDATIVE_BURST = "Oxidative Burst";

	public static final String IMMUNOCELLFUNCTOX_OXIDATIVE_BURST_DATA_TYPE = "Percent Oxidative Burst";

	public static final String IMMUNOCELLFUNCTOX_CHEMOTAXIS = "Chemotaxis";

	public static final String IMMUNOCELLFUNCTOX_CHEMOTAXIS_DATA_TYPE = "Relative Fluorescent Values";

	public static final String IMMUNOCELLFUNCTOX_CYTOKINE_INDUCTION = "Cytokine Induction";

	public static final String IMMUNOCELLFUNCTOX_CYTOKINE_INDUCTION_DATA_TYPE = "Cytokine Concentration";

	public static final String IMMUNOCELLFUNCTOX_COMPLEMENT_ACTIVATION = "Complement Activation";

	public static final String IMMUNOCELLFUNCTOX_COMPLEMENT_ACTIVATION_DATA_TYPE = "Percent Complement Activation";

	public static final String IMMUNOCELLFUNCTOX_LEUKOCYTE_PROLIFERATION = "Leukocyte Proliferation";

	public static final String IMMUNOCELLFUNCTOX_LEUKOCYTE_PROLIFERATION_DATA_TYPE = "Percent Leukocyte Proliferation";

	public static final String IMMUNOCELLFUNCTOX_NKCELL_CYTOTOXIC_ACTIVITY = "Cytotoxic Activity of NK Cells";

	public static final String IMMUNOCELLFUNCTOX_NKCELL_CYTOTOXIC_ACTIVITY_DATA_TYPE = "Percent Cytotoxic Activity";

	public static final String METABOLIC_STABILITY_CYP450 = "CYP450";

	public static final String METABOLIC_STABILITY_ROS = "ROS";

	public static final String METABOLIC_STABILITY_GLUCURONIDATION_SULPHATION = "Glucuronidation Sulphation";

	public static final String IMMUNOCELLFUNCTOX_CFU_GM = "CFU_GM";

	public static final String IMMUNOCELLFUNCTOX_CFU_GM_DATA_TYPE = "CFU_GM";

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

	public static final String[] CHARACTERIZATION_SOURCES = new String[] {
			"NCL", "Vendor" };

	public static final String[] CARBON_NANOTUBE_WALLTYPES = new String[] {
			"Single (SWNT)", "Double (DWMT)", "Multiple (MWNT)" };

	public static final String REPORT = "Report";

	public static final String ASSOCIATED_FILE = "Other Associated File";

	public static final String WORKFLOW_DATA = "workflow_data";

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

	public static final String[] DEFAULT_CELLLINES = new String[] { "LLC-PK1",
			"Hep-G2" };

	public static final String[] DEFAULT_SHAPE_TYPES = new String[] { "Cubic",
			"Hexagonal", "Irregular", "Needle", "Oblate", "Rod", "Spherical",
			"Tetrahedron", "Tetrapod", "Triangle", "Eliptical", "Composite",
			"Cylindrical", "Vesicular", "Elliposid" };

	public static final String[] DEFAULT_MORPHOLOGY_TYPES = new String[] {
			"Powder", "Liquid", "Solid", "Crystalline", "Copolymer", "Fibril",
			"Colloid", "Oil" };

	public static final String[] DEFAULT_SURFACE_GROUP_NAMES = new String[] {
			"Amine", "Carboxyl", "Hydroxyl" };

	public static final String ABBR_COMPOSITION = "CP";

	public static final String ABBR_SIZE = "SZ";

	public static final String ABBR_MOLECULAR_WEIGHT = "MW";

	public static final String ABBR_MORPHOLOGY = "MP";

	public static final String ABBR_SHAPE = "SH";

	public static final String ABBR_SURFACE = "SF";

	public static final String ABBR_SOLUBILITY = "SL";

	public static final String ABBR_PURITY = "PT";

	public static final String ABBR_OXIDATIVE_STRESS = "OS";

	public static final String ABBR_ENZYME_FUNCTION = "EF";

	public static final String ABBR_CELL_VIABILITY = "CV";

	public static final String ABBR_CASPASE3_ACTIVATION = "C3";

	public static final String ABBR_PLATELET_AGGREGATION = "PA";

	public static final String ABBR_HEMOLYSIS = "HM";

	public static final String ABBR_PLASMA_PROTEIN_BINDING = "PB";

	public static final String ABBR_COAGULATION = "CG";

	public static final String ABBR_OXIDATIVE_BURST = "OB";

	public static final String ABBR_CHEMOTAXIS = "CT";

	public static final String ABBR_LEUKOCYTE_PROLIFERATION = "LP";

	public static final String ABBR_PHAGOCYTOSIS = "PC";

	public static final String ABBR_CYTOKINE_INDUCTION = "CI";

	public static final String ABBR_CFU_GM = "CU";

	public static final String ABBR_COMPLEMENT_ACTIVATION = "CA";

	public static final String ABBR_NKCELL_CYTOTOXIC_ACTIVITY = "NK";

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

	public static final String CSM_PUBLIC_GROUP="Public";
	
	public static final String[] VISIBLE_GROUPS = new String[] { CSM_PI,
			CSM_RESEARCHER };

}
