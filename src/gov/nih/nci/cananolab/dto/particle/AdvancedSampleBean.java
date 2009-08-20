package gov.nih.nci.cananolab.dto.particle;

import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents shared properties of samples resulted from advanced
 * sample search.
 * 
 * @author pansu
 * 
 */
public class AdvancedSampleBean {
	private String sampleName;
	private String location; // e.g. NCICB, NCL, WUSTL, etc.
	private String sampleId;
	private List<PointOfContact> pointOfContacts;
	private List<Function> functions;
	private List<NanomaterialEntity> nanomaterialEntities;
	private List<FunctionalizingEntity> functionalizingEntities;
	private List<Characterization> characterizations;
	private List<Datum> data;
	private AdvancedSampleSearchBean advancedSearchBean;

	public AdvancedSampleBean() {
	}

	public AdvancedSampleBean(String sampleName) {
		super();
		this.sampleName = sampleName;
	}

	public AdvancedSampleBean(String sampleName, String location) {
		this(sampleName);
		this.location = location;
	}

	public AdvancedSampleBean(String sampleName, String sampleId,
			List<PointOfContact> pointOfContacts, List<Function> functions,
			List<NanomaterialEntity> nanomaterialEntities,
			List<FunctionalizingEntity> functionalizingEntities,
			List<Characterization> characterizations, List<Datum> data,
			AdvancedSampleSearchBean searchBean) {
		this(sampleName);
		this.sampleId = sampleId;
		this.pointOfContacts = pointOfContacts;
		this.functions = functions;
		this.nanomaterialEntities = nanomaterialEntities;
		this.functionalizingEntities = functionalizingEntities;
		this.characterizations = characterizations;
		this.data = data;
		advancedSearchBean = searchBean;
	}

	public AdvancedSampleSearchBean getAdvancedSearchBean() {
		return advancedSearchBean;
	}

	public void setAdvancedSearchBean(
			AdvancedSampleSearchBean advancedSearchBean) {
		this.advancedSearchBean = advancedSearchBean;
	}

	public String getSampleName() {
		return sampleName;
	}

	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}

	public String getSampleId() {
		return sampleId;
	}

	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}

	public Map<String, List<String>> getAttributeMap() {
		Map<String, List<String>> attributeMap = new LinkedHashMap<String, List<String>>();
		for (String columnName : advancedSearchBean.getQueryAsColumnNames()) {
			List<String> strs = new ArrayList<String>();
			if (columnName.contains("point of contact")) {
				for (PointOfContact poc : pointOfContacts) {
					PointOfContactBean pocBean = new PointOfContactBean(poc);
					strs.add(pocBean.getDisplayName());
				}
			} else if (columnName.contains("nanomaterial entity")) {
				boolean hasName = false;
				for (NanomaterialEntity entity : nanomaterialEntities) {
					// chemical name column
					String entityName = ClassUtils.getDisplayName(ClassUtils
							.getShortClassName(entity.getClass().getName()));
					if (columnName.contains(entityName)) {
						for (ComposingElement ce : entity
								.getComposingElementCollection()) {
							strs.add(ce.getName());
						}
						hasName = true;
						break;
					}
				}
				// entity type column
				if (!hasName) {
					for (NanomaterialEntity entity : nanomaterialEntities) {
						NanomaterialEntityBean entityBean = new NanomaterialEntityBean(
								entity);
						strs.add(entityBean.getType());
						for (ComposingElementBean ceBean : entityBean
								.getComposingElements()) {
							strs.add(ceBean.getAdvancedSearchDisplayName());
						}
					}
				}
			} else if (columnName.contains("functionalizing entity")) {
				boolean hasName = false;
				for (FunctionalizingEntity entity : functionalizingEntities) {
					String entityName = ClassUtils.getDisplayName(ClassUtils
							.getShortClassName(entity.getClass().getName()));
					FunctionalizingEntityBean entityBean = new FunctionalizingEntityBean(
							entity);
					if (columnName.contains(entityName)) {
						strs.add(entityBean.getAdvancedSearchDisplayName());
						hasName = true;
						break;
					}
				}
				if (!hasName) {
					for (FunctionalizingEntity entity : functionalizingEntities) {
						FunctionalizingEntityBean entityBean = new FunctionalizingEntityBean(
								entity);
						strs.add(entityBean.getAdvancedSearchDisplayName());
					}
				}
			} else if (columnName.contains("function")) {
				for (Function func : functions) {
					FunctionBean funcBean = new FunctionBean(func);
					strs.add(funcBean.getDisplayName());
				}
			} else if (columnName.contains("characterization")) {
				boolean hasDatum = false;
				for (Datum datum : data) {
					if (columnName.contains(datum.getName())) {
						strs.add(datum.getValue() + " " + datum.getValueUnit());
						hasDatum = true;
						break;
					}
				}
				if (!hasDatum && !columnName.contains("<br>")) {
					for (Characterization chara : characterizations) {
						String charName = ClassUtils.getDisplayName(ClassUtils
								.getShortClassName(chara.getClass().getName()));
						strs.add(charName);
					}
				}
			}
			attributeMap.put(columnName, strs);
		}
		return attributeMap;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<PointOfContact> getPointOfContacts() {
		return pointOfContacts;
	}

	public List<Function> getFunctions() {
		return functions;
	}

	public List<NanomaterialEntity> getNanomaterialEntities() {
		return nanomaterialEntities;
	}

	public List<FunctionalizingEntity> getFunctionalizingEntities() {
		return functionalizingEntities;
	}

	public List<Characterization> getCharacterizations() {
		return characterizations;
	}

	public List<Datum> getData() {
		return data;
	}
}
