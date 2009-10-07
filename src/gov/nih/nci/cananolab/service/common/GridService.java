package gov.nih.nci.cananolab.service.common;

import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cananolab.dto.common.GridNodeBean;
import gov.nih.nci.cananolab.exception.GridAutoDiscoveryException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.log4j.Logger;

/**
 * Grid service utils for grid node discovery and grid node lookup.
 * 
 * @author pansu
 * 
 */
public class GridService {
	private static Logger logger = Logger.getLogger(GridService.class);

	/**
	 * Discover all grid services by the given domain model name and version
	 * 
	 * @param indexServiceURL
	 * @param domainModelName
	 * @param domainModelVersion
	 * @return
	 * @throws GridAutoDiscoveryException
	 */
	public static List<GridNodeBean> discoverAllServices(
			String indexServiceURL, String domainModelName,
			String domainModelVersion) {
		EndpointReferenceType[] returnedServices = null;
		List<EndpointReferenceType> services = new ArrayList<EndpointReferenceType>();
		try {
			DiscoveryClient discoveryClient = new DiscoveryClient(
					indexServiceURL);
			returnedServices = discoveryClient
					.discoverDataServicesByDomainModel(domainModelName);
			if (returnedServices != null) {
				for (EndpointReferenceType returnedService : returnedServices) {
					// obtain the domain model version
					try {
						DomainModel domainModel = MetadataUtils
								.getDomainModel(returnedService);
						if (domainModel.getProjectVersion().equals(
								domainModelVersion)) {
							services.add(returnedService);
						}
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return populateServiceMetadata(services, domainModelName);
	}

	/**
	 * Discovered all new grid services by the given model name and version,
	 * remove ones that already exists
	 * 
	 * @param indexServiceURL
	 * @param domainModelName
	 * @param domainModelVersion
	 * @param extantURLs
	 * @return
	 */
	public static List<GridNodeBean> discoverNewServices(
			String indexServiceURL, String domainModelName,
			String domainModelVersion, Set<String> extantURLs) {
		EndpointReferenceType[] returnedServices = null;
		List<EndpointReferenceType> services = new ArrayList<EndpointReferenceType>();
		List<EndpointReferenceType> newRemoteServices = null;
		Set<EndpointReferenceType> servicesToRemove = new HashSet<EndpointReferenceType>();
		try {
			DiscoveryClient discoveryClient = new DiscoveryClient(
					indexServiceURL);
			returnedServices = discoveryClient
					.discoverDataServicesByDomainModel(domainModelName);
			if (returnedServices != null) {
				for (EndpointReferenceType returnedService : returnedServices) {
					// obtain the domain model version
					try {
						DomainModel domainModel = MetadataUtils
								.getDomainModel(returnedService);
						if (domainModel.getProjectVersion().equals(
								domainModelVersion)) {
							services.add(returnedService);
						}
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		if (!services.isEmpty()) {
			newRemoteServices = new ArrayList<EndpointReferenceType>(services);
			for (EndpointReferenceType service : services) {
				for (String extantURL : extantURLs) {
					// if the URL exists (because it's previously queried,
					// remove it)
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

	/**
	 * Populate grid service metadata such as hostname and grid service URL
	 * 
	 * @param services
	 * @param domainModelName
	 * @return
	 */
	public static List<GridNodeBean> populateServiceMetadata(
			List<EndpointReferenceType> services, String domainModelName) {
		List<GridNodeBean> gridNodes = new ArrayList<GridNodeBean>();
		if (services != null) {
			// catch exception separately for each service
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
							// remove model prefix
							hostName = hostName.replace(domainModelName + "-",
									"");
						}
					}
				} catch (Exception e) {
					String err = "Can't successfully obtain grid service metadata for: "
							+ address;
					hostName = null;
					logger.warn(err);
				}
				if (hostName != null) {
					GridNodeBean gridNode = new GridNodeBean(hostName, address,
							domainModelName, GridNodeBean.NodeType.REMOTE);
					logger.info("Adding to GridNodeBean list: " + hostName
							+ " : " + address);
					gridNodes.add(gridNode);
				}
			}
		}
		return gridNodes;
	}

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
