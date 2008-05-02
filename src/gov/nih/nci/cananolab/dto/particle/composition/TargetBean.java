package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.particle.samplecomposition.Antigen;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.OtherTarget;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.Target;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.Date;
import java.util.Map;

/**
 * Represents the view bean for the Target domain object
 * 
 * @author pansu
 * 
 */
public class TargetBean {
	private String type;

	private String name;

	private String description;

	private Antigen antigen = new Antigen();

	private String className;

	private Target domainTarget;

	public TargetBean() {
	}

	public TargetBean(Target target) {
		domainTarget = target;
		name=target.getName();
		description=target.getDescription();
		className = ClassUtils.getShortClassName(target.getClass()
				.getCanonicalName());
		if (target instanceof Antigen) {
			antigen = (Antigen) target;
		} else if (target instanceof OtherTarget) {
			type = ((OtherTarget) target).getType();
		}
	}

	public Antigen getAntigen() {
		return antigen;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Target getDomainTarget() {
		return domainTarget;
	}

	public void setupDomainTarget(Map<String, String> typeToClass)
			throws Exception {
		className = typeToClass.get(type);
		Class clazz = ClassUtils.getFullClass(className);
		if (domainTarget == null
				|| !clazz.getCanonicalName().equals(
						domainTarget.getClass().getCanonicalName())) {
			domainTarget = (Target) clazz.newInstance();
		}
		if (domainTarget instanceof OtherTarget) {
			((OtherTarget) domainTarget).setType(type);
		} else if (domainTarget instanceof Antigen) {
			domainTarget = antigen;
		}
		domainTarget.setName(name);
		domainTarget.setDescription(description);
	}
}
