import gov.nih.nci.cagrid.cananolab.client.CaNanoLabServiceClient;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.domain.particle.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.ActivationMethod;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.domain.characterization.physical.*;

public class GridClientTest {
	CaNanoLabServiceClient gridClient;

	public GridClientTest(CaNanoLabServiceClient gridClient) {
		this.gridClient = gridClient;
	}

	public void testComposingElement() throws Exception {
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target
				.setName("gov.nih.nci.cananolab.domain.particle.ComposingElement");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue("6062106");
		target.setAttribute(attribute);
		query.setTarget(target);
		CQLQueryResults results = gridClient.query(query);
		results
				.setTargetClassname("gov.nih.nci.cananolab.domain.particle.ComposingElement");
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		ComposingElement nanoEntity = null;
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			nanoEntity = (ComposingElement) obj;
			System.out.println("ComposingElement: "+nanoEntity.getDescription());
		}
	}
	
	public void testSampleComposition() throws Exception {
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target
				.setName("gov.nih.nci.cananolab.domain.particle.SampleComposition");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue("6160390");
		target.setAttribute(attribute);
		query.setTarget(target);
		CQLQueryResults results = gridClient.query(query);
		results
				.setTargetClassname("gov.nih.nci.cananolab.domain.particle.SampleComposition");
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		SampleComposition sc = null;
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			sc = (SampleComposition) obj;
			System.out.println("SampleComposition : "+sc.getId() );
		}
	}
	/*
	public void testChemicalAssociation() throws Exception {
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target
				.setName("gov.nih.nci.cananolab.domain.particle.ChemicalAssociation");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue("10846210");
		target.setAttribute(attribute);
		query.setTarget(target);
		CQLQueryResults results = gridClient.query(query);
		results
				.setTargetClassname("gov.nih.nci.cananolab.domain.particle.ChemicalAssociation");
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		ChemicalAssociation nanoEntity = null;
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			nanoEntity = (ChemicalAssociation) obj;
			System.out.println("ChemicalAssociation: "+nanoEntity.getDescription());
		}
	}
	
	public void testSampleComposition() throws Exception {
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target
				.setName("gov.nih.nci.cananolab.domain.particle.SampleComposition");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue("10846210");
		target.setAttribute(attribute);
		query.setTarget(target);
		CQLQueryResults results = gridClient.query(query);
		results
				.setTargetClassname("gov.nih.nci.cananolab.domain.particle.SampleComposition");
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		SampleComposition nanoEntity = null;
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			nanoEntity = (SampleComposition) obj;
			System.out.println("SampleComposition: "+nanoEntity.getId());
		}
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
			System.out.println("nano entity: "+nanoEntity.getDescription());
		}
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
			System.out.println("nano entity: "+nanoEntity.getDescription());
		}
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
			System.out.println("nano entity: "+nanoEntity.getDescription());
		}
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
			System.out.println("nano entity: "+nanoEntity.getDescription());
		}
	}
	*/
	public void testChemicalAssociation() throws Exception {
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target
				.setName("gov.nih.nci.cananolab.domain.particle.ChemicalAssociation");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue("8847369");
		target.setAttribute(attribute);
		query.setTarget(target);
		CQLQueryResults results = gridClient.query(query);
		results
				.setTargetClassname("gov.nih.nci.cananolab.domain.particle.ChemicalAssociation");
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		ChemicalAssociation ca = null;
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			ca = (ChemicalAssociation) obj;
			System.out.println("ChemicalAssociation Desc: "+ca.getDescription() + ", id: " + ca.getId());
		}
	}
	
	public void testActivationMethod() throws Exception {
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target
				.setName("gov.nih.nci.cananolab.domain.particle.ActivationMethod");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue("3833872");
		target.setAttribute(attribute);
		query.setTarget(target);
		CQLQueryResults results = gridClient.query(query);
		results
				.setTargetClassname("gov.nih.nci.cananolab.domain.particle.ActivationMethod");
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		ActivationMethod am = null;
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			am = (ActivationMethod) obj;
			System.out.println("Activation Effect: "+am.getActivationEffect() + ", Type: " + am.getType());
		}
	}
	
	public void testNanomaterialEntity() throws Exception {
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target
				.setName("gov.nih.nci.cananolab.domain.particle.NanomaterialEntity");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue("6160399");
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
			System.out.println("nano entity: "+nanoEntity.getDescription());
		}
	}
	

	public void testSample() throws Exception{
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target
				.setName("gov.nih.nci.cananolab.domain.particle.Sample");
		Attribute attribute = new Attribute();
		/*attribute.setName("name");
		attribute.setPredicate(Predicate.LIKE);
		attribute.setValue("NCL-23-1"); //"20917507"); //NCL-23-1*/
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue("3735553"); //"20917507"); //NCL-23-1
		target.setAttribute(attribute);
		query.setTarget(target);
		CQLQueryResults results = gridClient.query(query);
		results
				.setTargetClassname("gov.nih.nci.cananolab.domain.particle.Sample");
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		Sample sample = null;
		System.out.println("sample resutls: " + results + iter.hasNext());
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			sample = (Sample) obj;
			System.out.println("Sample: "+sample.getName() + ", " + sample.getId());
		}
	}
	public void testFunction() throws Exception{
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target
				.setName("gov.nih.nci.cananolab.domain.particle.Function");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue("10944705"); 
		target.setAttribute(attribute);
		query.setTarget(target);
		CQLQueryResults results = gridClient.query(query);
		results
				.setTargetClassname("gov.nih.nci.cananolab.domain.particle.Function");
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		Function fe = null;
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			fe = (Function) obj;
			System.out.println("Function: "+fe.getDescription() + ", " + fe.getId());
		}
	}
	
	public void testFunctionalizingEntity() throws Exception{
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target
				.setName("gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue("6225945"); 
		target.setAttribute(attribute);
		query.setTarget(target);
		CQLQueryResults results = gridClient.query(query);
		results
				.setTargetClassname("gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity");
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		FunctionalizingEntity fe = null;
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			fe = (FunctionalizingEntity) obj;
			System.out.println("FunctionalizingEntity: "+fe.getName() + ", " + fe.getId());
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
			System.out.println("characterization: "+chara.getDesignMethodsDescription());
		}
	}
	
	public void testgetSampleIds() throws Exception{
		String[] sampleIds = gridClient.getSampleIds("", "", null, null, null, null, null);
		
		for(String id: sampleIds){
			System.out.println("sampleId: " + id);
		}
	}

	public static void main(String[] args) {
		System.out.println("Running the Grid Service Client");
		try {
			if (!(args.length < 2)) {
				if (args[0].equals("-url")) {
					CaNanoLabServiceClient client = new CaNanoLabServiceClient(
							args[1]);
					GridClientTest test=new GridClientTest(client);
					//test.testgetSampleIds();
					test.testCharacterization();
					test.testNanomaterialEntity();
					test.testFunction();
					test.testComposingElement();
					test.testFunctionalizingEntity();
					test.testSample();
					test.testChemicalAssociation();
					test.testSampleComposition();
					
				} else {
					System.exit(1);
				}
			} else {
				System.exit(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
