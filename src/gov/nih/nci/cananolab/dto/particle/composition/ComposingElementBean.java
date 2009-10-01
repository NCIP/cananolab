package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.particle.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.axis.utils.StringUtils;

/**
 * Represents the view bean for ComposingElement domain object
 * 
 * @author pansu
 * 
 */
public class ComposingElementBean {
	private ComposingElement domain = new ComposingElement();

	private List<FunctionBean> inherentFunctions = new ArrayList<FunctionBean>();

	private FunctionBean theFunction = new FunctionBean();

	public ComposingElementBean(ComposingElement composingElement) {
		this.domain = composingElement;
		if (composingElement.getInherentFunctionCollection() != null) {
			for (Function function : composingElement
					.getInherentFunctionCollection()) {
				inherentFunctions.add(new FunctionBean(function));
			}
			Collections.sort(inherentFunctions,
					new Comparators.FunctionBeanDateComparator());
		}
	}

	public ComposingElementBean() {
	}

	public ComposingElement getDomain() {
		return domain;
	}

	public List<FunctionBean> getInherentFunctions() {
		return inherentFunctions;
	}

	public void addFunction(FunctionBean function) {
		int index = inherentFunctions.indexOf(function);
		if (index != -1) {
			inherentFunctions.remove(function);
			// retain the original order
			inherentFunctions.add(index, function);
		} else {
			inherentFunctions.add(function);
		}
	}

	public void removeFunction(FunctionBean function) {
		inherentFunctions.remove(function);
	}

	public String getDisplayName() {
		StringBuffer buffer = new StringBuffer();
		if (!StringUtils.isEmpty(getDomain().getType())) {
			buffer.append(getDomain().getType());
		}
		if (!StringUtils.isEmpty(getDomain().getName())) {
			buffer.append(" (name: ");
			buffer.append(getDomain().getName());
			if (getDomain().getValue() != null) {
				buffer.append(", amount: ");
				buffer.append(getDomain().getValue());
				if (!StringUtils.isEmpty(getDomain().getValueUnit())) {
					buffer.append(" ");
					buffer.append(getDomain().getValueUnit());
				}
			}
			buffer.append(")");
		}
		return buffer.toString();
	}

	public String getAdvancedSearchDisplayName() {
		StringBuffer buffer = new StringBuffer();
		if (!StringUtils.isEmpty(getDomain().getType())) {
			buffer.append(getDomain().getType());
		}		
		if (!StringUtils.isEmpty(getDomain().getName())) {
			buffer.append(" (");
			buffer.append(getDomain().getName());
			buffer.append(")");
		}
		return buffer.toString();
	}

	public String getMolecularFormulaDisplayName() {
		StringBuffer buffer = new StringBuffer();
		if (!StringUtils.isEmpty(getDomain().getMolecularFormula())) {
			buffer.append(getDomain().getMolecularFormula());
			if (!StringUtils.isEmpty(getDomain().getMolecularFormulaType())) {
				buffer.append(" (");
				buffer.append(getDomain().getMolecularFormulaType());
				buffer.append(")");
			}
		}
		return buffer.toString();
	}

	public String[] getFunctionDisplayNames() {
		List<String> displayNames = new ArrayList<String>();
		for (FunctionBean function : inherentFunctions) {
			displayNames.add(function.getDisplayName());
		}
		if (displayNames.isEmpty()) {
			return null;
		}
		return displayNames.toArray(new String[displayNames.size()]);
	}

	// for dwr ajax in bodySubmitChemicalAssociation.jsp
	public String getDomainId() {
		if (getDomain().getId() != null) {
			return getDomain().getId().toString();
		} else {
			return null;
		}
	}

	public void setupDomain(String createdBy, int index) throws Exception {
		if (domain.getId() == null
				|| domain.getCreatedBy() != null
				&& domain.getCreatedBy().equals(
						Constants.AUTO_COPY_ANNOTATION_PREFIX)) {
			domain.setCreatedBy(createdBy);
			// domain.setCreatedDate(new Date());
			// fix for MySQL database, which supports precision only up to
			// seconds
			domain.setCreatedDate(DateUtils.addSecondsToCurrentDate(index));
		}
		// update zero values defaulted from forms to null
		if (domain.getPubChemId() != null && domain.getPubChemId() == 0) {
			domain.setPubChemId(null);
		}
		if (domain.getValue() != null && domain.getValue() == 0) {
			domain.setValue(null);
		}
		if (domain.getInherentFunctionCollection() != null) {
			domain.getInherentFunctionCollection().clear();
		} else {
			domain.setInherentFunctionCollection(new HashSet<Function>());
		}
		int i = 0;
		for (FunctionBean functionBean : inherentFunctions) {
			functionBean.setupDomainFunction(createdBy, i);
			domain.getInherentFunctionCollection().add(
					functionBean.getDomainFunction());
			i++;
		}
	}

	public FunctionBean getTheFunction() {
		return theFunction;
	}

	public void setTheFunction(FunctionBean theFunction) {
		this.theFunction = theFunction;
	}

	public void setInherentFunctions(List<FunctionBean> inherentFunctions) {
		this.inherentFunctions = inherentFunctions;
	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof ComposingElementBean) {
			ComposingElementBean c = (ComposingElementBean) obj;
			Long thisId = this.getDomain().getId();
			if (thisId != null && thisId.equals(c.getDomain().getId())) {
				eq = true;
			}
		}
		return eq;
	}
}
