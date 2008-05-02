package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.particle.samplecomposition.Function;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.ImagingFunction;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.OtherFunction;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.Target;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.TargetingFunction;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 
 * Represents the view bean for Function domain object
 * 
 * @author pansu
 * 
 * 
 */
public class FunctionBean {
	private String type;

	private String description;

	private ImagingFunction imagingFunction = new ImagingFunction();

	private String className;

	private Function domainFunction;

	private List<TargetBean> targets = new ArrayList<TargetBean>();

	public FunctionBean() {
	}

	public FunctionBean(Function function) {
		description = function.getDescription();
		domainFunction = function;
		if (function instanceof ImagingFunction) {
			imagingFunction = (ImagingFunction) function;
		} else if (function instanceof TargetingFunction) {
			for (Target target : ((TargetingFunction) function)
					.getTargetCollection()) {
				targets.add(new TargetBean(target));
			}
		} else if (function instanceof OtherFunction) {
			type = ((OtherFunction) function).getType();
		}
		className = ClassUtils.getShortClassName(ImagingFunction.class
				.getCanonicalName());
	}

	public String getDescription() {
		return description;
	}

	public ImagingFunction getImagingFunction() {
		return imagingFunction;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<TargetBean> getTargets() {
		return targets;
	}

	public Function getDomainFunction() {
		return domainFunction;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void addTarget() {
		targets.add(new TargetBean());
	}

	public void removeTarget(int ind) {
		targets.remove(ind);
	}

	public void setupDomainFunction(Map<String, String> typeToClass,
			String createdBy) throws Exception {
		className = typeToClass.get(type);
		if (domainFunction == null) {
			Class clazz = ClassUtils.getFullClass(className);
			domainFunction = (Function) clazz.newInstance();
		}
		if (domainFunction.getId() == null) {
			domainFunction.setCreatedBy(createdBy);
			domainFunction.setCreatedDate(new Date());
		}
		if (domainFunction instanceof ImagingFunction) {
			domainFunction = imagingFunction;
		} else if (domainFunction instanceof TargetingFunction) {

			if (((TargetingFunction) domainFunction).getTargetCollection() != null) {
				((TargetingFunction) domainFunction).getTargetCollection()
						.clear();
			} else {
				((TargetingFunction) domainFunction)
						.setTargetCollection(new HashSet<Target>());
			}
			for (TargetBean targetBean : targets) {
				targetBean.setupDomainTarget(typeToClass);
				((TargetingFunction) domainFunction).getTargetCollection().add(
						targetBean.getDomainTarget());
			}
		} else if (domainFunction instanceof OtherFunction) {
			((OtherFunction) domainFunction).setType(type);
		}
		domainFunction.setDescription(description);
	}
}
