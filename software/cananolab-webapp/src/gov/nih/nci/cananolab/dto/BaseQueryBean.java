/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.dto;

import gov.nih.nci.cananolab.dto.particle.CharacterizationQueryBean;
import gov.nih.nci.cananolab.dto.particle.CompositionQueryBean;
import gov.nih.nci.cananolab.dto.particle.SampleQueryBean;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeName;

/**
 * Contains information about a basic query
 * 
 * @author pansu
 * 
 */
@JsonTypeInfo(
use = JsonTypeInfo.Id.NAME,
include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
//	@JsonSubTypes.Type(value = SampleQueryBean.class, name = "SampleQueryBean"),
//	@JsonSubTypes.Type(value = CompositionQueryBean.class, name = "CompositionQueryBean"),
//	@JsonSubTypes.Type(value = CharacterizationQueryBean.class, name = "CharacterizationQueryBean")
@Type(value = SampleQueryBean.class, name = "SampleQueryBean"),
@Type(value = CompositionQueryBean.class, name = "CompositionQueryBean"),
@Type(value = CharacterizationQueryBean.class, name = "CharacterizationQueryBean")
})
@JsonTypeName("BaseQueryBean")
public class BaseQueryBean {
	
	protected String type = "";
	private String id;
	private String operand = "";

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

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

	public String getDisplayName() {
		return "";
	}
}
