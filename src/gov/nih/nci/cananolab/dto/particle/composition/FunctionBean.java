package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.particle.samplecomposition.Function;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.ImagingFunction;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.OtherFunction;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.Target;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.TargetingFunction;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.DateUtil;

import java.util.ArrayList;
import java.util.Collections;
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
			if (((TargetingFunction) function).getTargetCollection() != null) {
				for (Target target : ((TargetingFunction) function)
						.getTargetCollection()) {
					targets.add(new TargetBean(target));
				}
				Collections.sort(targets,
						new CaNanoLabComparators.TargetBeanDateComparator());
			}
		}
		className = ClassUtils.getShortClassName(function.getClass()
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

	public void addTarget() {
		targets.add(new TargetBean());
	}

	public void removeTarget(int ind) {
		targets.remove(ind);
	}

	public void setupDomainFunction(Map<String, String> typeToClass,
			String createdBy, int index) throws Exception {
		className = typeToClass.get(type.toLowerCase());
		Class clazz = null;
		if (className != null) {
			clazz = ClassUtils.getFullClass(className);
		} else {
			clazz = OtherFunction.class;
		}
		if (domainFunction == null
				|| domainFunction != null
				&& !clazz.getCanonicalName().equals(
						domainFunction.getClass().getCanonicalName())) {
			try {
				domainFunction = (Function) clazz.newInstance();
			}catch (ClassCastException ex) {
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
			for (TargetBean targetBean : targets) {
				targetBean.setupDomainTarget(typeToClass, createdBy, i);
				((TargetingFunction) domainFunction).getTargetCollection().add(
						targetBean.getDomainTarget());
				i++;
			}
		} else if (domainFunction instanceof OtherFunction) {
			((OtherFunction) domainFunction).setType(type);
		}
		domainFunction.setDescription(description);
		if (domainFunction.getId() == null
				|| domainFunction.getCreatedBy() != null
				&& domainFunction.getCreatedBy().equals(
						CaNanoLabConstants.AUTO_COPY_ANNOTATION_PREFIX)) {
			domainFunction.setCreatedBy(createdBy);
			// domainFunction.setCreatedDate(new Date());
			// fix for MySQL database, which supports precision only up to
			// seconds
			domainFunction.setCreatedDate(DateUtil
					.addSecondsToCurrentDate(index));
		}
	}

	public void updateType(Map<String, String> classToType) {
		if (domainFunction instanceof OtherFunction) {
			type = ((OtherFunction) domainFunction).getType();
		} else {
			type = classToType.get(className);
		}
		if (domainFunction instanceof TargetingFunction) {
			for (TargetBean targetBean : getTargets()) {
				targetBean.updateType(classToType);
			}
		}
	}

	public void setType(String type) {
		this.type = type;
	}

}
