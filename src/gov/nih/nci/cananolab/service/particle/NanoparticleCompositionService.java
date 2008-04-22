package gov.nih.nci.cananolab.service.particle;

import gov.nih.nci.cananolab.domain.particle.samplecomposition.Function;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.OtherFunction;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.SampleComposition;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.OtherNanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.OtherFunctionalizingEntity;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanoparticleEntityBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.ParticleCompositionException;
import gov.nih.nci.cananolab.exception.ParticleException;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;

/**
 * This class includes service calls involved in creating nanoparticle general
 * info and adding functions and characterizations for nanoparticles, as well as
 * creating reports.
 * 
 * @author pansu
 * 
 */
public class NanoparticleCompositionService {
	private static Logger logger = Logger
			.getLogger(NanoparticleCompositionService.class);

	public NanoparticleCompositionService() {
	}

	public void saveNanoparticleEntity(ParticleBean particleBean,
			NanoparticleEntityBean entityBean) throws Exception {

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		if (entityBean.getDomainEntity().getId() != null) {
			try {
				NanoparticleEntity dbEntity = (NanoparticleEntity) appService
						.load(NanoparticleEntity.class, entityBean
								.getDomainEntity().getId());
			} catch (Exception e) {
				String err = "Object doesn't exist in the database anymore.  Please log in again.";
				logger.error(err);
				throw new ParticleCompositionException(err, e);
			}
		}
		NanoparticleEntity entity = entityBean.getDomainEntity();

		SampleComposition composition = particleBean.getParticleSample()
				.getSampleComposition();
		if (composition == null) {
			composition = new SampleComposition();
			entity.setSampleComposition(composition);
			particleBean.getParticleSample().setSampleComposition(composition);
			Collection<NanoparticleEntity> entityCollection = new HashSet<NanoparticleEntity>();
			entityCollection.add(entity);
			composition.setNanoparticleEntityCollection(entityCollection);
			composition.setNanoparticleSample(particleBean.getParticleSample());
		} else {
			entity.setSampleComposition(composition);
		}
		appService.saveOrUpdate(entity);
		if (entity instanceof OtherNanoparticleEntity) {
			// save other entity type
		}
	}

	public NanoparticleEntityBean findNanoparticleEntityBy(String entityId,
			UserBean user) throws ParticleException, CaNanoLabSecurityException {
		NanoparticleEntityBean entityBean = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			DetachedCriteria crit = DetachedCriteria.forClass(
					NanoparticleEntity.class).add(
					Property.forName("id").eq(new Long(entityId)));
			crit.setFetchMode("labFileCollection", FetchMode.JOIN);
			List result = appService.query(crit);
			if (!result.isEmpty()) {
				NanoparticleEntity entity = (NanoparticleEntity) result.get(0);
				entityBean = new NanoparticleEntityBean(entity);
			}
			return entityBean;
		} catch (Exception e) {
			logger.error("Problem finding the nanoparticle entity by id: "
					+ entityId, e);
			throw new ParticleException();
		}
	}

	public void saveFunctionalizingEntity(ParticleBean particleBean,
			FunctionalizingEntityBean entityBean) throws Exception {

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		FunctionalizingEntity entity = null;
		if (entityBean.getDomainEntity().getId() != null) {
			try {
				FunctionalizingEntity dbEntity = (FunctionalizingEntity) appService
						.load(FunctionalizingEntity.class, entityBean
								.getDomainEntity().getId());
			} catch (Exception e) {
				String err = "Object doesn't exist in the database anymore.  Please log in again.";
				logger.error(err);
				throw new ParticleCompositionException(err, e);
			}
		} else {
			entity = entityBean.getDomainEntity();
		}
		SampleComposition composition = particleBean.getParticleSample()
				.getSampleComposition();
		if (composition != null) {
			entity.setSampleComposition(composition);
			composition.getFunctionalizingEntityCollection().add(entity);
		} else {
			composition = new SampleComposition();
			entity.setSampleComposition(composition);
			particleBean.getParticleSample().setSampleComposition(composition);
			Collection<FunctionalizingEntity> entityCollection = new HashSet<FunctionalizingEntity>();
			entityCollection.add(entity);
			composition.setFunctionalizingEntityCollection(entityCollection);
			composition.setNanoparticleSample(particleBean.getParticleSample());
		}
		appService.saveOrUpdate(entity);
	}

	public FunctionalizingEntityBean findFunctionalizingEntityBy(
			String entityId, UserBean user) throws ParticleException,
			CaNanoLabSecurityException {
		FunctionalizingEntityBean entityBean = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			DetachedCriteria crit = DetachedCriteria.forClass(
					FunctionalizingEntity.class).add(
					Property.forName("id").eq(new Long(entityId)));
			crit.setFetchMode("labFileCollection", FetchMode.JOIN);
			List result = appService.query(crit);
			if (!result.isEmpty()) {
				FunctionalizingEntity entity = (FunctionalizingEntity) result
						.get(0);
				entityBean = new FunctionalizingEntityBean(entity);
			}
			return entityBean;
		} catch (Exception e) {
			logger.error("Problem finding the functionalizing entity by id: "
					+ entityId, e);
			throw new ParticleException();
		}
	}

	/**
	 * Return user-defined functionalizing entity types
	 * 
	 * @return
	 * @throws ParticleException
	 */
	public SortedSet<String> getAllOtherFunctionalizingEntityTypes()
			throws ParticleException {
		SortedSet<String> types = new TreeSet<String>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			List results = appService.getAll(OtherFunctionalizingEntity.class);
			for (Object obj : results) {
				OtherFunctionalizingEntity other = (OtherFunctionalizingEntity) obj;
				types.add(other.getType());
			}
		} catch (Exception e) {
			logger
					.error(
							"Error in retrieving other values for functionalizing entity",
							e);
			throw new ParticleException();
		}
		return types;
	}

	/**
	 * Return user-defined function types
	 * 
	 * @return
	 * @throws ParticleException
	 */
	public SortedSet<String> getAllOtherFunctionTypes()
			throws ParticleException {
		SortedSet<String> types = new TreeSet<String>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			List results = appService.getAll(OtherFunction.class);
			for (Object obj : results) {
				OtherFunction other = (OtherFunction) obj;
				types.add(other.getType());
			}
		} catch (Exception e) {
			logger.error("Error in retrieving other function types", e);
			throw new ParticleException();
		}
		return types;
	}

	/**
	 * Return user-defined functionalizing entity types
	 * 
	 * @return
	 * @throws ParticleException
	 */
	public SortedSet<String> getAllOtherNanoparticleEntityTypes()
			throws ParticleException {
		SortedSet<String> types = new TreeSet<String>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			List results = appService.getAll(OtherNanoparticleEntity.class);
			for (Object obj : results) {
				OtherNanoparticleEntity other = (OtherNanoparticleEntity) obj;
				types.add(other.getType());
			}
		} catch (Exception e) {
			logger.error(
					"Error in retrieving other values for nanoparticle entity",
					e);
			throw new ParticleException();
		}
		return types;
	}

}
