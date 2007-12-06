package gov.nih.nci.calab.service.particle;

import gov.nih.nci.calab.db.HibernateUtil;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.CarbonNanotubeComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.ComplexComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.DendrimerComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.EmulsionComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.FullereneComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.LiposomeComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.MetalParticleComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.ParticleComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.PolymerComposition;
import gov.nih.nci.calab.domain.nano.characterization.physical.composition.QuantumDotComposition;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;
import gov.nih.nci.calab.dto.characterization.composition.CarbonNanotubeBean;
import gov.nih.nci.calab.dto.characterization.composition.CompositionBean;
import gov.nih.nci.calab.dto.characterization.composition.DendrimerBean;
import gov.nih.nci.calab.dto.characterization.composition.EmulsionBean;
import gov.nih.nci.calab.dto.characterization.composition.FullereneBean;
import gov.nih.nci.calab.dto.characterization.composition.LiposomeBean;
import gov.nih.nci.calab.dto.characterization.composition.PolymerBean;
import gov.nih.nci.calab.exception.ParticleCompositionException;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.Session;

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

	public CompositionBean getCompositionBy(String compositionId)
			throws ParticleCompositionException {

		CompositionBean comp = null;
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			// ParticleComposition doComp = (ParticleComposition) session.load(
			// ParticleComposition.class, new Long(compositionId));
			List results = session.createQuery(
					"from ParticleComposition where id=" + compositionId)
					.list();
			ParticleComposition doComp = null;
			for (Object obj : results) {
				doComp = (ParticleComposition) obj;
			}
			if (doComp == null) {
				return null;
			}
			if (doComp instanceof DendrimerComposition) {
				comp = new DendrimerBean((DendrimerComposition) doComp);
			} else if (doComp instanceof PolymerComposition) {
				comp = new PolymerBean((PolymerComposition) doComp);
			} else if (doComp instanceof LiposomeComposition) {
				comp = new LiposomeBean((LiposomeComposition) doComp);
			} else if (doComp instanceof FullereneComposition) {
				comp = new FullereneBean((FullereneComposition) doComp);
			} else if (doComp instanceof CarbonNanotubeComposition) {
				comp = new CarbonNanotubeBean(
						(CarbonNanotubeComposition) doComp);
			} else if (doComp instanceof EmulsionComposition) {
				comp = new EmulsionBean((EmulsionComposition) doComp);
			} else {
				comp = new CompositionBean(doComp);
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem finding composition by ID " + compositionId,
					e);
			throw new ParticleCompositionException();
		} finally {
			HibernateUtil.closeSession();
		}
		return comp;
	}

	// this would be replaced when composition model is separated from
	// characterization model
	public List<CompositionBean> getCompositionInfo(String particleId)
			throws ParticleCompositionException {
		List<CompositionBean> compBeans = new ArrayList<CompositionBean>();
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			List results = session
					.createQuery(
							"select chara.id, chara.name, chara.identificationName from Nanoparticle particle join particle.characterizationCollection chara where particle.id="
									+ particleId
									+ " and chara.name='Composition' "
									+ " order by chara.identificationName")
					.list();
			for (Object obj : results) {
				String charId = ((Object[]) obj)[0].toString();
				String charName = (String) (((Object[]) obj)[1]);
				String viewTitle = (String) (((Object[]) obj)[2]);
				CompositionBean compBean = new CompositionBean(charId,
						charName, viewTitle);
				compBeans.add(compBean);
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem finding characterization info for particle: "
					+ particleId, e);
			throw new ParticleCompositionException();
		} finally {
			HibernateUtil.closeSession();
		}
		return compBeans;
	}

	/*
	 * check if viewTitle is already used the same type of composition for the
	 * same particle
	 */
	private boolean isCompositionViewTitleUsed(Session session,
			ParticleComposition comp, CompositionBean compBean) {
		String viewTitleQuery = "";
		if (compBean.getId() == null) {
			viewTitleQuery = "select count(achar.identificationName) from Nanoparticle particle join particle.characterizationCollection achar where particle.name='"
					+ compBean.getParticle().getSampleName()
					+ "' and particle.type='"
					+ compBean.getParticle().getSampleType()
					+ "' and achar.identificationName='"
					+ compBean.getViewTitle()
					+ "' and achar.name='"
					+ comp.getName() + "'";
		} else {
			viewTitleQuery = "select count(achar.identificationName) from Nanoparticle particle join particle.characterizationCollection achar where particle.name='"
					+ compBean.getParticle().getSampleName()
					+ "' and particle.type='"
					+ compBean.getParticle().getSampleType()
					+ "' and achar.identificationName='"
					+ compBean.getViewTitle()
					+ "' and achar.name='"
					+ comp.getName() + "' and achar.id!=" + compBean.getId();
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
	 * Saves the particle composition to the database
	 * 
	 * @param composition
	 * @param compositionType
	 * @throws ParticleCompositionException
	 */
	public void addParticleComposition(CompositionBean composition,
			String compositionType) throws ParticleCompositionException {
		ParticleComposition doComp = new ParticleComposition();
		if (compositionType
				.equals(CaNanoLabConstants.COMPOSITION_COMPLEX_PARTICLE_TYPE)) {
			doComp = new ComplexComposition();
		} else if (compositionType
				.equals(CaNanoLabConstants.COMPOSITION_METAL_PARTICLE_TYPE)) {
			doComp = new MetalParticleComposition();
		} else if (compositionType
				.equals(CaNanoLabConstants.COMPOSITION_QUANTUM_DOT_TYPE)) {
			doComp = new QuantumDotComposition();
		} else if (compositionType
				.equals(CaNanoLabConstants.COMPOSITION_CARBON_NANOTUBE_TYPE)) {
			doComp = new CarbonNanotubeComposition();
		} else if (compositionType
				.equals(CaNanoLabConstants.COMPOSITION_DENDRIMER_TYPE)) {
			doComp = new DendrimerComposition();
		} else if (compositionType
				.equals(CaNanoLabConstants.COMPOSITION_EMULSION_TYPE)) {
			doComp = new EmulsionComposition();
		} else if (compositionType
				.equals(CaNanoLabConstants.COMPOSITION_FULLERENE_TYPE)) {
			doComp = new FullereneComposition();
		} else if (compositionType
				.equals(CaNanoLabConstants.COMPOSITION_LIPOSOME_TYPE)) {
			doComp = new LiposomeComposition();
		} else if (compositionType
				.equals(CaNanoLabConstants.COMPOSITION_POLYMER_TYPE)) {
			doComp = new PolymerComposition();
		}
		// if ID is not set save to the database otherwise update
		Nanoparticle particle = null;
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();

			// check if viewTitle is already used the same type of
			// characterization for the same particle
			boolean viewTitleUsed = isCompositionViewTitleUsed(session, doComp,
					composition);
			if (viewTitleUsed) {
				throw new RuntimeException(
						"The view title is already in use.  Please enter a different one.");
			} else {
				// if ID exists, load from database
				if (composition.getId() != null) {
					// check if ID is still valid
					doComp = (ParticleComposition) session.get(
							ParticleComposition.class, new Long(composition
									.getId()));
					if (doComp == null)
						throw new Exception(
								"This composition is no longer in the database.  Please log in again to refresh.");
				}
				// update domain object
				if (doComp instanceof DendrimerComposition) {
					((DendrimerBean) composition)
							.updateDomainObj((DendrimerComposition) doComp);
				} else if (doComp instanceof CarbonNanotubeComposition) {
					((CarbonNanotubeBean) composition)
							.updateDomainObj((CarbonNanotubeComposition) doComp);
				} else if (doComp instanceof EmulsionComposition) {
					((EmulsionBean) composition)
							.updateDomainObj((EmulsionComposition) doComp);
				} else if (doComp instanceof FullereneComposition) {
					((FullereneBean) composition)
							.updateDomainObj((FullereneComposition) doComp);
				} else if (doComp instanceof LiposomeComposition) {
					((LiposomeBean) composition)
							.updateDomainObj((LiposomeComposition) doComp);
				} else if (doComp instanceof PolymerComposition) {
					((PolymerBean) composition)
							.updateDomainObj((PolymerComposition) doComp);
				} else {
					composition.updateDomainObj(doComp);
				}

				if (composition.getId() == null) {
					List results = session
							.createQuery(
									"from Nanoparticle particle left join fetch particle.characterizationCollection where particle.name='"
											+ composition.getParticle()
													.getSampleName()
											+ "' and particle.type='"
											+ composition.getParticle()
													.getSampleType() + "'")
							.list();

					for (Object obj : results) {
						particle = (Nanoparticle) obj;
					}

					if (particle != null) {
						particle.getCharacterizationCollection().add(doComp);
					}
				}
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem saving characterization. ", e);
			HibernateUtil.rollbackTransaction();
			throw new ParticleCompositionException();
		} finally {
			HibernateUtil.closeSession();
		}
	}

	public SortedSet<String> getAllDendrimerBranches()
			throws ParticleCompositionException {
		SortedSet<String> branches = new TreeSet<String>();

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select distinct dendrimer.branch from DendrimerComposition dendrimer where dendrimer.branch is not null";
			List results = session.createQuery(hqlString).list();
			HibernateUtil.commitTransaction();
			for (Object obj : results) {
				branches.add((String) obj);
			}

		} catch (Exception e) {
			logger.error("Problem to retrieve all Dendrimer Branches.", e);
			throw new ParticleCompositionException(
					"Problem to retrieve all Dendrimer Branches. ");
		} finally {
			HibernateUtil.closeSession();
		}
		branches.addAll(Arrays
				.asList(CaNanoLabConstants.DEFAULT_DENDRIMER_BRANCHES));

		return branches;
	}

	public SortedSet<String> getAllDendrimerGenerations()
			throws ParticleCompositionException {
		SortedSet<String> generations = new TreeSet<String>();

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select distinct dendrimer.generation from DendrimerComposition dendrimer where dendrimer.generation is not null ";
			List results = session.createQuery(hqlString).list();
			HibernateUtil.commitTransaction();
			for (Object obj : results) {
				generations.add(obj.toString());
			}

		} catch (Exception e) {
			logger.error("Problem to retrieve all Dendrimer Generations.", e);
			throw new ParticleCompositionException(
					"Problem to retrieve all Dendrimer Generations. ");
		} finally {
			HibernateUtil.closeSession();
		}
		generations.addAll(Arrays
				.asList(CaNanoLabConstants.DEFAULT_DENDRIMER_GENERATIONS));

		return generations;
	}

	public String[] getAllMetalCompositions() {
		String[] compositions = new String[] { "Gold", "Sliver", "Iron oxide" };
		return compositions;
	}

	public SortedSet<String> getAllPolymerInitiators()
			throws ParticleCompositionException {
		SortedSet<String> initiators = new TreeSet<String>();

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select distinct polymer.initiator from PolymerComposition polymer where polymer.initiator is not null ";
			List results = session.createQuery(hqlString).list();
			HibernateUtil.commitTransaction();
			for (Object obj : results) {
				initiators.add((String) obj);
			}

		} catch (Exception e) {
			logger.error("Problem to retrieve all Polymer Initiator.", e);
			throw new ParticleCompositionException(
					"Problem to retrieve all Polymer Initiator. ");
		} finally {
			HibernateUtil.closeSession();
		}
		initiators.addAll(Arrays
				.asList(CaNanoLabConstants.DEFAULT_POLYMER_INITIATORS));

		return initiators;
	}

	public SortedSet<String> getAllDendrimerCores() {
		SortedSet<String> cores = new TreeSet<String>();
		cores.add("Diamine");
		cores.add("Ethyline");
		return cores;
	}

	public SortedSet<String> getAllDendrimerSurfaceGroupNames()
			throws ParticleCompositionException {
		SortedSet<String> names = new TreeSet<String>();

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			String hqlString = "select distinct name from SurfaceGroupType";
			List results = session.createQuery(hqlString).list();
			HibernateUtil.commitTransaction();
			for (Object obj : results) {
				names.add((String) obj);
			}

		} catch (Exception e) {
			logger.error("Problem to retrieve all Surface Group name.", e);
			throw new ParticleCompositionException(
					"Problem to retrieve all Surface Group name. ");
		} finally {
			HibernateUtil.closeSession();
		}
		return names;
	}
}
