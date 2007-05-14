package gov.nih.nci.calab.service.submit;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.DerivedDataFile;
import gov.nih.nci.calab.domain.InstrumentType;
import gov.nih.nci.calab.domain.Keyword;
import gov.nih.nci.calab.domain.LabFile;
import gov.nih.nci.calab.domain.Manufacturer;
import gov.nih.nci.calab.domain.OutputFile;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.function.Agent;
import gov.nih.nci.calab.domain.nano.function.AgentTarget;
import gov.nih.nci.calab.domain.nano.function.Function;
import gov.nih.nci.calab.domain.nano.function.Linkage;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;
import gov.nih.nci.calab.dto.characterization.composition.CompositionBean;
import gov.nih.nci.calab.dto.characterization.invitro.CFU_GMBean;
import gov.nih.nci.calab.dto.characterization.invitro.CYP450Bean;
import gov.nih.nci.calab.dto.characterization.invitro.Caspase3ActivationBean;
import gov.nih.nci.calab.dto.characterization.invitro.CellViabilityBean;
import gov.nih.nci.calab.dto.characterization.invitro.ChemotaxisBean;
import gov.nih.nci.calab.dto.characterization.invitro.CoagulationBean;
import gov.nih.nci.calab.dto.characterization.invitro.ComplementActivationBean;
import gov.nih.nci.calab.dto.characterization.invitro.CytokineInductionBean;
import gov.nih.nci.calab.dto.characterization.invitro.EnzymeInductionBean;
import gov.nih.nci.calab.dto.characterization.invitro.GlucuronidationSulphationBean;
import gov.nih.nci.calab.dto.characterization.invitro.HemolysisBean;
import gov.nih.nci.calab.dto.characterization.invitro.LeukocyteProliferationBean;
import gov.nih.nci.calab.dto.characterization.invitro.NKCellCytotoxicActivityBean;
import gov.nih.nci.calab.dto.characterization.invitro.OxidativeBurstBean;
import gov.nih.nci.calab.dto.characterization.invitro.OxidativeStressBean;
import gov.nih.nci.calab.dto.characterization.invitro.PhagocytosisBean;
import gov.nih.nci.calab.dto.characterization.invitro.PlasmaProteinBindingBean;
import gov.nih.nci.calab.dto.characterization.invitro.PlateletAggregationBean;
import gov.nih.nci.calab.dto.characterization.invitro.ROSBean;
import gov.nih.nci.calab.dto.characterization.physical.MolecularWeightBean;
import gov.nih.nci.calab.dto.characterization.physical.MorphologyBean;
import gov.nih.nci.calab.dto.characterization.physical.PurityBean;
import gov.nih.nci.calab.dto.characterization.physical.ShapeBean;
import gov.nih.nci.calab.dto.characterization.physical.SizeBean;
import gov.nih.nci.calab.dto.characterization.physical.SolubilityBean;
import gov.nih.nci.calab.dto.characterization.physical.StabilityBean;
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
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;

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
			String particleName, Characterization achar) throws Exception {

		// if ID is not set save to the database otherwise update
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);

		Nanoparticle particle = null;
		int existingViewTitleCount = -1;
		try {
			ida.open();

			/*
			 * if (achar.getInstrument() != null)
			 * ida.store(achar.getInstrument());
			 */

			if (achar.getInstrument() != null) {
				Manufacturer manuf = achar.getInstrument().getManufacturer();
				String manufacturerQuery = " from Manufacturer manufacturer where manufacturer.name = '"
						+ manuf.getName() + "'";
				List result = ida.search(manufacturerQuery);
				Manufacturer manufacturer = null;
				boolean newManufacturer = false;
				for (Object obj : result) {
					manufacturer = (Manufacturer) obj;
				}
				if (manufacturer == null) {
					newManufacturer = true;
					manufacturer = manuf;
					ida.store(manufacturer);
				}

				InstrumentType iType = achar.getInstrument()
						.getInstrumentType();
				String instrumentTypeQuery = " from InstrumentType instrumentType left join fetch instrumentType.manufacturerCollection where instrumentType.name = '"
						+ iType.getName() + "'";
				result = ida.search(instrumentTypeQuery);
				InstrumentType instrumentType = null;
				for (Object obj : result) {
					instrumentType = (InstrumentType) obj;
				}
				if (instrumentType == null) {
					instrumentType = iType;

					ida.createObject(instrumentType);

					HashSet<Manufacturer> manufacturers = new HashSet<Manufacturer>();
					manufacturers.add(manufacturer);
					instrumentType.setManufacturerCollection(manufacturers);
				} else {
					if (newManufacturer) {
						instrumentType.getManufacturerCollection().add(
								manufacturer);
					}
				}
				ida.store(instrumentType);

				achar.getInstrument().setInstrumentType(instrumentType);
				achar.getInstrument().setManufacturer(manufacturer);
				ida.store(achar.getInstrument());
			}

			if (achar.getProtocolFile() != null) {
				ida.store(achar.getProtocolFile());
			}

			// check if viewTitle is already used the same type of
			// characterization for the same particle
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

			for (Object obj : viewTitleResult) {
				existingViewTitleCount = ((Integer) (obj)).intValue();
			}
			if (existingViewTitleCount == 0) {
				// if ID exists, do update
				if (achar.getId() != null) {
					ida.store(achar);
				} else {// get the existing particle and compositions
					// from database
					// created
					// during sample
					// creation
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

		Characterization doComp = composition.getDomainObj();
		addParticleCharacterization(particleType, particleName, doComp);
	}

	/**
	 * Saves the size characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param size
	 * @throws Exception
	 */
	public void addParticleSize(String particleType, String particleName,
			SizeBean size) throws Exception {

		Characterization doSize = size.getDomainObj();
		// TODO think about how to deal with characterization file.
		/*
		 * if (doSize.getInstrument() != null) { Instrument instrument =
		 * addInstrument(doSize.getInstrument());
		 * doSize.setInstrument(instrument); }
		 */
		addParticleCharacterization(particleType, particleName, doSize);
	}

	/**
	 * Saves the size characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param size
	 * @throws Exception
	 */
	public void addParticleSurface(String particleType, String particleName,
			SurfaceBean surface) throws Exception {

		Characterization doSurface = surface.getDomainObj();
		// TODO think about how to deal with characterization file.
		/*
		 * if (doSize.getInstrument() != null) { Instrument instrument =
		 * addInstrument(doSize.getInstrument());
		 * doSize.setInstrument(instrument); }
		 */
		addParticleCharacterization(particleType, particleName, doSurface);
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
			String particleName, MolecularWeightBean molecularWeight)
			throws Exception {
		Characterization doMolecularWeight = molecularWeight.getDomainObj();
		// TODO think about how to deal with characterization file.
		/*
		 * if (doSize.getInstrument() != null) { Instrument instrument =
		 * addInstrument(doSize.getInstrument());
		 * doSize.setInstrument(instrument); }
		 */
		addParticleCharacterization(particleType, particleName,
				doMolecularWeight);
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
			MorphologyBean morphology) throws Exception {
		Characterization doMorphology = morphology.getDomainObj();
		// TODO think about how to deal with characterization file.
		/*
		 * if (doSize.getInstrument() != null) { Instrument instrument =
		 * addInstrument(doSize.getInstrument());
		 * doSize.setInstrument(instrument); }
		 */
		addParticleCharacterization(particleType, particleName, doMorphology);
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
			ShapeBean shape) throws Exception {
		Characterization doShape = shape.getDomainObj();
		// TODO think about how to deal with characterization file.
		addParticleCharacterization(particleType, particleName, doShape);
	}

	/**
	 * Saves the stability characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param stability
	 * @throws Exception
	 */
	public void addParticleStability(String particleType, String particleName,
			StabilityBean stability) throws Exception {
		Characterization doStability = stability.getDomainObj();
		// TODO think about how to deal with characterization file.
		addParticleCharacterization(particleType, particleName, doStability);
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
			PurityBean purity) throws Exception {
		Characterization doPurity = purity.getDomainObj();
		// TODO think about how to deal with characterization file.
		addParticleCharacterization(particleType, particleName, doPurity);
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
			SolubilityBean solubility) throws Exception {
		Characterization doSolubility = solubility.getDomainObj();
		// TODO think about how to deal with characterization file.
		addParticleCharacterization(particleType, particleName, doSolubility);
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
			HemolysisBean hemolysis) throws Exception {
		Characterization doHemolysis = hemolysis.getDomainObj();
		// TODO think about how to deal with characterization file.
		addParticleCharacterization(particleType, particleName, doHemolysis);
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
			CoagulationBean coagulation) throws Exception {
		Characterization doCoagulation = coagulation.getDomainObj();
		// TODO think about how to deal with characterization file.
		addParticleCharacterization(particleType, particleName, doCoagulation);
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
			String particleName, PlateletAggregationBean plateletAggregation)
			throws Exception {
		Characterization doPlateletAggregation = plateletAggregation
				.getDomainObj();
		// TODO think about how to deal with characterization file.
		addParticleCharacterization(particleType, particleName,
				doPlateletAggregation);
	}

	/**
	 * Saves the invitro Complement Activation characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param hemolysis
	 * @throws Exception
	 */
	public void addComplementActivation(String particleType,
			String particleName, ComplementActivationBean complementActivation)
			throws Exception {
		Characterization doComplementActivation = complementActivation
				.getDomainObj();
		// TODO think about how to deal with characterization file.
		addParticleCharacterization(particleType, particleName,
				doComplementActivation);
	}

	/**
	 * Saves the invitro chemotaxis characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param hemolysis
	 * @throws Exception
	 */
	public void addChemotaxis(String particleType, String particleName,
			ChemotaxisBean chemotaxis) throws Exception {
		Characterization doChemotaxis = chemotaxis.getDomainObj();
		// TODO think about how to deal with characterization file.
		addParticleCharacterization(particleType, particleName, doChemotaxis);
	}

	/**
	 * Saves the invitro NKCellCytotoxicActivity characterization to the
	 * database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param hemolysis
	 * @throws Exception
	 */
	public void addNKCellCytotoxicActivity(String particleType,
			String particleName,
			NKCellCytotoxicActivityBean nkCellCytotoxicActivity)
			throws Exception {
		Characterization doNKCellCytotoxicActivity = nkCellCytotoxicActivity
				.getDomainObj();
		// TODO think about how to deal with characterization file.
		addParticleCharacterization(particleType, particleName,
				doNKCellCytotoxicActivity);
	}

	/**
	 * Saves the invitro LeukocyteProliferation characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param hemolysis
	 * @throws Exception
	 */
	public void addLeukocyteProliferation(String particleType,
			String particleName,
			LeukocyteProliferationBean leukocyteProliferation) throws Exception {
		Characterization doLeukocyteProliferation = leukocyteProliferation
				.getDomainObj();
		// TODO think about how to deal with characterization file.
		addParticleCharacterization(particleType, particleName,
				doLeukocyteProliferation);
	}

	/**
	 * Saves the invitro CFU_GM characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param hemolysis
	 * @throws Exception
	 */
	public void addCFU_GM(String particleType, String particleName,
			CFU_GMBean cfu_gm) throws Exception {
		Characterization doCFU_GM = cfu_gm.getDomainObj();
		// TODO think about how to deal with characterization file.
		addParticleCharacterization(particleType, particleName, doCFU_GM);
	}

	/**
	 * Saves the invitro OxidativeBurst characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param hemolysis
	 * @throws Exception
	 */
	public void addOxidativeBurst(String particleType, String particleName,
			OxidativeBurstBean oxidativeBurst) throws Exception {
		Characterization doOxidativeBurst = oxidativeBurst.getDomainObj();
		// TODO think about how to deal with characterization file.
		addParticleCharacterization(particleType, particleName,
				doOxidativeBurst);
	}

	/**
	 * Saves the invitro Phagocytosis characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param hemolysis
	 * @throws Exception
	 */
	public void addPhagocytosis(String particleType, String particleName,
			PhagocytosisBean phagocytosis) throws Exception {
		Characterization doPhagocytosis = phagocytosis.getDomainObj();
		// TODO think about how to deal with characterization file.
		addParticleCharacterization(particleType, particleName, doPhagocytosis);
	}

	/**
	 * Saves the invitro CytokineInduction characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param hemolysis
	 * @throws Exception
	 */
	public void addCytokineInduction(String particleType, String particleName,
			CytokineInductionBean cytokineInduction) throws Exception {
		Characterization doCytokineInduction = cytokineInduction.getDomainObj();
		// TODO think about how to deal with characterization file.
		addParticleCharacterization(particleType, particleName,
				doCytokineInduction);
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
			PlasmaProteinBindingBean plasmaProteinBinding) throws Exception {
		Characterization doProteinBinding = plasmaProteinBinding.getDomainObj();
		// TODO think about how to deal with characterization file.
		addParticleCharacterization(particleType, particleName,
				doProteinBinding);
	}

	/**
	 * Saves the invitro CellViability binding characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param plasmaProteinBinding
	 * @throws Exception
	 */
	public void addCellViability(String particleType, String particleName,
			CellViabilityBean cellViability) throws Exception {
		Characterization doCellViability = cellViability.getDomainObj();
		// TODO think about how to deal with characterization file.
		addParticleCharacterization(particleType, particleName, doCellViability);
	}

	/**
	 * Saves the invitro EnzymeInduction binding characterization to the
	 * database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param plasmaProteinBinding
	 * @throws Exception
	 */
	public void addEnzymeInduction(String particleType, String particleName,
			EnzymeInductionBean enzymeInduction) throws Exception {
		Characterization EnzymeInduction = enzymeInduction.getDomainObj();
		// TODO think about how to deal with characterization file.
		addParticleCharacterization(particleType, particleName, EnzymeInduction);
	}

	/**
	 * Saves the invitro OxidativeStress characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param hemolysis
	 * @throws Exception
	 */
	public void addOxidativeStress(String particleType, String particleName,
			OxidativeStressBean oxidativeStress) throws Exception {
		Characterization doOxidativeStress = oxidativeStress.getDomainObj();
		// TODO think about how to deal with characterization file.
		addParticleCharacterization(particleType, particleName,
				doOxidativeStress);
	}

	/**
	 * Saves the invitro Caspase3Activation characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param hemolysis
	 * @throws Exception
	 */
	public void addCaspase3Activation(String particleType, String particleName,
			Caspase3ActivationBean caspase3Activation) throws Exception {
		Characterization doCaspase3Activation = caspase3Activation
				.getDomainObj();
		// TODO think about how to deal with characterization file.
		addParticleCharacterization(particleType, particleName,
				doCaspase3Activation);
	}

	/**
	 * Saves the invitro GlucuronidationSulphation characterization to the
	 * database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param hemolysis
	 * @throws Exception
	 */
	public void addGlucuronidationSulphation(String particleType,
			String particleName,
			GlucuronidationSulphationBean glucuronidationSulphation)
			throws Exception {
		Characterization doGlucuronidationSulphation = glucuronidationSulphation
				.getDomainObj();
		// TODO think about how to deal with characterization file.
		addParticleCharacterization(particleType, particleName,
				doGlucuronidationSulphation);
	}

	/**
	 * Saves the invitro CYP450 characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param hemolysis
	 * @throws Exception
	 */
	public void addCYP450(String particleType, String particleName,
			CYP450Bean cyp450) throws Exception {
		Characterization doCYP450 = cyp450.getDomainObj();
		// TODO think about how to deal with characterization file.
		addParticleCharacterization(particleType, particleName, doCYP450);
	}

	/**
	 * Saves the invitro ROS characterization to the database
	 * 
	 * @param particleType
	 * @param particleName
	 * @param hemolysis
	 * @throws Exception
	 */
	public void addROS(String particleType, String particleName, ROSBean ros)
			throws Exception {
		Characterization doROS = ros.getDomainObj();
		// TODO think about how to deal with characterization file.
		addParticleCharacterization(particleType, particleName, doROS);
	}

	/**
	 * Save the characterization file into the database and file system
	 * 
	 * @param particleName
	 * @param uploadedFile
	 * @param title
	 * @param description
	 * @param comments
	 * @param keywords
	 * @param visibilities
	 */
	public LabFileBean saveCharacterizationFile(String particleName,
			FormFile uploadedFile, String characterizationName,
			LabFileBean fileBean) throws Exception {

		// write file to the file system
		String rootPath = PropertyReader.getProperty(
				CaNanoLabConstants.FILEUPLOAD_PROPERTY, "fileRepositoryDir");
		// get rid of trailing file separator
		if (rootPath.charAt(rootPath.length() - 1) == File.separatorChar)
			rootPath = rootPath.substring(0, rootPath.length() - 1);

		// add charaterizationName to the path
		String filePath = File.separator + CaNanoLabConstants.FOLDER_PARTICLE
				+ File.separator + particleName + File.separator
				+ characterizationName;

		FileService fileService = new FileService();
		String fileName = fileService.writeUploadedFile(uploadedFile, rootPath
				+ filePath, true);

		DerivedDataFile dataFile = fileBean.getDomainObjectDerivedDataFile();
		// set file name, path and keywords for DerivedDataFile specific
		dataFile.setFilename(uploadedFile.getFileName());
		// TODO need to remove the predefine the root path from outputFilename
		dataFile.setPath(filePath + File.separator + fileName);

		// save file to the database
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			ida.store(dataFile);

		} catch (Exception e) {
			e.printStackTrace();
			ida.rollback();
			logger.error("Problem saving characterization File: ");
			throw e;
		} finally {
			ida.close();
		}
		LabFileBean savedFileBean = new LabFileBean(dataFile,
				CaNanoLabConstants.OUTPUT);
		userService.setVisiblity(savedFileBean.getId(), savedFileBean
				.getVisibilityGroups());
		return savedFileBean;
	}

	/**
	 * Save the characterization file into the database. The file is a workflow
	 * output file
	 * 
	 * @param fileId
	 * @param title
	 * @param description
	 * @param keywords
	 * @param visibilities
	 */
	public LabFileBean saveCharacterizationFile(LabFileBean fileBean)
			throws Exception {

		DerivedDataFile dataFile = fileBean.getDomainObjectDerivedDataFile();
		// TODO saves file to the database
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			ida.createObject(dataFile);
		} catch (Exception e) {
			e.printStackTrace();
			ida.rollback();
			logger.error("Problem saving characterization File: ");
			throw e;
		} finally {
			ida.close();
		}
		LabFileBean savedFileBean = new LabFileBean(dataFile,
				CaNanoLabConstants.OUTPUT);
		userService.setVisiblity(savedFileBean.getId(), savedFileBean
				.getVisibilityGroups());
		return savedFileBean;
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
			// check if viewTitle is already used the same type of
			// function for the same particle
			String viewTitleQuery = "";
			if (function.getId() == null) {
				viewTitleQuery = "select count(function.identificationName) from Nanoparticle particle join particle.functionCollection function where particle.name='"
						+ particleName
						+ "' and particle.type='"
						+ particleType
						+ "' and function.identificationName='"
						+ doFunction.getIdentificationName()
						+ "' and function.type='" + doFunction.getType() + "'";
			} else {
				viewTitleQuery = "select count(function.identificationName) from Nanoparticle particle join particle.functionCollection function where particle.name='"
						+ particleName
						+ "' and particle.type='"
						+ particleType
						+ "' and function.identificationName='"
						+ doFunction.getIdentificationName()
						+ "' and function.id!="
						+ function.getId()
						+ " and function.type='" + doFunction.getType() + "'";

			}
			List viewTitleResult = ida.search(viewTitleQuery);

			for (Object obj : viewTitleResult) {
				existingViewTitleCount = ((Integer) (obj)).intValue();
			}
			if (existingViewTitleCount == 0) {
				// if ID exists, do update
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

	/**
	 * Load the file for the given fileId from the database
	 * 
	 * @param fileId
	 * @return
	 */
	public LabFileBean getFile(String fileId, String fileType) throws Exception {
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		LabFileBean fileBean = null;
		try {
			ida.open();

			LabFile file = (LabFile) ida.load(LabFile.class, StringUtils
					.convertToLong(fileId));
			fileBean = new LabFileBean(file, fileType);
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
	public LabFileBean getDerivedDataFile(String fileId) throws Exception {
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		LabFileBean fileBean = null;
		try {
			ida.open();

			DerivedDataFile file = (DerivedDataFile) ida.load(
					DerivedDataFile.class, StringUtils.convertToLong(fileId));
			// load keywords
			file.getKeywordCollection();
			fileBean = new LabFileBean(file, CaNanoLabConstants.OUTPUT);
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
					fileBean.setPath(file.getPath());
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
	public void updateDerivedDataFileMetaData(LabFileBean fileBean)
			throws Exception {

		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			DerivedDataFile file = (DerivedDataFile) ida.load(
					DerivedDataFile.class, StringUtils.convertToLong(fileBean
							.getId()));

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
}
