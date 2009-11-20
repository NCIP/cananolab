package gov.nih.nci.cananolab.dto.particle;

import gov.nih.nci.cananolab.dto.BaseQueryBean;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Information for the composition query form
 * 
 * @author pansu
 * 
 */
public class CompositionQueryBean extends BaseQueryBean {
	private String compositionType = "";
	private String entityType = "";
	private String chemicalName = "";

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

	public String getDisplayName() {
		List<String> strs = new ArrayList<String>();
		strs.add(compositionType);
		strs.add(entityType);
		strs.add(getOperand());
		strs.add(chemicalName);
		return StringUtils.join(strs, " ");
	}

	public String getQueryAsColumnName() {
		if (compositionType.equals("function")) {
			return compositionType;
		}
		if (StringUtils.isEmpty(chemicalName)) {
			return compositionType;
		} else {
			return compositionType+"<br>"+entityType;
		}
	}
}
