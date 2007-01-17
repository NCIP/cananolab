/**
 * 
 */
package gov.nih.nci.calab.domain.nano.characterization;

import gov.nih.nci.calab.domain.Instrument;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * @author zengje
 * 
 */
public interface Characterization extends Serializable {
	public void setId(Long id);

	public Long getId();

	public void setSource(String source);

	public String getSource();

	public String getClassification();

	public void setIdentificationName(String name);

	public String getIdentificationName();

	public void setDescription(String description);

	public String getDescription();

	public String getName();

	public void setCreatedBy(String createdBy);

	public String getCreatedBy();

	public void setCreatedDate(Date createdDate);

	public Date getCreatedDate();

	public void setDerivedBioAssayDataCollection(
			Collection<DerivedBioAssayData> derivedBioAssayData);

	public Collection<DerivedBioAssayData> getDerivedBioAssayDataCollection();

	public void setInstrument(Instrument instrument);

	public Instrument getInstrument();

	public void setCharacterizationProtocol(CharacterizationProtocol protocol);

	public CharacterizationProtocol getCharacterizationProtocol();

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

	// public static final String DOMAIN_CYTOTOXICITY_CELL_VIABILITY_DATA_TYPE =
	// "Percent Cell Viability";

	public static final String CYTOTOXICITY_CASPASE3_ACTIVIATION = "Caspase 3 Activation";

	// public static final String
	// DOMAIN_CYTOTOXICITY_CASPASE3_ACTIVIATION_DATA_TYPE = "Percent Caspase 3
	// Activation";

	public static final String BLOODCONTACTTOX_PLATE_AGGREGATION = "Platelet Aggregation";

	// public static final String
	// DOMAIN_BLOODCONTACTTOX_PLATE_AGGREGATION_DATA_TYPE = "Percent Platelet
	// Aggregation";

	public static final String BLOODCONTACTTOX_HEMOLYSIS = "Hemolysis";

	// public static final String DOMAIN_BLOODCONTACTTOX_HEMOLYSIS_DATA_TYPE =
	// "Percent Hemolysis";

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

}
