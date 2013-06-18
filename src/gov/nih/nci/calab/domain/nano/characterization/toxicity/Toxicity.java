/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.calab.domain.nano.characterization.toxicity;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;

public class Toxicity extends Characterization {

	private static final long serialVersionUID = 1234567890L;

	public String getClassification() {
		return TOXICITY_CHARACTERIZATION;
	}

	public String getName() {
		return TOXICITY;
	}

}
