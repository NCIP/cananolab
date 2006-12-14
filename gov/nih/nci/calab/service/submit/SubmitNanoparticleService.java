package gov.nih.nci.calab.service.submit;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.AssociatedFile;
import gov.nih.nci.calab.domain.DerivedDataFile;
import gov.nih.nci.calab.domain.InstrumentType;
import gov.nih.nci.calab.domain.Keyword;
import gov.nih.nci.calab.domain.LabFile;
import gov.nih.nci.calab.domain.Manufacturer;
import gov.nih.nci.calab.domain.OutputFile;
import gov.nih.nci.calab.domain.Report;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.function.Agent;
import gov.nih.nci.calab.domain.nano.function.AgentTarget;
import gov.nih.nci.calab.domain.nano.function.Attachment;
import gov.nih.nci.calab.domain.nano.function.Encapsulation;
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
import gov.nih.nci.calab.dto.characterization.invitro.PlateAggregationBean;
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
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.CananoConstants;
import gov.nih.nci.calab.service.util.PropertyReader;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.service.util.file.HttpFileUploadSessionData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;

/**
 * This class includes service calls involved in creating nanoparticles and
 * adding functions and characterizations for nanoparticles, as well as
 * creating reports.
 * 
 * @author pansu
 * 
 */
public class SubmitNanoparticleService {
	private static Logger logger = Logger
			.getLogger(SubmitNanoparticleService.class);

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
	public void createNanoparticle(String particleType, String particleName,
			String[] keywords, String[] visibilities) throws Exception {

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

		// remove existing visiblities for the nanoparticle
		UserService userService = new UserService(CalabConstants.CSM_APP_NAME);
		List<String> currentVisibilities = userService.getAccessibleGroups(
				particleName, CalabConstants.CSM_READ_ROLE);
		for (String visiblity : currentVisibilities) {
			userService.removeAccessibleGroup(particleName, visiblity,
					CalabConstants.CSM_READ_ROLE);
		}
		// set new visibilities for the nanoparticle
		// by default, always set visibility to NCL_PI and NCL_Researcher to
		// be true
		for (String defaultGroup : CananoConstants.DEFAULT_VISIBLE_GROUPS) {
			userService.secureObject(particleName, defaultGroup,
					CalabConstants.CSM_READ_ROLE);
		}

		if (visibilities != null) {
			for (String visibility : visibilities) {
				userService.secureObject(particleName, visibility,
						CalabConstants.CSM_READ_ROLE);
			}
		}

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
				InstrumentType iType = achar.getInstrument().getInstrumentType();
				if ( manuf != null && !manuf.getName().equals("") &&
					 iType != null && !iType.getName().equals("") ) {
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
			}

			if (achar.getCharacterizationProtocol() != null) {
				ida.store(achar.getCharacterizationProtocol());
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
	 * Save Instrument to the database.
	 * 
	 * @param particleType
	 * @param particleName
	 * @param achar
	 * @throws Exception
	 */
	/*
	 * private Instrument addInstrument(Instrument instrument) throws Exception {
	 * Instrument rInstrument = null; // if ID is not set save to the database
	 * otherwise update IDataAccess ida = (new DataAccessProxy())
	 * .getInstance(IDataAccess.HIBERNATE);
	 * 
	 * //int existingInstrumentCount = -1; Instrument existingInstrument = null;
	 * try { ida.open(); // check if instrument is already existed String
	 * viewQuery = ""; if (instrument.getId() == null) { viewQuery = "select
	 * instrument from Instrument instrument where instrument.type='" +
	 * instrument.getType() + "' and instrument.manufacturer='" +
	 * instrument.getManufacturer() + "'"; } else { viewQuery = "select
	 * instrument from Instrument instrument where instrument.type='" +
	 * instrument.getType() + "' and instrument.manufacturer='" +
	 * instrument.getManufacturer() + "' and instrument.id!=" +
	 * instrument.getId(); } List viewTitleResult = ida.search(viewQuery);
	 * 
	 * for (Object obj : viewTitleResult) { existingInstrument = (Instrument)
	 * obj; } if (existingInstrument == null) { ida.store(instrument);
	 * rInstrument = instrument; } else { rInstrument = existingInstrument; } }
	 * catch (Exception e) { e.printStackTrace(); ida.rollback();
	 * logger.error("Problem saving characterization: "); throw e; } finally {
	 * ida.close(); } return rInstrument; }
	 */
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
	 * @param plateAggregation
	 * @throws Exception
	 */
	public void addPlateAggregation(String particleType, String particleName,
			PlateAggregationBean plateAggregation) throws Exception {
		Characterization doPlateAggregation = plateAggregation.getDomainObj();
		// TODO think about how to deal with characterization file.
		addParticleCharacterization(particleType, particleName,
				doPlateAggregation);
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
	 * @param file
	 * @param title
	 * @param description
	 * @param comments
	 * @param keywords
	 * @param visibilities
	 */
	public LabFileBean saveCharacterizationFile(
			String particleName, FormFile file, String title,
			String description, String comments, String[] keywords,
			String[] visibilities, String path, String fileNumber,
			String rootPath) throws Exception {

		// TODO saves file to the file system
		HttpFileUploadSessionData sData = new HttpFileUploadSessionData();
		String tagFileName = sData.getTimeStamp() + "_" + file.getFileName();
		String outputFilename = rootPath + path + tagFileName;

		FileOutputStream oStream = new FileOutputStream(
				new File(outputFilename));

		this.saveFile(file.getInputStream(), oStream);

		DerivedDataFile dataFile = new DerivedDataFile();
		dataFile.setDescription(description);
		dataFile.setFilename(file.getFileName());
		if (keywords != null && keywords.length > 0) {
			for (int i = 0; i < keywords.length; i++) {
				Keyword keyword = new Keyword();
				keyword.setName(keywords[i]);
				dataFile.getKeywordCollection().add(keyword);
			}
		}

		// TODO need to remove the predefine the root path from outputFilename
		dataFile.setPath(path + tagFileName);
		dataFile.setTitle(title);

		// TODO saves file to the database
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
		LabFileBean fileBean = new LabFileBean(
				dataFile);

		UserService userService = new UserService(CalabConstants.CSM_APP_NAME);

		if (visibilities != null) {
			for (String visibility : visibilities) {
				// by default, always set visibility to NCL_PI and
				// NCL_Researcher to
				// be true
				// TODO once the files is successfully saved, use fileId instead
				// of fileName				
				userService.secureObject(fileBean.getId(), visibility,
						CalabConstants.CSM_READ_ROLE);
			}
			for (String defaultGroup : CananoConstants.DEFAULT_VISIBLE_GROUPS) {
				userService.secureObject(fileBean.getId(), defaultGroup,
						CalabConstants.CSM_READ_ROLE);
			}
		}
		return fileBean;
	}

	/**
	 * Save the characterization file into the database and file system The file
	 * is a workflow output file
	 * 
	 * @param fileId
	 * @param title
	 * @param description
	 * @param keywords
	 * @param visibilities
	 */
	public LabFileBean saveCharacterizationFile(String fileId,
			String title, String description, String[] keywords,
			String[] visibilities) throws Exception {

		LabFileBean fileBean = getFile(fileId);
		fileBean.setTitle(title);
		fileBean.setDescription(description);

		DerivedDataFile dataFile = fileBean.getDomainObject();
		// Retrieve all existing keywords
		Collection<String> words = new ArrayList<String>();
		for (Keyword keyword : dataFile.getKeywordCollection()) {
			words.add(keyword.getName());
		}
		// only add the new keyword
		if (keywords != null && keywords.length > 0) {
			for (int i = 0; i < keywords.length; i++) {
				if (!words.contains(keywords[i])) {
					Keyword keyword = new Keyword();
					keyword.setName(keywords[i]);
					dataFile.getKeywordCollection().add(keyword);
				}
			}
		}
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

		UserService userService = new UserService(CalabConstants.CSM_APP_NAME);
		fileBean = new LabFileBean(dataFile);
		if (visibilities != null) {
			for (String visibility : visibilities) {
				// by default, always set visibility to NCL_PI and
				// NCL_Researcher to
				// be true
				// TODO once the files is successfully saved, use fileId instead
				// of fileName				
				userService.secureObject(fileBean.getId(), visibility,
						CalabConstants.CSM_READ_ROLE);
			}
			for (String defaultGroup : CananoConstants.DEFAULT_VISIBLE_GROUPS) {
				userService.secureObject(fileBean.getId(), defaultGroup,
						CalabConstants.CSM_READ_ROLE);
			}
		}
		return fileBean;
	}

	public void saveFile(InputStream is, FileOutputStream os) {
		byte[] bytes = new byte[32768];

		try {
			int numRead = 0;
			while ((numRead = is.read(bytes)) > 0) {
				os.write(bytes, 0, numRead);
			}
			os.close();

		} catch (Exception e) {

		}
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
			// Have to seperate this section out in a different hibernate session.
			// check linkage id object type
			ida.open();
			if (doFunction.getLinkageCollection() != null) {
				for (Linkage linkage : doFunction.getLinkageCollection()) {
					// check linkage id object type
					if (linkage.getId() != null) {
						List result = ida.search("from Linkage linkage where linkage.id = " + linkage.getId());
						if (result!=null && result.size()>0){
							Linkage existingObj = (Linkage)result.get(0);
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
	public LabFileBean getFile(String fileId) throws Exception {
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		LabFileBean fileBean = null;
		try {
			ida.open();
			LabFile charFile = (LabFile) ida.load(LabFile.class, StringUtils
					.convertToLong(fileId));
			fileBean = new LabFileBean(charFile);
		} catch (Exception e) {
			e.printStackTrace();
			ida.rollback();
			logger.error("Problem getting file with file ID: " + fileId);
			throw e;
		} finally {
			ida.close();
		}
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
	
	public void createReport(String[] particleNames, String reportType,
			FormFile report, String title, String description, String comment,
			String[] visibilities) throws Exception {

		// TODO saves reportFile to the file system
		String rootPath = PropertyReader.getProperty(
				CalabConstants.FILEUPLOAD_PROPERTY, "fileRepositoryDir");
		if (rootPath.charAt(rootPath.length() - 1) == File.separatorChar)
			rootPath = rootPath.substring(0, rootPath.length() - 1);

		String path = File.separator + "reports" + File.separator;

		File pathDir = new File(rootPath + path);
		if (!pathDir.exists())
			pathDir.mkdirs();

		HttpFileUploadSessionData sData = new HttpFileUploadSessionData();
		String tagFileName = sData.getTimeStamp() + "_" + report.getFileName();
		String outputFilename = rootPath + path + tagFileName;

		FileOutputStream oStream = new FileOutputStream(
				new File(outputFilename));

		this.saveFile(report.getInputStream(), oStream);

		LabFile dataFile = null;
		if (reportType.equalsIgnoreCase(CananoConstants.NCL_REPORT))
			dataFile = new Report();
		else
			dataFile = new AssociatedFile();

		dataFile.setDescription(description);
		dataFile.setFilename(report.getFileName());

		dataFile.setPath(path + tagFileName);
		dataFile.setTitle(title.toUpperCase()); //convert to upper case
		Date date = new Date();
		dataFile.setCreatedDate(date);
		dataFile.setComments(comment);

		// TODO daves reportFile path to the database
		// look up the samples for each particleNames
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);

		try {
			ida.open();

			ida.store(dataFile);

		} catch (Exception e) {
			e.printStackTrace();
			ida.rollback();
			logger.error("Problem saving report File: ");
			throw e;
		} finally {
			ida.close();
		}

		Nanoparticle particle = null;

		for (String particleName : particleNames) {
			try {
				ida.open();

				List results = ida
						.search("select particle from Nanoparticle particle left join fetch particle.reportCollection where particle.name='"
								+ particleName + "'");

				for (Object obj : results) {
					particle = (Nanoparticle) obj;
				}

				if (particle != null) {
					if (reportType.equalsIgnoreCase(CananoConstants.NCL_REPORT))
						particle.getReportCollection().add((Report)dataFile);
					else
						particle.getAssociatedFileCollection().add((AssociatedFile)dataFile);
				}

			} catch (Exception e) {
				e.printStackTrace();
				ida.rollback();
				logger.error("Problem saving report File: ");
				throw e;
			} finally {
				ida.close();
			}

		}

		UserService userService = new UserService(CalabConstants.CSM_APP_NAME);
//		String fileName = report.getFileName();

		for (String visibility : visibilities) {
			// by default, always set visibility to NCL_PI and NCL_Researcher to
			// be true
			// TODO once the files is successfully saved, use fileId instead of
			// fileName
		
			userService.secureObject(dataFile.getId().toString(), visibility,
					CalabConstants.CSM_READ_ROLE);
		}
		for (String defaultGroup : CananoConstants.DEFAULT_VISIBLE_GROUPS) {
			userService.secureObject(dataFile.getId().toString(), defaultGroup,
					CalabConstants.CSM_READ_ROLE);
		}
	}

}
