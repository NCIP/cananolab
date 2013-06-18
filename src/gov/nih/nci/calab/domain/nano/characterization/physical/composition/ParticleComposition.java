/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.calab.domain.nano.characterization.physical.composition;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;

import java.util.ArrayList;
import java.util.Collection;

public class ParticleComposition extends Characterization {

	private static final long serialVersionUID = 123456789L;

	private Collection<ComposingElement> composingElementCollection = new ArrayList<ComposingElement>();

	public void setComposingElementCollection(
			Collection<ComposingElement> element) {
		this.composingElementCollection = element;
	}

	public Collection<ComposingElement> getComposingElementCollection() {
		return this.composingElementCollection;
	}

}
