package gov.nih.nci.cananolab.service.common;

import gov.nih.nci.cananolab.dto.common.GridNodeBean;
import gov.nih.nci.cananolab.exception.GridAutoDiscoveryException;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
	private static Map<String, GridNodeBean> gridNodeMap = Collections
			.synchronizedMap(new HashMap<String, GridNodeBean>());

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
	 * @return the current grid node map if not empty, otherwise query
	 */
	public Map<String, GridNodeBean> getAllGridNodes() {
		if (gridNodeMap.isEmpty()) {
			populateAllServices();
		}
		return gridNodeMap;
	}

	public void populateAllServices() {
		Set<String> extantURLs = new HashSet<String>();
		if (gridNodeMap.isEmpty()) {
			// populate remote services but eliminate local duplicates
			gridNodeMap = populateNewServices(extantURLs);
		} else { // just update service
			// 1. extract existing grid addresses
			for (GridNodeBean grid : gridNodeMap.values()) {
				extantURLs.add(grid.getAddress());
			}
			// 2. populate remote services but eliminate existing duplicate
			// addresses
			Map<String, GridNodeBean> remoteNodes = populateNewServices(extantURLs);
			gridNodeMap.putAll(remoteNodes);
		}
	}

	/**
	 * @return map of all new grid services since last discovery
	 */
	private Map<String, GridNodeBean> populateNewServices(Set<String> extantURLs) {
		logger.debug("Refreshing Grid Nodes via discoverServices");
		Map<String, GridNodeBean> map = null;

		// auto-discover grid nodes and save in session
		try {
			map = GridService.discoverServices(
					CaNanoLabConstants.GRID_INDEX_SERVICE_URL,
					CaNanoLabConstants.DOMAIN_MODEL_NAME,
					CaNanoLabConstants.DOMAIN_MODEL_VERSION, extantURLs);
			System.out.println("refreshing discovery and found " + map.size()
					+ " services.");
		} catch (GridAutoDiscoveryException e) {
			String err = "Error in discovering grid nodes from the index server";
			logger.warn(err);
			map = null;
		}
		return map;
	}
}
