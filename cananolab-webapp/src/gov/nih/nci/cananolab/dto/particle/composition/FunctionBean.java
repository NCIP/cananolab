package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.function.ImagingFunction;
import gov.nih.nci.cananolab.domain.function.OtherFunction;
import gov.nih.nci.cananolab.domain.function.Target;
import gov.nih.nci.cananolab.domain.function.TargetingFunction;
import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
	// needed for use in DWR ordering functions in the session.
	private String id;

	private String type;

	private String description;

	private ImagingFunction imagingFunction = new ImagingFunction();

	private String className;

	private Function domainFunction;

	private List<TargetBean> targets = new ArrayList<TargetBean>();

	private TargetBean theTarget = new TargetBean();

	public FunctionBean() {
	}

	public FunctionBean(Function function) {
		// when function is copied its id is intentionally set to null.
		if (function.getId() != null && function.getId() != 0) {
			id = function.getId().toString();
		}
		description = function.getDescription();
		domainFunction = function;
		if (function instanceof ImagingFunction) {
			imagingFunction = (ImagingFunction) function;
		} else if (function instanceof TargetingFunction) {
			if (((TargetingFunction) function).getTargetCollection() != null) {
				for (Target target : ((TargetingFunction) function)
						.getTargetCollection()) {
					targets.add(new TargetBean(target));
				}
				Collections.sort(targets,
						new Comparators.TargetBeanDateComparator());
			}
		}
		className = ClassUtils.getShortClassName(function.getClass()
				.getCanonicalName());
		updateType();
	}

	public String getDescription() {
		return description;
	}

	public String getDescriptionDisplayName() {
		return StringUtils.escapeXmlButPreserveLineBreaks(description);
	}

	public ImagingFunction getImagingFunction() {
		return imagingFunction;
	}

	public String getType() {
		return type;
	}

	public String getClassName() {
		return className;
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

	public void setupDomainFunction(String createdBy, int index)
			throws Exception {
		className = ClassUtils.getShortClassNameFromDisplayName(type);

		Class clazz = ClassUtils.getFullClass(className);
		// special case for transfection as a function
		if (clazz == null || className.equals("Transfection")) {
			clazz = OtherFunction.class;
		}
		if (domainFunction == null
				|| domainFunction != null
				&& !clazz.getCanonicalName().equals(
						domainFunction.getClass().getCanonicalName())) {
			try {
				domainFunction = (Function) clazz.newInstance();
			} catch (ClassCastException ex) {
				String tmpType = type;
				this.setType(null);
				throw new ClassCastException(tmpType);
			}
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
			int i = 0;
			// targets are not saved separately so needs setupDomainTarget here.
			for (TargetBean targetBean : targets) {
				targetBean.setupDomainTarget(createdBy, i);
				((TargetingFunction) domainFunction).getTargetCollection().add(
						targetBean.getDomainTarget());
				i++;
			}
		} else if (domainFunction instanceof OtherFunction) {
			((OtherFunction) domainFunction).setType(type);
		}
		domainFunction.setDescription(description);

		// updated created_date and created_by if id is null
		if (domainFunction.getId() == null) {
			domainFunction.setCreatedBy(createdBy);
			// fix for MySQL database, which supports precision only up to
			// seconds
			domainFunction.setCreatedDate(DateUtils
					.addSecondsToCurrentDate(index));
		}
		// updated created_by if created_by contains copy, but keep the original
		// created_date
		if (domainFunction.getId() != null
				|| !StringUtils.isEmpty(domainFunction.getCreatedBy())
				&& domainFunction.getCreatedBy().contains(
						Constants.AUTO_COPY_ANNOTATION_PREFIX)) {
			domainFunction.setCreatedBy(createdBy);
		}
	}

	private void updateType() {
		if (domainFunction instanceof OtherFunction) {
			type = ((OtherFunction) domainFunction).getType();
		} else {
			type = ClassUtils.getDisplayName(className);
		}
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDisplayName() {
		StringBuffer buffer = new StringBuffer();
		if (!StringUtils.isEmpty(type)) {
			buffer.append(type);
		}
		if (!StringUtils.isEmpty(getDescription())) {
			buffer.append(": " + description);
		}
		if (!StringUtils.isEmpty(imagingFunction.getModality())) {
			buffer.append(" (imaging modality: ");
			buffer.append(imagingFunction.getModality());
			buffer.append(")");
		}
		if (targets != null && !targets.isEmpty()) {
			buffer.append(" (targets: ");
			int i = 0;
			for (TargetBean target : targets) {
				buffer.append(target.getDisplayName());
				i++;
				if (i < targets.size()) {
					buffer.append(", ");
				}
			}
			buffer.append(")");
		}
		return buffer.toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setImagingFunction(ImagingFunction imagingFunction) {
		this.imagingFunction = imagingFunction;
	}

	public void setTargets(List<TargetBean> targets) {
		this.targets = targets;
	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof FunctionBean) {
			FunctionBean f = (FunctionBean) obj;
			String thisId = this.getId();
			if (thisId != null && thisId.equals(f.getId())) {
				eq = true;
			}
		}
		return eq;
	}

	public String[] getTargetDisplayNames() {
		List<String> displayNames = new ArrayList<String>();
		for (TargetBean target : targets) {
			displayNames.add(target.getDisplayName());
		}
		if (displayNames.isEmpty()) {
			return null;
		}
		return displayNames.toArray(new String[displayNames.size()]);
	}

	public void addTarget(TargetBean target) {
		// if an old one exists, remove it first
		int index = targets.indexOf(target);
		if (index != -1) {
			targets.remove(target);
			// retain the original order
			targets.add(index, target);
		} else {
			targets.add(target);
		}
	}

	public void removeTarget(TargetBean target) {
		targets.remove(target);
	}

	public TargetBean getTheTarget() {
		return theTarget;
	}

	public void setTheTarget(TargetBean theTarget) {
		this.theTarget = theTarget;
	}

	public void resetDomainCopy(String createdBy, Function copy) {
		copy.setId(null);
		copy.setCreatedBy(createdBy + ":"
				+ Constants.AUTO_COPY_ANNOTATION_PREFIX);
		if (copy instanceof TargetingFunction) {
			if (((TargetingFunction) copy).getTargetCollection() == null
					|| ((TargetingFunction) copy).getTargetCollection()
							.isEmpty()) {
				((TargetingFunction) copy).setTargetCollection(null);
			} else {
				Collection<Target> targets = ((TargetingFunction) copy)
						.getTargetCollection();
				((TargetingFunction) copy)
						.setTargetCollection(new HashSet<Target>());
				((TargetingFunction) copy).getTargetCollection()
						.addAll(targets);
				for (Target target : ((TargetingFunction) copy)
						.getTargetCollection()) {
					target.setId(null);
					target.setCreatedBy(createdBy + ":"
							+ Constants.AUTO_COPY_ANNOTATION_PREFIX);
				}
			}
		}
	}
}
