package gov.nih.nci.calab.service.particle;

import gov.nih.nci.calab.db.HibernateUtil;
import gov.nih.nci.calab.domain.nano.function.Agent;
import gov.nih.nci.calab.domain.nano.function.Attachment;
import gov.nih.nci.calab.domain.nano.function.BondType;
import gov.nih.nci.calab.domain.nano.function.Function;
import gov.nih.nci.calab.domain.nano.function.ImageContrastAgent;
import gov.nih.nci.calab.domain.nano.function.ImageContrastAgentType;
import gov.nih.nci.calab.domain.nano.function.Linkage;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;
import gov.nih.nci.calab.dto.function.FunctionBean;
import gov.nih.nci.calab.exception.ParticleFunctionException;
import gov.nih.nci.calab.service.common.LookupService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 * This class includes service calls involved in creating and searching
 * nanoparticle functions
 * 
 * @author pansu
 * 
 */
public class NanoparticleFunctionService {
	private static Logger logger = Logger
			.getLogger(NanoparticleFunctionService.class);

	public NanoparticleFunctionService() {
	}

	public FunctionBean getFunctionBy(String funcId)
			throws ParticleFunctionException {
		FunctionBean functionBean = null;
		try {

			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			Function func = (Function) session.load(Function.class, new Long(
					funcId));
			if (func != null)
				functionBean = new FunctionBean(func);
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem finding functions", e);
			throw new ParticleFunctionException();
		} finally {
			HibernateUtil.closeSession();
		}
		return functionBean;
	}

	public Map<String, List<FunctionBean>> getFunctionInfo(String particleId)
			throws ParticleFunctionException {
		Map<String, List<FunctionBean>> funcTypeFuncs = new HashMap<String, List<FunctionBean>>();

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			List results = session
					.createQuery(
							"select func.id, func.type, func.identificationName from Nanoparticle particle join particle.functionCollection func where particle.id="
									+ particleId).list();
			List<FunctionBean> funcs = new ArrayList<FunctionBean>();
			for (Object obj : results) {
				String funcId = ((Object[]) obj)[0].toString();
				String funcType = ((Object[]) obj)[1].toString();
				String viewTitle = (String) (((Object[]) obj)[2]);
				FunctionBean funcBean = new FunctionBean(funcId, funcType,
						viewTitle);
				if (funcTypeFuncs.get(funcType) != null) {
					funcs = (funcTypeFuncs.get(funcType));
				} else {
					funcs = new ArrayList<FunctionBean>();
					funcTypeFuncs.put(funcType, funcs);
				}
				funcs.add(funcBean);
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem finding characterization info for particle: "
					+ particleId, e);
			throw new ParticleFunctionException();
		} finally {
			HibernateUtil.closeSession();
		}
		return funcTypeFuncs;
	}

	/**
	 * 
	 */
	public void addParticleFunction(String particleId, FunctionBean function)
			throws ParticleFunctionException {

		// if ID is not set save to the database otherwise update
		Function doFunction = new Function();
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();

			boolean viewTitleUsed = isFunctionViewTitleUsed(session,
					particleId, function);

			if (viewTitleUsed) {
				throw new RuntimeException(
						"The view title is already in use.  Please enter a different one.");
			} else {
				// if function already exists in the database, load it first
				if (function.getId() != null) {
					doFunction = (Function) session.get(Function.class,
							new Long(function.getId()));
					function.updateDomainObj(doFunction);
				} else {
					function.updateDomainObj(doFunction);
					Nanoparticle particle = (Nanoparticle) session.load(
							Nanoparticle.class, new Long(particleId));
					if (particle != null) {
						particle.getFunctionCollection().add(doFunction);
					}
				}
			}

			// persist bondType and image contrast agent type drop-down
			for (Linkage linkage : doFunction.getLinkageCollection()) {
				if (linkage instanceof Attachment) {
					String bondType = ((Attachment) linkage).getBondType();
					BondType lookup = new BondType();
					LookupService.addLookupType(session, lookup, bondType);
				}
				Agent agent = linkage.getAgent();
				if (agent instanceof ImageContrastAgent) {
					String agentType = ((ImageContrastAgent) agent).getType();
					ImageContrastAgentType lookup = new ImageContrastAgentType();
					LookupService.addLookupType(session, lookup, agentType);
				}
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			logger.error("Problem saving function: ", e);
			throw new ParticleFunctionException();
		} finally {
			HibernateUtil.closeSession();
		}
	}

	/*
	 * check if viewTitle is already used the same type of function for the same
	 * particle
	 */
	private boolean isFunctionViewTitleUsed(Session session, String particleId,
			FunctionBean function) {
		// check if viewTitle is already used the same type of
		// function for the same particle
		String viewTitleQuery = "";
		if (function.getId() == null || function.getId().length()==0) {
			viewTitleQuery = "select count(function.identificationName) from Nanoparticle particle join particle.functionCollection function where particle.id="
					+ particleId
					+ " and function.identificationName='"
					+ function.getViewTitle()
					+ "' and function.type='"
					+ function.getType() + "'";
		} else {
			viewTitleQuery = "select count(function.identificationName) from Nanoparticle particle join particle.functionCollection function where particle.id="
					+ particleId
					+ " and function.identificationName='"
					+ function.getViewTitle()
					+ "' and function.id!="
					+ function.getId()
					+ " and function.type='"
					+ function.getType() + "'";
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
}
