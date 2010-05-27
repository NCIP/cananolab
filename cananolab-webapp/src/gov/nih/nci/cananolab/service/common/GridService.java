package gov.nih.nci.cananolab.service.common;

import gov.nih.nci.cananolab.dto.common.GridNodeBean;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * Grid service utils for grid node discovery and grid node lookup.
 * 
 * @author pansu
 * 
 */
public class GridService {
	private static Logger logger = Logger.getLogger(GridService.class);

	public static GridNodeBean getGridNodeByURL(List<GridNodeBean> gridNodes,
			String gridServiceURL) {
		GridNodeBean theNode = null;
		for (GridNodeBean node : gridNodes) {
			if (node.getAddress().equalsIgnoreCase(gridServiceURL)) {
				theNode = node;
				break;
			}
		}
		return theNode;
	}

	public static GridNodeBean getGridNodeByHostName(
			List<GridNodeBean> gridNodes, String gridServiceHostName) {
		GridNodeBean theNode = null;
		for (GridNodeBean node : gridNodes) {
			if (node.getHostName().equals(gridServiceHostName)) {
				theNode = node;
				break;
			}
		}
		return theNode;
	}
}
