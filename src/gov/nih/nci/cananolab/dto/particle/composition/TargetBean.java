package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.particle.samplecomposition.Antigen;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.Gene;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.OtherTarget;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.Receptor;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.Target;
import gov.nih.nci.cananolab.util.ClassUtils;

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

	private OtherTarget otherTarget = new OtherTarget();

	private String className;

	private Target domainTarget = new Target();

	public TargetBean() {
	}

	public TargetBean(Target target) {
		if (target instanceof Antigen) {
			antigen = (Antigen) target;
			domainTarget = antigen;
			className = ClassUtils.getShortClassName(Antigen.class
					.getCanonicalName());
		} else if (target instanceof Receptor) {
			domainTarget = (Receptor) target;
			className = ClassUtils.getShortClassName(Receptor.class
					.getCanonicalName());
		} else if (target instanceof Gene) {
			domainTarget = (Gene) target;
			className = ClassUtils.getShortClassName(Gene.class
					.getCanonicalName());
		} else if (target instanceof OtherTarget) {
			otherTarget = (OtherTarget) target;
			domainTarget = otherTarget;
			className = ClassUtils.getShortClassName(OtherTarget.class
					.getCanonicalName());
		}
	}

	public Antigen getAntigen() {
		domainTarget = antigen;
		return antigen;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		domainTarget.setDescription(description);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		domainTarget.setName(name);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		otherTarget.setType(type);
	}

	public OtherTarget getOtherTarget() {
		domainTarget = otherTarget;
		return otherTarget;
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
}
