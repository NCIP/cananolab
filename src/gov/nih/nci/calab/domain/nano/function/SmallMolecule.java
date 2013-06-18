/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.calab.domain.nano.function;

public class SmallMolecule extends Agent {
	private static final long serialVersionUID = 1234567890L;

	private String name;

	private String compoundName;

	public SmallMolecule() {

	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompoundName() {
		return this.compoundName;
	}

	public void setCompoundName(String compoundName) {
		this.compoundName = compoundName;
	}
}