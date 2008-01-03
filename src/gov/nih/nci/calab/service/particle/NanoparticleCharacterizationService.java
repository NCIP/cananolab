package gov.nih.nci.calab.service.particle;

import gov.nih.nci.calab.db.HibernateUtil;
import gov.nih.nci.calab.domain.Instrument;
import gov.nih.nci.calab.domain.InstrumentConfiguration;
import gov.nih.nci.calab.domain.LookupType;
import gov.nih.nci.calab.domain.MeasureType;
import gov.nih.nci.calab.domain.MeasureUnit;
import gov.nih.nci.calab.domain.ProtocolFile;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.CharacterizationFileType;
import gov.nih.nci.calab.domain.nano.characterization.DatumName;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayData;
import gov.nih.nci.calab.domain.nano.characterization.DerivedBioAssayDataCategory;
import gov.nih.nci.calab.domain.nano.characterization.invitro.CFU_GM;
import gov.nih.nci.calab.domain.nano.characterization.invitro.Caspase3Activation;
import gov.nih.nci.calab.domain.nano.characterization.invitro.CellLineType;
import gov.nih.nci.calab.domain.nano.characterization.invitro.CellViability;
import gov.nih.nci.calab.domain.nano.characterization.invitro.Chemotaxis;
import gov.nih.nci.calab.domain.nano.characterization.invitro.Coagulation;
import gov.nih.nci.calab.domain.nano.characterization.invitro.ComplementActivation;
import gov.nih.nci.calab.domain.nano.characterization.invitro.CytokineInduction;
import gov.nih.nci.calab.domain.nano.characterization.invitro.EnzymeInduction;
import gov.nih.nci.calab.domain.nano.characterization.invitro.Hemolysis;
import gov.nih.nci.calab.domain.nano.characterization.invitro.LeukocyteProliferation;
import gov.nih.nci.calab.domain.nano.characterization.invitro.NKCellCytotoxicActivity;
import gov.nih.nci.calab.domain.nano.characterization.invitro.OxidativeBurst;
import gov.nih.nci.calab.domain.nano.characterization.invitro.OxidativeStress;
import gov.nih.nci.calab.domain.nano.characterization.invitro.Phagocytosis;
import gov.nih.nci.calab.domain.nano.characterization.invitro.PlasmaProteinBinding;
import gov.nih.nci.calab.domain.nano.characterization.invitro.PlateletAggregation;
import gov.nih.nci.calab.domain.nano.characterization.physical.MolecularWeight;
import gov.nih.nci.calab.domain.nano.characterization.physical.Morphology;
import gov.nih.nci.calab.domain.nano.characterization.physical.MorphologyType;
import gov.nih.nci.calab.domain.nano.characterization.physical.Purity;
import gov.nih.nci.calab.domain.nano.characterization.physical.Shape;
import gov.nih.nci.calab.domain.nano.characterization.physical.ShapeType;
import gov.nih.nci.calab.domain.nano.characterization.physical.Size;
import gov.nih.nci.calab.domain.nano.characterization.physical.Solubility;
import gov.nih.nci.calab.domain.nano.characterization.physical.SolventType;
import gov.nih.nci.calab.domain.nano.characterization.physical.Surface;
import gov.nih.nci.calab.domain.nano.characterization.toxicity.Cytotoxicity;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;
import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.CharacterizationSummaryBean;
import gov.nih.nci.calab.dto.characterization.CharacterizationTypeBean;
import gov.nih.nci.calab.dto.characterization.DatumBean;
import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.calab.dto.characterization.invitro.CytotoxicityBean;
import gov.nih.nci.calab.dto.characterization.physical.MorphologyBean;
import gov.nih.nci.calab.dto.characterization.physical.ShapeBean;
import gov.nih.nci.calab.dto.characterization.physical.SolubilityBean;
import gov.nih.nci.calab.dto.characterization.physical.SurfaceBean;
import gov.nih.nci.calab.dto.common.InstrumentBean;
import gov.nih.nci.calab.dto.common.InstrumentConfigBean;
import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.common.ProtocolFileBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.exception.CaNanoLabSecurityException;
import gov.nih.nci.calab.exception.ParticleCharacterizationException;
import gov.nih.nci.calab.service.common.FileService;
import gov.nih.nci.calab.service.common.LookupService;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;
import gov.nih.nci.calab.service.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

/**
 * This class includes service calls involved in creating nanoparticle general
 * info and adding functions and characterizations for nanoparticles, as well as
 * creating reports.
 * 
 * @author pansu
 * 
 */
public class NanoparticleCharacterizationService {
	private static Logger logger = Logger
			.getLogger(NanoparticleCharacterizationService.class);

	// remove existing visibilities for the data
	private UserService userService;

	public NanoparticleCharacterizationService()
			throws CaNanoLabSecurityException {
		this.userService = new UserService(CaNanoLabConstants.CSM_APP_NAME);
	}

	public CharacterizationBean getCharacterizationBy(String charId,
			UserBean user) throws ParticleCharacterizationException,
			CaNanoLabSecurityException {
		CharacterizationBean charBean = null;
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			// Characterization aChar = (Characterization) session.load(
			// Characterization.class, new Long(charId));
			List results = session.createQuery(
					"from Characterization where id=" + charId).list();
			Characterization aChar = null;
			for (Object obj : results) {
				aChar = (Characterization) obj;
			}
			if (aChar == null) {
				return null;
			}
			if (aChar instanceof Shape) {
				charBean = new ShapeBean((Shape) aChar);
			} else if (aChar instanceof Morphology) {
				charBean = new MorphologyBean((Morphology) aChar);
			} else if (aChar instanceof Solubility) {
				charBean = new SolubilityBean((Solubility) aChar);
			} else if (aChar instanceof Surface) {
				charBean = new SurfaceBean((Surface) aChar);
			} else if (aChar instanceof Cytotoxicity) {
				charBean = new CytotoxicityBean((Cytotoxicity) aChar);
			} else {
				charBean = new CharacterizationBean(aChar);
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem finding characterization of ID " + charId, e);
			throw new ParticleCharacterizationException();
		} finally {
			HibernateUtil.closeSession();
		}
		if (user != null) {
			this.setCharacterizationUserVisiblity(charBean, user);
		}
		return charBean;
	}

	public List<CharacterizationBean> getCharacterizationInfo(String particleId)
			throws ParticleCharacterizationException {
		List<CharacterizationBean> charBeans = new ArrayList<CharacterizationBean>();

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			List results = session
					.createQuery(
							"select chara.id, chara.name, chara.identificationName from Nanoparticle particle join particle.characterizationCollection chara where particle.id="
									+ particleId
									+ " order by chara.name, chara.identificationName")
					.list();
			for (Object obj : results) {
				String charId = ((Object[]) obj)[0].toString();
				String charName = (String) (((Object[]) obj)[1]);
				String viewTitle = (String) (((Object[]) obj)[2]);
				CharacterizationBean charBean = new CharacterizationBean(
						charId, charName, viewTitle);
				charBeans.add(charBean);
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem finding characterization info for particle: "
					+ particleId, e);
			throw new ParticleCharacterizationException();
		} finally {
			HibernateUtil.closeSession();
		}
		return charBeans;
	}

	public List<CharacterizationSummaryBean> getParticleCharacterizationSummaryByName(
			String charName, String particleId, UserBean user)
			throws ParticleCharacterizationException,
			CaNanoLabSecurityException {
		List<CharacterizationSummaryBean> charSummaryBeans = new ArrayList<CharacterizationSummaryBean>();
		List<CharacterizationBean> charBeans = getParticleCharacterizationsByName(
				charName, particleId);
		if (charBeans.isEmpty()) {
			return null;
		}
		for (CharacterizationBean charBean : charBeans) {
			setCharacterizationUserVisiblity(charBean, user);
			if (charBean.getDerivedBioAssayDataList() != null
					&& !charBean.getDerivedBioAssayDataList().isEmpty()) {
				for (DerivedBioAssayDataBean charFile : charBean
						.getDerivedBioAssayDataList()) {
					Map<String, String> datumMap = new HashMap<String, String>();
					for (DatumBean data : charFile.getDatumList()) {
						String datumLabel = data.getName();
						if (data.getUnit() != null
								&& data.getUnit().length() > 0) {
							datumLabel += "(" + data.getUnit() + ")";
						}
						datumMap.put(datumLabel, data.getValue());
					}
					CharacterizationSummaryBean charSummaryBean = new CharacterizationSummaryBean();
					charSummaryBean.setCharBean(charBean);
					charSummaryBean.setDatumMap(datumMap);
					charSummaryBean.setCharFile(charFile);
					charSummaryBeans.add(charSummaryBean);
				}
			} else {
				CharacterizationSummaryBean charSummaryBean = new CharacterizationSummaryBean();
				charSummaryBean.setCharBean(charBean);
				charSummaryBeans.add(charSummaryBean);
			}
		}
		return charSummaryBeans;
	}

	public List<CharacterizationBean> getParticleCharacterizationsByName(
			String charName, String particleId)
			throws ParticleCharacterizationException {
		List<CharacterizationBean> charBeans = new ArrayList<CharacterizationBean>();
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			// Characterization aChar = (Characterization) session.load(
			// Characterization.class, new Long(charId));
			List results = session
					.createQuery(
							"select chara from Nanoparticle particle join particle.characterizationCollection chara where particle.id="
									+ particleId
									+ " and chara.name='"
									+ charName
									+ "'"
									+ " order by chara.identificationName")
					.list();

			Characterization aChar = null;
			for (Object obj : results) {
				aChar = (Characterization) obj;
				CharacterizationBean charBean = null;
				if (aChar instanceof Shape) {
					charBean = new ShapeBean((Shape) aChar);
				} else if (aChar instanceof Morphology) {
					charBean = new MorphologyBean((Morphology) aChar);
				} else if (aChar instanceof Solubility) {
					charBean = new SolubilityBean((Solubility) aChar);
				} else if (aChar instanceof Surface) {
					charBean = new SurfaceBean((Surface) aChar);
				} else if (aChar instanceof Cytotoxicity) {
					charBean = new CytotoxicityBean((Cytotoxicity) aChar);
				} else {
					charBean = new CharacterizationBean(aChar);
				}
				if (charBean != null) {
					charBeans.add(charBean);
				}
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem finding characterizations with name "
					+ charName, e);
			throw new ParticleCharacterizationException();
		} finally {
			HibernateUtil.closeSession();
		}
		return charBeans;
	}

	/**
	 * Save characterization to the database.
	 * 
	 * @param particleType
	 * @param particleName
	 * @param achar
	 * @throws ParticleCharacterizationException
	 */
	private void addParticleCharacterization(Characterization achar,
			CharacterizationBean charBean)
			throws ParticleCharacterizationException {

		// if ID is not set save to the database otherwise update
		Nanoparticle particle = null;
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();

			// check if viewTitle is already used the same type of
			// characterization for the same particle
			boolean viewTitleUsed = isCharacterizationViewTitleUsed(session,
					achar, charBean);
			if (viewTitleUsed) {
				throw new ParticleCharacterizationException(
						"The view title is already in use.  Please enter a different one");
			} else {
				// if ID exists, load from database
				if (charBean.getId() != null) {
					// check if ID is still valid
					achar = (Characterization) session.get(
							Characterization.class, new Long(charBean.getId()));
					if (achar == null)
						throw new ParticleCharacterizationException(
								"This characterization is no longer in the database.  Please log in again to refresh");
				}
				// update domain object
				if (achar instanceof Shape) {
					((ShapeBean) charBean).updateDomainObj((Shape) achar);
				} else if (achar instanceof Morphology) {
					((MorphologyBean) charBean)
							.updateDomainObj((Morphology) achar);
				} else if (achar instanceof Solubility) {
					((SolubilityBean) charBean)
							.updateDomainObj((Solubility) achar);
				} else if (achar instanceof Surface) {
					((SurfaceBean) charBean).updateDomainObj((Surface) achar);
				} else if (achar instanceof Cytotoxicity) {
					((CytotoxicityBean) charBean)
							.updateDomainObj((Cytotoxicity) achar);
				} else
					charBean.updateDomainObj(achar);

				addProtocolFile(charBean.getProtocolFileBean(), achar, session);
				// store instrumentConfig and instrument
				addInstrumentConfig(charBean.getInstrumentConfigBean(), achar,
						session);

				if (charBean.getId() == null) {
					List results = session
							.createQuery(
									"from Nanoparticle particle left join fetch particle.characterizationCollection where particle.name='"
											+ charBean.getParticle()
													.getSampleName()
											+ "' and particle.type='"
											+ charBean.getParticle()
													.getSampleType() + "'")
							.list();

					for (Object obj : results) {
						particle = (Nanoparticle) obj;
					}

					if (particle != null) {
						particle.getCharacterizationCollection().add(achar);
					}
				}
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem saving characterization. ", e);
			HibernateUtil.rollbackTransaction();
			throw new ParticleCharacterizationException(e.getMessage());
		} finally {
			HibernateUtil.closeSession();
			// skip if there is database error above and achar has not
			// been persisted
			if (achar.getId() != null) {
				charBean.setId(achar.getId().toString());
				// save file to the file system
				// if this block of code is inside the db try block,
				// hibernate
				// doesn't persist derivedBioAssayData
				if (!achar.getDerivedBioAssayDataCollection().isEmpty()) {
					int count = 0;
					for (DerivedBioAssayData derivedBioAssayData : achar
							.getDerivedBioAssayDataCollection()) {
						// skip if there is database error above and
						// derivedBioAssayData has not been persisted
						if (derivedBioAssayData.getId() != null) {
							DerivedBioAssayDataBean derivedBioAssayDataBean = charBean
									.getDerivedBioAssayDataList().get(count);
							derivedBioAssayDataBean.setId(derivedBioAssayData
									.getId().toString());
							saveCharacterizationFile(derivedBioAssayDataBean);
							count++;
						}
					}
				}
			}
		}
	}

	public void addNewCharacterizationDataDropdowns(
			CharacterizationBean charBean, String characterizationName)
			throws ParticleCharacterizationException {
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();

			if (!charBean.getDerivedBioAssayDataList().isEmpty()) {
				for (DerivedBioAssayDataBean derivedBioAssayDataBean : charBean
						.getDerivedBioAssayDataList()) {
					// add new characterization file type if necessary
					if (derivedBioAssayDataBean.getType().length() > 0) {
						CharacterizationFileType fileType = new CharacterizationFileType();
						LookupService.addLookupType(session, fileType,
								derivedBioAssayDataBean.getType());
					}
					// add new derived data cateory
					for (String category : derivedBioAssayDataBean
							.getCategories()) {
						addDerivedDataCategory(session, category,
								characterizationName);
					}
					// add new datum name, measure type, and unit
					for (DatumBean datumBean : derivedBioAssayDataBean
							.getDatumList()) {
						addDatumName(session, datumBean.getName(),
								characterizationName);
						MeasureType measureType = new MeasureType();
						LookupService.addLookupType(session, measureType,
								datumBean.getStatisticsType());
						addMeasureUnit(session, datumBean.getUnit(),
								characterizationName);
					}
				}
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			logger
					.error("Problem saving characterization data drop downs. ",
							e);
			throw new ParticleCharacterizationException();
		} finally {
			HibernateUtil.closeSession();
		}
	}

	/*
	 * check if viewTitle is already used the same type of characterization for
	 * the same particle
	 */
	private boolean isCharacterizationViewTitleUsed(Session session,
			Characterization achar, CharacterizationBean charBean) {
		String viewTitleQuery = "";
		if (charBean.getId() == null) {
			viewTitleQuery = "select count(achar.identificationName) from Nanoparticle particle join particle.characterizationCollection achar where particle.name='"
					+ charBean.getParticle().getSampleName()
					+ "' and particle.type='"
					+ charBean.getParticle().getSampleType()
					+ "' and achar.identificationName='"
					+ charBean.getViewTitle()
					+ "' and achar.name='"
					+ achar.getName() + "'";
		} else {
			viewTitleQuery = "select count(achar.identificationName) from Nanoparticle particle join particle.characterizationCollection achar where particle.name='"
					+ charBean.getParticle().getSampleName()
					+ "' and particle.type='"
					+ charBean.getParticle().getSampleType()
					+ "' and achar.identificationName='"
					+ charBean.getViewTitle()
					+ "' and achar.name='"
					+ achar.getName() + "' and achar.id!=" + charBean.getId();
		}
		List viewTitleResult = session.createQuery(viewTitleQuery).list();

		int existingViewTitleCount = -1;
		for (Object obj : viewTitleResult) {
			existingViewTitleCount = ((Integer) (obj)).intValue();
		}
		if (existingViewTitleCount > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Save the file to the file system
	 * 
	 * @param fileBean
	 */
	public void saveCharacterizationFile(DerivedBioAssayDataBean fileBean)
			throws ParticleCharacterizationException {
		byte[] fileContent = fileBean.getFileContent();
		String rootPath = PropertyReader.getProperty(
				CaNanoLabConstants.FILEUPLOAD_PROPERTY, "fileRepositoryDir");
		try {
			if (fileContent != null) {
				FileService fileService = new FileService();
				fileService.writeFile(fileContent, rootPath + File.separator
						+ fileBean.getUri());
			}
			this.userService.setVisiblity(fileBean.getId(), fileBean
					.getVisibilityGroups());
		} catch (Exception e) {
			logger.error("Error saving characterization file", e);
			throw new ParticleCharacterizationException();
		}
	}

	private void addInstrumentConfig(InstrumentConfigBean instrumentConfigBean,
			Characterization doChar, Session session) {
		InstrumentBean instrumentBean = instrumentConfigBean
				.getInstrumentBean();

		if (instrumentBean.getType() == null
				&& instrumentBean.getManufacturer() == null
				&& instrumentConfigBean.getDescription() == null) {
			return;
		}
		if (instrumentBean.getType() != null
				&& instrumentBean.getType().length() == 0
				&& instrumentBean.getManufacturer().length() == 0) {
			doChar.setInstrumentConfiguration(null);
			return;
		}

		// check if instrument is already in database based on instrument type
		Instrument instrument = null;
		List instrumentResults = session.createQuery(
				"select instrument from Instrument instrument where instrument.type='"
						+ instrumentBean.getType() + "'").list();

		List<Instrument> instruments = new ArrayList<Instrument>();
		for (Object obj : instrumentResults) {
			Instrument instrumentObj = (Instrument) obj;
			instruments.add(instrumentObj);
		}
		// if not in the database, create a new one
		if (instruments.isEmpty()) {
			instrument = new Instrument();
			instrument.setType(instrumentBean.getType());
			instrument.setManufacturer(instrumentBean.getManufacturer());
			session.save(instrument);
		}
		// instrument is in the database, check whether manufacturer is in the
		// database
		else {
			String abbrev = null;
			for (Instrument instrumentObj : instruments) {
				// if same, skip
				if (instrumentObj.getManufacturer().equals(
						instrumentBean.getManufacturer())) {
					instrument = instrumentObj;
					break;
				} else {
					if (instrumentObj.getAbbreviation() != null) {
						abbrev = instrumentObj.getAbbreviation();
					}
				}
			}
			// no matching manufacturer create a new one and copy abbreviation
			if (instrument == null) {
				instrument = new Instrument();
				instrument.setType(instrumentBean.getType());
				instrument.setManufacturer(instrumentBean.getManufacturer());
				if (abbrev != null) {
					instrument.setAbbreviation(abbrev);
				}
				session.save(instrument);
			}
		}

		InstrumentConfiguration instrumentConfig = null;
		// new instrumentConfig
		if (instrumentConfigBean.getId() == null) {
			instrumentConfig = new InstrumentConfiguration();
			doChar.setInstrumentConfiguration(instrumentConfig);

		} else {
			instrumentConfig = doChar.getInstrumentConfiguration();
		}
		instrumentConfig.setDescription(instrumentConfigBean.getDescription());
		instrumentConfig.setInstrument(instrument);
		session.saveOrUpdate(instrumentConfig);
	}

	private void addProtocolFile(ProtocolFileBean protocolFileBean,
			Characterization doChar, Session session) {
		ProtocolFile protocolFile = null;
		if (protocolFileBean.getId() != null
				&& protocolFileBean.getId().length() > 0) {
			protocolFile = (ProtocolFile) session.get(ProtocolFile.class,
					new Long(protocolFileBean.getId()));

			// updated protocolFileBean in charBean
			protocolFileBean.setName(protocolFile.getFilename());
			protocolFileBean.setUri(protocolFile.getUri());
			protocolFileBean.setTitle(protocolFile.getTitle());
			protocolFileBean.setDescription(protocolFile.getDescription());
			protocolFileBean.setCreatedBy(protocolFile.getCreatedBy());
			protocolFileBean.setCreatedDate(protocolFile.getCreatedDate());
			protocolFileBean.setVersion(protocolFile.getVersion());
		}
		doChar.setProtocolFile(protocolFile);
	}

	/**
	 * Saves the size characterization to the database
	 * 
	 * @param size
	 * @throws ParticleCharacterizationException
	 */
	public void addParticleSize(CharacterizationBean size)
			throws ParticleCharacterizationException {
		Size doSize = new Size();
		addParticleCharacterization(doSize, size);
	}

	/**
	 * Saves the size characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param surface
	 * @throws ParticleCharacterizationException
	 */
	public void addParticleSurface(CharacterizationBean surface)
			throws ParticleCharacterizationException {
		Surface doSurface = new Surface();
		addParticleCharacterization(doSurface, surface);
		// addMeasureUnit(doSurface.getCharge().getUnitOfMeasurement(),
		// CaNanoLabConstants.UNIT_TYPE_CHARGE);
		// addMeasureUnit(doSurface.getSurfaceArea().getUnitOfMeasurement(),
		// CaNanoLabConstants.UNIT_TYPE_AREA);
		// addMeasureUnit(doSurface.getZetaPotential().getUnitOfMeasurement(),
		// CaNanoLabConstants.UNIT_TYPE_ZETA_POTENTIAL);

	}

	private void addDatumName(Session session, String name,
			String characterizationName)
			throws ParticleCharacterizationException {
		if (name != null && name.length() > 0) {
			List results = session.createQuery(
					"select count(distinct name) from DatumName"
							+ " where characterizationName='"
							+ characterizationName + "'" + " and name='" + name
							+ "'").list();
			DatumName datumName = new DatumName();
			datumName.setName(name);
			datumName.setCharacterizationName(characterizationName);
			datumName.setDatumParsed(false);
			int count = -1;
			for (Object obj : results) {
				count = ((Integer) (obj)).intValue();
			}
			if (count == 0) {
				session.save(datumName);
			}
		}
	}

	private void addDerivedDataCategory(Session session, String name,
			String characterizationName) {
		if (name != null && name.length() > 0) {
			List results = session.createQuery(
					"select count(distinct name) from DerivedBioAssayDataCategory"
							+ " where characterizationName='"
							+ characterizationName + "'" + " and name='" + name
							+ "'").list();
			DerivedBioAssayDataCategory category = new DerivedBioAssayDataCategory();
			category.setName(name);
			category.setCharacterizationName(characterizationName);
			int count = -1;
			for (Object obj : results) {
				count = ((Integer) (obj)).intValue();
			}
			if (count == 0) {
				session.save(category);
			}
		}
	}

	private void addMeasureUnit(Session session, String unit, String type) {
		if (unit == null || unit.length() == 0) {
			return;
		}
		List results = session.createQuery(
				"select count(distinct name) from "
						+ " MeasureUnit where name='" + unit + "' and type='"
						+ type + "'").list();
		int count = -1;
		for (Object obj : results) {
			count = ((Integer) (obj)).intValue();
		}
		MeasureUnit measureUnit = new MeasureUnit();
		if (count == 0) {
			measureUnit.setName(unit);
			measureUnit.setType(type);
			session.save(measureUnit);
		}
	}

	private void addLookupType(LookupType lookupType, String type)
			throws ParticleCharacterizationException {
		if (type != null && type.length() > 0) {
			// if ID is not set save to the database otherwise update
			String className = lookupType.getClass().getSimpleName();
			try {
				Session session = HibernateUtil.currentSession();
				HibernateUtil.beginTransaction();
				List results = session.createQuery(
						"select count(distinct name) from " + className
								+ " type where name='" + type + "'").list();
				lookupType.setName(type);
				int count = -1;
				for (Object obj : results) {
					count = ((Integer) (obj)).intValue();
				}
				if (count == 0) {
					session.save(lookupType);
				}
				HibernateUtil.commitTransaction();
			} catch (Exception e) {
				HibernateUtil.rollbackTransaction();
				logger.error("Problem saving look up type: " + type, e);
				throw new ParticleCharacterizationException();
			} finally {
				HibernateUtil.closeSession();
			}
		}
	}

	/**
	 * Saves the molecular weight characterization to the database
	 * 
	 * @param molecularWeight
	 * @throws ParticleCharacterizationException
	 */
	public void addParticleMolecularWeight(CharacterizationBean molecularWeight)
			throws ParticleCharacterizationException {
		MolecularWeight doMolecularWeight = new MolecularWeight();
		addParticleCharacterization(doMolecularWeight, molecularWeight);
	}

	/**
	 * Saves the morphology characterization to the database
	 * 
	 * @param morphology
	 * @throws ParticleCharacterizationException
	 */
	public void addParticleMorphology(MorphologyBean morphology)
			throws ParticleCharacterizationException {
		Morphology doMorphology = new Morphology();
		addParticleCharacterization(doMorphology, morphology);
		MorphologyType morphologyType = new MorphologyType();
		addLookupType(morphologyType, morphology.getType());
	}

	/**
	 * Saves the shape characterization to the database
	 * 
	 * @param shape
	 * @throws ParticleCharacterizationException
	 */
	public void addParticleShape(ShapeBean shape)
			throws ParticleCharacterizationException {
		Shape doShape = new Shape();
		addParticleCharacterization(doShape, shape);
		ShapeType shapeType = new ShapeType();
		addLookupType(shapeType, shape.getType());
	}

	/**
	 * Saves the purity characterization to the database
	 * 
	 * @param purity
	 * @throws ParticleCharacterizationException
	 */
	public void addParticlePurity(CharacterizationBean purity)
			throws ParticleCharacterizationException {
		Purity doPurity = new Purity();
		addParticleCharacterization(doPurity, purity);
	}

	/**
	 * Saves the solubility characterization to the database
	 * 
	 * @param solubility
	 * @throws ParticleCharacterizationException
	 */
	public void addParticleSolubility(SolubilityBean solubility)
			throws ParticleCharacterizationException {
		Solubility doSolubility = new Solubility();
		addParticleCharacterization(doSolubility, solubility);
		SolventType solventType = new SolventType();
		addLookupType(solventType, solubility.getSolvent());
		// addMeasureUnit(solubility.getCriticalConcentration()
		// .getUnitOfMeasurement(),
		// CaNanoLabConstants.UNIT_TYPE_CONCENTRATION);
	}

	/**
	 * Saves the invitro hemolysis characterization to the database
	 * 
	 * @param hemolysis
	 * @throws ParticleCharacterizationException
	 */
	public void addHemolysis(CharacterizationBean hemolysis)
			throws ParticleCharacterizationException {
		Hemolysis doHemolysis = new Hemolysis();
		addParticleCharacterization(doHemolysis, hemolysis);
	}

	/**
	 * Saves the invitro coagulation characterization to the database
	 * 
	 * @param coagulation
	 * @throws ParticleCharacterizationException
	 */
	public void addCoagulation(CharacterizationBean coagulation)
			throws ParticleCharacterizationException {
		Coagulation doCoagulation = new Coagulation();
		addParticleCharacterization(doCoagulation, coagulation);
	}

	/**
	 * Saves the invitro plate aggregation characterization to the database
	 * 
	 * @param plateletAggregation
	 * @throws ParticleCharacterizationException
	 */
	public void addPlateletAggregation(CharacterizationBean plateletAggregation)
			throws ParticleCharacterizationException {
		PlateletAggregation doPlateletAggregation = new PlateletAggregation();
		addParticleCharacterization(doPlateletAggregation, plateletAggregation);
	}

	/**
	 * Saves the invitro chemotaxis characterization to the database
	 * 
	 * @param chemotaxis
	 * @throws ParticleCharacterizationException
	 */
	public void addChemotaxis(CharacterizationBean chemotaxis)
			throws ParticleCharacterizationException {
		Chemotaxis doChemotaxis = new Chemotaxis();
		addParticleCharacterization(doChemotaxis, chemotaxis);
	}

	/**
	 * Saves the invitro NKCellCytotoxicActivity characterization to the
	 * database
	 * 
	 * @param nkCellCytotoxicActivity
	 * @throws ParticleCharacterizationException
	 */
	public void addNKCellCytotoxicActivity(
			CharacterizationBean nkCellCytotoxicActivity)
			throws ParticleCharacterizationException {
		NKCellCytotoxicActivity doNKCellCytotoxicActivity = new NKCellCytotoxicActivity();
		addParticleCharacterization(doNKCellCytotoxicActivity,
				nkCellCytotoxicActivity);
	}

	/**
	 * Saves the invitro LeukocyteProliferation characterization to the database
	 * 
	 * @param leukocyteProliferation
	 * @throws ParticleCharacterizationException
	 */
	public void addLeukocyteProliferation(
			CharacterizationBean leukocyteProliferation)
			throws ParticleCharacterizationException {
		LeukocyteProliferation doLeukocyteProliferation = new LeukocyteProliferation();
		addParticleCharacterization(doLeukocyteProliferation,
				leukocyteProliferation);
	}

	/**
	 * Saves the invitro CFU_GM characterization to the database
	 * 
	 * @param cfu_gm
	 * @throws ParticleCharacterizationException
	 */
	public void addCFU_GM(CharacterizationBean cfu_gm)
			throws ParticleCharacterizationException {
		CFU_GM doCFU_GM = new CFU_GM();
		addParticleCharacterization(doCFU_GM, cfu_gm);
	}

	/**
	 * Saves the invitro Complement Activation characterization to the database
	 * 
	 * @param complementActivation
	 * @throws ParticleCharacterizationException
	 */
	public void addComplementActivation(
			CharacterizationBean complementActivation)
			throws ParticleCharacterizationException {
		ComplementActivation doComplementActivation = new ComplementActivation();
		addParticleCharacterization(doComplementActivation,
				complementActivation);
	}

	/**
	 * Saves the invitro OxidativeBurst characterization to the database
	 * 
	 * @param oxidativeBurst
	 * @throws ParticleCharacterizationException
	 */
	public void addOxidativeBurst(CharacterizationBean oxidativeBurst)
			throws ParticleCharacterizationException {
		OxidativeBurst doOxidativeBurst = new OxidativeBurst();
		addParticleCharacterization(doOxidativeBurst, oxidativeBurst);
	}

	/**
	 * Saves the invitro Phagocytosis characterization to the database
	 * 
	 * @param phagocytosis
	 * @throws ParticleCharacterizationException
	 */
	public void addPhagocytosis(CharacterizationBean phagocytosis)
			throws ParticleCharacterizationException {
		Phagocytosis doPhagocytosis = new Phagocytosis();
		addParticleCharacterization(doPhagocytosis, phagocytosis);
	}

	/**
	 * Saves the invitro CytokineInduction characterization to the database
	 * 
	 * @param cytokineInduction
	 * @throws ParticleCharacterizationException
	 */
	public void addCytokineInduction(CharacterizationBean cytokineInduction)
			throws ParticleCharacterizationException {
		CytokineInduction doCytokineInduction = new CytokineInduction();
		addParticleCharacterization(doCytokineInduction, cytokineInduction);
	}

	/**
	 * Saves the invitro plasma protein binding characterization to the database
	 * 
	 * @param plasmaProteinBinding
	 * @throws ParticleCharacterizationException
	 */
	public void addProteinBinding(CharacterizationBean plasmaProteinBinding)
			throws ParticleCharacterizationException {
		PlasmaProteinBinding doProteinBinding = new PlasmaProteinBinding();
		addParticleCharacterization(doProteinBinding, plasmaProteinBinding);
	}

	/**
	 * Saves the invitro binding characterization to the database
	 * 
	 * @param cellViability
	 * @throws ParticleCharacterizationException
	 */
	public void addCellViability(CytotoxicityBean cellViability)
			throws ParticleCharacterizationException {
		CellViability doCellViability = new CellViability();
		addParticleCharacterization(doCellViability, cellViability);
		CellLineType cellLineType = new CellLineType();
		addLookupType(cellLineType, cellViability.getCellLine());
	}

	/**
	 * Saves the invitro EnzymeInduction binding characterization to the
	 * database
	 * 
	 * @param enzymeInduction
	 * @throws ParticleCharacterizationException
	 */
	public void addEnzymeInduction(CharacterizationBean enzymeInduction)
			throws ParticleCharacterizationException {
		EnzymeInduction doEnzymeInduction = new EnzymeInduction();
		addParticleCharacterization(doEnzymeInduction, enzymeInduction);
	}

	/**
	 * Saves the invitro OxidativeStress characterization to the database
	 * 
	 * @param oxidativeStress
	 * @throws ParticleCharacterizationException
	 */
	public void addOxidativeStress(CharacterizationBean oxidativeStress)
			throws ParticleCharacterizationException {
		OxidativeStress doOxidativeStress = new OxidativeStress();
		addParticleCharacterization(doOxidativeStress, oxidativeStress);
	}

	/**
	 * Saves the invitro Caspase3Activation characterization to the database
	 * 
	 * @param caspase3Activation
	 * @throws ParticleCharacterizationException
	 */
	public void addCaspase3Activation(CytotoxicityBean caspase3Activation)
			throws ParticleCharacterizationException {
		Caspase3Activation doCaspase3Activation = new Caspase3Activation();
		addParticleCharacterization(doCaspase3Activation, caspase3Activation);
		CellLineType cellLineType = new CellLineType();
		addLookupType(cellLineType, caspase3Activation.getCellLine());
	}

	/**
	 * Delete the characterizations
	 */
	public void deleteCharacterizations(String[] charIds)
			throws ParticleCharacterizationException {
		// if ID is not set save to the database otherwise update
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();

			// Get ID
			for (String strCharId : charIds) {

				Long charId = Long.parseLong(strCharId);
				Object charObj = session.load(Characterization.class, charId);
				// deassociate first
				String hqlString = "from Nanoparticle particle where particle.characterizationCollection.id = '"
						+ strCharId + "'";
				List results = session.createQuery(hqlString).list();
				for (Object obj : results) {
					Nanoparticle particle = (Nanoparticle) obj;
					particle.getCharacterizationCollection().remove(charObj);
				}
				// then delete
				// session.delete(charObj);
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			logger.error("Problem deleting characterization: ", e);
			throw new ParticleCharacterizationException(
					"The characterization is no longer exist in the database, please login again to refresh the view");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	public SortedSet<String> getAllCharacterizationFileTypes()
			throws ParticleCharacterizationException {
		SortedSet<String> fileTypes = new TreeSet<String>();
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select distinct fileType.name from CharacterizationFileType fileType order by fileType.name";
			List results = session.createQuery(hqlString).list();
			HibernateUtil.commitTransaction();
			for (Object obj : results) {
				String type = (String) obj;
				fileTypes.add(type);
			}

		} catch (Exception e) {
			logger.error(
					"Problem to retrieve all characterization file types. ", e);
			throw new ParticleCharacterizationException(
					"Problem to retrieve all characterization file types ", e);
		} finally {
			HibernateUtil.closeSession();
		}
		return fileTypes;
	}

	public List<CharacterizationTypeBean> getAllCharacterizationTypes()
			throws ParticleCharacterizationException {
		List<CharacterizationTypeBean> charTypes = new ArrayList<CharacterizationTypeBean>();

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String query = "select distinct category, has_action, indent_level, category_order from def_characterization_category order by category_order";
			SQLQuery queryObj = session.createSQLQuery(query);
			queryObj.addScalar("CATEGORY", Hibernate.STRING);
			queryObj.addScalar("HAS_ACTION", Hibernate.INTEGER);
			queryObj.addScalar("INDENT_LEVEL", Hibernate.INTEGER);
			queryObj.addScalar("CATEGORY_ORDER", Hibernate.INTEGER);
			List results = queryObj.list();
			HibernateUtil.commitTransaction();
			for (Object obj : results) {
				Object[] objarr = (Object[]) obj;
				String type = objarr[0].toString();
				boolean hasAction = ((Integer) objarr[1] == 0) ? false : true;
				int indentLevel = (Integer) objarr[2];
				CharacterizationTypeBean charType = new CharacterizationTypeBean(
						type, indentLevel, hasAction);
				charTypes.add(charType);
			}

		} catch (Exception e) {
			logger.error("Problem to retrieve all characterization types. ", e);
			throw new ParticleCharacterizationException(
					"Problem to retrieve all characteriztaion types ", e);
		} finally {
			HibernateUtil.closeSession();
		}
		return charTypes;
	}

	public Map<String, SortedSet<String>> getDerivedDataCategoryMap(
			String characterizationName)
			throws ParticleCharacterizationException {
		Map<String, SortedSet<String>> categoryMap = new HashMap<String, SortedSet<String>>();

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select category.name, datumName.name from DerivedBioAssayDataCategory category left join category.datumNameCollection datumName where datumName.datumParsed=false and category.characterizationName='"
					+ characterizationName + "'";
			List results = session.createQuery(hqlString).list();
			HibernateUtil.commitTransaction();
			SortedSet<String> datumNames = null;
			for (Object obj : results) {
				String categoryName = ((Object[]) obj)[0].toString();
				String datumName = ((Object[]) obj)[1].toString();
				if (categoryMap.get(categoryName) != null) {
					datumNames = categoryMap.get(categoryName);
				} else {
					datumNames = new TreeSet<String>();
					categoryMap.put(categoryName, datumNames);
				}
				datumNames.add(datumName);
			}

		} catch (Exception e) {
			logger
					.error(
							"Problem to retrieve all derived bioassay data categories. ",
							e);
			throw new ParticleCharacterizationException(
					"Problem to retrieve all derived bioassay data categories",
					e);

		} finally {
			HibernateUtil.closeSession();
		}
		return categoryMap;
	}

	public SortedSet<String> getDerivedDatumNames(String characterizationName)
			throws ParticleCharacterizationException {
		SortedSet<String> datumNames = new TreeSet<String>();
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select distinct name from DatumName where datumParsed=false and characterizationName='"
					+ characterizationName + "'";
			List results = session.createQuery(hqlString).list();
			HibernateUtil.commitTransaction();
			for (Object obj : results) {
				String datumName = obj.toString();
				datumNames.add(datumName);
			}

		} catch (Exception e) {
			logger
					.error(
							"Problem to retrieve all derived bioassay datum names. ",
							e);
			throw new ParticleCharacterizationException(
					"Problem in retrieving all derived bioassay datum names", e);

		} finally {
			HibernateUtil.closeSession();
		}
		return datumNames;
	}

	public SortedSet<String> getDerivedDataCategories(
			String characterizationName)
			throws ParticleCharacterizationException {
		SortedSet<String> categories = new TreeSet<String>();

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select distinct name from DerivedBioAssayDataCategory where characterizationName='"
					+ characterizationName + "'";
			List results = session.createQuery(hqlString).list();
			HibernateUtil.commitTransaction();
			for (Object obj : results) {
				String category = obj.toString();
				categories.add(category);
			}

		} catch (Exception e) {
			logger
					.error(
							"Problem to retrieve all derived bioassay data categories.",
							e);
			throw new ParticleCharacterizationException(
					"Problem to retrieve all derived bioassay data categories");

		} finally {
			HibernateUtil.closeSession();
		}
		return categories;
	}

	public SortedSet<String> getAllCharacterizationSources()
			throws ParticleCharacterizationException {
		SortedSet<String> sources = new TreeSet<String>();

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select distinct char.source from Characterization char where char.source is not null";
			List results = session.createQuery(hqlString).list();
			HibernateUtil.commitTransaction();
			for (Object obj : results) {
				sources.add((String) obj);
			}

		} catch (Exception e) {
			logger
					.error("Problem to retrieve all Characterization Sources.",
							e);
			throw new ParticleCharacterizationException(
					"Problem to retrieve all Characterization Sources ");
		} finally {
			HibernateUtil.closeSession();
		}
		sources.addAll(Arrays
				.asList(CaNanoLabConstants.DEFAULT_CHARACTERIZATION_SOURCES));

		return sources;
	}

	public SortedSet<String> getAllManufacturers()
			throws ParticleCharacterizationException {
		SortedSet<String> manufacturers = new TreeSet<String>();
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select distinct instrument.manufacturer from Instrument instrument";
			List results = session.createQuery(hqlString).list();
			for (Object obj : results) {
				String manufacturer = (String) obj;
				if (manufacturer != null)
					manufacturers.add(manufacturer);
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem to retrieve all manufacturers. ", e);
			throw new ParticleCharacterizationException(
					"Problem to retrieve all manufacturers");
		} finally {
			HibernateUtil.closeSession();
		}
		return manufacturers;
	}

	public Map<String, SortedSet<String>> getAllInstrumentManufacturers()
			throws ParticleCharacterizationException {
		Map<String, SortedSet<String>> instrumentManufacturers = new HashMap<String, SortedSet<String>>();

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();

			String hqlString = "select distinct instrumentType.name, manufacturer.name from InstrumentType instrumentType join instrumentType.manufacturerCollection manufacturer ";
			List results = session.createQuery(hqlString).list();
			SortedSet<String> manufacturers = null;
			for (Object obj : results) {
				String instrumentType = ((Object[]) obj)[0].toString();
				String manufacturer = ((Object[]) obj)[1].toString();
				if (instrumentManufacturers.get(instrumentType) != null) {
					manufacturers = (SortedSet<String>) instrumentManufacturers
							.get(instrumentType);
				} else {
					manufacturers = new TreeSet<String>();
					instrumentManufacturers.put(instrumentType, manufacturers);
				}
				manufacturers.add(manufacturer);
			}
			List allResult = session
					.createQuery(
							"select manufacturer.name from Manufacturer manufacturer where manufacturer.name is not null")
					.list();
			HibernateUtil.commitTransaction();
			SortedSet<String> allManufacturers = null;
			allManufacturers = new TreeSet<String>();
			for (Object obj : allResult) {
				String name = (String) obj;
				allManufacturers.add(name);
			}

		} catch (Exception e) {
			logger
					.error(
							"Problem to retrieve manufacturers for intrument types ",
							e);
			throw new ParticleCharacterizationException(
					"Problem to retrieve manufacturers for intrument types");
		} finally {
			HibernateUtil.closeSession();
		}
		return instrumentManufacturers;
	}

	public List<InstrumentBean> getAllInstruments()
			throws ParticleCharacterizationException {
		List<InstrumentBean> instruments = new ArrayList<InstrumentBean>();

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "from Instrument instrument where instrument.type is not null order by instrument.type";
			List results = session.createQuery(hqlString).list();
			HibernateUtil.commitTransaction();
			for (Object obj : results) {
				Instrument instrument = (Instrument) obj;
				instruments.add(new InstrumentBean(instrument));
			}

		} catch (Exception e) {
			logger.error("Problem to retrieve all instruments. ", e);
			throw new ParticleCharacterizationException(
					"Problem to retrieve all intruments ");
		} finally {
			HibernateUtil.closeSession();
		}
		return instruments;
	}

	public String getInstrumentAbbreviation(String instrumentType)
			throws ParticleCharacterizationException {
		String instrumentAbbreviation = null;
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select distinct instrument.abbreviation from Instrument instrument where instrument.type='"
					+ instrumentType + "'";
			List results = session.createQuery(hqlString).list();
			HibernateUtil.commitTransaction();
			for (Object obj : results) {
				instrumentAbbreviation = (String) obj;
			}

		} catch (Exception e) {
			logger.error("Problem to retrieve instrument abbreviation. ", e);
			throw new ParticleCharacterizationException(
					"Problem to retrieve instrument abbreviation ");
		} finally {
			HibernateUtil.closeSession();
		}
		return instrumentAbbreviation;
	}

	/**
	 * 
	 * @return a map between a characterization type and its child
	 *         characterizations.
	 */
	public Map<String, List<CharacterizationBean>> getCharacterizationTypeCharacterizations()
			throws ParticleCharacterizationException {
		Map<String, List<CharacterizationBean>> charTypeChars = new HashMap<String, List<CharacterizationBean>>();

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			List<CharacterizationBean> chars = null;
			String query = "select distinct a.category, a.name, a.name_abbreviation from def_characterization_category a "
					// + "where a.name not in (select distinct b.category from
					// def_characterization_category b) "
					+ "order by a.category, a.name, a.name_abbreviation";
			SQLQuery queryObj = session.createSQLQuery(query);
			queryObj.addScalar("CATEGORY", Hibernate.STRING);
			queryObj.addScalar("NAME", Hibernate.STRING);
			queryObj.addScalar("NAME_ABBREVIATION", Hibernate.STRING);
			List results = queryObj.list();
			HibernateUtil.commitTransaction();
			for (Object obj : results) {
				Object[] objarr = (Object[]) obj;
				String type = objarr[0].toString();
				String name = objarr[1].toString();
				String abbr;
				if (objarr[2] == null) {
					abbr = "";
				} else {
					abbr = objarr[2].toString();
				}
				if (charTypeChars.get(type) != null) {
					chars = (List<CharacterizationBean>) charTypeChars
							.get(type);
				} else {
					chars = new ArrayList<CharacterizationBean>();
					charTypeChars.put(type, chars);
				}
				CharacterizationBean charBean = new CharacterizationBean(name,
						abbr);
				chars.add(charBean);
			}

		} catch (Exception e) {
			logger
					.error(
							"Problem to retrieve all characterization type characterizations. ",
							e);
			throw new ParticleCharacterizationException(
					"Problem to retrieve all characteriztaion type characterizations ");
		} finally {
			HibernateUtil.closeSession();
		}

		return charTypeChars;

	}

	public Map<String, String> getCharacterizationCategoryMap()
			throws ParticleCharacterizationException {
		Map<String, String> charNameToCharCategory = new HashMap<String, String>();
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String query = "select distinct a.category, a.name from def_characterization_category a ";
			SQLQuery queryObj = session.createSQLQuery(query);
			queryObj.addScalar("CATEGORY", Hibernate.STRING);
			queryObj.addScalar("NAME", Hibernate.STRING);
			List results = queryObj.list();
			HibernateUtil.commitTransaction();
			for (Object obj : results) {
				Object[] objarr = (Object[]) obj;
				String category = objarr[0].toString();
				String name = objarr[1].toString();
				charNameToCharCategory.put(name, category);
			}
		} catch (Exception e) {
			logger.error("Error retrieving characterization catgories", e);
			throw new ParticleCharacterizationException();
		} finally {
			HibernateUtil.closeSession();
		}
		return charNameToCharCategory;
	}

	/**
	 * 
	 * @return a map between a characterization type and its child
	 *         characterizations.
	 */
	public Map<String, Map<String, List<CharacterizationBean>>> getCharacterizationTypeTree()
			throws ParticleCharacterizationException {
		Map charTypeChars = new HashMap<String, Map>();

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			List<CharacterizationBean> chars = null;
			String query = "select distinct a.category, a.name, a.name_abbreviation from def_characterization_category a "
					+ "where a.name not in (select distinct b.category from def_characterization_category b) "
					+ "order by a.category, a.name, a.name_abbreviation";
			SQLQuery queryObj = session.createSQLQuery(query);
			queryObj.addScalar("CATEGORY", Hibernate.STRING);
			queryObj.addScalar("NAME", Hibernate.STRING);
			queryObj.addScalar("NAME_ABBREVIATION", Hibernate.STRING);
			List results = queryObj.list();
			HibernateUtil.commitTransaction();
			for (Object obj : results) {
				Object[] objarr = (Object[]) obj;
				String type = objarr[0].toString();
				String name = objarr[1].toString();
				String abbr = objarr[2].toString();
				if (charTypeChars.get(type) != null) {
					chars = (List<CharacterizationBean>) charTypeChars
							.get(type);
				} else {
					chars = new ArrayList<CharacterizationBean>();
					charTypeChars.put(type, chars);
				}
				CharacterizationBean charBean = new CharacterizationBean(name,
						abbr);
				chars.add(charBean);
			}

		} catch (Exception e) {
			logger
					.error(
							"Problem to retrieve all characterization type characterizations. ",
							e);
			throw new ParticleCharacterizationException(
					"Problem to retrieve all characteriztaion type characterizations ");
		} finally {
			HibernateUtil.closeSession();
		}
		return charTypeChars;
	}

	String getExportDownloadFilePath(String fileId, UserBean user) {
		String path = null;
		try {
			FileService service = new FileService();
			LabFileBean fileBean = service.getFile(fileId, user);
			String fileRoot = PropertyReader
					.getProperty(CaNanoLabConstants.FILEUPLOAD_PROPERTY,
							"fileRepositoryDir");
			path = fileRoot + File.separator + fileBean.getUri();

		} catch (Exception e) {
			logger
					.error(
							"Problem to get image file when exporting detail view data: ",
							e);
		}

		return path;
	}

	private int loadPicture(String path, HSSFWorkbook wb) throws IOException {
		int pictureIndex;
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		try {
			fis = new FileInputStream(path);
			bos = new ByteArrayOutputStream();
			int c;
			while ((c = fis.read()) != -1)
				bos.write(c);
			pictureIndex = wb.addPicture(bos.toByteArray(),
					HSSFWorkbook.PICTURE_TYPE_PNG);
		} finally {
			if (fis != null)
				fis.close();
			if (bos != null)
				bos.close();
		}
		return pictureIndex;
	}

	public void exportDetailService(ParticleBean particle,
			CharacterizationBean achar, HSSFWorkbook wb, UserBean user) {
		HSSFSheet sheet = wb.createSheet("detailSheet");
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		short startRow = 0;
		setDetailSheet(particle, achar, user, wb, sheet, patriarch, startRow);
	}

	private short setDetailSheet(ParticleBean particle,
			CharacterizationBean achar, UserBean user, HSSFWorkbook wb,
			HSSFSheet sheet, HSSFPatriarch patriarch, short rowCount) {
		HSSFFont headerFont = wb.createFont();
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle headerStyle = wb.createCellStyle();
		headerStyle.setFont(headerFont);

		HSSFRow row = null;
		HSSFCell cell = null;

		// description row
		String description = achar.getDescription();
		if (description != null) {
			row = sheet.createRow(rowCount++);
			short cellCount = 0;
			cell = row.createCell(cellCount++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue(new HSSFRichTextString("Description"));

			row.createCell(cellCount++).setCellValue(
					new HSSFRichTextString(description));
		}

		// protocol row
		ProtocolFileBean protocolFileBean = achar.getProtocolFileBean();
		String protocolId = protocolFileBean.getId();
		if (protocolId != null) {
			row = sheet.createRow(rowCount++);
			short cellCount = 0;
			cell = row.createCell(cellCount++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue(new HSSFRichTextString("Protocol"));

			if (protocolFileBean.isHidden()) {
				row.createCell(cellCount++).setCellValue(
						new HSSFRichTextString("Private protocol"));
			} else {
				StringBuffer buf = new StringBuffer();
				buf.append(protocolFileBean.getDisplayName());
				if (protocolFileBean.getUri() != null) {
					buf.append(" ");
					buf.append(protocolFileBean.getUri());
				}
				row.createCell(cellCount++).setCellValue(
						new HSSFRichTextString(buf.toString()));
			}
		}

		// instrument
		InstrumentConfigBean instrumentConfigBean = achar
				.getInstrumentConfigBean();
		InstrumentBean instrumentBean = instrumentConfigBean
				.getInstrumentBean();
		if (instrumentConfigBean != null && instrumentBean.getType() != null) {
			short cellCount = 0;
			row = sheet.createRow(rowCount++);
			cell = row.createCell(cellCount++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue(new HSSFRichTextString("Instrument"));

			StringBuffer ibuf = new StringBuffer();
			ibuf.append(instrumentBean.getType());
			ibuf.append("-");
			ibuf.append(instrumentBean.getManufacturer());
			if (instrumentBean.getAbbreviation() != null
					&& instrumentBean.getAbbreviation().length() > 0) {
				ibuf.append(" (" + instrumentBean.getAbbreviation() + ")");
			}
			row.createCell(cellCount++).setCellValue(
					new HSSFRichTextString(ibuf.toString()));

			if (instrumentConfigBean.getDescription() != null) {
				row.createCell(cellCount).setCellValue(
						new HSSFRichTextString(instrumentConfigBean
								.getDescription()));
			}
		}

		List<DerivedBioAssayDataBean> derivedBioAssayDataList = achar
				.getDerivedBioAssayDataList();
		int fileIndex = 1;

		for (DerivedBioAssayDataBean derivedBioAssayData : derivedBioAssayDataList) {
			String dDescription = derivedBioAssayData.getDescription();
			short cellCount = 0;
			if (dDescription != null) {
				row = sheet.createRow(rowCount++);
				cell = row.createCell(cellCount++);
				cell.setCellStyle(headerStyle);
				cell
						.setCellValue(new HSSFRichTextString(
								"Characterization File #" + fileIndex
										+ " Description"));
				row.createCell(cellCount++).setCellValue(
						new HSSFRichTextString(dDescription));

			}

			if (derivedBioAssayData != null
					&& derivedBioAssayData.getUri() != null) {
				row = sheet.createRow(rowCount++);
				cellCount = 0;
				cell = row.createCell(cellCount++);
				cell.setCellStyle(headerStyle);
				cell.setCellValue(new HSSFRichTextString(
						"Characterization File #" + fileIndex));
				/*
				 * if(derivedBioAssayData.getType() != null) {
				 * row.createCell(cellCount++).setCellValue(new
				 * HSSFRichTextString(derivedBioAssayData.getType())); }
				 */
				if (derivedBioAssayData.isHidden()) {
					row.createCell(cellCount++).setCellValue(
							new HSSFRichTextString("Private file"));
				} else {
					if (derivedBioAssayData.isImage()) {
						row.createCell(cellCount).setCellValue(
								new HSSFRichTextString(derivedBioAssayData
										.getTitle()));
						try {
							String filePath = getExportDownloadFilePath(
									derivedBioAssayData.getId(), user);
							HSSFClientAnchor anchor;
							short topLeftCell = cellCount;
							short bottomRightCell = (short) (cellCount + 7);
							int topLeftRow = rowCount + 1;
							int bottomRightRow = rowCount + 22;
							anchor = new HSSFClientAnchor(0, 0, 0, 255,
									topLeftCell, topLeftRow, bottomRightCell,
									bottomRightRow);
							anchor.setAnchorType(2);
							patriarch.createPicture(anchor, loadPicture(
									filePath, wb));
							cellCount = bottomRightCell;
							rowCount = (short) (bottomRightRow + 3);
						} catch (IOException ioe) {
							logger
									.error(
											"Input/output problem to export detail view image data ",
											ioe);
						}

					} else { // question? download the whole file to a cell
						// if not image?
						row.createCell(cellCount++).setCellValue(
								new HSSFRichTextString(derivedBioAssayData
										.getTitle()));
					}

				}
			}

			List<DatumBean> datumList = derivedBioAssayData.getDatumList();
			if (datumList != null && !datumList.isEmpty()) {
				cellCount = 0;
				row = sheet.createRow(rowCount);
				rowCount++;
				cell = row.createCell(cellCount++);
				cell.setCellStyle(headerStyle);
				cell.setCellValue(new HSSFRichTextString(
						"Characterization Derived Data #" + fileIndex));

				row = sheet.createRow(rowCount++);
				row = sheet.createRow(rowCount++);

				short ccount = 0;
				for (DatumBean datum : datumList) {
					if (datum.getUnit() != null
							&& datum.getUnit().trim().length() > 0) {
						cell = row.createCell(ccount++);
						cell.setCellStyle(headerStyle);
						cell.setCellValue(new HSSFRichTextString(datum
								.getName()
								+ " (" + datum.getUnit() + ")"));
					} else {
						cell = row.createCell(ccount++);
						cell.setCellStyle(headerStyle);
						cell.setCellValue(new HSSFRichTextString(datum
								.getName()));
					}
				}

				row = sheet.createRow(rowCount++);
				ccount = 0;
				for (DatumBean datum : datumList) {
					row.createCell(ccount++).setCellValue(
							new HSSFRichTextString(datum.getValue()));
				}
				rowCount++;
			}
			fileIndex++;
		}
		return rowCount;
	}

	public void exportFullSummaryService(List<CharacterizationBean> charBeans,
			SortedSet<String> datumLabels, UserBean user,
			List<CharacterizationSummaryBean> summaryBeans,
			ParticleBean particle, OutputStream out) throws IOException {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("summarySheet");
		short startRow = 0;
		short rowCount = setSummarySheet(datumLabels, summaryBeans, particle,
				wb, sheet, startRow);
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		rowCount += 2;
		for (CharacterizationBean cbean : charBeans) {
			rowCount = setDetailSheet(particle, cbean, user, wb, sheet,
					patriarch, rowCount);
			rowCount += 2;
		}
		wb.write(out);
		if (out != null) {
			out.flush();
			out.close();
		}
	}

	public void exportSummaryService(SortedSet<String> datumLabels,
			List<CharacterizationSummaryBean> summaryBeans,
			ParticleBean particle, OutputStream out) throws IOException {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("summarySheet");
		short startRow = 0;
		setSummarySheet(datumLabels, summaryBeans, particle, wb, sheet,
				startRow);
		wb.write(out);
		if (out != null) {
			out.flush();
			out.close();
		}
	}

	private short setSummarySheet(SortedSet<String> datumLabels,
			List<CharacterizationSummaryBean> summaryBeans,
			ParticleBean particle, HSSFWorkbook wb, HSSFSheet sheet,
			short rowCount) {
		HSSFFont headerFont = wb.createFont();
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle headerStyle = wb.createCellStyle();
		headerStyle.setFont(headerFont);

		HSSFCellStyle newLineStyle = wb.createCellStyle();
		// Word Wrap MUST be turned on
		newLineStyle.setWrapText(true);

		short cellCount = 0;
		HSSFRow row = null;
		HSSFCell cell = null;

		// summary header
		row = sheet.createRow(rowCount);
		rowCount++;

		cell = row.createCell(cellCount++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("View Title"));

		cell = row.createCell(cellCount++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Description"));

		for (String label : datumLabels) {
			cell = row.createCell(cellCount++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue(new HSSFRichTextString(label));
		}
		cell = row.createCell(cellCount++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Characterization File"));

		cell = row.createCell(cellCount++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Instrument Info"));

		// data
		for (CharacterizationSummaryBean sbean : summaryBeans) {
			row = sheet.createRow(rowCount);
			rowCount++;
			cellCount = 0;

			// view title
			CharacterizationBean achar = (CharacterizationBean) sbean
					.getCharBean();
			row.createCell(cellCount++).setCellValue(
					new HSSFRichTextString(achar.getViewTitle()));

			// description
			String description = achar.getDescription();
			if (description == null)
				description = "";
			row.createCell(cellCount++).setCellValue(
					new HSSFRichTextString(description));

			Map datumMap = sbean.getDatumMap();
			for (String label : datumLabels) {
				row.createCell(cellCount++).setCellValue(
						new HSSFRichTextString((String) datumMap.get(label)));
			}

			DerivedBioAssayDataBean charFile = sbean.getCharFile();
			if (charFile != null) {
				StringBuffer sbuf = new StringBuffer();
				if (charFile.getType() != null
						&& charFile.getType().length() > 0) {
					sbuf.append(charFile.getType() + " ");
				}
				if (charFile.getUri() != null) {
					if (charFile.isHidden()) {
						sbuf.append("Private file");
					} else if (charFile.getTitle() != null) {
						sbuf.append(charFile.getTitle());
					}
				}
				row.createCell(cellCount++).setCellValue(
						new HSSFRichTextString(sbuf.toString()));
			} else {
				row.createCell(cellCount++); // empty cell
			}

			// instrument
			InstrumentConfigBean instrumentConfigBean = achar
					.getInstrumentConfigBean();
			InstrumentBean instrumentBean = instrumentConfigBean
					.getInstrumentBean();
			if (instrumentConfigBean != null
					&& instrumentBean.getType() != null) {
				StringBuffer sb = new StringBuffer();
				sb.append(instrumentBean.getType());
				sb.append("-");
				sb.append(instrumentBean.getManufacturer());

				if (instrumentBean.getAbbreviation() != null
						&& instrumentBean.getAbbreviation().length() > 0)
					sb.append(" (" + instrumentBean.getAbbreviation() + ")");

				if (instrumentConfigBean.getDescription() != null)
					sb.append("  " + instrumentConfigBean.getDescription());

				cell = row.createCell(cellCount++);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(new HSSFRichTextString(sb.toString()));
				// cell.setCellStyle(newLineStyle);
			}

		} // for sbean

		return rowCount;
	}

	/*
	 * set data labels and file visibility, and whether file is an image
	 */
	public SortedSet<String> setDataLabelsAndFileVisibility(UserBean user,
			List<CharacterizationSummaryBean> charSummaryBeans,
			List<CharacterizationBean> charBeans)
			throws CaNanoLabSecurityException {
		// List<CharacterizationBean> charBeans = new
		// ArrayList<CharacterizationBean>();
		SortedSet<String> datumLabels = new TreeSet<String>();
		for (CharacterizationSummaryBean summaryBean : charSummaryBeans) {
			Map<String, String> datumMap = summaryBean.getDatumMap();
			if (datumMap != null && !datumMap.isEmpty()) {
				datumLabels.addAll(datumMap.keySet());
			}
			DerivedBioAssayDataBean fileBean = summaryBean.getCharFile();
			if (fileBean != null) {
				boolean accessStatus = userService.checkReadPermission(user,
						fileBean.getId());
				if (accessStatus) {
					List<String> accessibleGroups = userService
							.getAccessibleGroups(fileBean.getId(),
									CaNanoLabConstants.CSM_READ_ROLE);
					String[] visibilityGroups = accessibleGroups
							.toArray(new String[0]);
					fileBean.setVisibilityGroups(visibilityGroups);
					fileBean.setHidden(false);
				} else {
					fileBean.setHidden(true);
				}
				boolean imageStatus = false;
				if (fileBean.getName() != null) {
					imageStatus = StringUtils.isImgFileExt(fileBean.getName());
				}
				fileBean.setImage(imageStatus);
			}
			if (!charBeans.contains(summaryBean.getCharBean())) {
				charBeans.add(summaryBean.getCharBean());
			}
		}
		return datumLabels;
	}

	public String getExportFileName(ParticleBean particle,
			CharacterizationBean achar, String function) {
		StringBuffer sbuf = new StringBuffer(particle.getSampleName());
		sbuf.append("_");
		sbuf
				.append(StringUtils.getOneWordUpperCaseFirstLetter(achar
						.getName()));
		sbuf.append("_");
		sbuf.append(function);
		sbuf.append("_");
		sbuf.append(StringUtils.convertDateToString(new Date(),
				"yyyyMMdd_HH-mm-ss-SSS"));
		return sbuf.toString();
	}

	public void setCharacterizationUserVisiblity(CharacterizationBean charBean,
			UserBean user) throws CaNanoLabSecurityException {
		// set up characterization files visibility, and whether file is an
		// image
		for (DerivedBioAssayDataBean fileBean : charBean
				.getDerivedBioAssayDataList()) {
			boolean accessStatus = userService.checkReadPermission(user,
					fileBean.getId());
			if (accessStatus) {
				List<String> accessibleGroups = userService
						.getAccessibleGroups(fileBean.getId(),
								CaNanoLabConstants.CSM_READ_ROLE);
				String[] visibilityGroups = accessibleGroups
						.toArray(new String[0]);
				fileBean.setVisibilityGroups(visibilityGroups);
				fileBean.setHidden(false);
			} else {
				fileBean.setHidden(true);
			}
			boolean imageStatus = false;
			if (fileBean.getName() != null) {
				imageStatus = StringUtils.isImgFileExt(fileBean.getName());
				fileBean.setImage(imageStatus);
			}
		}
		// set up protocol file visibility
		ProtocolFileBean protocolFileBean = charBean.getProtocolFileBean();
		if (protocolFileBean != null) {
			boolean status = false;
			if (protocolFileBean.getId() != null) {
				status = userService.checkReadPermission(user, protocolFileBean
						.getId());
			}
			if (status) {
				protocolFileBean.setHidden(false);
			} else {
				protocolFileBean.setHidden(true);
			}
		}
	}
}
