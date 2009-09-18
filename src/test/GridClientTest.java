package test;

import java.util.List;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabServiceClient;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.dto.common.GridNodeBean;
import gov.nih.nci.cananolab.service.common.GridService;

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
		NanomaterialEntity nanoEntity = null;
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			nanoEntity = (NanomaterialEntity) obj;
			System.out.println("nano entity: " + nanoEntity.getDescription());
		}
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
			System.out.println("characterization: "
					+ chara.getDesignMethodsDescription());
		}
	}

	public static void main(String[] args) {
		System.out.println("Running the Grid Service Client");
		try {
			if (!(args.length < 2)) {
				if (args[0].equals("-url")) {
					CaNanoLabServiceClient client = new CaNanoLabServiceClient(
							args[1]);
					GridClientTest test = new GridClientTest(client);
					test.testCharacterization();
					test.testNanomaterialEntity();
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
						test.testCharacterization();
						test.testNanomaterialEntity();
					}
				}
			}
			else {
				System.out.println("Please enter either an argument either as -url or -indexUrl");
				System.exit(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
