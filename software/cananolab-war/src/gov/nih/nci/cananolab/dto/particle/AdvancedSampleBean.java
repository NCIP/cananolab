package gov.nih.nci.cananolab.dto.particle;

import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.LinkableItem;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.StringUtils;

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
	public static String SAMPLE_DETAIL_URL = "sample.do?page=0&dispatch=setupView";
	public static String NANOMATERIAL_DETAIL_URL = "nanomaterialEntity.do?page=0&dispatch=setupView";
	public static String AGENTMATERIAL_DETAIL_URL = "functionalizingEntity.do?page=0&dispatch=setupView";
	public static String CHARACTERIZATION_DETAIL_URL = "characterization.do?page=0&dispatch=setupView";
	private Sample domainSample;
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
			AdvancedSampleSearchBean searchBean, Sample sample) {
		this(sampleName);
		this.sampleId = sampleId;
		this.pointOfContacts = pointOfContacts;
		this.functions = functions;
		this.nanomaterialEntities = nanomaterialEntities;
		this.functionalizingEntities = functionalizingEntities;
		this.characterizations = characterizations;
		this.data = data;
		advancedSearchBean = searchBean;
		this.domainSample = sample;
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

	public Map<String, List<LinkableItem>> getAttributeMap() {
		String linkSuffix = "&sampleId=" + sampleId + "&location=" + location;
		Map<String, List<LinkableItem>> attributeMap = new LinkedHashMap<String, List<LinkableItem>>();
		for (String columnName : advancedSearchBean.getQueryAsColumnNames()) {
			List<LinkableItem> items = new ArrayList<LinkableItem>();
			if (columnName.contains("point of contact")) {
				LinkableItem item = new LinkableItem();
				item.setAction(SAMPLE_DETAIL_URL + linkSuffix);
				for (PointOfContact poc : pointOfContacts) {
					PointOfContactBean pocBean = new PointOfContactBean(poc);
					item.getDisplayStrings().add(pocBean.getDisplayName());
				}
				items.add(item);
			} else if (columnName.contains("nanomaterial entity")) {
				boolean hasName = false;
				for (NanomaterialEntity entity : nanomaterialEntities) {
					NanomaterialEntityBean entityBean = new NanomaterialEntityBean(
							entity);
					// chemical name column
					String entityName = ClassUtils.getDisplayName(ClassUtils
							.getShortClassName(entity.getClass().getName()));
					if (columnName.contains(entityName)) {
						LinkableItem item = new LinkableItem();
						item.setAction(NANOMATERIAL_DETAIL_URL + linkSuffix
								+ "&dataId=" + entity.getId());
						item.getDisplayStrings().add(entityBean.getType());
						for (ComposingElement ce : entity
								.getComposingElementCollection()) {
							ComposingElementBean ceBean = new ComposingElementBean(
									ce);
							item.getDisplayStrings().add(
									ceBean.getAdvancedSearchDisplayName());
						}
						hasName = true;
						items.add(item);
					}
				}
				// entity type column
				if (!hasName) {
					for (NanomaterialEntity entity : nanomaterialEntities) {
						NanomaterialEntityBean entityBean = new NanomaterialEntityBean(
								entity);
						LinkableItem item = new LinkableItem();
						item.setAction(NANOMATERIAL_DETAIL_URL + linkSuffix
								+ "&dataId=" + entity.getId());
						item.getDisplayStrings().add(entityBean.getType());
						for (ComposingElementBean ceBean : entityBean
								.getComposingElements()) {
							item.getDisplayStrings().add(
									ceBean.getAdvancedSearchDisplayName());
						}
						items.add(item);
					}
				}
			} else if (columnName.contains("functionalizing entity")) {
				boolean hasName = false;
				for (FunctionalizingEntity entity : functionalizingEntities) {
					String entityName = ClassUtils.getDisplayName(ClassUtils
							.getShortClassName(entity.getClass().getName()));
					if (columnName.contains(entityName)) {
						FunctionalizingEntityBean entityBean = new FunctionalizingEntityBean(
								entity);
						LinkableItem item = new LinkableItem();
						item.setAction(AGENTMATERIAL_DETAIL_URL + linkSuffix
								+ "&dataId=" + entity.getId());
						item.getDisplayStrings().add(
								entityBean.getAdvancedSearchDisplayName());
						items.add(item);
						hasName = true;
					}

				}
				if (!hasName) {
					for (FunctionalizingEntity entity : functionalizingEntities) {
						LinkableItem item = new LinkableItem();
						item.setAction(AGENTMATERIAL_DETAIL_URL + linkSuffix
								+ "&dataId=" + entity.getId());
						FunctionalizingEntityBean entityBean = new FunctionalizingEntityBean(
								entity);
						item.getDisplayStrings().add(
								entityBean.getAdvancedSearchDisplayName());
						items.add(item);
					}
				}
			} else if (columnName.contains("function")) {
				for (Function func : functions) {
					LinkableItem item = new LinkableItem();
					FunctionBean funcBean = new FunctionBean(func);
					// find corresponding nanomaterial entity from domainSample
					for (NanomaterialEntity entity : domainSample
							.getSampleComposition()
							.getNanomaterialEntityCollection()) {
						for (ComposingElement ce : entity
								.getComposingElementCollection()) {
							for (Function function : ce
									.getInherentFunctionCollection()) {
								if (func.equals(function)) {
									item
											.setAction(NANOMATERIAL_DETAIL_URL
													+ linkSuffix + "&dataId="
													+ entity.getId());
									break;
								}
							}
						}
					}
					// find corresponding functionalizing entity from
					// domainSample
					for (FunctionalizingEntity entity : domainSample
							.getSampleComposition()
							.getFunctionalizingEntityCollection()) {
						for (Function function : entity.getFunctionCollection()) {
							if (func.equals(function)) {
								item.setAction(AGENTMATERIAL_DETAIL_URL
										+ linkSuffix + "&dataId=" + entity.getId());
								break;
							}
						}
					}
					item.getDisplayStrings().add(funcBean.getDisplayName());
					items.add(item);
				}
			} else if (columnName.contains("characterization")) {
				boolean hasDatum = false;
				for (Datum datum : data) {
					if (columnName.contains(datum.getName())) {
						LinkableItem item = new LinkableItem();
						// find corresponding characterization from domainSample
						for (Characterization chara : domainSample
								.getCharacterizationCollection()) {
							for (Finding finding : chara.getFindingCollection()) {
								for (Datum datum0 : finding
										.getDatumCollection()) {
									if (datum.equals(datum0)) {
										item
												.setAction(CHARACTERIZATION_DETAIL_URL
														+ linkSuffix+"&charId="
														+ chara.getId());
										break;
									}
								}
							}
						}
						item.getDisplayStrings().add(
								datum.getValue() + " " + datum.getValueUnit());
						hasDatum = true;
						items.add(item);
					}
				}
				if (!hasDatum && !columnName.contains("<br>")) {
					for (Characterization chara : characterizations) {
						String characterizationType = ClassUtils
								.getDisplayName(ClassUtils
										.getShortClassName(chara.getClass()
												.getSuperclass().getName()));
						LinkableItem item = new LinkableItem();
						item.setAction(CHARACTERIZATION_DETAIL_URL + linkSuffix
								+ "&charId=" + chara.getId());
						if (columnName.contains(characterizationType)) {
							String charName = ClassUtils
									.getDisplayName(ClassUtils
											.getShortClassName(chara.getClass()
													.getName()));
							if (!StringUtils.isEmpty(chara.getAssayType())) {
								item.getDisplayStrings().add(
										charName + ":" + chara.getAssayType());
							} else {
								item.getDisplayStrings().add(charName);
							}
						}
						items.add(item);
					}
				}
			}
			attributeMap.put(columnName, items);
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

	public Sample getDomainSample() {
		return domainSample;
	}
}
