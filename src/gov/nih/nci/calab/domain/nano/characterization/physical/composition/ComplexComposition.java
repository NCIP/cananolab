/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.calab.domain.nano.characterization.physical.composition;


public class ComplexComposition extends ParticleComposition {

	private static final long serialVersionUID = 1234567890L;
	public ComplexComposition() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return PHYSICAL_COMPOSITION;
	}
	public String getClassification() {
		return PHYSICAL_CHARACTERIZATION;
	}
}
