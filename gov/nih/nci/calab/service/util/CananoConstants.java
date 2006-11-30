package gov.nih.nci.calab.service.util;

public class CananoConstants {

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

	public static final String TOXICITY_ENZYME_FUNCTION = "Enzyme Function";

	public static final String CYTOTOXICITY_CELL_VIABILITY = "Cell Viability";

	public static final String CYTOTOXICITY_CASPASE3_ACTIVIATION = "Caspase 3 Activation";

	public static final String BLOODCONTACTTOX_PLATE_AGGREGATION = "Plate Aggregation";

	public static final String BLOODCONTACTTOX_HEMOLYSIS = "Hemolysis";

	public static final String BLOODCONTACTTOX_COAGULATION = "Coagulation";

	public static final String BLOODCONTACTTOX_PLASMA_PROTEIN_BINDING = "Plasma Protein Binding";

	public static final String IMMUNOCELLFUNCTOX_PHAGOCYTOSIS = "Phagocytosis";

	public static final String IMMUNOCELLFUNCTOX_OXIDATIVE_BURST = "Oxidative Burst";

	public static final String IMMUNOCELLFUNCTOX_CHEMOTAXIS = "Chemotaxis";

	public static final String IMMUNOCELLFUNCTOX_CYTOKINE_INDUCTION = "Cytokine Induction";

	public static final String IMMUNOCELLFUNCTOX_COMPLEMENT_ACTIVATION = "Complement Activation";

	public static final String IMMUNOCELLFUNCTOX_LEUKOCYTE_PROLIFERATION = "Leukocyte Proliferation";

	public static final String IMMUNOCELLFUNCTOX_NKCELL_CYTOTOXIC_ACTIVITY = "Cytotoxic Activity of NK Cells";

	public static final String METABOLIC_STABILITY_CYP450 = "CYP450";

	public static final String METABOLIC_STABILITY_ROS = "ROS";

	public static final String METABOLIC_STABILITY_GLUCURONIDATION_SULPHATION = "Glucuronidation Sulphation";

	public static final String IMMUNOCELLFUNCTOX_CFU_GM = "CFU_GM";

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

	public static final String BOOLEAN_YES = "Yes";

	public static final String BOOLEAN_NO = "No";

	public static final String[] BOOLEAN_CHOICES = new String[] { BOOLEAN_YES,
			BOOLEAN_NO };

	public static final String[] CHARACTERIZATION_SOURCES = new String[] {
			"NCL", "Vendor" };

	public static final String[] CARBON_NANOTUBE_WALLTYPES = new String[] {
			"Single (SWNT)", "Double (DWMT)", "Multiple (MWNT)" };

	public static final String[] REPORT_TYPES = new String[] { "NCL Report",
			"Other Associated File" };

	public static final String[] DEFAULT_POLYMER_INITIATORS = new String[] {
			"Free Radicals", "Peroxide" };
	
	public static final String[]DEFAULT_DENDRIMER_BRANCHES = new String[] {
		    "1-2", "1-3"};
	
	public static final String[]DEFAULT_DENDRIMER_GENERATIONS = new String[] {
	    "0","0.5","1.0","1.5","2.0","2.5","3.0","3.5","4.0","4.5","5.0","5.5","6.0","6.5","7.0","7.5","8.0","8.5","9.0","9.5","10.0"};

	public static final String OTHER = "Other";
	
	public static final String CHARACTERIZATION_FILE = "characterizationFile";
	
	public static final String DNA = "DNA";
	
	public static final String PEPTIDE = "Peptide";
	
	public static final String SMALL_MOLECULE = "SmallMolecule";
	
	public static final String PROBE = "Probe";
	
	public static final String ANTIBODY = "Antibody";
	
	public static final String IMAGE_CONTRAST_AGENT ="ImageContrastAgent";
	
	public static final String ATTACHMENT = "Attachment";
	
	public static final String ENCAPSULATION = "Encapsulation";
	
	public static final String RECEPTOR = "Receptor";
	
	public static final String ANTIGEN = "Antigen";
	
	public static final String NCL_REPORT = "NCL Report";
	
	public static final int MAX_VIEW_TITLE_LENGTH=25;


}