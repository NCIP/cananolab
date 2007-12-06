package gov.nih.nci.calab.service.remote;

import gov.nih.nci.cagrid.cananolab.service.globus.resource.ResourceConstants;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.ResourcePropertyHelper;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.calab.dto.remote.GridNodeBean;
import gov.nih.nci.calab.exception.GridAutoDiscoveryException;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.log4j.Logger;
import org.globus.wsrf.utils.XmlUtils;
import org.w3c.dom.Element;

/**
 * Grid service utils for grid node discovery and grid node URL lookup.
 * 
 * @author pansu
 * 
 */
public class GridService {
	private static Logger logger = Logger.getLogger(GridService.class);

	/**
	 * Query the grid index service by domain model name and return a map of
	 * GridNodeBeans with keys being the hostnames.
	 * 
	 * @param indexServiceURL
	 * @param domainModelName
	 * @return
	 * @throws GridAutoDiscoveryException
	 */
	public static Map<String, GridNodeBean> discoverServices(
			String indexServiceURL, String domainModelName)
			throws GridAutoDiscoveryException {

		Map<String, GridNodeBean> gridNodeMap = new TreeMap<String, GridNodeBean>();
		EndpointReferenceType[] services = null;
		try {
			DiscoveryClient discoveryClient = new DiscoveryClient(
					indexServiceURL);
			services = discoveryClient
					.discoverDataServicesByDomainModel(domainModelName);
		} catch (Exception e) {
			logger
					.error("Error in discovering caNanoLab nodes from the index server:"
							+ e);
			throw new GridAutoDiscoveryException();
		}
		if (services != null) {
			for (EndpointReferenceType service : services) {
				String address = service.getAddress().toString();
				String hostName = "", appServiceURL = "";
				// catch RemoteResourcePropertyRetrievalException in case
				// service is not successfully deregistered from the index
				// server when the service is shut down.
				try {
					ServiceMetadata serviceMetaData = MetadataUtils
							.getServiceMetadata(service);
					if (serviceMetaData != null) {
						if (serviceMetaData.getHostingResearchCenter() != null) {
							if (serviceMetaData.getHostingResearchCenter()
									.getResearchCenter() != null) {
								hostName = serviceMetaData
										.getHostingResearchCenter()
										.getResearchCenter().getDisplayName();
							}
						}
					}

					// retrieve customized metadata
					Element resourceProp = ResourcePropertyHelper
							.getResourceProperty(
									service,
									ResourceConstants.APPLICATIONSERVICEURL_MD_RP);
					Reader xmlReader = new StringReader(XmlUtils
							.toString(resourceProp));
					appServiceURL = (String) Utils.deserializeObject(xmlReader,
							String.class);

					GridNodeBean gridNode = new GridNodeBean(hostName, address,
							appServiceURL);
					gridNodeMap.put(hostName, gridNode);
				} catch (Exception e) {
					logger.error(
							"Can't successfully obtain grid service metadata: "
									+ address, e);
					throw new GridAutoDiscoveryException();
				}
			}
			return gridNodeMap;
		} else {
			return null;
		}
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
			gridNodes = new GridNodeBean[selectedGrids.size()];
			selectedGrids.toArray(gridNodes);
		} else {
			gridNodes = new GridNodeBean[gridNodeHosts.length];
			int i = 0;
			for (String host : gridNodeHosts) {
				gridNodes[i] = gridNodeMap.get(host);
				i++;
			}
		}
		return gridNodes;
	}
}
