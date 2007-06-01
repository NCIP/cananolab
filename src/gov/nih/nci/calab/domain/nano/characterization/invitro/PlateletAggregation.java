/**
 * 
 */
package gov.nih.nci.calab.domain.nano.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.toxicity.ImmunoToxicity;

/**
 * @author zengje
 * 
 */
public class PlateletAggregation extends ImmunoToxicity {
	private static final long serialVersionUID = 1234567890L;

	/**
	 * 
	 */
	public PlateletAggregation() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getImmunotoxiticyType() {
		return BLOOD_CONTACT_IMMUNOTOXICITY_CHARACTERIZATION;
	}

	public String getClassification() {
		return INVITRO_CHARACTERIZATION;
	}

	public String getName() {
		return BLOODCONTACTTOX_PLATE_AGGREGATION;
	}

}
