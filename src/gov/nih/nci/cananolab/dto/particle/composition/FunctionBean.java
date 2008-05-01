package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.particle.samplecomposition.Function;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.ImagingFunction;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.OtherFunction;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.Target;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.TargetingFunction;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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

	private OtherFunction otherFunction = new OtherFunction();

	private String className;

	private Function domainFunction = new Function();

	private List<TargetBean> targets = new ArrayList<TargetBean>();

	public FunctionBean() {
	}

	public FunctionBean(Function function) {
		description = function.getDescription();

		if (function instanceof ImagingFunction) {
			imagingFunction = (ImagingFunction) function;
			domainFunction = imagingFunction;
			className = ClassUtils.getShortClassName(ImagingFunction.class
					.getCanonicalName());
		} else if (function instanceof TargetingFunction) {
			for (Target target : ((TargetingFunction) function)
					.getTargetCollection()) {
				targets.add(new TargetBean(target));
			}
			domainFunction = (TargetingFunction) function;
			className = ClassUtils.getShortClassName(TargetingFunction.class
					.getCanonicalName());
		} else if (function instanceof OtherFunction) {
			otherFunction = (OtherFunction) function;
			domainFunction = otherFunction;
			className = ClassUtils.getShortClassName(OtherFunction.class
					.getCanonicalName());
		}
	}

	public String getDescription() {
		return description;
	}

	public ImagingFunction getImagingFunction() {
		domainFunction = imagingFunction;
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

	private TargetingFunction domainTargetingFunction = new TargetingFunction();
	public List<TargetBean> getTargets() {
		
		/*
		if (((TargetingFunction) domainFunction).getTargetCollection() != null) {
			((TargetingFunction) domainFunction).getTargetCollection().clear();
		} else {
			((TargetingFunction) domainFunction)
					.setTargetCollection(new HashSet<Target>());
		}
		for (TargetBean targetBean : targets) {
			((TargetingFunction) domainFunction).getTargetCollection().add(
					targetBean.getDomainTarget());
		}
		*/
		
		if (domainTargetingFunction.getTargetCollection() != null) {
			domainTargetingFunction.getTargetCollection().clear();
		} else {
			domainTargetingFunction
					.setTargetCollection(new HashSet<Target>());
		}
		for (TargetBean targetBean : targets) {
			domainTargetingFunction.getTargetCollection().add(
					targetBean.getDomainTarget());
		}
		return targets;
	}

	public Function getDomainFunction() {
		return domainFunction;
	}

	public OtherFunction getOtherFunction() {
		domainFunction = otherFunction;
		return otherFunction;
	}

	public void setDescription(String description) {
		this.description = description;
		domainFunction.setDescription(description);
	}

	public void addTarget() {
		targets.add(new TargetBean());
	}

	public void removeTarget(int ind) {
		targets.remove(ind);
	}
}
