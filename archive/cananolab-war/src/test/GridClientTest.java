package test;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabServiceClient;
import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cananolab.domain.characterization.Characterization;
import gov.nih.nci.cananolab.dto.common.GridNodeBean;
import gov.nih.nci.cananolab.service.common.GridService;

import java.io.FileReader;
import java.util.List;

public class GridClientTest {
	CaNanoLabServiceClient gridClient;

	public GridClientTest(CaNanoLabServiceClient gridClient) {
		this.gridClient = gridClient;
	}

	public void testNanomaterialEntity() throws Exception {
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target
				.setName("gov.nih.nci.cananolab.domain.particle.NanomaterialEntity");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue("10846210");
		target.setAttribute(attribute);
		query.setTarget(target);
		CQLQueryResults results = gridClient.query(query);
		results
				.setTargetClassname("gov.nih.nci.cananolab.domain.particle.NanomaterialEntity");
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
//		NanomaterialEntity nanoEntity = null;
//		while (iter.hasNext()) {
//			java.lang.Object obj = iter.next();
//			nanoEntity = (NanomaterialEntity) obj;
//			System.out.println("nano entity: " + nanoEntity.getDescription());
//		}
	}

	public void testCharacterization() throws Exception {
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target
				.setName("gov.nih.nci.cananolab.domain.particle.Characterization");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue("10977286");
		target.setAttribute(attribute);
		query.setTarget(target);
		CQLQueryResults results = gridClient.query(query);
		results
				.setTargetClassname("gov.nih.nci.cananolab.domain.particle.Characterization");
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		Characterization chara = null;
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			chara = (Characterization) obj;
//			System.out.println("characterization: "
//					+ chara.getDesignMethodsDescription());
		}
	}

	public void testSampleQueries(String sampleId) throws Exception {
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		String fullClassName = "gov.nih.nci.cananolab.domain.characterization.physical.Size";
		target.setName(fullClassName);
		Association association = new Association();
		association.setName("gov.nih.nci.cananolab.domain.particle.Sample");
		association.setRoleName("sample");

		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(sampleId);
		association.setAttribute(attribute);
		target.setAssociation(association);
		query.setTarget(target);
		CQLQueryResults results = gridClient.query(query);
		results.setTargetClassname(fullClassName);
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		Characterization chara = null;
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			chara = (Characterization) obj;
			System.out.println("characterization: " + chara.getId());
		}
	}

	public void testCQLFromFile() throws Exception {
		String fullClassName = "gov.nih.nci.cananolab.domain.characterization.physical.Size";
		 CQLQuery query = (CQLQuery) gov.nih.nci.cagrid.common.Utils.deserializeObject(
	             new FileReader("c:/devel/caNanoLab/src/cql_example.xml"), CQLQuery.class);
		CQLQueryResults results = gridClient.query(query);
		results.setTargetClassname(fullClassName);
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		Characterization chara = null;
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			chara = (Characterization) obj;
			System.out.println("characterization: " + chara.getId());
		}
	}

	public void testAll(String gridServiceUrl) throws Exception {
		// testCharacterization();
		// testNanomaterialEntity();
		// SampleService sampleService = new SampleServiceRemoteImpl(
		// gridServiceUrl);
		// List<String> sampleNames = sampleService.findSampleNamesBy(null,
		// null,
		// null, null, null, null, null, null, null, null, null);
		// System.out.println("total samples: "+sampleNames.size());
		// for (String sampleName : sampleNames) {
		// System.out.println(sampleName);
		// }
		// PublicationService pubService = new PublicationServiceRemoteImpl(
		// gridServiceUrl);
		// List<String>pubIds=pubService.findPublicationIdsBy(null, null, null,
		// null, null, null, null, null,
		// null, null,
		// null,
		// null, null,
		// null, null);
		// System.out.println("total publications: "+pubIds.size());
		// for(String pubId: pubIds) {
		// System.out.println(pubId);
		// }
		//testSampleQueries("10780676");
		testCQLFromFile();
	}

	public static void main(String[] args) {
		System.out.println("Running the Grid Service Client");
		try {
			if (!(args.length < 2)) {
				if (args[0].equals("-url")) {
					CaNanoLabServiceClient client = new CaNanoLabServiceClient(
							args[1]);
					GridClientTest test = new GridClientTest(client);
					test.testAll(args[1]);
				} else if (args[0].equals("-indexUrl")) {
					// String indexServiceURL =
					// "http://cbiovdev5012.nci.nih.gov:8080/wsrf/services/DefaultIndexService";
					// test autodiscovery
					List<GridNodeBean> gridNodes = GridService
							.discoverAllServices(args[1], "caNanoLab", "1.5");
					for (GridNodeBean node : gridNodes) {
						System.out.println(node.getAddress());
						CaNanoLabServiceClient client = new CaNanoLabServiceClient(
								node.getAddress());
						GridClientTest test = new GridClientTest(client);
						test.testAll(node.getAddress());
					}
				}
			} else {
				System.out
						.println("Please enter an argument either as -url for grid service URL or -indexUrl for grid index service URL");
				System.exit(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
