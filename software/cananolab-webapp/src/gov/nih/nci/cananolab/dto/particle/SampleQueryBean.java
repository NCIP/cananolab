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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonTypeName;

/**
 * Information for the sample query form
 * 
 * @author pansu
 * 
 */
@JsonTypeName("SampleQueryBean")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SampleQueryBean extends BaseQueryBean {
	private String nameType="";
	private String name="";
	
	
//	
//	
//	private String id;
//	private String operand = "";
//
//	public String getId() {
//		return id;
//	}
//
//	public void setId(String id) {
//		this.id = id;
//	}
//
//	public String getOperand() {
//		return operand;
//	}
//
//	public void setOperand(String operand) {
//		this.operand = operand;
//	}

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
	
	
	

	@Override
	public String getType() {
		return "SampleQueryBean";
	}




	@Override
	public void setType(String type) {
		super.setType(type);
	}




	public String getNameType() {
		return nameType;
	}

	public void setNameType(String nameType) {
		this.nameType = nameType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.trim();
	}
	
	public String getDisplayName() {
		List<String> strs = new ArrayList<String>();
		strs.add(nameType);
		strs.add(getOperand());
		strs.add(name);
		return StringUtils.join(strs, " ");
	}

	public String getQueryAsColumnName() {
		if (nameType.equals("sample name")) {
			return null;
		}
		return nameType;
	}
}
