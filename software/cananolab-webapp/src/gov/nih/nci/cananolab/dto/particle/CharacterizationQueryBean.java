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

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonTypeName;

/**
 * Information for the characterization query form
 * 
 * @author pansu
 * 
 */
@JsonTypeName("CharacterizationQueryBean")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CharacterizationQueryBean extends BaseQueryBean {
	private String characterizationType = "";
	private String characterizationName = "";
	private String assayType = "";
	private Boolean datumValueBoolean = false;
	private String datumName = "";
	private String datumValue = "";
	private String datumValueUnit = "";
	
	
	private String id;
	private String operand = "";

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
	
	
	public String getCharacterizationType() {
		return characterizationType;
	}

	public void setCharacterizationType(String characterizationType) {
		this.characterizationType = characterizationType;
	}

	public String getCharacterizationName() {
		return characterizationName;
	}

	public void setCharacterizationName(String characterizationName) {
		this.characterizationName = characterizationName;
	}

	public String getAssayType() {
		return assayType;
	}

	public void setAssayType(String assayType) {
		this.assayType = assayType;
	}

	public String getDatumName() {
		return datumName;
	}

	public void setDatumName(String datumName) {
		this.datumName = datumName;
	}

	public String getDatumValue() {
		return datumValue;
	}

	public void setDatumValue(String datumValue) {
		this.datumValue = datumValue.trim();
	}

	public String getDatumValueUnit() {
		return datumValueUnit;
	}

	public void setDatumValueUnit(String datumValueUnit) {
		this.datumValueUnit = datumValueUnit;
	}

	public Boolean getDatumValueBoolean() {
		return datumValueBoolean;
	}

	public void setDatumValueBoolean(Boolean datumValueBoolean) {
		this.datumValueBoolean = datumValueBoolean;
	}

	public String getDisplayName() {
		List<String> strs = new ArrayList<String>();
		strs.add(characterizationType);
		if (!StringUtils.isEmpty(assayType)) {
			strs.add(characterizationName + ":" + assayType);
		} else {
			strs.add(characterizationName);
		}
		strs.add(datumName);
		strs.add(getOperand());
		strs.add(datumValue);
		strs.add(datumValueUnit);
		return StringUtils.join(strs, " ");
	}

	public String getQueryAsColumnName() {
		if (StringUtils.isEmpty(datumName) && StringUtils.isEmpty(datumValue)) {
			return characterizationType;
		} else {
			return characterizationType + "<br>" + characterizationName
					+ "<br>" + datumName;
		}
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "CharacterizationQueryBean";
	}

	@Override
	public void setType(String type) {
		// TODO Auto-generated method stub
		super.setType(type);
	}
	
	
}
