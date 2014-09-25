/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.dto.particle;

import gov.nih.nci.cananolab.dto.BaseQueryBean;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonTypeName;

/**
 * Information for the composition query form
 * 
 * @author pansu
 * 
 */
@JsonTypeName("CompositionQueryBean")
public class CompositionQueryBean { //extends BaseQueryBean {
	private String compositionType = "";
	private String entityType = "";
	private String chemicalName = "";
	
	
	private String id;
	private String operand;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOperand() {
		return operand;
	}

	public void setOperand(String operand) {
		this.operand = operand;
	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof BaseQueryBean) {
			BaseQueryBean q = (BaseQueryBean) obj;
			String thisId = this.getId();
			if (thisId != null && thisId.equals(q.getId())) {
				eq = true;
			}
		}
		return eq;
	}
	


	public String getCompositionType() {
		return compositionType;
	}

	public void setCompositionType(String compositionType) {
		this.compositionType = compositionType;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getChemicalName() {
		return chemicalName;
	}

	public void setChemicalName(String chemicalName) {
		this.chemicalName = chemicalName.trim();
	}

//	public String getDisplayName() {
//		List<String> strs = new ArrayList<String>();
//		strs.add(compositionType);
//		strs.add(entityType);
//		strs.add(getOperand());
//		strs.add(chemicalName);
//		return StringUtils.join(strs, " ");
//	}
//
//	public String getQueryAsColumnName() {
//		if (compositionType.equals("function")) {
//			return compositionType;
//		}
//		if (StringUtils.isEmpty(chemicalName)) {
//			return compositionType;
//		} else {
//			return compositionType+"<br>"+entityType;
//		}
//	}
}
