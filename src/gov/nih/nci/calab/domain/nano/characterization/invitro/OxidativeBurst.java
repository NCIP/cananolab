/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.calab.domain.nano.characterization.invitro;

import gov.nih.nci.calab.domain.nano.characterization.toxicity.ImmunoToxicity;

public class OxidativeBurst extends ImmunoToxicity {

	public OxidativeBurst() {

	}

	private static final long serialVersionUID = 1234567890L;

	public String getImmunotoxiticyType() {
		return IMMUNE_CELL_FUNCTION_IMMUNOTOXICITY_CHARACTERIZATION;
	}

	public String getClassification() {
		return INVITRO_CHARACTERIZATION;
	}

	public String getName() {
		return IMMUNOCELLFUNCTOX_OXIDATIVE_BURST;
	}
}
