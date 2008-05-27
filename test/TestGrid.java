package test;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabServiceClient;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import gov.nih.nci.cananolab.domain.common.Report;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;

import org.apache.axis.message.addressing.EndpointReferenceType;

public class TestGrid {
	private String indexServiceUrl;

	public TestGrid(String indexServiceURL) throws Exception {
		this.indexServiceUrl = indexServiceURL;
	}

	public EndpointReferenceType[] discover(String domainModelName)
			throws Exception {
		EndpointReferenceType[] services = null;
		try {
			DiscoveryClient discoveryClient = new DiscoveryClient(
					indexServiceUrl);
			services = discoveryClient
					.discoverDataServicesByDomainModel(domainModelName);
			// services=discoveryClient.getAllDataServices();
			// services=discoveryClient.getAllServices();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (services != null) {
			// for (EndpointReferenceType service : services) {
			// String address = service.getAddress().toString();
			// String hostName = "", appServiceURL = "";
			// // catch RemoteResourcePropertyRetrievalException in case
			// // service is not successfully deregistered from the index
			// // server when the service is shut down.
			// try {
			// ServiceMetadata serviceMetaData = MetadataUtils
			// .getServiceMetadata(service);
			// if (serviceMetaData != null) {
			// if (serviceMetaData.getHostingResearchCenter() != null) {
			// if (serviceMetaData.getHostingResearchCenter()
			// .getResearchCenter() != null) {
			// hostName = serviceMetaData
			// .getHostingResearchCenter()
			// .getResearchCenter().getDisplayName();
			// }
			// }
			// }
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// }
			return services;
		} else {
			return null;
		}
	}

	public void testCQL(String serviceUrl) throws Exception {
		CaNanoLabServiceClient gridClient = new CaNanoLabServiceClient(
				serviceUrl);
		System.out.println("Running CQL .............");
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName("gov.nih.nci.cananolab.domain.common.Report");
		Attribute attribute = new Attribute();
		attribute.setName("name");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue("120406.pdf");
		target.setAttribute(attribute);
		query.setTarget(target);
		CQLQueryResults results = gridClient.query(query);
		results
				.setTargetClassname("gov.nih.nci.cananolab.domain.common.Report");
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			Report report = (Report) obj;
			System.out.println(report.getId());
		}
	}

	public void testOperation(String serviceUrl) throws Exception {
		CaNanoLabServiceClient gridClient = new CaNanoLabServiceClient(
				serviceUrl);
		System.out.println("Running operation .............");
		String particleSource = "DNT";
		NanoparticleSample[] particleSamples = gridClient
				.getNanoparticleSamplesBy(particleSource, null, null, null,
						null, null);
		for (NanoparticleSample particleSample : particleSamples) {
			System.out.println(particleSample.getName());
		}
	}

	public static void main(String[] args) {
		try {
			TestGrid test = new TestGrid(
					"http://cbiovdev5012.nci.nih.gov:8080/wsrf/services/DefaultIndexService");
			EndpointReferenceType[] services = test.discover("caNanoLab");
			if (services != null) {
				for (EndpointReferenceType service : services) {
					System.out.println(service.getAddress());
					test.testCQL(service.getAddress().toString());
					test.testOperation(service.getAddress().toString());
				}
			} else {
				System.out.println("No grid nodes found");
			}
			// test
			// .testCQL("http://165.112.132.206:8080/wsrf/services/cagrid/CaNanoLabService");
			// test
			// .testOperation("http://165.112.132.206:8080/wsrf/services/cagrid/CaNanoLabService");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
