package gov.nih.nci.cananolab.ui.core;

import gov.nih.nci.cananolab.dto.common.GridNodeBean;
import gov.nih.nci.cananolab.service.common.GridService;
import gov.nih.nci.cananolab.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Borrowed and modified from the LSD browser project
 * 
 * @author pansu, sahnih
 * 
 */
public class GridDiscoveryServiceJob implements Job {
	private static Logger logger = Logger
			.getLogger(GridDiscoveryServiceJob.class.getName());
	private static List<GridNodeBean> gridNodes = Collections
			.synchronizedList(new ArrayList<GridNodeBean>());

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		populateAllServices();
	}

	/**
	 * @return the current grid node list if not empty, otherwise query
	 */
	public List<GridNodeBean> getAllGridNodes() {
		if (gridNodes.isEmpty()) {
			populateAllServices();
		}
		return gridNodes;
	}

	public void populateAllServices() {
		Set<String> extantURLs = new HashSet<String>();
		if (gridNodes.isEmpty()) {
			// populate remote services but eliminate local duplicates
			gridNodes = populateNewServices(extantURLs);
		} else { // just update service
			// 1. extract existing grid addresses
			for (GridNodeBean grid : gridNodes) {
				extantURLs.add(grid.getAddress());
			}
			// 2. populate remote services but eliminate existing duplicate
			// addresses
			List<GridNodeBean> remoteNodes = populateNewServices(extantURLs);
			if (remoteNodes.size()>0) {
				System.out.println("Found "+remoteNodes.size()+ " new grid service(s).");
			}
			gridNodes.addAll(remoteNodes);
		}
	}

	/**
	 * @return a list of all new grid services since last discovery
	 */
	private List<GridNodeBean> populateNewServices(Set<String> extantURLs) {
		logger.info("Discovering new grid nodes via scheduler...");

		List<GridNodeBean> nodes = GridService.discoverNewServices(
				Constants.GRID_INDEX_SERVICE_URL,
				Constants.DOMAIN_MODEL_NAME,
				Constants.DOMAIN_MODEL_VERSION, extantURLs);
		logger.info("Found " + nodes.size() + " new services.");
		return nodes;
	}
}
