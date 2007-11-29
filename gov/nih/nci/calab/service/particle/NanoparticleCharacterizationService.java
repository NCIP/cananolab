package gov.nih.nci.calab.service.particle;

import gov.nih.nci.calab.db.HibernateUtil;
import gov.nih.nci.calab.domain.Instrument;
import gov.nih.nci.calab.domain.InstrumentConfiguration;
import gov.nih.nci.calab.domain.LabFile;
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
import gov.nih.nci.calab.service.common.FileService;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;
import gov.nih.nci.calab.service.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
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

	public NanoparticleCharacterizationService() throws Exception {
		this.userService = new UserService(CaNanoLabConstants.CSM_APP_NAME);
	}

	public CharacterizationBean getCharacterizationBy(String charId)
			throws Exception {
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
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return charBean;
	}

	public List<CharacterizationBean> getCharacterizationInfo(String particleId)
			throws Exception {
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
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return charBeans;
	}

	public List<CharacterizationSummaryBean> getParticleCharacterizationSummaryByName(
			String charName, String particleId) throws Exception {
		List<CharacterizationSummaryBean> charSummaryBeans = new ArrayList<CharacterizationSummaryBean>();
		List<CharacterizationBean> charBeans = getParticleCharacterizationsByName(
				charName, particleId);
		if (charBeans.isEmpty()) {
			return null;
		}
		for (CharacterizationBean charBean : charBeans) {
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
			String charName, String particleId) throws Exception {
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
			throw e;
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
	 * @throws Exception
	 */
	private void addParticleCharacterization(Characterization achar,
			CharacterizationBean charBean) throws Exception {

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
				throw new RuntimeException(
						"The view title is already in use.  Please enter a different one.");
			} else {
				// if ID exists, load from database
				if (charBean.getId() != null) {
					// check if ID is still valid
					achar = (Characterization) session.get(
							Characterization.class, new Long(charBean.getId()));
					if (achar == null)
						throw new Exception(
								"This characterization is no longer in the database.  Please log in again to refresh.");
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
			throw e;
		} finally {
			HibernateUtil.closeSession();
			// skip if there is database error above and achar has not
			// been persisted
			if (achar.getId() != null) {
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
							DerivedBioAssayDataBean derivedBioAssayDataBean = new DerivedBioAssayDataBean(
									derivedBioAssayData);
							// assign visibility
							DerivedBioAssayDataBean unsaved = charBean
									.getDerivedBioAssayDataList().get(count);
							derivedBioAssayDataBean.setVisibilityGroups(unsaved
									.getVisibilityGroups());
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
			throws Exception {
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();

			if (!charBean.getDerivedBioAssayDataList().isEmpty()) {
				for (DerivedBioAssayDataBean derivedBioAssayDataBean : charBean
						.getDerivedBioAssayDataList()) {
					// add new characterization file type if necessary
					if (derivedBioAssayDataBean.getType().length() > 0) {
						CharacterizationFileType fileType = new CharacterizationFileType();
						NanoparticleService.addLookupType(session, fileType,
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
						NanoparticleService.addLookupType(session, measureType,
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
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}

	/*
	 * check if viewTitle is already used the same type of characterization for
	 * the same particle
	 */
	private boolean isCharacterizationViewTitleUsed(Session session,
			Characterization achar, CharacterizationBean charBean)
			throws Exception {
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
			throws Exception {

		byte[] fileContent = fileBean.getFileContent();
		String rootPath = PropertyReader.getProperty(
				CaNanoLabConstants.FILEUPLOAD_PROPERTY, "fileRepositoryDir");
		if (fileContent != null) {
			FileService fileService = new FileService();
			fileService.writeFile(fileContent, rootPath + File.separator
					+ fileBean.getUri());
		}
		this.userService.setVisiblity(fileBean.getId(), fileBean
				.getVisibilityGroups());
	}

	private void addInstrumentConfig(InstrumentConfigBean instrumentConfigBean,
			Characterization doChar, Session session) throws Exception {
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

		// check if instrument is already in database based on type and
		// manufacturer
		Instrument instrument = null;
		List instrumentResults = session.createQuery(
				"select instrument from Instrument instrument where instrument.type='"
						+ instrumentBean.getType()
						+ "' and instrument.manufacturer='"
						+ instrumentBean.getManufacturer() + "'").list();

		for (Object obj : instrumentResults) {
			instrument = (Instrument) obj;
		}
		// if not in the database, create one
		if (instrument == null) {
			instrument = new Instrument();
			instrument.setType(instrumentBean.getType());
			instrument.setManufacturer(instrumentBean.getManufacturer());
			session.save(instrument);
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
			Characterization doChar, Session session) throws Exception {
		if (protocolFileBean.getId() != null
				&& protocolFileBean.getId().length() > 0) {
			ProtocolFile protocolFile = (ProtocolFile) session.get(
					ProtocolFile.class, new Long(protocolFileBean.getId()));
			doChar.setProtocolFile(protocolFile);
		}
	}

	/**
	 * Saves the size characterization to the database
	 * 
	 * @param size
	 * @throws Exception
	 */
	public void addParticleSize(CharacterizationBean size) throws Exception {
		Size doSize = new Size();
		addParticleCharacterization(doSize, size);
	}

	/**
	 * Saves the size characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param surface
	 * @throws Exception
	 */
	public void addParticleSurface(CharacterizationBean surface)
			throws Exception {
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
			String characterizationName) throws Exception {
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
			String characterizationName) throws Exception {
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

	private void addMeasureUnit(Session session, String unit, String type)
			throws Exception {
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
			throws Exception {
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
				throw e;
			} finally {
				HibernateUtil.closeSession();
			}
		}
	}

	/**
	 * Saves the molecular weight characterization to the database
	 * 
	 * @param molecularWeight
	 * @throws Exception
	 */
	public void addParticleMolecularWeight(CharacterizationBean molecularWeight)
			throws Exception {
		MolecularWeight doMolecularWeight = new MolecularWeight();
		addParticleCharacterization(doMolecularWeight, molecularWeight);
	}

	/**
	 * Saves the morphology characterization to the database
	 * 
	 * @param morphology
	 * @throws Exception
	 */
	public void addParticleMorphology(MorphologyBean morphology)
			throws Exception {
		Morphology doMorphology = new Morphology();
		addParticleCharacterization(doMorphology, morphology);
		MorphologyType morphologyType = new MorphologyType();
		addLookupType(morphologyType, morphology.getType());
	}

	/**
	 * Saves the shape characterization to the database
	 * 
	 * @param shape
	 * @throws Exception
	 */
	public void addParticleShape(ShapeBean shape) throws Exception {
		Shape doShape = new Shape();
		addParticleCharacterization(doShape, shape);
		ShapeType shapeType = new ShapeType();
		addLookupType(shapeType, shape.getType());
	}

	/**
	 * Saves the purity characterization to the database
	 * 
	 * @param purity
	 * @throws Exception
	 */
	public void addParticlePurity(CharacterizationBean purity) throws Exception {
		Purity doPurity = new Purity();
		addParticleCharacterization(doPurity, purity);
	}

	/**
	 * Saves the solubility characterization to the database
	 * 
	 * @param solubility
	 * @throws Exception
	 */
	public void addParticleSolubility(SolubilityBean solubility)
			throws Exception {
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
	 * @throws Exception
	 */
	public void addHemolysis(CharacterizationBean hemolysis) throws Exception {
		Hemolysis doHemolysis = new Hemolysis();
		addParticleCharacterization(doHemolysis, hemolysis);
	}

	/**
	 * Saves the invitro coagulation characterization to the database
	 * 
	 * @param coagulation
	 * @throws Exception
	 */
	public void addCoagulation(CharacterizationBean coagulation)
			throws Exception {
		Coagulation doCoagulation = new Coagulation();
		addParticleCharacterization(doCoagulation, coagulation);
	}

	/**
	 * Saves the invitro plate aggregation characterization to the database
	 * 
	 * @param plateletAggregation
	 * @throws Exception
	 */
	public void addPlateletAggregation(CharacterizationBean plateletAggregation)
			throws Exception {
		PlateletAggregation doPlateletAggregation = new PlateletAggregation();
		addParticleCharacterization(doPlateletAggregation, plateletAggregation);
	}

	/**
	 * Saves the invitro chemotaxis characterization to the database
	 * 
	 * @param chemotaxis
	 * @throws Exception
	 */
	public void addChemotaxis(CharacterizationBean chemotaxis) throws Exception {
		Chemotaxis doChemotaxis = new Chemotaxis();
		addParticleCharacterization(doChemotaxis, chemotaxis);
	}

	/**
	 * Saves the invitro NKCellCytotoxicActivity characterization to the
	 * database
	 * 
	 * @param nkCellCytotoxicActivity
	 * @throws Exception
	 */
	public void addNKCellCytotoxicActivity(
			CharacterizationBean nkCellCytotoxicActivity) throws Exception {
		NKCellCytotoxicActivity doNKCellCytotoxicActivity = new NKCellCytotoxicActivity();
		addParticleCharacterization(doNKCellCytotoxicActivity,
				nkCellCytotoxicActivity);
	}

	/**
	 * Saves the invitro LeukocyteProliferation characterization to the database
	 * 
	 * @param leukocyteProliferation
	 * @throws Exception
	 */
	public void addLeukocyteProliferation(
			CharacterizationBean leukocyteProliferation) throws Exception {
		LeukocyteProliferation doLeukocyteProliferation = new LeukocyteProliferation();
		addParticleCharacterization(doLeukocyteProliferation,
				leukocyteProliferation);
	}

	/**
	 * Saves the invitro CFU_GM characterization to the database
	 * 
	 * @param cfu_gm
	 * @throws Exception
	 */
	public void addCFU_GM(CharacterizationBean cfu_gm) throws Exception {
		CFU_GM doCFU_GM = new CFU_GM();
		addParticleCharacterization(doCFU_GM, cfu_gm);
	}

	/**
	 * Saves the invitro Complement Activation characterization to the database
	 * 
	 * @param complementActivation
	 * @throws Exception
	 */
	public void addComplementActivation(
			CharacterizationBean complementActivation) throws Exception {
		ComplementActivation doComplementActivation = new ComplementActivation();
		addParticleCharacterization(doComplementActivation,
				complementActivation);
	}

	/**
	 * Saves the invitro OxidativeBurst characterization to the database
	 * 
	 * @param oxidativeBurst
	 * @throws Exception
	 */
	public void addOxidativeBurst(CharacterizationBean oxidativeBurst)
			throws Exception {
		OxidativeBurst doOxidativeBurst = new OxidativeBurst();
		addParticleCharacterization(doOxidativeBurst, oxidativeBurst);
	}

	/**
	 * Saves the invitro Phagocytosis characterization to the database
	 * 
	 * @param phagocytosis
	 * @throws Exception
	 */
	public void addPhagocytosis(CharacterizationBean phagocytosis)
			throws Exception {
		Phagocytosis doPhagocytosis = new Phagocytosis();
		addParticleCharacterization(doPhagocytosis, phagocytosis);
	}

	/**
	 * Saves the invitro CytokineInduction characterization to the database
	 * 
	 * @param cytokineInduction
	 * @throws Exception
	 */
	public void addCytokineInduction(CharacterizationBean cytokineInduction)
			throws Exception {
		CytokineInduction doCytokineInduction = new CytokineInduction();
		addParticleCharacterization(doCytokineInduction, cytokineInduction);
	}

	/**
	 * Saves the invitro plasma protein binding characterization to the database
	 * 
	 * @param plasmaProteinBinding
	 * @throws Exception
	 */
	public void addProteinBinding(CharacterizationBean plasmaProteinBinding)
			throws Exception {
		PlasmaProteinBinding doProteinBinding = new PlasmaProteinBinding();
		addParticleCharacterization(doProteinBinding, plasmaProteinBinding);
	}

	/**
	 * Saves the invitro binding characterization to the database
	 * 
	 * @param cellViability
	 * @throws Exception
	 */
	public void addCellViability(CytotoxicityBean cellViability)
			throws Exception {
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
	 * @throws Exception
	 */
	public void addEnzymeInduction(CharacterizationBean enzymeInduction)
			throws Exception {
		EnzymeInduction doEnzymeInduction = new EnzymeInduction();
		addParticleCharacterization(doEnzymeInduction, enzymeInduction);
	}

	/**
	 * Saves the invitro OxidativeStress characterization to the database
	 * 
	 * @param oxidativeStress
	 * @throws Exception
	 */
	public void addOxidativeStress(CharacterizationBean oxidativeStress)
			throws Exception {
		OxidativeStress doOxidativeStress = new OxidativeStress();
		addParticleCharacterization(doOxidativeStress, oxidativeStress);
	}

	/**
	 * Saves the invitro Caspase3Activation characterization to the database
	 * 
	 * @param caspase3Activation
	 * @throws Exception
	 */
	public void addCaspase3Activation(CytotoxicityBean caspase3Activation)
			throws Exception {
		Caspase3Activation doCaspase3Activation = new Caspase3Activation();
		addParticleCharacterization(doCaspase3Activation, caspase3Activation);
		CellLineType cellLineType = new CellLineType();
		addLookupType(cellLineType, caspase3Activation.getCellLine());
	}

	/**
	 * Load the derived data file for the given fileId from the database
	 * 
	 * @param fileId
	 * @return
	 */
	public DerivedBioAssayDataBean getDerivedBioAssayData(String fileId)
			throws Exception {

		DerivedBioAssayDataBean fileBean = null;
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			DerivedBioAssayData file = (DerivedBioAssayData) session.load(
					DerivedBioAssayData.class, StringUtils
							.convertToLong(fileId));
			// load keywords
			file.getKeywordCollection();
			fileBean = new DerivedBioAssayDataBean(file,
					CaNanoLabConstants.OUTPUT);
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem getting file with file ID: " + fileId, e);
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		// get visibilities
		UserService userService = new UserService(
				CaNanoLabConstants.CSM_APP_NAME);
		List<String> accessibleGroups = userService.getAccessibleGroups(
				fileBean.getId(), CaNanoLabConstants.CSM_READ_ROLE);
		String[] visibilityGroups = accessibleGroups.toArray(new String[0]);
		fileBean.setVisibilityGroups(visibilityGroups);
		return fileBean;
	}

	/**
	 * Delete the characterizations
	 */
	public void deleteCharacterizations(String[] charIds) throws Exception {
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
			throw new Exception(
					"The characterization is no longer exist in the database, please login again to refresh the view.");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	public SortedSet<String> getAllCharacterizationFileTypes() throws Exception {
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
			throw new RuntimeException(
					"Problem to retrieve all characterization file types. ", e);
		} finally {
			HibernateUtil.closeSession();
		}
		return fileTypes;
	}

	public List<CharacterizationTypeBean> getAllCharacterizationTypes()
			throws Exception {
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
			throw new RuntimeException(
					"Problem to retrieve all characteriztaion types. ", e);
		} finally {
			HibernateUtil.closeSession();
		}
		return charTypes;
	}

	public Map<String, SortedSet<String>> getDerivedDataCategoryMap(
			String characterizationName) throws Exception {
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
			throw new RuntimeException(
					"Problem to retrieve all derived bioassay data categories.",
					e);

		} finally {
			HibernateUtil.closeSession();
		}
		return categoryMap;
	}

	public SortedSet<String> getDerivedDatumNames(String characterizationName)
			throws Exception {
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
			throw new RuntimeException(
					"Problem to retrieve all derived bioassay datum names.", e);

		} finally {
			HibernateUtil.closeSession();
		}
		return datumNames;
	}

	public SortedSet<String> getDerivedDataCategories(
			String characterizationName) throws Exception {
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
			throw new RuntimeException(
					"Problem to retrieve all derived bioassay data categories.",
					e);

		} finally {
			HibernateUtil.closeSession();
		}
		return categories;
	}

	public SortedSet<String> getAllCharacterizationSources() throws Exception {
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
			throw new RuntimeException(
					"Problem to retrieve all Characterization Sources. ");
		} finally {
			HibernateUtil.closeSession();
		}
		sources.addAll(Arrays
				.asList(CaNanoLabConstants.DEFAULT_CHARACTERIZATION_SOURCES));

		return sources;
	}

	public SortedSet<String> getAllManufacturers() throws Exception {
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
			throw new RuntimeException(
					"Problem to retrieve all manufacturers. ", e);
		} finally {
			HibernateUtil.closeSession();
		}
		return manufacturers;
	}

	public Map<String, SortedSet<String>> getAllInstrumentManufacturers()
			throws Exception {
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
			throw new RuntimeException(
					"Problem to retrieve manufacturers for intrument types.");
		} finally {
			HibernateUtil.closeSession();
		}
		return instrumentManufacturers;
	}

	public List<InstrumentBean> getAllInstruments() throws Exception {
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
			throw new RuntimeException("Problem to retrieve all intruments. ");
		} finally {
			HibernateUtil.closeSession();
		}
		return instruments;
	}


	/**
	 * 
	 * @return a map between a characterization type and its child
	 *         characterizations.
	 */
	public Map<String, List<CharacterizationBean>> getCharacterizationTypeCharacterizations()
			throws Exception {
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
			throw new RuntimeException(
					"Problem to retrieve all characteriztaion type characterizations. ");
		} finally {
			HibernateUtil.closeSession();
		}

		return charTypeChars;

	}

	/**
	 * 
	 * @return a map between a characterization type and its child
	 *         characterizations.
	 */
	public Map<String, Map<String, List<CharacterizationBean>>> getCharacterizationTypeTree()
			throws Exception {
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
			throw new RuntimeException(
					"Problem to retrieve all characteriztaion type characterizations. ");
		} finally {
			HibernateUtil.closeSession();
		}
		return charTypeChars;
	}
}
