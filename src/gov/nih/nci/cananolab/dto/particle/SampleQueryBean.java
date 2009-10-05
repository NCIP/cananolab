package gov.nih.nci.cananolab.dto.particle;

import gov.nih.nci.cananolab.dto.BaseQueryBean;

import java.util.ArrayList;
import java.util.List;

import gov.nih.nci.cananolab.util.StringUtils;

/**
 * Information for the sample query form
 * 
 * @author pansu
 * 
 */
public class SampleQueryBean extends BaseQueryBean {
	private String nameType="";
	private String name="";

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
		this.name = name;
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
