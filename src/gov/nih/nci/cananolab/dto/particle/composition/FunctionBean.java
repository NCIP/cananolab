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
import java.util.Set;

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
		className = ClassUtils.getShortClassName(function.getClass()
				.getCanonicalName());
		if (function instanceof ImagingFunction) {
			imagingFunction = (ImagingFunction) function;
		} else if (function instanceof TargetingFunction) {
			for (Target target : ((TargetingFunction) function)
					.getTargetCollection()) {
				targets.add(new TargetBean(target));
			}
		} else if (function instanceof OtherFunction) {
			otherFunction = (OtherFunction) function;
		}
		domainFunction = function;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		domainFunction.setDescription(description);
	}

	public ImagingFunction getImagingFunction() {
		return imagingFunction;
	}

	public void setImagingFunction(ImagingFunction imagingFunction) {
		this.imagingFunction = imagingFunction;
		domainFunction = imagingFunction;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		otherFunction.setType(type);
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

	public void setTargets(List<TargetBean> targets) {
		this.targets = targets;
		Set<Target> domainTargets = new HashSet<Target>();
		for (TargetBean targetBean : targets) {
			domainTargets.add(targetBean.getDomainTarget());
		}
		((TargetingFunction) domainFunction).setTargetCollection(domainTargets);
	}

	public Function getDomainFunction() {
		return domainFunction;
	}

	public OtherFunction getOtherFunction() {
		return otherFunction;
	}

	public void setOtherFunction(OtherFunction otherFunction) {
		this.otherFunction = otherFunction;
		domainFunction = otherFunction;
	}
}
