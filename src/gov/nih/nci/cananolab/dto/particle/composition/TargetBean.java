package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.function.Antigen;
import gov.nih.nci.cananolab.domain.function.OtherTarget;
import gov.nih.nci.cananolab.domain.function.Target;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.DateUtil;

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
		name = target.getName();
		description = target.getDescription();
		className = ClassUtils.getShortClassName(target.getClass()
				.getCanonicalName());
		if (target instanceof Antigen) {
			antigen = (Antigen) target;
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

	public String getClassName() {
		return className;
	}

	public Target getDomainTarget() {
		return domainTarget;
	}

	public void setupDomainTarget(Map<String, String> typeToClass,
			String createdBy, int index) throws Exception {
		className = typeToClass.get(type.toLowerCase());
		Class clazz = null;
		if (className != null) {
			clazz = ClassUtils.getFullClass(className);
		} else {
			clazz = OtherTarget.class;
		}
		if (domainTarget == null
				|| domainTarget != null
				&& !clazz.getCanonicalName().equals(
						domainTarget.getClass().getCanonicalName())) {
			try {
				domainTarget = (Target) clazz.newInstance();
			}catch (ClassCastException ex) {
				String tmpType = type;
				this.setType(null);
				throw new ClassCastException(tmpType);
			}
		}
		if (domainTarget instanceof OtherTarget) {
			((OtherTarget) domainTarget).setType(type);
		} else if (domainTarget instanceof Antigen) {
			domainTarget = antigen;
		}
		domainTarget.setName(name);
		domainTarget.setDescription(description);
		if (domainTarget.getId() == null
				|| (domainTarget.getCreatedBy() != null && domainTarget
						.getCreatedBy().equals(
								CaNanoLabConstants.AUTO_COPY_ANNOTATION_PREFIX))) {
			domainTarget.setCreatedBy(createdBy);
			//domainTarget.setCreatedDate(new Date());
			// fix for MySQL database, which supports precision only up to
			// seconds
			domainTarget.setCreatedDate(DateUtil
					.addSecondsToCurrentDate(index));
		}
	}

	public void updateType(Map<String, String> classToType) {
		if (domainTarget instanceof OtherTarget) {
			type = ((OtherTarget) domainTarget).getType();
		} else {
			type = classToType.get(className);
		}
	}

	public void setType(String type) {
		this.type = type;
	}
}
