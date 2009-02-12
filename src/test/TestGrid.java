package test;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabServiceClient;
import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import gov.nih.nci.cananolab.domain.function.Target;
import gov.nih.nci.cananolab.domain.particle.AssociatedElement;
import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.domain.particle.NanoparticleEntity;

import java.util.ArrayList;
import java.util.List;

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
			//services = discoveryClient.getAllDataServices();
			//services=discoveryClient.getAllServices(false);
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
		// CQLQuery query = new CQLQuery();
		// gov.nih.nci.cagrid.cqlquery.Object target = new
		// gov.nih.nci.cagrid.cqlquery.Object();
		// target.setName("gov.nih.nci.cananolab.domain.common.Report");
		// Attribute attribute = new Attribute();
		// attribute.setName("name");
		// attribute.setPredicate(Predicate.EQUAL_TO);
		// attribute.setValue("120406.pdf");
		// target.setAttribute(attribute);
		// query.setTarget(target);
		// CQLQueryResults results = gridClient.query(query);
		// results
		// .setTargetClassname("gov.nih.nci.cananolab.domain.common.Report");
		// CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		// while (iter.hasNext()) {
		// java.lang.Object obj = iter.next();
		// Report report = (Report) obj;
		// System.out.println(report.getId());
		// }
		//
		// CQLQuery query = new CQLQuery();
		// gov.nih.nci.cagrid.cqlquery.Object target = new
		// gov.nih.nci.cagrid.cqlquery.Object();
		// target
		// .setName("gov.nih.nci.cananolab.domain.particle.NanoparticleSample");
		// query.setTarget(target);
		// QueryModifier modifier = new QueryModifier();
		// modifier.setCountOnly(true);
		// query.setQueryModifier(modifier);
		// CQLQueryResults results = gridClient.query(query);
		// results
		// .setTargetClassname("gov.nih.nci.cananolab.domain.particle.NanoparticleSample");
		// CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		// int count = 0;
		// while (iter.hasNext()) {
		// java.lang.Object obj = iter.next();
		// count = ((Long) obj).intValue();
		// }
		//
		// CQLQuery query = new CQLQuery();
		// gov.nih.nci.cagrid.cqlquery.Object target = new
		// gov.nih.nci.cagrid.cqlquery.Object();
		// target.setName("gov.nih.nci.cananolab.domain.common.ProtocolFile");
		// query.setTarget(target);
		// QueryModifier modifier = new QueryModifier();
		// modifier.setCountOnly(true);
		// query.setQueryModifier(modifier);
		//
		// CQLQueryResults results = gridClient.query(query);
		// results
		// .setTargetClassname("gov.nih.nci.cananolab.domain.common.ProtocolFile");
		// CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		// int count = 0;
		// while (iter.hasNext()) {
		// java.lang.Object obj = iter.next();
		// count = ((Long) obj).intValue();
		// }
		// System.out.println(count);
		//
		// CQLQuery query = new CQLQuery();
		// gov.nih.nci.cagrid.cqlquery.Object target = new
		// gov.nih.nci.cagrid.cqlquery.Object();
		// target.setName("gov.nih.nci.cananolab.domain.common.Protocol");
		// Association association = new Association();
		// association.setName("gov.nih.nci.cananolab.domain.common.ProtocolFile");
		// association.setRoleName("protocolFileCollection");
		// Attribute attribute = new Attribute();
		// attribute.setName("id");
		// attribute.setPredicate(Predicate.EQUAL_TO);
		// attribute.setValue("102");
		// association.setAttribute(attribute);
		// target.setAssociation(association);
		// query.setTarget(target);
		// CQLQueryResults results = gridClient.query(query);
		// results
		// .setTargetClassname("gov.nih.nci.cananolab.domain.common.Protocol");
		// CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		// while (iter.hasNext()) {
		// java.lang.Object obj = iter.next();
		// Protocol protocol = (Protocol) obj;
		// System.out.println(protocol.getName());
		// }

		// CQLQuery query = new CQLQuery();
		// gov.nih.nci.cagrid.cqlquery.Object target = new
		// gov.nih.nci.cagrid.cqlquery.Object();
		// target
		// .setName("gov.nih.nci.cananolab.domain.particle.characterization.Characterization");
		// Association association = new Association();
		// association
		// .setName("gov.nih.nci.cananolab.domain.particle.NanoparticleSample");
		// association.setRoleName("nanoparticleSample");
		//
		// Attribute attribute = new Attribute();
		// attribute.setName("id");
		// attribute.setPredicate(Predicate.EQUAL_TO);
		// attribute.setValue("917504");
		//
		// association.setAttribute(attribute);
		//
		// target.setAssociation(association);
		// query.setTarget(target);
		// CQLQueryResults results = gridClient.query(query);
		// results
		// .setTargetClassname("gov.nih.nci.cananolab.domain.particle.characterization.Characterization");
		// CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		// Characterization chara = null;
		// List<Characterization> characterizationCollection = new
		// ArrayList<Characterization>();
		// while (iter.hasNext()) {
		// java.lang.Object obj = iter.next();
		// chara = (Characterization) obj;
		// // loadCharacterizationAssociations(chara);
		// characterizationCollection.add(chara);
		// System.out.println(chara.getIdentificationName());
		// }

		// CQLQuery query = new CQLQuery();
		// gov.nih.nci.cagrid.cqlquery.Object target = new
		// gov.nih.nci.cagrid.cqlquery.Object();
		// target
		// .setName("gov.nih.nci.cananolab.domain.particle.characterization.Characterization");
		// Attribute attribute = new Attribute();
		// attribute.setName("id");
		// attribute.setPredicate(Predicate.EQUAL_TO);
		// attribute.setValue("1376257");
		// target.setAttribute(attribute);
		// query.setTarget(target);
		// CQLQueryResults results = gridClient.query(query);
		// results
		// .setTargetClassname("gov.nih.nci.cananolab.domain.particle.characterization.Characterization");
		// CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		// Characterization achar = null;
		// while (iter.hasNext()) {
		// java.lang.Object obj = iter.next();
		// achar = (Characterization) obj;
		// System.out.println(achar.getId());
		// }

		// CQLQuery query = new CQLQuery();
		// gov.nih.nci.cagrid.cqlquery.Object target = new
		// gov.nih.nci.cagrid.cqlquery.Object();
		// target
		// .setName("gov.nih.nci.cananolab.domain.particle.samplecomposition.SampleComposition");
		// Association association = new Association();
		// association
		// .setName("gov.nih.nci.cananolab.domain.particle.NanoparticleSample");
		// association.setRoleName("nanoparticleSample");
		//
		// Attribute attribute = new Attribute();
		// attribute.setName("id");
		// attribute.setPredicate(Predicate.EQUAL_TO);
		// attribute.setValue("917504");
		//
		// association.setAttribute(attribute);
		//
		// target.setAssociation(association);
		// query.setTarget(target);
		// CQLQueryResults results = gridClient.query(query);
		// results
		// .setTargetClassname("gov.nih.nci.cananolab.domain.particle.samplecomposition.SampleComposition");
		// CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		// SampleComposition sampleComposition = null;
		// while (iter.hasNext()) {
		// java.lang.Object obj = iter.next();
		// sampleComposition = (SampleComposition) obj;
		// System.out.println(sampleComposition.getId());
		// }

		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target
				.setName("gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity");
		Association association = new Association();
		association
				.setName("gov.nih.nci.cananolab.domain.particle.samplecomposition.SampleComposition");
		association.setRoleName("sampleComposition");

		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue("1376256");
		association.setAttribute(attribute);

		target.setAssociation(association);
		query.setTarget(target);
		CQLQueryResults results = gridClient.query(query);
		results
				.setTargetClassname("gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity");
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		NanoparticleEntity nanoEntity = null;

		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			nanoEntity = (NanoparticleEntity) obj;
			System.out.println(nanoEntity.getId());
		}
	}

	public void testOperation(String serviceUrl) throws Exception {
		CaNanoLabServiceClient gridClient = new CaNanoLabServiceClient(
				serviceUrl);
		System.out.println("Running operation .............");
		// String particleSource = "DNT";
		// String[] nanoparticleEntityClassNames = new String[] { "Dendrimer",
		// "CarbonNanotube" };
		// String[] functionClassNames = new String[] { "TargetingFunction",
		// "TherapeuticFunction" };
		// String[] characterizationClassNames = new String[] {
		// "EnzymeInduction" };
		// NanoparticleSample[] particleSamples = gridClient
		// .getNanoparticleSamplesBy(particleSource,
		// nanoparticleEntityClassNames, null, functionClassNames,
		// characterizationClassNames, null);
		// for (NanoparticleSample particleSample : particleSamples) {
		// System.out.println(particleSample.getName());
		// }
		//
		// String protocolType = null;
		// String protocolName = null;
		// String protocolFileTitle = null;
		// ProtocolFile[] protocolFiles = gridClient.getProtocolFilesBy(
		// protocolType, protocolName, protocolFileTitle);
		// for (ProtocolFile pf : protocolFiles) {
		// System.out.println("PF: " + pf.getName());
		// }
		//TODO fix grid client
/*
		String composingElementId = "1409024";
		Function[] functions = gridClient
				.getInherentFunctionsByComposingElementId(composingElementId);
		if (functions != null)
			for (Function function : functions) {
				System.out.println(function.getId());
			}

		String functionId = "5898240";
		Target[] targets = gridClient.getTargetsByFunctionId(functionId);
		if (targets != null)
			for (Target target : targets) {
				System.out.println(target.getId());
			}

		String assocId = "10584064";
		AssociatedElement eleA = gridClient
				.getAssociatedElementAByChemicalAssociationId(assocId);
		AssociatedElement eleB = gridClient
				.getAssociatedElementBByChemicalAssociationId(assocId);
		System.out.println(eleA+" "+eleB);
	*/
	}

	public static void main(String[] args) {
		try {
			List<String> tests = new ArrayList<String>();
			TestGrid test = new TestGrid(
					"http://cagrid-index-qa.nci.nih.gov:8080/wsrf/services/DefaultIndexService");
			//
//			 TestGrid test = new TestGrid(
//			 "http://cbiovdev5012.nci.nih.gov:8080/wsrf/services/DefaultIndexService");
			EndpointReferenceType[] services = test.discover("caNanoLab");
			if (services != null) {
				for (EndpointReferenceType service : services) {
					System.out.println(service.getAddress());
					// test.testCQL(service.getAddress().toString());
					// test.testOperation(service.getAddress().toString());
				}
			} else {
				System.out.println("No grid nodes found");
			}
//			test
//					.testCQL("http://cananolab-dev.nci.nih.gov/wsrf-canano/services/cagrid/CaNanoLabService");
//			test
//					.testOperation("http://cananolab-dev.nci.nih.gov/wsrf-canano/services/cagrid/CaNanoLabService");
//			test
//			.testOperation("http://NCI-Pansu-1.nci.nih.gov:8080/wsrf-canano/services/cagrid/CaNanoLabService");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
