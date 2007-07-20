package gov.nih.nci.calab.service.submit;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.HibernateDataAccess;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.Instrument;
import gov.nih.nci.calab.domain.InstrumentConfiguration;
import gov.nih.nci.calab.domain.Keyword;
import gov.nih.nci.calab.domain.LabFile;
import gov.nih.nci.calab.domain.LookupType;
import gov.nih.nci.calab.domain.MeasureType;
import gov.nih.nci.calab.domain.MeasureUnit;
import gov.nih.nci.calab.domain.OutputFile;
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
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.ParticleComposition;
import gov.nih.nci.calab.domain.nano.function.Agent;
import gov.nih.nci.calab.domain.nano.function.AgentTarget;
import gov.nih.nci.calab.domain.nano.function.Function;
import gov.nih.nci.calab.domain.nano.function.Linkage;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;
import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.DatumBean;
import gov.nih.nci.calab.dto.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.calab.dto.characterization.composition.CompositionBean;
import gov.nih.nci.calab.dto.characterization.invitro.CytotoxicityBean;
import gov.nih.nci.calab.dto.characterization.physical.MorphologyBean;
import gov.nih.nci.calab.dto.characterization.physical.ShapeBean;
import gov.nih.nci.calab.dto.characterization.physical.SolubilityBean;
import gov.nih.nci.calab.dto.characterization.physical.SurfaceBean;
import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.function.FunctionBean;
import gov.nih.nci.calab.exception.CalabException;
import gov.nih.nci.calab.service.common.FileService;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;
import gov.nih.nci.calab.service.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * This class includes service calls involved in creating nanoparticle general
 * info and adding functions and characterizations for nanoparticles, as well as
 * creating reports.
 * 
 * @author pansu
 * 
 */
public class SubmitNanoparticleService {
	private static Logger logger = Logger
			.getLogger(SubmitNanoparticleService.class);

	// remove existing visibilities for the data
	private UserService userService;

	public SubmitNanoparticleService() throws Exception {
		userService = new UserService(CaNanoLabConstants.CSM_APP_NAME);
	}

	/**
	 * Update keywords and visibilities for the particle with the given name and
	 * type
	 * 
	 * @param particleType
	 * @param particleName
	 * @param keywords
	 * @param visibilities
	 * @throws Exception
	 */
	public void addParticleGeneralInfo(String particleType,
			String particleName, String[] keywords, String[] visibilities)
			throws Exception {

		// save nanoparticle to the database
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			// get the existing particle from database created during sample
			// creation
			List results = ida.search("from Nanoparticle where name='"
					+ particleName + "' and type='" + particleType + "'");
			Nanoparticle particle = null;
			for (Object obj : results) {
				particle = (Nanoparticle) obj;
			}
			if (particle == null) {
				throw new CalabException("No such particle in the database");
			}

			particle.getKeywordCollection().clear();
			if (keywords != null) {
				for (String keyword : keywords) {
					Keyword keywordObj = new Keyword();
					keywordObj.setName(keyword);
					particle.getKeywordCollection().add(keywordObj);
				}
			}

		} catch (Exception e) {
			ida.rollback();
			logger
					.error("Problem updating particle with name: "
							+ particleName);
			throw e;
		} finally {
			ida.close();
		}
		userService.setVisiblity(particleName, visibilities);
	}

	/**
	 * Save characterization to the database.
	 * 
	 * @param particleType
	 * @param particleName
	 * @param achar
	 * @throws Exception
	 */
	private void addParticleCharacterization(String particleType,
			String particleName, Characterization achar,
			CharacterizationBean charBean) throws Exception {

		// if ID is not set save to the database otherwise update
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);

		Nanoparticle particle = null;
		int existingViewTitleCount = -1;
		try {
			ida.open();
			// check if viewTitle is already used the same type of
			// characterization for the same particle
			boolean viewTitleUsed = isCharacterizationViewTitleUsed(ida,
					particleType, particleName, achar);
			if (!viewTitleUsed) {
				if (achar.getInstrumentConfiguration() != null) {
					addInstrumentConfig(achar.getInstrumentConfiguration(), ida);
				}
				// if ID exists, do update
				if (achar.getId() != null) {
					// check if ID is still valid
					try {
						Characterization storedChara = (Characterization) ida
								.load(Characterization.class, achar.getId());
					} catch (Exception e) {
						throw new Exception(
								"This characterization is no longer in the database.  Please log in again to refresh.");
					}
					ida.store(achar);
				} else {// get the existing particle and characterizations
					// from database created during sample creation
					List results = ida
							.search("select particle from Nanoparticle particle left join fetch particle.characterizationCollection where particle.name='"
									+ particleName
									+ "' and particle.type='"
									+ particleType + "'");

					for (Object obj : results) {
						particle = (Nanoparticle) obj;
					}

					if (particle != null) {
						particle.getCharacterizationCollection().add(achar);
					}
				}
			}
			if (existingViewTitleCount > 0) {
				throw new Exception(
						"The view title is already in use.  Please enter a different one.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			ida.rollback();
			logger.error("Problem saving characterization. ");
			throw e;
		} finally {
			ida.close();
		}

		// save file to the file system
		// if this block of code is inside the db try catch block, hibernate
		// doesn't persist derivedBioAssayData
		if (!charBean.getDerivedBioAssayDataList().isEmpty()) {
			for (DerivedBioAssayDataBean derivedBioAssayDataBean : charBean
					.getDerivedBioAssayDataList()) {
				saveCharacterizationFile(derivedBioAssayDataBean);
			}
		}
	}

	public void addNewCharacterizationDataDropdowns(
			CharacterizationBean charBean, String characterizationName)
			throws Exception {
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			if (!charBean.getDerivedBioAssayDataList().isEmpty()) {
				for (DerivedBioAssayDataBean derivedBioAssayDataBean : charBean
						.getDerivedBioAssayDataList()) {
					// add new characterization file type if necessary
					if (derivedBioAssayDataBean.getType().length() > 0) {
						CharacterizationFileType fileType = new CharacterizationFileType();
						addLookupType(ida, fileType, derivedBioAssayDataBean
								.getType());
					}
					// add new derived data cateory
					for (String category : derivedBioAssayDataBean
							.getCategories()) {
						addDerivedDataCategory(ida, category,
								characterizationName);
					}
					// add new datum name, measure type, and unit
					for (DatumBean datumBean : derivedBioAssayDataBean
							.getDatumList()) {
						addDatumName(ida, datumBean.getName(),
								characterizationName);
						MeasureType measureType = new MeasureType();
						addLookupType(ida, measureType, datumBean
								.getStatisticsType());
						addMeasureUnit(ida, datumBean.getUnit(),
								characterizationName);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ida.rollback();
			logger.error("Problem saving characterization data drop downs. ");
			throw e;
		} finally {
			ida.close();
		}
	}

	/*
	 * check if viewTitle is already used the same type of characterization for
	 * the same particle
	 */
	private boolean isCharacterizationViewTitleUsed(IDataAccess ida,
			String particleType, String particleName, Characterization achar)
			throws Exception {
		String viewTitleQuery = "";

		if (achar.getId() == null) {
			viewTitleQuery = "select count(achar.identificationName) from Nanoparticle particle join particle.characterizationCollection achar where particle.name='"
					+ particleName
					+ "' and particle.type='"
					+ particleType
					+ "' and achar.identificationName='"
					+ achar.getIdentificationName()
					+ "' and achar.name='"
					+ achar.getName() + "'";
		} else {
			viewTitleQuery = "select count(achar.identificationName) from Nanoparticle particle join particle.characterizationCollection achar where particle.name='"
					+ particleName
					+ "' and particle.type='"
					+ particleType
					+ "' and achar.identificationName='"
					+ achar.getIdentificationName()
					+ "' and achar.name='"
					+ achar.getName() + "' and achar.id!=" + achar.getId();
		}
		List viewTitleResult = ida.search(viewTitleQuery);

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
		userService.setVisiblity(fileBean.getId(), fileBean
				.getVisibilityGroups());
	}

	private void addInstrumentConfig(InstrumentConfiguration instrumentConfig,
			IDataAccess ida) throws Exception {
		Instrument instrument = instrumentConfig.getInstrument();

		// check if instrument is already in database
		List instrumentResults = ida
				.search("select instrument from Instrument instrument where instrument.type='"
						+ instrument.getType()
						+ "' and instrument.manufacturer='"
						+ instrument.getManufacturer() + "'");

		Instrument storedInstrument = null;
		for (Object obj : instrumentResults) {
			storedInstrument = (Instrument) obj;
		}
		if (storedInstrument != null) {
			instrument.setId(storedInstrument.getId());
		} else {
			ida.createObject(instrument);
		}

		// if new instrumentConfig, save it
		if (instrumentConfig.getId() == null) {
			ida.createObject(instrumentConfig);
		} else {
			InstrumentConfiguration storedInstrumentConfig = (InstrumentConfiguration) ida
					.load(InstrumentConfiguration.class, instrumentConfig
							.getId());
			storedInstrumentConfig.setDescription(instrumentConfig
					.getDescription());
			storedInstrumentConfig.setInstrument(instrument);
		}
	}

	/**
	 * Saves the particle composition to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param composition
	 * @throws Exception
	 */
	public void addParticleComposition(String particleType,
			String particleName, CompositionBean composition) throws Exception {
		ParticleComposition doComp = composition.getDomainObj();
		addParticleCharacterization(particleType, particleName, doComp,
				composition);
	}

	/**
	 * O Saves the size characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param size
	 * @throws Exception
	 */
	public void addParticleSize(String particleType, String particleName,
			CharacterizationBean size) throws Exception {

		Size doSize = new Size();
		size.updateDomainObj(doSize);
		addParticleCharacterization(particleType, particleName, doSize, size);
	}

	/**
	 * Saves the size characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param surface
	 * @throws Exception
	 */
	public void addParticleSurface(String particleType, String particleName,
			CharacterizationBean surface) throws Exception {

		Surface doSurface = new Surface();
		((SurfaceBean) surface).updateDomainObj(doSurface);
		addParticleCharacterization(particleType, particleName, doSurface,
				surface);
//		addMeasureUnit(doSurface.getCharge().getUnitOfMeasurement(),
//				CaNanoLabConstants.UNIT_TYPE_CHARGE);
//		addMeasureUnit(doSurface.getSurfaceArea().getUnitOfMeasurement(),
//				CaNanoLabConstants.UNIT_TYPE_AREA);
//		addMeasureUnit(doSurface.getZetaPotential().getUnitOfMeasurement(),
//				CaNanoLabConstants.UNIT_TYPE_ZETA_POTENTIAL);

	}

	private void addLookupType(IDataAccess ida, LookupType lookupType,
			String type) throws Exception {
		String className = lookupType.getClass().getSimpleName();

		List results = ida.search("select count(distinct name) from "
				+ className + " type where name='" + type + "'");
		lookupType.setName(type);
		int count = -1;
		for (Object obj : results) {
			count = ((Integer) (obj)).intValue();
		}
		if (count == 0) {
			ida.createObject(lookupType);
		}
	}

	private void addDatumName(IDataAccess ida, String name,
			String characterizationName) throws Exception {

		List results = ida.search("select count(distinct name) from DatumName"
				+ " where characterizationName='" + characterizationName + "'"
				+ " and name='" + name + "'");
		DatumName datumName = new DatumName();
		datumName.setName(name);
		datumName.setCharacterizationName(characterizationName);
		datumName.setDatumParsed(false);
		int count = -1;
		for (Object obj : results) {
			count = ((Integer) (obj)).intValue();
		}
		if (count == 0) {
			ida.createObject(datumName);
		}
	}

	private void addDerivedDataCategory(IDataAccess ida, String name,
			String characterizationName) throws Exception {

		List results = ida
				.search("select count(distinct name) from DerivedBioAssayDataCategory"
						+ " where characterizationName='"
						+ characterizationName
						+ "'"
						+ " and name='"
						+ name
						+ "'");
		DerivedBioAssayDataCategory category = new DerivedBioAssayDataCategory();
		category.setName(name);
		category.setCharacterizationName(characterizationName);
		int count = -1;
		for (Object obj : results) {
			count = ((Integer) (obj)).intValue();
		}
		if (count == 0) {
			ida.createObject(category);
		}

	}

	private void addLookupType(LookupType lookupType, String type)
			throws Exception {
		// if ID is not set save to the database otherwise update
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		String className = lookupType.getClass().getSimpleName();
		try {
			ida.open();
			List results = ida.search("select count(distinct name) from "
					+ className + " type where name='" + type + "'");
			lookupType.setName(type);
			int count = -1;
			for (Object obj : results) {
				count = ((Integer) (obj)).intValue();
			}
			if (count == 0) {
				ida.createObject(lookupType);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ida.rollback();
			logger.error("Problem saving look up type: " + type);
			throw e;
		} finally {
			ida.close();
		}
	}

//	private void addMeasureUnit(String unit, String type) throws Exception {
//		if (unit == null || unit.length() == 0) {
//			return;
//		}
//		// if ID is not set save to the database otherwise update
//		IDataAccess ida = (new DataAccessProxy())
//				.getInstance(IDataAccess.HIBERNATE);
//		MeasureUnit measureUnit = new MeasureUnit();
//		try {
//			ida.open();
//			List results = ida
//					.search("select count(distinct measureUnit.name) from "
//							+ "MeasureUnit measureUnit where measureUnit.name='"
//							+ unit + "' and measureUnit.type='" + type + "'");
//			int count = -1;
//			for (Object obj : results) {
//				count = ((Integer) (obj)).intValue();
//			}
//			if (count == 0) {
//				measureUnit.setName(unit);
//				measureUnit.setType(type);
//				ida.createObject(measureUnit);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			ida.rollback();
//			logger.error("Problem saving look up type: " + type);
//			throw e;
//		} finally {
//			ida.close();
//		}
//	}

	private void addMeasureUnit(IDataAccess ida, String unit, String type)
			throws Exception {
		if (unit == null || unit.length() == 0) {
			return;
		}
		List results = ida.search("select count(distinct name) from "
				+ " MeasureUnit where name='" + unit + "' and type='" + type
				+ "'");
		int count = -1;
		for (Object obj : results) {
			count = ((Integer) (obj)).intValue();
		}
		MeasureUnit measureUnit = new MeasureUnit();
		if (count == 0) {
			measureUnit.setName(unit);
			measureUnit.setType(type);
			ida.createObject(measureUnit);
		}
	}

	/**
	 * Saves the molecular weight characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param molecularWeight
	 * @throws Exception
	 */
	public void addParticleMolecularWeight(String particleType,
			String particleName, CharacterizationBean molecularWeight)
			throws Exception {
		MolecularWeight doMolecularWeight = new MolecularWeight();
		molecularWeight.updateDomainObj(doMolecularWeight);
		addParticleCharacterization(particleType, particleName,
				doMolecularWeight, molecularWeight);
	}

	/**
	 * Saves the morphology characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param morphology
	 * @throws Exception
	 */
	public void addParticleMorphology(String particleType, String particleName,
			CharacterizationBean morphology) throws Exception {
		Morphology doMorphology = new Morphology();
		((MorphologyBean) morphology).updateDomainObj(doMorphology);
		addParticleCharacterization(particleType, particleName, doMorphology,
				morphology);
		MorphologyType morphologyType = new MorphologyType();
		addLookupType(morphologyType, doMorphology.getType());
	}

	/**
	 * Saves the shape characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param shape
	 * @throws Exception
	 */
	public void addParticleShape(String particleType, String particleName,
			CharacterizationBean shape) throws Exception {
		Shape doShape = new Shape();
		((ShapeBean) shape).updateDomainObj(doShape);
		addParticleCharacterization(particleType, particleName, doShape, shape);
		ShapeType shapeType = new ShapeType();
		addLookupType(shapeType, doShape.getType());
	}

	/**
	 * Saves the purity characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param purity
	 * @throws Exception
	 */
	public void addParticlePurity(String particleType, String particleName,
			CharacterizationBean purity) throws Exception {
		Purity doPurity = new Purity();
		purity.updateDomainObj(doPurity);
		// TODO think about how to deal with characterization file.
		addParticleCharacterization(particleType, particleName, doPurity,
				purity);
	}

	/**
	 * Saves the solubility characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param solubility
	 * @throws Exception
	 */
	public void addParticleSolubility(String particleType, String particleName,
			CharacterizationBean solubility) throws Exception {
		Solubility doSolubility = new Solubility();
		((SolubilityBean) solubility).updateDomainObj(doSolubility);
		// TODO think about how to deal with characterization file.
		addParticleCharacterization(particleType, particleName, doSolubility,
				solubility);
		SolventType solventType = new SolventType();
		addLookupType(solventType, doSolubility.getSolvent());
//		addMeasureUnit(doSolubility.getCriticalConcentration()
//				.getUnitOfMeasurement(),
//				CaNanoLabConstants.UNIT_TYPE_CONCENTRATION);
	}

	/**
	 * Saves the invitro hemolysis characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param hemolysis
	 * @throws Exception
	 */
	public void addHemolysis(String particleType, String particleName,
			CharacterizationBean hemolysis) throws Exception {
		Hemolysis doHemolysis = new Hemolysis();
		hemolysis.updateDomainObj(doHemolysis);
		// TODO think about how to deal with characterization file.
		addParticleCharacterization(particleType, particleName, doHemolysis,
				hemolysis);
	}

	/**
	 * Saves the invitro coagulation characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param coagulation
	 * @throws Exception
	 */
	public void addCoagulation(String particleType, String particleName,
			CharacterizationBean coagulation) throws Exception {
		Coagulation doCoagulation = new Coagulation();
		coagulation.updateDomainObj(doCoagulation);
		addParticleCharacterization(particleType, particleName, doCoagulation,
				coagulation);
	}

	/**
	 * Saves the invitro plate aggregation characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param plateletAggregation
	 * @throws Exception
	 */
	public void addPlateletAggregation(String particleType,
			String particleName, CharacterizationBean plateletAggregation)
			throws Exception {
		PlateletAggregation doPlateletAggregation = new PlateletAggregation();
		plateletAggregation.updateDomainObj(doPlateletAggregation);
		addParticleCharacterization(particleType, particleName,
				doPlateletAggregation, plateletAggregation);
	}

	/**
	 * Saves the invitro Complement Activation characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param complementActivation
	 * @throws Exception
	 */
	public void addComplementActivation(String particleType,
			String particleName, CharacterizationBean complementActivation)
			throws Exception {
		ComplementActivation doComplementActivation = new ComplementActivation();
		complementActivation.updateDomainObj(doComplementActivation);
		addParticleCharacterization(particleType, particleName,
				doComplementActivation, complementActivation);
	}

	/**
	 * Saves the invitro chemotaxis characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param chemotaxis
	 * @throws Exception
	 */
	public void addChemotaxis(String particleType, String particleName,
			CharacterizationBean chemotaxis) throws Exception {

		Chemotaxis doChemotaxis = new Chemotaxis();
		chemotaxis.updateDomainObj(doChemotaxis);
		addParticleCharacterization(particleType, particleName, doChemotaxis,
				chemotaxis);
	}

	/**
	 * Saves the invitro NKCellCytotoxicActivity characterization to the
	 * database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param nkCellCytotoxicActivity
	 * @throws Exception
	 */
	public void addNKCellCytotoxicActivity(String particleType,
			String particleName, CharacterizationBean nkCellCytotoxicActivity)
			throws Exception {

		NKCellCytotoxicActivity doNKCellCytotoxicActivity = new NKCellCytotoxicActivity();
		nkCellCytotoxicActivity.updateDomainObj(doNKCellCytotoxicActivity);
		addParticleCharacterization(particleType, particleName,
				doNKCellCytotoxicActivity, nkCellCytotoxicActivity);
	}

	/**
	 * Saves the invitro LeukocyteProliferation characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param leukocyteProliferation
	 * @throws Exception
	 */
	public void addLeukocyteProliferation(String particleType,
			String particleName, CharacterizationBean leukocyteProliferation)
			throws Exception {
		LeukocyteProliferation doLeukocyteProliferation = new LeukocyteProliferation();
		leukocyteProliferation.updateDomainObj(doLeukocyteProliferation);
		addParticleCharacterization(particleType, particleName,
				doLeukocyteProliferation, leukocyteProliferation);
	}

	/**
	 * Saves the invitro CFU_GM characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param cfu_gm
	 * @throws Exception
	 */
	public void addCFU_GM(String particleType, String particleName,
			CharacterizationBean cfu_gm) throws Exception {
		CFU_GM doCFU_GM = new CFU_GM();
		cfu_gm.updateDomainObj(doCFU_GM);
		addParticleCharacterization(particleType, particleName, doCFU_GM,
				cfu_gm);
	}

	/**
	 * Saves the invitro OxidativeBurst characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param oxidativeBurst
	 * @throws Exception
	 */
	public void addOxidativeBurst(String particleType, String particleName,
			CharacterizationBean oxidativeBurst) throws Exception {
		OxidativeBurst doOxidativeBurst = new OxidativeBurst();
		oxidativeBurst.updateDomainObj(doOxidativeBurst);
		addParticleCharacterization(particleType, particleName,
				doOxidativeBurst, oxidativeBurst);
	}

	/**
	 * Saves the invitro Phagocytosis characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param phagocytosis
	 * @throws Exception
	 */
	public void addPhagocytosis(String particleType, String particleName,
			CharacterizationBean phagocytosis) throws Exception {
		Phagocytosis doPhagocytosis = new Phagocytosis();
		phagocytosis.updateDomainObj(doPhagocytosis);
		addParticleCharacterization(particleType, particleName, doPhagocytosis,
				phagocytosis);
	}

	/**
	 * Saves the invitro CytokineInduction characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param cytokineInduction
	 * @throws Exception
	 */
	public void addCytokineInduction(String particleType, String particleName,
			CharacterizationBean cytokineInduction) throws Exception {
		CytokineInduction doCytokineInduction = new CytokineInduction();
		cytokineInduction.updateDomainObj(doCytokineInduction);

		addParticleCharacterization(particleType, particleName,
				doCytokineInduction, cytokineInduction);
	}

	/**
	 * Saves the invitro plasma protein binding characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param plasmaProteinBinding
	 * @throws Exception
	 */
	public void addProteinBinding(String particleType, String particleName,
			CharacterizationBean plasmaProteinBinding) throws Exception {
		PlasmaProteinBinding doProteinBinding = new PlasmaProteinBinding();
		plasmaProteinBinding.updateDomainObj(doProteinBinding);
		addParticleCharacterization(particleType, particleName,
				doProteinBinding, plasmaProteinBinding);
	}

	/**
	 * Saves the invitro binding characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param cellViability
	 * @throws Exception
	 */
	public void addCellViability(String particleType, String particleName,
			CharacterizationBean cellViability) throws Exception {
		CellViability doCellViability = new CellViability();
		((CytotoxicityBean) cellViability).updateDomainObj(doCellViability);
		addParticleCharacterization(particleType, particleName,
				doCellViability, cellViability);
		CellLineType cellLineType = new CellLineType();
		addLookupType(cellLineType, doCellViability.getCellLine());
	}

	/**
	 * Saves the invitro EnzymeInduction binding characterization to the
	 * database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param enzymeInduction
	 * @throws Exception
	 */
	public void addEnzymeInduction(String particleType, String particleName,
			CharacterizationBean enzymeInduction) throws Exception {
		EnzymeInduction doEnzymeInduction = new EnzymeInduction();
		enzymeInduction.updateDomainObj(doEnzymeInduction);
		addParticleCharacterization(particleType, particleName,
				doEnzymeInduction, enzymeInduction);
	}

	/**
	 * Saves the invitro OxidativeStress characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param oxidativeStress
	 * @throws Exception
	 */
	public void addOxidativeStress(String particleType, String particleName,
			CharacterizationBean oxidativeStress) throws Exception {
		OxidativeStress doOxidativeStress = new OxidativeStress();
		oxidativeStress.updateDomainObj(doOxidativeStress);
		addParticleCharacterization(particleType, particleName,
				doOxidativeStress, oxidativeStress);
	}

	/**
	 * Saves the invitro Caspase3Activation characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param caspase3Activation
	 * @throws Exception
	 */
	public void addCaspase3Activation(String particleType, String particleName,
			CharacterizationBean caspase3Activation) throws Exception {
		Caspase3Activation doCaspase3Activation = new Caspase3Activation();
		caspase3Activation.updateDomainObj(doCaspase3Activation);
		addParticleCharacterization(particleType, particleName,
				doCaspase3Activation, caspase3Activation);
		CellLineType cellLineType = new CellLineType();
		addLookupType(cellLineType, doCaspase3Activation.getCellLine());
	}

	public void setCharacterizationFile(String particleName,
			String characterizationName, LabFileBean fileBean) {

	}

	/**
	 * 
	 */
	public void addParticleFunction(String particleType, String particleName,
			FunctionBean function) throws Exception {
		Function doFunction = function.getDomainObj();

		// if ID is not set save to the database otherwise update
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);

		Nanoparticle particle = null;
		int existingViewTitleCount = -1;
		try {
			// Have to seperate this section out in a different hibernate
			// session.
			// check linkage id object type
			ida.open();
			if (doFunction.getId() != null
					&& doFunction.getLinkageCollection() != null) {
				for (Linkage linkage : doFunction.getLinkageCollection()) {
					// check linkage id object type
					if (linkage.getId() != null) {
						List result = ida
								.search("from Linkage linkage where linkage.id = "
										+ linkage.getId());
						if (result != null && result.size() > 0) {
							Linkage existingObj = (Linkage) result.get(0);
							// the the type is different,
							if (existingObj.getClass() != linkage.getClass()) {
								linkage.setId(null);
								ida.removeObject(existingObj);
							}
						}
					}
				}
			}
			ida.close();

			ida.open();
			if (doFunction.getLinkageCollection() != null) {
				for (Linkage linkage : doFunction.getLinkageCollection()) {
					Agent agent = linkage.getAgent();
					if (agent != null) {
						for (AgentTarget agentTarget : agent
								.getAgentTargetCollection()) {
							ida.store(agentTarget);
						}
						ida.store(agent);
					}
					ida.store(linkage);
				}
			}

			boolean viewTitleUsed = isFunctionViewTitleUsed(ida, particleType,
					particleName, doFunction);
			if (!viewTitleUsed) {
				if (doFunction.getId() != null) {
					ida.store(doFunction);
				} else {// get the existing particle and compositions
					// from database created during sample creation
					List results = ida
							.search("select particle from Nanoparticle particle left join fetch particle.functionCollection where particle.name='"
									+ particleName
									+ "' and particle.type='"
									+ particleType + "'");

					for (Object obj : results) {
						particle = (Nanoparticle) obj;
					}

					if (particle != null) {
						particle.getFunctionCollection().add(doFunction);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ida.rollback();
			logger.error("Problem saving characterization: ");
			throw e;
		} finally {
			ida.close();
		}
		if (existingViewTitleCount > 0) {
			throw new CalabException(
					"The view title is already in use.  Please enter a different one.");
		}
	}

	/*
	 * check if viewTitle is already used the same type of function for the same
	 * particle
	 */
	private boolean isFunctionViewTitleUsed(IDataAccess ida,
			String particleType, String particleName, Function function)
			throws Exception {
		// check if viewTitle is already used the same type of
		// function for the same particle
		String viewTitleQuery = "";
		if (function.getId() == null) {
			viewTitleQuery = "select count(function.identificationName) from Nanoparticle particle join particle.functionCollection function where particle.name='"
					+ particleName
					+ "' and particle.type='"
					+ particleType
					+ "' and function.identificationName='"
					+ function.getIdentificationName()
					+ "' and function.type='" + function.getType() + "'";
		} else {
			viewTitleQuery = "select count(function.identificationName) from Nanoparticle particle join particle.functionCollection function where particle.name='"
					+ particleName
					+ "' and particle.type='"
					+ particleType
					+ "' and function.identificationName='"
					+ function.getIdentificationName()
					+ "' and function.id!="
					+ function.getId()
					+ " and function.type='"
					+ function.getType() + "'";
		}
		List viewTitleResult = ida.search(viewTitleQuery);
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
	 * Load the file for the given fileId from the database
	 * 
	 * @param fileId
	 * @return
	 */
	public LabFileBean getFile(String fileId) throws Exception {
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		LabFileBean fileBean = null;
		try {
			ida.open();

			LabFile file = (LabFile) ida.load(LabFile.class, StringUtils
					.convertToLong(fileId));
			fileBean = new LabFileBean(file);
		} catch (Exception e) {
			e.printStackTrace();
			ida.rollback();
			logger.error("Problem getting file with file ID: " + fileId);
			throw e;
		} finally {
			ida.close();
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
	 * Load the derived data file for the given fileId from the database
	 * 
	 * @param fileId
	 * @return
	 */
	public DerivedBioAssayDataBean getDerivedBioAssayData(String fileId)
			throws Exception {
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		DerivedBioAssayDataBean fileBean = null;
		try {
			ida.open();

			DerivedBioAssayData file = (DerivedBioAssayData) ida.load(
					DerivedBioAssayData.class, StringUtils
							.convertToLong(fileId));
			// load keywords
			file.getKeywordCollection();
			fileBean = new DerivedBioAssayDataBean(file,
					CaNanoLabConstants.OUTPUT);
		} catch (Exception e) {
			e.printStackTrace();
			ida.rollback();
			logger.error("Problem getting file with file ID: " + fileId);
			throw e;
		} finally {
			ida.close();
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
	 * Get the list of all run output files associated with a particle
	 * 
	 * @param particleName
	 * @return
	 * @throws Exception
	 */
	public List<LabFileBean> getAllRunFiles(String particleName)
			throws Exception {
		List<LabFileBean> runFiles = new ArrayList<LabFileBean>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String query = "select distinct outFile from Run run join run.outputFileCollection outFile join run.runSampleContainerCollection runContainer where runContainer.sampleContainer.sample.name='"
					+ particleName + "'";
			List results = ida.search(query);

			for (Object obj : results) {
				OutputFile file = (OutputFile) obj;
				// active status only
				if (file.getDataStatus() == null) {
					LabFileBean fileBean = new LabFileBean();
					fileBean.setId(file.getId().toString());
					fileBean.setName(file.getFilename());
					fileBean.setUri(file.getUri());
					runFiles.add(fileBean);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ida.rollback();
			logger.error("Problem getting run files for particle: "
					+ particleName);
			throw e;
		} finally {
			ida.close();
		}
		return runFiles;
	}

	/**
	 * Update the meta data associated with a file stored in the database
	 * 
	 * @param fileBean
	 * @throws Exception
	 */
	public void updateFileMetaData(LabFileBean fileBean) throws Exception {

		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			LabFile file = (LabFile) ida.load(LabFile.class, StringUtils
					.convertToLong(fileBean.getId()));

			file.setTitle(fileBean.getTitle().toUpperCase());
			file.setDescription(fileBean.getDescription());
			file.setComments(fileBean.getComments());
		} catch (Exception e) {
			e.printStackTrace();
			ida.rollback();
			logger.error("Problem updating file meta data: ");
			throw e;
		} finally {
			ida.close();
		}

		userService.setVisiblity(fileBean.getId(), fileBean
				.getVisibilityGroups());
	}

	/**
	 * Update the meta data associated with a file stored in the database
	 * 
	 * @param fileBean
	 * @throws Exception
	 */
	public void updateDerivedBioAssayDataMetaData(
			DerivedBioAssayDataBean fileBean) throws Exception {

		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			DerivedBioAssayData file = (DerivedBioAssayData) ida.load(
					DerivedBioAssayData.class, StringUtils
							.convertToLong(fileBean.getId()));

			file.setTitle(fileBean.getTitle().toUpperCase());
			file.setDescription(fileBean.getDescription());
			file.getKeywordCollection().clear();
			if (fileBean.getKeywords() != null) {
				for (String keyword : fileBean.getKeywords()) {
					Keyword keywordObj = new Keyword();
					keywordObj.setName(keyword);
					file.getKeywordCollection().add(keywordObj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ida.rollback();
			logger.error("Problem updating derived data file meta data: ");
			throw e;
		} finally {
			ida.close();
		}
		userService.setVisiblity(fileBean.getId(), fileBean
				.getVisibilityGroups());
	}

	/**
	 * Delete the characterization
	 */
	public void deleteCharacterization(String strCharId) throws Exception {
		// if ID is not set save to the database otherwise update
		HibernateDataAccess ida = (HibernateDataAccess) (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();

			// Get ID
			Long charId = Long.parseLong(strCharId);

			Object charObj = ida.load(Characterization.class, charId);

			ida.delete(charObj);

		} catch (Exception e) {
			e.printStackTrace();
			ida.rollback();
			logger.error("Problem saving characterization: ");
			throw e;
		} finally {
			ida.close();
		}
	}

	/**
	 * Delete the characterizations
	 */
	public void deleteCharacterizations(String particleName,
			String particleType, String[] charIds) throws Exception {
		// if ID is not set save to the database otherwise update
		HibernateDataAccess ida = (HibernateDataAccess) (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			// Get ID
			for (String strCharId : charIds) {

				Long charId = Long.parseLong(strCharId);

				Object charObj = ida.load(Characterization.class, charId);
				// deassociate first
				String hqlString = "from Nanoparticle particle where particle.characterizationCollection.id = '"
						+ strCharId + "'";
				List results = ida.search(hqlString);
				for (Object obj : results) {
					Nanoparticle particle = (Nanoparticle) obj;
					particle.getCharacterizationCollection().remove(charObj);
				}
				// then delete
				ida.delete(charObj);

			}
		} catch (Exception e) {
			e.printStackTrace();
			ida.rollback();
			logger.error("Problem deleting characterization: ");
			throw new Exception(
					"The characterization is no longer exist in the database, please login again to refresh the view.");
		} finally {
			ida.close();
		}
	}
}
