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
		domainFunction.setDescription(description);
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
		Set<Target> domainTargets = new HashSet<Target>();
		for (TargetBean targetBean : targets) {
			domainTargets.add(targetBean.getDomainTarget());
		}
		((TargetingFunction) domainFunction).setTargetCollection(domainTargets);
		return targets;
	}

	public Function getDomainFunction() {
		return domainFunction;
	}

	public OtherFunction getOtherFunction() {		
		domainFunction = otherFunction;
		otherFunction.setType(type);
		return otherFunction;
	}

	public void setDescription(String description) {
		this.description = description;		
	}
}
