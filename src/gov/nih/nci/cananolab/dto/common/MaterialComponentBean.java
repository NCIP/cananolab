/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.MaterialComponent;

public class MaterialComponentBean {
	private MaterialComponent domain=new MaterialComponent();
	private MaterialBean componentMaterial;
	
	public MaterialComponent getDomain() {  
		return domain;
	}
	
	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof MaterialComponentBean) {
			MaterialComponentBean c = (MaterialComponentBean) obj;
			Long thisId = this.getDomain().getId();
			if (thisId != null && thisId.equals(c.getDomain().getId())) {
				eq = true;
			}
		}
		return eq;
	}
}
