/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.domain.nano.characterization.physical.composition.FullereneComposition;

/**
 * This class represents properties of a Fullerene composition to be shown in
 * the view page.
 * 
 * @author pansu
 * 
 */
public class FullereneBean extends CompositionBean {
	private String numberOfCarbons="";

	public FullereneBean() {

	}

	public FullereneBean(FullereneComposition fullerene) {
		super(fullerene);
		this.numberOfCarbons = (fullerene.getNumberOfCarbon() == null) ? ""
				: fullerene.getNumberOfCarbon().toString();
	}

	public String getNumberOfCarbons() {
		if (this.numberOfCarbons.length()==0) {
			this.numberOfCarbons="0";
		}
		return this.numberOfCarbons;
	}

	public void setNumberOfCarbons(String numberOfCarbons) {
		this.numberOfCarbons = numberOfCarbons;
	}

	public void updateDomainObj(FullereneComposition doComp) {	
		super.updateDomainObj(doComp);
		doComp.setNumberOfCarbon(new Integer(this.numberOfCarbons));		
	}
}
