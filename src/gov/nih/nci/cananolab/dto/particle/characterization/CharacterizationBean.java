package gov.nih.nci.cananolab.dto.particle.characterization;

import gov.nih.nci.cananolab.domain.common.DerivedBioAssayData;
import gov.nih.nci.cananolab.domain.common.DerivedDatum;
import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.common.InstrumentConfiguration;
import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.dto.common.ProtocolFileBean;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * This class represents shared characterization properties to be shown in
 * characterization view pages.
 * 
 * @author pansu
 * 
 */
public class CharacterizationBean {
	private String characterizationSource;

	// used to distinguish different instances of characterizations, which are
	// shown as different links on the view pages.
	private String viewTitle;

	private String description;

	private String type; // e.g. Molecular Weight, Size

	private InstrumentConfiguration instrumentConfig = new InstrumentConfiguration();

	private List<DerivedBioAssayDataBean> derivedBioAssayDataList = new ArrayList<DerivedBioAssayDataBean>();

	private ProtocolFileBean protocolFileBean = new ProtocolFileBean();

	protected Characterization domainChar;

	private String className;

	public CharacterizationBean() {
		instrumentConfig.setInstrument(new Instrument());
	}

	public CharacterizationBean(Characterization chara) {
		domainChar = chara;
		className = ClassUtils.getShortClassName(chara.getClass().getName());
		this.description = chara.getDescription();
		this.viewTitle = chara.getIdentificationName();
		this.characterizationSource = chara.getSource();
		if (chara.getInstrumentConfiguration() != null) {
			instrumentConfig = chara.getInstrumentConfiguration();
		}
		if (instrumentConfig.getInstrument() == null) {
			instrumentConfig.setInstrument(new Instrument());
		}
		if (chara.getDerivedBioAssayDataCollection() != null) {
			for (DerivedBioAssayData bioassayData : chara
					.getDerivedBioAssayDataCollection()) {
				derivedBioAssayDataList.add(new DerivedBioAssayDataBean(
						bioassayData));
			}
		}
		if (chara.getProtocolFile() != null) {
			protocolFileBean = new ProtocolFileBean(chara.getProtocolFile());
		}
	}

	public Characterization getDomainCopy(boolean copyDerivedDatum) {
		Characterization copy = (Characterization) ClassUtils
				.deepCopy(domainChar);
		// clear Ids, reset createdBy and createdDate, add prefix to
		copy.setId(null);
		copy.setCreatedBy(CaNanoLabConstants.AUTO_COPY_ANNOTATION_PREFIX);
		copy.setCreatedDate(new Date());
		copy
				.setIdentificationName(CaNanoLabConstants.AUTO_COPY_ANNOTATION_PREFIX
						+ "_"
						+ StringUtils.convertDateToString(new Date(),
								"yyyyMMdd_HH-mm-ss-SSS"));
		copy.getInstrumentConfiguration().setId(null);
		if (copy.getDerivedBioAssayDataCollection().isEmpty()) {
			copy.setDerivedBioAssayDataCollection(null);
		} else {
			Collection<DerivedBioAssayData> bioassays = copy
					.getDerivedBioAssayDataCollection();
			copy
					.setDerivedBioAssayDataCollection(new HashSet<DerivedBioAssayData>());
			copy.getDerivedBioAssayDataCollection().addAll(bioassays);
			for (DerivedBioAssayData bioassay : copy
					.getDerivedBioAssayDataCollection()) {
				bioassay.setId(null);
				bioassay
						.setCreatedBy(CaNanoLabConstants.AUTO_COPY_ANNOTATION_PREFIX);
				bioassay.setCreatedDate(new Date());
				if (bioassay.getDerivedDatumCollection().isEmpty()
						|| !copyDerivedDatum) {
					bioassay.setDerivedDatumCollection(null);
				} else {
					Collection<DerivedDatum> data = bioassay
							.getDerivedDatumCollection();
					bioassay
							.setDerivedDatumCollection(new HashSet<DerivedDatum>());
					bioassay.getDerivedDatumCollection().addAll(data);
					for (DerivedDatum datum : bioassay
							.getDerivedDatumCollection()) {
						datum.setId(null);
						datum
								.setCreatedBy(CaNanoLabConstants.AUTO_COPY_ANNOTATION_PREFIX);
						datum.setCreatedDate(new Date());
					}
				}
			}
		}
		return copy;
	}

	public void setupDomainChar(Map<String, String> typeToClass,
			String createdBy) {
		try {
			// take care of characterizations that don't have any special
			// properties shown in the form, e.g. Size
			className = typeToClass.get(type);
			if (domainChar == null) {
				Class clazz = ClassUtils.getFullClass(className);
				domainChar = (Characterization) clazz.newInstance();
			}
			if (domainChar.getId() == null
					|| domainChar.getCreatedBy().equals(
							CaNanoLabConstants.AUTO_COPY_ANNOTATION_PREFIX)) {
				domainChar.setCreatedBy(createdBy);
				domainChar.setCreatedDate(new Date());
			}
			domainChar.setDescription(description);
			domainChar.setIdentificationName(viewTitle);
			domainChar.setSource(characterizationSource);

			if (instrumentConfig.getInstrument() != null
					&& instrumentConfig.getInstrument().getType() != null
					&& instrumentConfig.getInstrument().getType().length() > 0) {
				if (instrumentConfig.getId() == null) {
					instrumentConfig.setCreatedBy(createdBy);
					instrumentConfig.setCreatedDate(new Date());
				}
				domainChar.setInstrumentConfiguration(instrumentConfig);
			}
			// domainChar
			// .setProtocolFile(protocolFileBean.getDomainProtocolFile());
			if (domainChar.getDerivedBioAssayDataCollection() != null) {
				domainChar.getDerivedBioAssayDataCollection().clear();
			} else {
				domainChar
						.setDerivedBioAssayDataCollection(new HashSet<DerivedBioAssayData>());
			}
			// set createdBy and createdDate
			for (DerivedBioAssayDataBean bioAssayData : derivedBioAssayDataList) {
				if (bioAssayData.getDomainBioAssayData().getId() == null) {
					bioAssayData.getDomainBioAssayData()
							.setCreatedBy(createdBy);
					bioAssayData.getDomainBioAssayData().setCreatedDate(
							new Date());
					domainChar.getDerivedBioAssayDataCollection().add(
							bioAssayData.getDomainBioAssayData());
					for (DerivedDatum datum : bioAssayData.getDatumList()) {
						if (datum.getId() == null) {
							datum.setCreatedBy(createdBy);
							datum.setCreatedDate(new Date());
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getCharacterizationSource() {
		return this.characterizationSource;
	}

	public void setCharacterizationSource(String characterizationSource) {
		this.characterizationSource = characterizationSource;
	}

	public String getViewTitle() {
		// get only the first number of characters of the title
		if (this.viewTitle != null
				&& this.viewTitle.length() > CaNanoLabConstants.MAX_VIEW_TITLE_LENGTH) {
			return this.viewTitle.substring(0,
					CaNanoLabConstants.MAX_VIEW_TITLE_LENGTH);
		}
		return this.viewTitle;
	}

	public void setViewTitle(String viewTitle) {
		this.viewTitle = viewTitle;
	}

	public void addDerivedBioAssayData() {
		derivedBioAssayDataList.add(new DerivedBioAssayDataBean());
	}

	public void removeDerivedBioAssayData(int ind) {
		derivedBioAssayDataList.remove(ind);
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<DerivedBioAssayDataBean> getDerivedBioAssayDataList() {
		return this.derivedBioAssayDataList;
	}

	public InstrumentConfiguration getInstrumentConfiguration() {
		return instrumentConfig;
	}

	public ProtocolFileBean getProtocolFileBean() {
		return protocolFileBean;
	}

	public Characterization getDomainChar() {
		return domainChar;
	}

	public String getClassName() {
		return className;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
