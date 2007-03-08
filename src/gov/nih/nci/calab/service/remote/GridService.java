package gov.nih.nci.calab.service.remote;

import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.calab.dto.common.GridNodeBean;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.apache.axis.message.addressing.EndpointReferenceType;

/**
 * Grid service utils for grid node discovery and grid node URL lookup.
 * @author pansu
 *
 */
public class GridService {

	/**
	 * Temp code to be replaced
	 * 
	 * @param indexServiceURL
	 * @param domainModelName
	 * @return
	 * @throws Exception
	 */
	public static Map<String, GridNodeBean> discoverServices(
			String indexServiceURL, String domainModelName) throws Exception {

		Map<String, GridNodeBean> gridNodeMap = new TreeMap<String, GridNodeBean>();
		GridNodeBean nclNode = new GridNodeBean("NCL",
				"http://6116-pansu-2.nci.nih.gov:8880/wsrf/services/cagrid/CaNanoLabSvc");
		GridNodeBean washUNode = new GridNodeBean("WashU",
				"http://6116-zengje-1.nci.nih.gov:8880/wsrf/services/cagrid/CaNanoLabSvc");
		gridNodeMap.put("NCL", nclNode);
		gridNodeMap.put("WashU", washUNode);
		return gridNodeMap;
	}

	/**
	 * Query the grid index service by domain model name and return a map of
	 * GridNodeBeans with keys being the hostnames.
	 * 
	 * @param indexServiceURL
	 * @param domainModelName
	 * @return
	 * @throws Exception
	 */
	public static Map<String, GridNodeBean> discoverServicesToKeep(
			String indexServiceURL, String domainModelName) throws Exception {

		Map<String, GridNodeBean> gridNodeMap = new TreeMap<String, GridNodeBean>();
		DiscoveryClient discoveryClient = new DiscoveryClient(indexServiceURL);
		EndpointReferenceType[] services = discoveryClient
				.discoverDataServicesByDomainModel(domainModelName);
		for (EndpointReferenceType service : services) {
			String address = service.getAddress().getPath();
			ServiceMetadata serviceMetaData = MetadataUtils
					.getServiceMetadata(service);
			String hostName = serviceMetaData.getHostingResearchCenter()
					.getResearchCenter().getDisplayName();
			GridNodeBean gridNode = new GridNodeBean(hostName, address);
			gridNodeMap.put(hostName, gridNode);
		}
		return gridNodeMap;
	}

	/**
	 * Return an array of GridNodeBean based on grid host names
	 * 
	 * @param gridNodeMap
	 * @param gridNodeHosts
	 * @return
	 */
public static GridNodeBean[] getGridNodesFromHostNames(
			Map<String, GridNodeBean> gridNodeMap, String[] gridNodeHosts) {
		GridNodeBean[] gridNodes = null;
		if (gridNodeHosts.length == 0) {
			Collection<GridNodeBean> selectedGrids = gridNodeMap.values();
			gridNodes=new GridNodeBean[selectedGrids.size()];
			selectedGrids.toArray(gridNodes);
		} else {
			gridNodes=new GridNodeBean[gridNodeHosts.length];
			int i = 0;
			for (String host : gridNodeHosts) {
				gridNodes[i] = gridNodeMap.get(host);
				i++;
			}
		}
		return gridNodes;
	}}
