package gov.nih.nci.calab.service.submit;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.InstrumentType;
import gov.nih.nci.calab.domain.Keyword;
import gov.nih.nci.calab.domain.Manufacturer;
import gov.nih.nci.calab.domain.OutputFile;
import gov.nih.nci.calab.domain.Run;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;
import gov.nih.nci.calab.dto.characterization.CharacterizationFileBean;
import gov.nih.nci.calab.dto.characterization.MolecularWeightBean;
import gov.nih.nci.calab.dto.characterization.MorphologyBean;
import gov.nih.nci.calab.dto.characterization.SizeBean;
import gov.nih.nci.calab.dto.characterization.composition.CompositionBean;
import gov.nih.nci.calab.dto.characterization.invitro.CoagulationBean;
import gov.nih.nci.calab.dto.characterization.invitro.HemolysisBean;
import gov.nih.nci.calab.dto.characterization.invitro.PlasmaProteinBindingBean;
import gov.nih.nci.calab.dto.characterization.invitro.PlateAggregationBean;
import gov.nih.nci.calab.dto.characterization.physical.SurfaceBean;
import gov.nih.nci.calab.exception.CalabException;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CalabConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;

/**
 * This class includes service calls involved in creating nanoparticles and
 * adding functions and characterizations for nanoparticles.
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
				particleName, "R");
		for (String visiblity : currentVisibilities) {
			userService.removeAccessibleGroup(particleName, visiblity, "R");
		}
		// set new visibilities for the nanoparticle
		// by default, always set visibility to NCL_PI and NCL_Researcher to
		// be true
		userService.secureObject(particleName, "NCL_PI", "R");
		userService.secureObject(particleName, "NCL_Researcher", "R");
		if (visibilities != null) {
			for (String visibility : visibilities) {
				userService.secureObject(particleName, visibility, "R");
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
			if (achar.getInstrument() != null)
				ida.store(achar.getInstrument());
			*/
			
			if (achar.getInstrument() != null) {
				Manufacturer manuf = achar.getInstrument().getManufacturer();
				String manufacturerQuery = " from Manufacturer manufacturer where manufacturer.name = '" + manuf.getName() + "'";
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
				
				
				InstrumentType iType = achar.getInstrument().getInstrumentType();
				String instrumentTypeQuery = " from InstrumentType instrumentType left join fetch instrumentType.manufacturerCollection where instrumentType.name = '" + iType.getName() + "'";
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
						instrumentType.getManufacturerCollection().add(manufacturer);
					}
				}
				ida.store(instrumentType);
					
					
				achar.getInstrument().setInstrumentType(instrumentType);
				achar.getInstrument().setManufacturer(manufacturer);
				ida.store(achar.getInstrument());
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
	private Instrument addInstrument(Instrument instrument) throws Exception {
		Instrument rInstrument = null;
		
		// if ID is not set save to the database otherwise update
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);

		//int existingInstrumentCount = -1;
		Instrument existingInstrument = null;
		try {
			ida.open();
			// check if instrument is already existed
			String viewQuery = "";
			if (instrument.getId() == null) {
				viewQuery = "select instrument from Instrument instrument where instrument.type='"
						+ instrument.getType()
						+ "' and instrument.manufacturer='"
						+ instrument.getManufacturer()
						+ "'";
			} else {
				viewQuery = "select instrument from Instrument instrument where instrument.type='"
						+ instrument.getType()
						+ "' and instrument.manufacturer='"
						+ instrument.getManufacturer()
						+ "' and instrument.id!=" + instrument.getId();
			}
			List viewTitleResult = ida.search(viewQuery);

			for (Object obj : viewTitleResult) {
				existingInstrument = (Instrument) obj;
			}
			if (existingInstrument == null) {
				ida.store(instrument);
				rInstrument = instrument;
			} else {
				rInstrument = existingInstrument;
			}
		} catch (Exception e) {
			e.printStackTrace();
			ida.rollback();
			logger.error("Problem saving characterization: ");
			throw e;
		} finally {
			ida.close();
		}
		return rInstrument;
	}
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
		if (doSize.getInstrument() != null) {
			Instrument instrument = addInstrument(doSize.getInstrument());
			doSize.setInstrument(instrument);
		}
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
		if (doSize.getInstrument() != null) {
			Instrument instrument = addInstrument(doSize.getInstrument());
			doSize.setInstrument(instrument);
		}
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
	public void addParticleMolecularWeight(String particleType, String particleName,
			MolecularWeightBean molecularWeight) throws Exception {
		Characterization doMolecularWeight = molecularWeight.getDomainObj();
		// TODO think about how to deal with characterization file.
		/*
		if (doSize.getInstrument() != null) {
			Instrument instrument = addInstrument(doSize.getInstrument());
			doSize.setInstrument(instrument);
		}
		*/
		addParticleCharacterization(particleType, particleName, doMolecularWeight);
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
		if (doSize.getInstrument() != null) {
			Instrument instrument = addInstrument(doSize.getInstrument());
			doSize.setInstrument(instrument);
		}
		*/
		addParticleCharacterization(particleType, particleName, doMorphology);
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
		addParticleCharacterization(particleType, particleName, doPlateAggregation);
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
		addParticleCharacterization(particleType, particleName, doProteinBinding);
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
	public CharacterizationFileBean saveCharacterizationFile(
			String particleName, FormFile file, String title,
			String description, String comments, String[] keywords,
			String[] visibilities,
			String path,
			String fileNumber) throws Exception {

		// TODO saves file to the file system
		String outputFilename = path + file.getFileName();
		FileOutputStream oStream = new FileOutputStream(new File(outputFilename));
		this.saveFile(file.getInputStream(), oStream);
		
		// TODO saves file to the database
		
		CharacterizationFileBean fileBean = new CharacterizationFileBean();
		fileBean.setName(file.getFileName());
		fileBean.setPath(path);
		fileBean.setId(fileNumber);
		fileBean.setVisibilityGroups(visibilities);
		UserService userService = new UserService(CalabConstants.CSM_APP_NAME);
		String fileName = fileBean.getName();
		if (visibilities != null) {
			for (String visibility : visibilities) {
				// by default, always set visibility to NCL_PI and
				// NCL_Researcher to
				// be true
				// TODO once the files is successfully saved, use fileId instead
				// of fileName
				userService.secureObject(fileName, "NCL_PI", "R");
				userService.secureObject(fileName, "NCL_Researcher", "R");
				userService.secureObject(fileName, visibility, "R");
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
	 * Load the file for the given fileId from the database
	 * 
	 * @param fileId
	 * @return
	 */
	public CharacterizationFileBean getFile(String fileId) throws Exception {
		// TODO query from database.
		CharacterizationFileBean fileBean = new CharacterizationFileBean();
		fileBean.setName("existing test file");
		fileBean.setId("2");
		return fileBean;
	}

	/**
	 * Get the list of all run output files associated with a particle 
	 * @param particleName
	 * @return
	 * @throws Exception
	 */
	public List<CharacterizationFileBean> getAllRunFiles(String particleName)
			throws Exception {
		List<CharacterizationFileBean> runFiles = new ArrayList<CharacterizationFileBean>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();
			String query = "from Run run join fetch run.outputFileCollection join run.runSampleContainerCollection runContainer where runContainer.sampleContainer.sample.name='"
					+ particleName + "'";
			List results = ida.search(query);

			for (Object obj : results) {
				Run run = (Run) obj;
				for (Object fileObj : run.getOutputFileCollection()) {
					OutputFile file = (OutputFile) fileObj;
					// active status only
					if (file.getDataStatus() == null) {
						CharacterizationFileBean fileBean = new CharacterizationFileBean();
						fileBean.setId(file.getId().toString());
						fileBean.setName(file.getFilename());
						fileBean.setPath(file.getPath());
						runFiles.add(fileBean);
					}
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
}
