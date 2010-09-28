import gov.nih.nci.cagrid.cananolab.client.CaNanoLabServiceClient;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cananolab.domain.common.ExperimentConfig;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.ActivationMethod;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;

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
	
	public void testGetKeywordsBySampleId() throws Exception{
		String sampleId = "20917507";
		System.out.println("Testing getKeyworkdsBySampleId: 20917507: ");
		Keyword[] keywords = gridClient.getKeywordsBySampleId(sampleId);
		for(Keyword keyword: keywords){
			System.out.println("Keyword name: " + keyword.getName() + "\tId: " + keyword.getId());
		}
		System.out.println("Finished printing getKeyworkdsBySampleId results for 20917507: ");
	}
	
	public void testGetPrimaryPointOfContactBySampleId() throws Exception{
		String sampleId = "20917507";
		System.out.println("Testing getPrimaryPointOfContactBySampleId 20917507: ");
		PointOfContact contact = gridClient.getPrimaryPointOfContactBySampleId(sampleId);
		
		System.out.println("primary contact name: " + contact.getFirstName() + "\t" +contact.getLastName() + 
				"\tId: " + contact.getId() + "\tPhone: " + contact.getPhone() + "\tRole: " + 
				contact.getRole() + "\tEmail: " +contact.getEmail());
		
		System.out.println("Finished printing getPrimaryPointOfContactBySampleId results for 20917507: ");
	}
	
	public void testGetOtherPointOfContactsBySampleId() throws Exception{
		String sampleId = "3735553";
		System.out.println("Testing getOtherPointOfContactsBySampleId 3735553: ");
		PointOfContact[] contacts = gridClient.getOtherPointOfContactsBySampleId(sampleId);
		System.out.println("contacts " + contacts);
		for(PointOfContact contact: contacts){
			System.out.println("primary contact name: " + contact.getFirstName() + "\t" +contact.getLastName() + 
				"\tId: " + contact.getId() + "\tPhone: " + contact.getPhone() + "\tRole: " + 
				contact.getRole() + "\tEmail: " +contact.getEmail());
		}
		System.out.println("Finished printing getPrimaryPointOfContactBySampleId results for 3735553: ");
	}
	
	public void testGetExperimentConfigsByCharacterizationId() throws Exception{
		String charId = "3932251";
		System.out.println("Testing testGetExperimentConfigsByCharacterizationId 3932251: ");
		ExperimentConfig[] experimentConfigs = gridClient.getExperimentConfigsByCharacterizationId(charId);
		
		for(ExperimentConfig exp: experimentConfigs){
			System.out.println("ExperimentConfig Id: " + exp.getId() + "\tDesc: " +exp.getDescription() );
		}
		System.out.println("Finished printing testGetExperimentConfigsByCharacterizationId results for 3932251: ");
	}
	
	public void testGetFindingsByCharacterizationId() throws Exception{
		String charId = "3932251";
		System.out.println("Testing testGetFindingsByCharacterizationId 3932251: ");
		Finding[] findings = gridClient.getFindingsByCharacterizationId(charId);
		
		for(Finding f: findings){
			System.out.println("Finding Id: " + f.getId() + "\tCreatedBy: " +f.getCreatedBy() );
		}
		System.out.println("Finished printing testGetFindingsByCharacterizationId results for 3932251: ");
	}
	
	public void testGetProtocolByCharacterizationId() throws Exception{
		String charId = "21867791";
		System.out.println("Testing testGetProtocolByCharacterizationId 21867791: ");
		Protocol p = gridClient.getProtocolByCharacterizationId(charId);
		
		System.out.println("Protocol: " + p);
		
		System.out.println("Protocol Id: " + p.getId() + "\tCreatedBy: " +p.getCreatedBy() + "\tName: "
				+ p.getName() + "\tType: " + p.getType());
		
		System.out.println("Finished printing testGetProtocolByCharacterizationId results for 21867791: ");
	}
	
	public void testGetPublicationBySampleId() throws Exception{
		String sampleId = "20917507";
		System.out.println("Testing getPublicationBySampleId 20917507: ");
		Publication[] pubs = gridClient.getPublicationsBySampleId(sampleId);
		
		for(Publication p: pubs){
			System.out.print("Publication Id: " + p.getId() + "\tDesc: " + p.getDescription() +
					"\tName: " + p.getName() + "\tJournalName: " + p.getJournalName() + "\tTitle: " +p.getTitle());
		}
		System.out.println("Finished printing getPrimaryPointOfContactBySampleId results for 20917507: ");
	}
	
	public void testGetSampleIds() throws Exception{
		String[] sampleIds = gridClient.getSampleIds("", "", null, null, null, null, null);
		System.out.println("Testing getSampleIds operation.... The results: ");		
		for(String id: sampleIds){
			System.out.print("sampleId: " + id + " \t");
		}
		System.out.println("\nFinished printing getSampleIds operation results.");
	}
	
	public void testGetPublicationIdsBy() throws Exception{
		String[] pubIds = gridClient.getPublicationIdsBy("", "", "", null, null, null, null, null, null, null, null);
		System.out.println("Testing getPublicationIdsBy operation.... The results: ");		
		for(String id: pubIds){
			System.out.print("publicationId: " + id + " \t");
		}
		System.out.println("\nFinished printing getPublicationIdsBy operation results.");
	}
	
	public void testGetFileByProtocolId() throws Exception{
		String protocolId = "24390915";
		File file = gridClient.getFileByProtocolId(protocolId);
		if(file != null){
			System.out.println("File desc: " + file.getDescription() + "\tName: "+ file.getName() + "\tUri: " +file.getUri());
		}
	}
	
	public void testGetFilesByCompositionInfoId() throws Exception{
		String id = "21376285";//"21376285";
		String className="NanoMaterialEntity";
		File[] files = gridClient.getFilesByCompositionInfoId(id,className);
		if(files != null){
			for(File file: files){
				System.out.println("File desc: " + file.getDescription() + "\tName: "+ file.getName() + "\tUri: " +file.getUri());
			}
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
					test.testGetSampleIds();
					test.testGetKeywordsBySampleId();
					test.testGetPrimaryPointOfContactBySampleId();
					test.testGetExperimentConfigsByCharacterizationId();
					test.testGetFindingsByCharacterizationId();
					test.testGetOtherPointOfContactsBySampleId();
					test.testGetPrimaryPointOfContactBySampleId();
					test.testGetPublicationBySampleId();
					test.testGetProtocolByCharacterizationId();
					test.testGetPublicationIdsBy();
					test.testGetFileByProtocolId();
					test.testGetFilesByCompositionInfoId();
					
					test.testCharacterization();
					test.testNanomaterialEntity();
					test.testFunction();
					test.testComposingElement();
					test.testFunctionalizingEntity();
					test.testSample();
					test.testChemicalAssociation();
					test.testSampleComposition();
					test.testActivationMethod();
					
					
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
