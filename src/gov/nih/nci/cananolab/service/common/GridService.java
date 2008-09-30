package gov.nih.nci.cananolab.service.common;

import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.exceptions.QueryInvalidException;
import gov.nih.nci.cagrid.metadata.exceptions.RemoteResourcePropertyRetrievalException;
import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;
import gov.nih.nci.cananolab.dto.common.GridNodeBean;
import gov.nih.nci.cananolab.exception.GridAutoDiscoveryException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.apache.log4j.Logger;

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
			String indexServiceURL, String domainModelName, String appOwner)
			throws GridAutoDiscoveryException {

		Map<String, GridNodeBean> gridNodeMap = new TreeMap<String, GridNodeBean>();
		EndpointReferenceType[] services = null;
		try {
			DiscoveryClient discoveryClient = new DiscoveryClient(
					indexServiceURL);
			services = discoveryClient
					.discoverDataServicesByDomainModel(domainModelName);
		} catch (Exception e) {
			String err = "Error in discovering caNanoLab nodes from the index server";
			logger.error(err, e);
			throw new GridAutoDiscoveryException(err, e);
		}
		if (services != null) {
			for (EndpointReferenceType service : services) {
				String address = service.getAddress().toString();
				String hostName = "";
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
								// remove caNanoLab prefix
								hostName = hostName.replace("caNanoLab-", "");
							}
						}
					}

					// remove local grid node from the list
					if (!hostName.contains(appOwner)) {
						GridNodeBean gridNode = new GridNodeBean(hostName,
								address);
						gridNodeMap.put(hostName, gridNode);
					}
				} catch (Exception e) {
					String err = "Can't successfully obtain grid service metadata: "
							+ address;
					logger.error(err, e);
					throw new GridAutoDiscoveryException(err);
				}
			}
			return gridNodeMap;
		} else {
			return null;
		}
	}

	/**
	 * Query the grid index service by domain model name and return a map of
	 * GridNodeBeans with keys being the hostnames. Remove the local node.
	 * 
	 * @param indexServiceURL
	 * @param domainModelName
	 * @return
	 * @throws GridAutoDiscoveryException
	 */
	public static Map<String, GridNodeBean> discoverServices(
			String indexServiceURL, String domainModelName,
			String domainModelVersion, String appOwner)
			throws GridAutoDiscoveryException {

		Map<String, GridNodeBean> gridNodeMap = new TreeMap<String, GridNodeBean>();
		EndpointReferenceType[] services = null;
		List<EndpointReferenceType> matchedServices = null;
		try {
			DiscoveryClient discoveryClient = new DiscoveryClient(
					indexServiceURL);
			services = discoveryClient
					.discoverDataServicesByDomainModel(domainModelName);
		} catch (Exception e) {
			String err = "Error in discovering caNanoLab nodes from the index server";
			logger.error(err, e);
			throw new GridAutoDiscoveryException(err, e);
		}
		if (services != null) {
			matchedServices = new ArrayList<EndpointReferenceType>(Arrays
					.asList(services));

			for (EndpointReferenceType service : services) {
				// catch RemoteResourcePropertyRetrievalException in case
				// service is not successfully deregistered from the index
				// server when the service is shut down.
				String address = service.getAddress().toString();
				try {
					// obtain the domain model version
					DomainModel domainModel = MetadataUtils
							.getDomainModel(service);
					if (domainModel.getProjectVersion().equals(
							domainModelVersion)) {
						matchedServices.add(service);
					} else {
						logger.warn("Different domain model version found ("
								+ domainModel.getProjectVersion() + ") for "
								+ address);
					}
				} catch (Exception e) {
					String err = "Can't successfully obtain grid service domain model version from "
							+ address;
					logger.error(err, e);
				}
			}

			for (EndpointReferenceType service : matchedServices) {
				String address = service.getAddress().toString();
				String hostName = "";
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
								// remove caNanoLab prefix
								hostName = hostName.replace("caNanoLab-", "");
							}
						}
					}

					// remove local grid node from the list
					if (!hostName.contains(appOwner)) {
						GridNodeBean gridNode = new GridNodeBean(hostName,
								address);
						gridNodeMap.put(hostName, gridNode);
					}
				} catch (Exception e) {
					String err = "Can't successfully obtain grid service metadata: "
							+ address;
					logger.error(err, e);
					// throw new GridAutoDiscoveryException(err);
				}
			}
			return gridNodeMap;
		} else {
			return null;
		}
	}

	public static Map<String, GridNodeBean> discoverServices(
			String indexServiceURL, String domainModelName,
			String domainModelVersion, Set<String> extantURLs)
			throws GridAutoDiscoveryException {
		EndpointReferenceType[] returnedServices = null;
		List<EndpointReferenceType> services = new ArrayList<EndpointReferenceType>();
		List<EndpointReferenceType> newRemoteServices = null;
		Set<EndpointReferenceType> servicesToRemove = new HashSet<EndpointReferenceType>();
		try {
			DiscoveryClient discoveryClient = new DiscoveryClient(
					indexServiceURL);
			returnedServices = discoveryClient
					.discoverDataServicesByDomainModel(domainModelName);
			for (EndpointReferenceType returnedService : returnedServices) {
				// obtain the domain model version
				DomainModel domainModel = MetadataUtils
						.getDomainModel(returnedService);
				if (domainModel.getProjectVersion().equals(domainModelVersion)) {
					services.add(returnedService);
				}
			}
		} catch (MalformedURIException e) {
			logger.error(e.getMessage(), e);
		} catch (RemoteResourcePropertyRetrievalException e) {
			logger.error(e.getMessage(), e);
		} catch (QueryInvalidException e) {
			logger.error(e.getMessage(), e);
		} catch (ResourcePropertyRetrievalException e) {
			logger.error(e.getMessage(), e);
		}

		if (services != null) {
			newRemoteServices = new ArrayList<EndpointReferenceType>(services);
			for (EndpointReferenceType service : services) {
				for (String extantURL : extantURLs) {
					// if the URL exists (because it's local or previously
					// queried, than make it to be removed
					if (extantURL.equals(service.getAddress().toString())) {
						servicesToRemove.add(service);
					}
				}
			}
			if (servicesToRemove != null && !servicesToRemove.isEmpty()) {
				newRemoteServices.removeAll(servicesToRemove);
			}
		}
		return populateServiceMetadata(newRemoteServices, domainModelName);
	}

	public static Map<String, GridNodeBean> populateServiceMetadata(
			List<EndpointReferenceType> services, String domainModelName) {
		Map<String, GridNodeBean> gridNodeMap = new HashMap<String, GridNodeBean>();
		if (services != null) {
			for (EndpointReferenceType service : services) {
				String address = service.getAddress().toString();
				String hostName = null;
				try {
					ServiceMetadata serviceMetaData = MetadataUtils
							.getServiceMetadata(service);
					if (serviceMetaData != null) {
						if (serviceMetaData.getHostingResearchCenter()
								.getResearchCenter() != null) {
							hostName = serviceMetaData
									.getHostingResearchCenter()
									.getResearchCenter().getDisplayName();
							// remove caNanoLab prefix
							hostName = hostName.replace("caNanoLab-", "");
						}
					}
				} catch (Exception e) {
					String err = "Can't successfully obtain grid service metadata: "
							+ address;
					hostName = null;
					logger.warn(err);
					// throw new GridAutoDiscoveryException(err, e);
				}
				if (hostName != null) {
					GridNodeBean gridNode = new GridNodeBean(hostName, address,
							domainModelName, GridNodeBean.NodeType.REMOTE);
					logger.info("Adding to GridNodeBean: " + hostName + " : "
							+ address);
					gridNodeMap.put(hostName, gridNode);
				}
			}
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
			gridNodes = new GridNodeBean[selectedGrids.size()];
			selectedGrids.toArray(gridNodes);
		} else {
			gridNodes = new GridNodeBean[gridNodeHosts.length];
			int i = 0;
			for (String host : gridNodeHosts) {
				if (host != null) {
					gridNodes[i] = gridNodeMap.get(host);
					i++;
				}
			}
		}
		return gridNodes;
	}
}
