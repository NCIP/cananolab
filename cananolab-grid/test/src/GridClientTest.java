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
//import gov.nih.nci.cananolab.domain.nanomaterial.
import gov.nih.nci.cananolab.domain.agentmaterial.*;
import gov.nih.nci.cananolab.domain.characterization.*;
import gov.nih.nci.cananolab.domain.function.*;
import gov.nih.nci.cananolab.domain.characterization.physical.*;

public class GridClientTest {
	CaNanoLabServiceClient gridClient;

	public GridClientTest(CaNanoLabServiceClient gridClient) {
		this.gridClient = gridClient;
	}

	public void testComposingElement(String id)  {
		//"6062106"
		System.out.println("Testing ComposingElement with id=" + id);
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target
				.setName("gov.nih.nci.cananolab.domain.particle.ComposingElement");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(id);
		target.setAttribute(attribute);
		query.setTarget(target);
		try{
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.ComposingElement");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			ComposingElement ce = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				ce = (ComposingElement) obj;
				System.out.println("ComposingElement: id="+ce.getId() + "\tDesc="+ce.getDescription() + "\tName="+ ce.getName());
			}
		}catch(Exception e){
			System.out.println("Exception getting ComposingElement for id="+ id + ": " + e);
		}
	}
	
	public void testSampleComposition(String id)  {
		//"6160390"
		System.out.println("Testing SampleComposition with id=" + id);
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target
				.setName("gov.nih.nci.cananolab.domain.particle.SampleComposition");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(id);
		target.setAttribute(attribute);
		query.setTarget(target);
		try{
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.SampleComposition");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			SampleComposition sc = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				sc = (SampleComposition) obj;
				System.out.println("SampleComposition : id="+sc.getId() );
			}
		}catch(Exception e){
			System.out.println("Exception getting SampleComposition for id=" + id + ": " +e);
		}
	}
	
	public void testGetAllCharacterizationByCQLQuery()  {
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target
				.setName("gov.nih.nci.cananolab.domain.particle.Characterization");
		/*Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue("10846210");
		target.setAttribute(attribute);*/
		query.setTarget(target);
		try{
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.Characterization");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Characterization chara = null;
			System.out.println("Testing GetAllCharacterizationByCQLQuery, for every characterization test \n"+
					"GetFinding, GetProtocol, GetExperimentConfigs, and Characterization.");
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				chara = (Characterization) obj;
				if(chara != null){
					testGetFindingsByCharacterizationId(chara.getId().toString());
					testGetProtocolByCharacterizationId(chara.getId().toString());
					testGetExperimentConfigsByCharacterizationId(chara.getId().toString());
					testCharacterization(chara.getId().toString());
				}
			}
		}catch(Exception e){
			System.out.println("Exception getting all Characterization by CQLQuery: " + e);
		}
	}
	public void testProtocol(String id)  {
		
		System.out.println("Test Protocol with id="+id);
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target
				.setName("gov.nih.nci.cananolab.domain.common.Protocol");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(id);
		target.setAttribute(attribute);
		query.setTarget(target);
		try{
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.common.Protocol");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Protocol p = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				p = (Protocol) obj;
				System.out.println("Protocol: id="+p.getId() + "\tName=" +p.getName() + "\tAbbreviation=" +p.getAbbreviation() + "\tType=" +p.getType() + "\tVersion=" +p.getVersion());
			}
		}catch(Exception e){
			System.out.println("Exception getting Protocol for id=" + id + ": " +e);
		}
	}
	public void testChemicalAssociation(String id) {
		//"8847369"
		System.out.println("Test ChemicalAssociation with id="+id);
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target
				.setName("gov.nih.nci.cananolab.domain.particle.ChemicalAssociation");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(id);
		target.setAttribute(attribute);
		query.setTarget(target);
		try{
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
		}catch(Exception e){
			System.out.println("Exception getting ChemicalAssociation for id="+ id + ": " + e);
		}
	}
	
	public void testPublication(String id)  {
		//"8847369"
		System.out.println("Test Publication with id="+id);
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target
				.setName("gov.nih.nci.cananolab.domain.common.Publication");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(id);
		target.setAttribute(attribute);
		query.setTarget(target);
		try{
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.common.Publication");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Publication p = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				p = (Publication) obj;
				System.out.println("Publication: id=" + p.getId() + "\tName=" + p.getName() + "\tDesc="+p.getDescription() + "\tTitle=" + p.getTitle());
			}
		}catch(Exception e){
			System.out.println("Exception getting Publication for id=" + id + ": " +e);
		}
	}
	
	public void testActivationMethod(String id) {
		//"3833872"
		System.out.println("Testing ActivationMethod with id="+id);
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target
				.setName("gov.nih.nci.cananolab.domain.particle.ActivationMethod");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(id);
		target.setAttribute(attribute);
		query.setTarget(target);
		try{
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.ActivationMethod");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			ActivationMethod am = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				am = (ActivationMethod) obj;
				System.out.println("Activation Effect: id="+ am.getId() + "\tActivationEffect=" +am.getActivationEffect() + ", Type=" + am.getType());
			}
		}catch(Exception e){
			System.out.println("Exception getting ActivationMethod for id=" + id + ": " +e);
		}
	}
	
	public void testNanomaterialEntity(String id)  {
		//"6160399"
		System.out.println("Testing NanoMaterialEntity with id="+ id);
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target
				.setName("gov.nih.nci.cananolab.domain.particle.NanomaterialEntity");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(id);
		target.setAttribute(attribute);
		query.setTarget(target);
		try{
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.NanomaterialEntity");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			NanomaterialEntity nanoEntity = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				nanoEntity = (NanomaterialEntity) obj;
				System.out.println("NanoMaterial entity: id="+nanoEntity.getId() + "\tDesc=" +nanoEntity.getDescription() + "\tCreatedBy=" +nanoEntity.getCreatedBy());
			}
		}catch(Exception e){
			System.out.println("Exception getting NanomaterialEntity for id=" + id + ": " +e);
		}
	}
	

	public void testSample(String id) {
		System.out.println("Testing Sample with id=" +id);
		//"3735553"
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
		attribute.setValue(id); 
		target.setAttribute(attribute);
		query.setTarget(target);
		try{
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.Sample");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Sample sample = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				sample = (Sample) obj;
				System.out.println("Sample: Name="+sample.getName() + ", id=" + sample.getId());
			}
		}catch(Exception e){
			System.out.println("Exception getting Sample for id=" + id + ": " +e);
		}
	}
	public void testFunction(String id) {
		//"10944705"
		System.out.println("Testing Function with id=" + id);
		CQLQuery query = new CQLQuery(); 
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target
				.setName("gov.nih.nci.cananolab.domain.particle.Function");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(id); 
		target.setAttribute(attribute);
		query.setTarget(target);
		try{
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.Function");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Function fe = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				fe = (Function) obj;
				System.out.println("Function: desc="+fe.getDescription() + "\tId=" + fe.getId());
			}
		}catch(Exception e){
			System.out.println("Exception getting Function for id="+ id + ": " + e);
		}
	}
	
	public void testFunctionalizingEntity(String id) {
		//"6225945"
		System.out.println("Testing FunctionalizingEntity with id=" + id);
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target
				.setName("gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(id); 
		target.setAttribute(attribute);
		query.setTarget(target);
		try{
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			FunctionalizingEntity fe = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				fe = (FunctionalizingEntity) obj;
				System.out.println("FunctionalizingEntity: name="+fe.getName() + "\tId=" + fe.getId());
			}
		}catch(Exception e){
			System.out.println("Exception getting FunctionalizaingEntity for id="+ id + ": " + e);
		}
	}
	public void testCharacterization(String id)  {
		//"10977286"
		System.out.println("Testing characterization with id=" +id);
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target
				.setName("gov.nih.nci.cananolab.domain.particle.Characterization");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(id);
		target.setAttribute(attribute);
		query.setTarget(target);
		try{
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.Characterization");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Characterization chara = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				chara = (Characterization) obj;
				System.out.println("characterization: id="+chara.getId() + "\tDesignMethodDesc: " +chara.getDesignMethodsDescription());
			}
		}catch(Exception e){
			System.out.println("Exception getting Characterization for id="+ id + ": " + e);
		}
	}
	
	public void testGetKeywordsBySampleId(String sampleId) {
		//String sampleId = "20917507";
		System.out.println("Testing getKeyworkdsBySampleId: " + sampleId);
		try{
			Keyword[] keywords = gridClient.getKeywordsBySampleId(sampleId);
			if(keywords != null){
				for(Keyword keyword: keywords){
					if(keyword != null){
						System.out.println("Keyword name: " + keyword.getName() + "\tId: " + keyword.getId());
					}
				}
			}
		}catch(Exception e){
			System.out.println("Exception getting KeywordsBySampleId for sampleId="+ sampleId + ": " + e);
		}
		System.out.println("Finished printing getKeyworkdsBySampleId results for sampleId: " + sampleId);
	}
	
	public void testGetPrimaryPointOfContactBySampleId(String sampleId) {
		//String sampleId = "20917507";
		System.out.println("Testing getPrimaryPointOfContactBySampleId : " + sampleId);
		try{
			PointOfContact contact = gridClient.getPrimaryPointOfContactBySampleId(sampleId);
			if(contact != null){
				System.out.println("primary contact name: " + contact.getFirstName() + "\t" +contact.getLastName() + 
					"\tId: " + contact.getId() + "\tPhone: " + contact.getPhone() + "\tRole: " + 
					contact.getRole() + "\tEmail: " +contact.getEmail());
			}
		}catch(Exception e){
			System.out.println("Exception getting PrimaryPointOfContactBySampleId for sampleId="+ sampleId + ": " + e);
		}
		System.out.println("Finished printing getPrimaryPointOfContactBySampleId results for sampleId: " + sampleId);
	}
	
	public void testGetOtherPointOfContactsBySampleId(String sampleId) {
		//String sampleId = "3735553";
		System.out.println("Testing getOtherPointOfContactsBySampleId : " + sampleId);
		try{
			PointOfContact[] contacts = gridClient.getOtherPointOfContactsBySampleId(sampleId);
			if(contacts != null){
				for(PointOfContact contact: contacts){
					if(contact != null){
						System.out.println("primary contact name: " + contact.getFirstName() + "\t" +contact.getLastName() + 
						"\tId: " + contact.getId() + "\tPhone: " + contact.getPhone() + "\tRole: " + 
						contact.getRole() + "\tEmail: " +contact.getEmail());
					}
				}
			}
		}catch(Exception e){
			System.out.println("Exception getting OtherPointOfContactsBySampleId for sampleId="+ sampleId + ": " + e);
		}
		System.out.println("Finished printing getPrimaryPointOfContactBySampleId results for sampleId: " + sampleId);
	}
	
	public void testGetExperimentConfigsByCharacterizationId(String charId) {
		
		System.out.println("Testing testGetExperimentConfigsByCharacterizationId : " + charId);
		try{
			ExperimentConfig[] experimentConfigs = gridClient.getExperimentConfigsByCharacterizationId(charId);
			
			for(ExperimentConfig exp: experimentConfigs){
				if(exp != null){
					System.out.println("ExperimentConfig Id: " + exp.getId() + "\tDesc: " +exp.getDescription() );
			
				}
			}
		}catch(Exception e){
			System.out.println("Exception getting ExperimentConfigsByCharacterizationId for charid="+ charId + ": " + e);
		}
		System.out.println("Finished printing testGetExperimentConfigsByCharacterizationId results for charId: " + charId);
	}
	
	public void testGetFindingsByCharacterizationId(String charId) {
		//String charId = "3932251";
		System.out.println("Testing testGetFindingsByCharacterizationId : " + charId);
		try{
			Finding[] findings = gridClient.getFindingsByCharacterizationId(charId);
			if(findings != null){
				for(Finding f: findings){
					if(f != null){
						System.out.println("Finding Id: " + f.getId() + "\tCreatedBy: " +f.getCreatedBy() );
					}
				}
			}
		}catch(Exception e){
			System.out.println("Exception getting FindingsByCharacterizationId for charid="+ charId + ": " + e);
		}
		System.out.println("Finished printing testGetFindingsByCharacterizationId results for charId: " + charId);
	}
	
	public void testGetProtocolByCharacterizationId(String charId) {
		//String charId = "21867791";
		System.out.println("Testing testGetProtocolByCharacterizationId : " + charId);
		try{
			Protocol p = gridClient.getProtocolByCharacterizationId(charId);			
			if(p != null){			
				System.out.println("Protocol Id: " + p.getId() + "\tCreatedBy: " +p.getCreatedBy() + "\tName: "
					+ p.getName() + "\tType: " + p.getType());
			}
		}catch(Exception e){
			System.out.println("Exception getting ProtocolByCharacterizationId for charid="+ charId + ": " + e);
		}
		System.out.println("Finished printing testGetProtocolByCharacterizationId results for charId: " + charId);
	}
	
	public void testGetPublicationBySampleId(String sampleId) {
		//String sampleId = "20917507";
		System.out.println("Testing getPublicationBySampleId : " + sampleId);
		try{
		Publication[] pubs = gridClient.getPublicationsBySampleId(sampleId);
		if(pubs != null){
			for(Publication p: pubs){
				if(p != null){
					System.out.print("Publication Id: " + p.getId() + "\tDesc: " + p.getDescription() +
						"\tName: " + p.getName() + "\tJournalName: " + p.getJournalName() + "\tTitle: " +p.getTitle());
				}
			}
		}
		}catch(Exception e){
			System.out.println("Exception when testing getPublicationBySampleId for sampleId=" + sampleId);
		}
		System.out.println("Finished printing getPrimaryPointOfContactBySampleId results for sampleId: " + sampleId);
	}
	
	public void testGetSampleIds() {
		try{
			String[] sampleIds = gridClient.getSampleIds("", "", null, null, null, null, null);
			System.out.println("Testing getSampleIds operation.... \n" +
					"For every sample, test get Publication, keywords, primary contact and other contact.");		
			for(String id: sampleIds){
				testSample(id);
				testGetPublicationBySampleId(id);
				testGetKeywordsBySampleId(id);
				testGetOtherPointOfContactsBySampleId(id);
				testGetPrimaryPointOfContactBySampleId(id);			
			}
		}catch(Exception e){
			System.out.println("Exception getting SampleIds: " + e);
		}
		System.out.println("\nFinished testing samples.... \n");
	}
	
	public void testGetPublicationIdsBy() {
		try{
			String[] pubIds = gridClient.getPublicationIdsBy("", "", "", null, null, null, null, null, null, null, null);
			System.out.println("Testing getPublicationIdsBy operation.... \n " + 
					"For every publication, test Publication");		
			if(pubIds != null){
				for(String id: pubIds){
					if(id != null){
						testPublication(id);
					}
				}
			}
		}catch(Exception e){
			System.out.println("Exception getting PublicationIds: " + e);
		}
		System.out.println("\nFinished testing publications.....\n");
	}
	
	public void testGetFileByProtocolId(String protocolId){
		//String protocolId = "24390915";
		System.out.println("Testing getFileByProtocolId: " + protocolId);
		try{
			File file = gridClient.getFileByProtocolId(protocolId);
			if(file != null){
				System.out.println("File desc: " + file.getDescription() + "\tName: "+ file.getName() + "\tUri: " +file.getUri());
			}
		}catch(Exception e){
			System.out.println("Exception getting ExperimentConfigsByCharacterizationId for protocolid="+ protocolId + ": " + e);
		}
	}
	
	public void testGetFilesByCompositionInfoId(String id, String className) {
		//String id = "21376285";//"21376285";
		//String className="NanoMaterialEntity";
		System.out.println("Test getFilesByCompositionInfoId: id=" + id + ", className=" + className);
		try{
			File[] files = gridClient.getFilesByCompositionInfoId(id,className);
			if(files != null){
				for(File file: files){
					System.out.println("File desc: " + file.getDescription() + "\tName: "+ file.getName() + "\tUri: " +file.getUri());
				}
			}
		}catch(Exception e){
			System.out.println("Exception getting FilesByCompositionInfoId for id="+ id + ", className=" +className + ": " + e);
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
					
					//test.testGetPrimaryPointOfContactBySampleId("10354688");
					//these methods has loops to test other methods
					test.testGetSampleIds();
					test.testGetPublicationIdsBy();
					//test.testGetAllCharacterizationByCQLQuery();
					
					//these methods user can plug in any parameter
					test.testGetFindingsByCharacterizationId("3932251");
					test.testGetProtocolByCharacterizationId("3932251");
					test.testGetExperimentConfigsByCharacterizationId("3932251");
					test.testCharacterization("3932251");
					test.testGetFileByProtocolId("24390915");
					test.testGetFilesByCompositionInfoId("21376285","NanomaterialEntity");					
					test.testNanomaterialEntity("6160399");
					test.testFunction("10944705");
					test.testComposingElement("6062106");
					test.testFunctionalizingEntity("6225945");
					
					test.testChemicalAssociation("8847369");
					test.testSampleComposition("6160390");
					test.testActivationMethod("3833872");
					test.testProtocol("24390915");
					
					
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