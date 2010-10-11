import gov.nih.nci.cagrid.cananolab.client.CaNanoLabServiceClient;
import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cananolab.domain.agentmaterial.OtherFunctionalizingEntity;
import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.Condition;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.common.ExperimentConfig;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.common.Technique;
import gov.nih.nci.cananolab.domain.linkage.OtherChemicalAssociation;
import gov.nih.nci.cananolab.domain.nanomaterial.OtherNanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.ActivationMethod;
import gov.nih.nci.cananolab.domain.particle.AssociatedElement;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

public class GridClientTest {
	CaNanoLabServiceClient gridClient;
	StringBuilder builder= new StringBuilder();
	private static Logger logger = Logger.getLogger(GridClientTest.class);
	
	public GridClientTest(CaNanoLabServiceClient gridClient) {
		this.gridClient = gridClient;
	}

	public void testComposingElement(String id) {
		// "6062106"
		System.out.println("Testing ComposingElement with id=" + id);
		
		builder.append("Testing ComposingElement with id=" + id + "\n");
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
		try {
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.ComposingElement");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			ComposingElement ce = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				ce = (ComposingElement) obj;
				System.out.println("ComposingElement: id=" + ce.getId()
						+ "\tDesc=" + ce.getDescription() + "\tName="
						+ ce.getName());
				builder.append("ComposingElement: id=" + ce.getId()
						+ "\tDesc=" + ce.getDescription() + "\tName="
						+ ce.getName() + "\n");
				// TODO add CQL for functions by composing element id
				loadInherentFunctionsForComposingElement(ce);

			}
		} catch (Exception e) {
			System.out.println("Exception getting ComposingElement for id="
					+ id + ": " + e);
			builder.append("Exception getting ComposingElement for id="
					+ id + ": " + e + "\n");
		}
	}

	public void testSampleComposition(String id) {
		// "6160390"
		System.out.println("Testing SampleComposition with id=" + id);
		
		builder.append("Testing SampleComposition with id=" + id + "\n");
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
		try {
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.SampleComposition");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			SampleComposition sc = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				sc = (SampleComposition) obj;
				System.out.println("SampleComposition : id=" + sc.getId());
				builder.append("SampleComposition : id=" + sc.getId());
			}
		} catch (Exception e) {
			System.out.println("Exception getting SampleComposition for id="
					+ id + ": " + e);
			builder.append("Exception getting SampleComposition for id="
					+ id + ": " + e + "\n");
		}
	}

	public void testGetAllCharacterizationByCQLQuery() {
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target
				.setName("gov.nih.nci.cananolab.domain.particle.Characterization");
		/*
		 * Attribute attribute = new Attribute(); attribute.setName("id");
		 * attribute.setPredicate(Predicate.EQUAL_TO);
		 * attribute.setValue("10846210"); target.setAttribute(attribute);
		 */
		query.setTarget(target);
		try {
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.Characterization");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Characterization chara = null;
			System.out
					.println("Testing GetAllCharacterizationByCQLQuery, for every characterization test \n"
							+ "GetFinding, GetProtocol, GetExperimentConfigs, and Characterization.");
			builder.append("Testing GetAllCharacterizationByCQLQuery, for every characterization test \n"
							+ "GetFinding, GetProtocol, GetExperimentConfigs, and Characterization." + "\n");
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				chara = (Characterization) obj;
				if (chara != null) {
					testGetFindingsByCharacterizationId(chara.getId()
							.toString());
					testGetProtocolByCharacterizationId(chara.getId()
							.toString());
					testGetExperimentConfigsByCharacterizationId(chara.getId()
							.toString());
					testCharacterization(chara.getId().toString());
				}
			}
		} catch (Exception e) {
			System.out
					.println("Exception getting all Characterization by CQLQuery: "
							+ e);
			builder.append("Exception getting all Characterization by CQLQuery: "
							+ e + "\n");
		}
	}

	public void testProtocol(String id) {

		System.out.println("Test Protocol with id=" + id);
		builder.append("Test Protocol with id=" + id + "\n");
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName("gov.nih.nci.cananolab.domain.common.Protocol");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(id);
		target.setAttribute(attribute);
		query.setTarget(target);
		try {
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.common.Protocol");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Protocol p = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				p = (Protocol) obj;
				System.out.println("Protocol: id=" + p.getId() + "\tName="
						+ p.getName() + "\tAbbreviation=" + p.getAbbreviation()
						+ "\tType=" + p.getType() + "\tVersion="
						+ p.getVersion());
				builder.append("Protocol: id=" + p.getId() + "\tName="
						+ p.getName() + "\tAbbreviation=" + p.getAbbreviation()
						+ "\tType=" + p.getType() + "\tVersion="
						+ p.getVersion() + "\n");
				// add function for get protocol file by protocol id
				testGetFileByProtocolId(p.getId().toString());
			}
		} catch (Exception e) {
			System.out.println("Exception getting Protocol for id=" + id + ": "
					+ e);
			builder.append("Exception getting Protocol for id=" + id + ": "
					+ e + "\n");
		}
	}

	public void testGetAllProtocolsByCQLQuery() {
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName("gov.nih.nci.cananolab.domain.common.Protocol");
		query.setTarget(target);
		try {
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.common.Protocol");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Protocol p = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				p = (Protocol) obj;
				System.out.println("Protocol: id=" + p.getId() + "\tName="
						+ p.getName() + "\tAbbreviation=" + p.getAbbreviation()
						+ "\tType=" + p.getType() + "\tVersion="
						+ p.getVersion());
				builder.append("Protocol: id=" + p.getId() + "\tName="
						+ p.getName() + "\tAbbreviation=" + p.getAbbreviation()
						+ "\tType=" + p.getType() + "\tVersion="
						+ p.getVersion() + "\n");
				// add function for get protocol file by protocol id
				testGetFileByProtocolId(p.getId().toString());
			}
		} catch (Exception e) {
			System.out.println("Exception getting all protocols: " + e);
			builder.append("Exception getting all protocols: " + e + "\n");
		}
	}

	public void testChemicalAssociation(String id) {
		// "8847369"
		System.out.println("Test ChemicalAssociation with id=" + id);
		builder.append("Test ChemicalAssociation with id=" + id + "\n");
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
		try {
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.ChemicalAssociation");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			ChemicalAssociation ca = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				ca = (ChemicalAssociation) obj;
				System.out.println("ChemicalAssociation Desc: "
						+ ca.getDescription() + ", id: " + ca.getId());
				builder.append("ChemicalAssociation Desc: "
						+ ca.getDescription() + ", id: " + ca.getId() + "\n");
			}
		} catch (Exception e) {
			System.out.println("Exception getting ChemicalAssociation for id="
					+ id + ": " + e);
			builder.append("Exception getting ChemicalAssociation for id="
					+ id + ": " + e + "\n");
		}
	}

	public void testPublication(String id) {
		// "8847369"
		System.out.println("Test Publication with id=" + id);
		builder.append("Test Publication with id=" + id + "\n");
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName("gov.nih.nci.cananolab.domain.common.Publication");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(id);
		target.setAttribute(attribute);
		query.setTarget(target);
		try {
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.common.Publication");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Publication p = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				p = (Publication) obj;
				System.out.println("Publication: id=" + p.getId() + "\tName="
						+ p.getName() + "\tDesc=" + p.getDescription()
						+ "\tTitle=" + p.getTitle());
				builder.append("Publication: id=" + p.getId() + "\tName="
						+ p.getName() + "\tDesc=" + p.getDescription()
						+ "\tTitle=" + p.getTitle() + "\n");
				// TODO add CQL for authors and keywords by publication
				// id
				loadAuthorsForPublication(p);
				Collection<Author> authorCollection = p.getAuthorCollection();
				for (java.util.Iterator<Author> itr = authorCollection
						.iterator(); itr.hasNext();) {
					Author a = itr.next();
					System.out.println("Author First Name=" + a.getFirstName()
							+ "\tLast Name=" + a.getLastName());
					builder.append("Author First Name=" + a.getFirstName()
							+ "\tLast Name=" + a.getLastName() + "\n");
				}
				loadKeywordsForPublication(p);
				Collection<Keyword> keywordCollection = p
						.getKeywordCollection();
				for (java.util.Iterator<Keyword> itr = keywordCollection
						.iterator(); itr.hasNext();) {
					Keyword k = itr.next();
					System.out.println("Keyword Name=" + k.getName() + "\tId="
							+ k.getId());
					builder.append("Keyword Name=" + k.getName() + "\tId="
							+ k.getId() + "\n");
				}
			}
		} catch (Exception e) {
			System.out.println("Exception getting Publication for id=" + id
					+ ": " + e);
			builder.append("Exception getting Publication for id=" + id
					+ ": " + e + "\n");
		}
	}

	private void loadAuthorsForPublication(Publication publication) {
		CQLQuery query = new CQLQuery();
		builder.append("Test get AuthorsForPublication id=" + publication.getId() + "\n");
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName("gov.nih.nci.cananolab.domain.common.Author");
		Association association = new Association();
		association.setName("gov.nih.nci.cananolab.domain.common.Publication");
		association.setRoleName("publicationCollection");

		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(publication.getId().toString());
		association.setAttribute(attribute);

		target.setAssociation(association);
		query.setTarget(target);
		try {
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.common.Author");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Author author = null;
			publication.setAuthorCollection(new HashSet<Author>());
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				author = (Author) obj;
				System.out.println("Author id=" + author.getId() + "\tName=" + author.getFirstName() + 
						" " + author.getLastName());
				builder.append("Author id=" + author.getId() + "\tName=" + author.getFirstName() + 
						" " + author.getLastName() + "\n");
				publication.getAuthorCollection().add(author);
			}
		} catch (Exception e) {
			System.out.println("Exception getting Authors for Publication id="
					+ publication.getId() + ": " + e);
			builder.append("Exception getting Authors for Publication id="
					+ publication.getId() + ": " + e + "\n");
		}
	}

	private void loadKeywordsForPublication(Publication publication) {
		// load keywords for a file
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName("gov.nih.nci.cananolab.domain.common.Keyword");
		Association association = new Association();
		association.setName("gov.nih.nci.cananolab.domain.common.File");
		association.setRoleName("fileCollection");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(publication.getId().toString());
		association.setAttribute(attribute);
		target.setAssociation(association);
		query.setTarget(target);
		try {
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.common.Keyword");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Set<Keyword> keywords = new HashSet<Keyword>();
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();				
				Keyword keyword = (Keyword) obj;
				builder.append("Keyword id=" +keyword.getId() + "\tName=" + keyword.getName() + "\n");
				keywords.add(keyword);
			}
			publication.setKeywordCollection(keywords);
		} catch (Exception e) {
			System.out.println("Exception getting keyword for publication: "
					+ e);
			builder.append("Exception getting keyword for publication: "
					+ e + "\n");
		}
	}

	public void testActivationMethod(String id) {
		// "3833872"
		System.out.println("Testing ActivationMethod with id=" + id);
		builder.append("Testing ActivationMethod with id=" + id + "\n");
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
		try {
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.ActivationMethod");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			ActivationMethod am = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				am = (ActivationMethod) obj;
				System.out.println("Activation Effect: id=" + am.getId()
						+ "\tActivationEffect=" + am.getActivationEffect()
						+ ", Type=" + am.getType());
				builder.append("Activation Effect: id=" + am.getId()
						+ "\tActivationEffect=" + am.getActivationEffect()
						+ ", Type=" + am.getType() + "\n");
			}
		} catch (Exception e) {
			System.out.println("Exception getting ActivationMethod for id="
					+ id + ": " + e);
			builder.append("Exception getting ActivationMethod for id="
					+ id + ": " + e + "\n");
		}
	}

	public void testNanomaterialEntity(String id) {
		// "6160399"
		System.out.println("Testing NanoMaterialEntity with id=" + id);
		builder.append("Testing NanoMaterialEntity with id=" + id + "\n");
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
		try {
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.NanomaterialEntity");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			NanomaterialEntity nanoEntity = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				nanoEntity = (NanomaterialEntity) obj;
				System.out.println("NanoMaterial entity: id="
						+ nanoEntity.getId() + "\tDesc="
						+ nanoEntity.getDescription() + "\tCreatedBy="
						+ nanoEntity.getCreatedBy());
				builder.append("NanoMaterial entity: id="
						+ nanoEntity.getId() + "\tDesc="
						+ nanoEntity.getDescription() + "\tCreatedBy="
						+ nanoEntity.getCreatedBy() + "\n");
			}
		} catch (Exception e) {
			System.out.println("Exception getting NanomaterialEntity for id="
					+ id + ": " + e);
			builder.append("Exception getting NanomaterialEntity for id="
					+ id + ": " + e + "\n");
		}
	}

	public void testSample(String id) {
		System.out.println("Testing Sample with id=" + id);
		builder.append("Testing Sample with id=" + id + "\n");
		// "3735553"
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName("gov.nih.nci.cananolab.domain.particle.Sample");
		Attribute attribute = new Attribute();
		/*
		 * attribute.setName("name"); attribute.setPredicate(Predicate.LIKE);
		 * attribute.setValue("NCL-23-1"); //"20917507"); //NCL-23-1
		 */
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(id);
		target.setAttribute(attribute);
		query.setTarget(target);
		try {
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.Sample");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Sample sample = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				sample = (Sample) obj;
				System.out.println("Sample: Name=" + sample.getName() + ", id="
						+ sample.getId());
				builder.append("Sample: Name=" + sample.getName() + ", id="
						+ sample.getId() + "\n");
				//testGetPublicationsBySampleId(id);
				testGetKeywordsBySampleId(id);
				testGetOtherPointOfContactsBySampleId(id);
				testGetPrimaryPointOfContactBySampleId(id);
				testGetCharacterizationsBySampleIdByCQLQuery(id);
				testGetCompositionBySampleIdByCQLQuery(id);
				
			}
		} catch (Exception e) {
			System.out.println("Exception getting Sample for id=" + id + ": "
					+ e);
			builder.append("Exception getting Sample for id=" + id + ": "
					+ e + "\n");
		}
	}

	public void testFunction(String id) {
		// "10944705"
		System.out.println("Testing Function with id=" + id);
		builder.append("Testing Function with id=" + id + "\n");
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName("gov.nih.nci.cananolab.domain.particle.Function");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(id);
		target.setAttribute(attribute);
		query.setTarget(target);
		try {
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.Function");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Function fe = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				fe = (Function) obj;
				System.out.println("Function: desc=" + fe.getDescription()
						+ "\tId=" + fe.getId());
				builder.append("Function: desc=" + fe.getDescription()
						+ "\tId=" + fe.getId() + "\n");
			}
		} catch (Exception e) {
			System.out.println("Exception getting Function for id=" + id + ": "
					+ e);
			builder.append("Exception getting Function for id=" + id + ": "
					+ e + "\n");
		}
	}

	public void testFunctionalizingEntity(String id) {
		// "6225945"
		System.out.println("Testing FunctionalizingEntity with id=" + id);
		builder.append("Testing FunctionalizingEntity with id=" + id + "\n");
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
		try {
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			FunctionalizingEntity fe = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				fe = (FunctionalizingEntity) obj;
				System.out.println("FunctionalizingEntity: name="
						+ fe.getName() + "\tId=" + fe.getId());
				builder.append("FunctionalizingEntity: name="
						+ fe.getName() + "\tId=" + fe.getId() + "\n");
			}
		} catch (Exception e) {
			System.out
					.println("Exception getting FunctionalizaingEntity for id="
							+ id + ": " + e);
			builder.append("Exception getting FunctionalizaingEntity for id="
					+ id + ": " + e + "\n");
		}
	}

	public void testCharacterization(String id) {
		// "10977286"
		System.out.println("Testing characterization with id=" + id);
		builder.append("Testing characterization with id=" + id + "\n");
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
		try {
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.Characterization");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Characterization chara = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				chara = (Characterization) obj;
				System.out.println("characterization: id=" + chara.getId()
						+ "\tDesignMethodDesc: "
						+ chara.getDesignMethodsDescription());
				builder.append("characterization: id=" + chara.getId()
						+ "\tDesignMethodDesc: "
						+ chara.getDesignMethodsDescription() + "\n");
				
			}
		} catch (Exception e) {
			System.out.println("Exception getting Characterization for id="
					+ id + ": " + e);
			builder.append("Exception getting Characterization for id="
					+ id + ": " + e + "\n");
		}
	}

	public void testGetKeywordsBySampleId(String sampleId) {
		// String sampleId = "20917507";
		System.out.println("Testing getKeyworkdsBySampleId: " + sampleId);
		builder.append("Testing getKeyworkdsBySampleId: " + sampleId);
		try {
			Keyword[] keywords = gridClient.getKeywordsBySampleId(sampleId);
			if (keywords != null) {
				for (Keyword keyword : keywords) {
					if (keyword != null) {
						System.out.println("Keyword name: " + keyword.getName()
								+ "\tId: " + keyword.getId());
						builder.append("Keyword name: " + keyword.getName()
								+ "\tId: " + keyword.getId() + "\n");
					}
				}
			}
		} catch (Exception e) {
			System.out
					.println("Exception getting KeywordsBySampleId for sampleId="
							+ sampleId + ": " + e);
			builder.append("Exception getting KeywordsBySampleId for sampleId="
					+ sampleId + ": " + e + "\n");
		}
		System.out
				.println("Finished printing getKeyworkdsBySampleId results for sampleId: "
						+ sampleId);
		builder.append("Finished printing getKeyworkdsBySampleId results for sampleId: "
				+ sampleId + "\n");
	}

	public void testGetPrimaryPointOfContactBySampleId(String sampleId) {
		// String sampleId = "20917507";
		System.out.println("Testing getPrimaryPointOfContactBySampleId : "
				+ sampleId);
		builder.append("Testing getPrimaryPointOfContactBySampleId : "
				+ sampleId + "\n");
		try {
			PointOfContact contact = gridClient
					.getPrimaryPointOfContactBySampleId(sampleId);
			if (contact != null) {
				System.out.println("primary contact name: "
						+ contact.getFirstName() + "\t" + contact.getLastName()
						+ "\tId: " + contact.getId() + "\tPhone: "
						+ contact.getPhone() + "\tRole: " + contact.getRole()
						+ "\tEmail: " + contact.getEmail());
				builder.append("primary contact name: "
						+ contact.getFirstName() + "\t" + contact.getLastName()
						+ "\tId: " + contact.getId() + "\tPhone: "
						+ contact.getPhone() + "\tRole: " + contact.getRole()
						+ "\tEmail: " + contact.getEmail() + "\n");
				// TODO add CQL for organization by POC id
				System.out
						.println("Getting Organization for point of contact id="
								+ contact.getId());
				builder.append("Getting Organization for point of contact id="
						+ contact.getId() + "\n");
				loadOrganizationForPointOfContact(contact);

			}
		} catch (Exception e) {
			System.out
					.println("Exception getting PrimaryPointOfContactBySampleId for sampleId="
							+ sampleId + ": " + e);
			builder.append("Exception getting PrimaryPointOfContactBySampleId for sampleId="
					+ sampleId + ": " + e + "\n");
		}
		System.out
				.println("Finished printing getPrimaryPointOfContactBySampleId results for sampleId: "
						+ sampleId );
		
	}

	private void loadOrganizationForPointOfContact(PointOfContact poc) {

		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName("gov.nih.nci.cananolab.domain.common.Organization");
		Association association = new Association();
		association
				.setName("gov.nih.nci.cananolab.domain.common.PointOfContact");
		association.setRoleName("pointOfContactCollection");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(poc.getId().toString());
		association.setAttribute(attribute);
		target.setAssociation(association);
		query.setTarget(target);
		try {
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.common.Organization");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Organization org = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				org = (Organization) obj;
				if (org != null) {
					System.out.println("Organization: id=" + org.getId()
							+ "\tName=" + org.getName() + "\tAddress="
							+ org.getStreetAddress1() + " " + org.getCity()
							+ " " + org.getState() + " " + org.getPostalCode());
					builder.append("Organization: id=" + org.getId()
							+ "\tName=" + org.getName() + "\tAddress="
							+ org.getStreetAddress1() + " " + org.getCity()
							+ " " + org.getState() + " " + org.getPostalCode() + "\n");
				}
			}
			poc.setOrganization(org);
		} catch (Exception e) {
			System.out
					.println("Exception getting organization for point of contact id="
							+ poc.getId() + ": " + e);
			builder.append("Exception getting organization for point of contact id="
					+ poc.getId() + ": " + e + "\n");
		}
	}

	public void testGetOtherPointOfContactsBySampleId(String sampleId) {
		// String sampleId = "3735553";
		System.out.println("Testing getOtherPointOfContactsBySampleId : "
				+ sampleId);
		builder.append("Testing getOtherPointOfContactsBySampleId : "
				+ sampleId + "\n");
		try {
			PointOfContact[] contacts = gridClient
					.getOtherPointOfContactsBySampleId(sampleId);
			if (contacts != null) {
				for (PointOfContact contact : contacts) {
					if (contact != null) {
						System.out.println("primary contact name: "
								+ contact.getFirstName() + "\t"
								+ contact.getLastName() + "\tId: "
								+ contact.getId() + "\tPhone: "
								+ contact.getPhone() + "\tRole: "
								+ contact.getRole() + "\tEmail: "
								+ contact.getEmail());
						builder.append("primary contact name: "
								+ contact.getFirstName() + "\t"
								+ contact.getLastName() + "\tId: "
								+ contact.getId() + "\tPhone: "
								+ contact.getPhone() + "\tRole: "
								+ contact.getRole() + "\tEmail: "
								+ contact.getEmail() + "\n");
						// TODO add CQL for organization by POC id
						System.out
								.println("Getting Organization for other point of contact id="
										+ contact.getId());
						builder.append("Getting Organization for other point of contact id="
										+ contact.getId() + "\n");
						loadOrganizationForPointOfContact(contact);
					}
				}
			}
		} catch (Exception e) {
			System.out
					.println("Exception getting OtherPointOfContactsBySampleId for sampleId="
							+ sampleId + ": " + e);
			builder.append("Exception getting OtherPointOfContactsBySampleId for sampleId="
					+ sampleId + ": " + e + "\n");
		}
		System.out
				.println("Finished printing getPrimaryPointOfContactBySampleId results for sampleId: "
						+ sampleId);
	}

	public void testGetExperimentConfigsByCharacterizationId(String charId) {

		System.out
				.println("Testing testGetExperimentConfigsByCharacterizationId : "
						+ charId);
		builder.append("Testing testGetExperimentConfigsByCharacterizationId : "
				+ charId + "\n");
		try {
			ExperimentConfig[] experimentConfigs = gridClient
					.getExperimentConfigsByCharacterizationId(charId);

			if (experimentConfigs != null) {
				for (ExperimentConfig exp : experimentConfigs) {
					if (exp != null) {
						System.out.println("ExperimentConfig Id: "
								+ exp.getId() + "\tDesc: "
								+ exp.getDescription());
						builder.append("ExperimentConfig Id: "
								+ exp.getId() + "\tDesc: "
								+ exp.getDescription() + "\n");
						// TODO add CQL for instruments and technique by
						// experiment
						// config id
						loadTechniqueForConfig(exp);
						loadInstrumentsForConfig(exp);

					}
				}
			}
		} catch (Exception e) {
			System.out
					.println("Exception getting ExperimentConfigsByCharacterizationId for charid="
							+ charId + ": " + e);
			builder.append("Exception getting ExperimentConfigsByCharacterizationId for charid="
					+ charId + ": " + e + "\n");
		}
		System.out
				.println("Finished printing testGetExperimentConfigsByCharacterizationId results for charId: "
						+ charId);
	}

	private void loadTechniqueForConfig(ExperimentConfig config) {
		System.out.println("getting Technique for Config id=" + config.getId());
		builder.append("getting Technique for Config id=" + config.getId() + "\n");
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName("gov.nih.nci.cananolab.domain.common.Technique");
		Association association = new Association();
		association
				.setName("gov.nih.nci.cananolab.domain.common.ExperimentConfig");
		association.setRoleName("experimentConfigCollection");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(config.getId().toString());
		association.setAttribute(attribute);

		target.setAssociation(association);
		query.setTarget(target);
		try {
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.common.Technique");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Technique technique = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				technique = (Technique) obj;
				if (technique != null) {
					System.out.println("Technique id=" + technique.getId()
							+ "\tAbbreviation=" + technique.getAbbreviation()
							+ "\tType=" + technique.getType());
					builder.append("Technique id=" + technique.getId()
							+ "\tAbbreviation=" + technique.getAbbreviation()
							+ "\tType=" + technique.getType() + "\n");
				}
			}
			config.setTechnique(technique);
		} catch (Exception e) {
			System.out
					.println("Exception getting Technique for ExperimentConfig id="
							+ config.getId() + ": " + e);
			builder.append("Exception getting Technique for ExperimentConfig id="
					+ config.getId() + ": " + e + "\n");
		}
	}

	private void loadInstrumentsForConfig(ExperimentConfig config) {
		System.out.println("Getting Instruments for Config id="
				+ config.getId());
		builder.append("Getting Instruments for Config id="
				+ config.getId() + "\n");
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName("gov.nih.nci.cananolab.domain.common.Instrument");
		Association association = new Association();
		association
				.setName("gov.nih.nci.cananolab.domain.common.ExperimentConfig");
		association.setRoleName("experimentConfigCollection");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(config.getId().toString());
		association.setAttribute(attribute);

		target.setAssociation(association);
		query.setTarget(target);
		try {
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.common.Instrument");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			config.setInstrumentCollection(new HashSet<Instrument>());
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				Instrument instrument = (Instrument) obj;
				config.getInstrumentCollection().add(instrument);
				if (instrument != null) {
					System.out.println("Instrument id=" + instrument.getId()
							+ "\tType=" + instrument.getType() + "\tModelName="
							+ instrument.getModelName() + "\tManufacturer="
							+ instrument.getManufacturer());
					builder.append("Instrument id=" + instrument.getId()
							+ "\tType=" + instrument.getType() + "\tModelName="
							+ instrument.getModelName() + "\tManufacturer="
							+ instrument.getManufacturer() + "\n");
				}
			}
		} catch (Exception e) {
			System.out
					.println("Exception getting Instrument for ExperimentConfig id="
							+ config.getId() + ": " + e);
			builder.append("Exception getting Instrument for ExperimentConfig id="
					+ config.getId() + ": " + e + "\n");
		}
	}

	public void testGetFindingsByCharacterizationId(String charId) {
		// String charId = "3932251";
		System.out.println("Testing testGetFindingsByCharacterizationId : "
				+ charId);
		builder.append("Testing testGetFindingsByCharacterizationId : "
				+ charId + "\n");
		try {
			Finding[] findings = gridClient
					.getFindingsByCharacterizationId(charId);
			if (findings != null) {
				for (Finding f : findings) {
					if (f != null) {
						System.out.println("Finding Id: " + f.getId()
								+ "\tCreatedBy: " + f.getCreatedBy());
						builder.append("Finding Id: " + f.getId()
								+ "\tCreatedBy: " + f.getCreatedBy() + "\n");
						// TODO add CQL for data and files by finding id, and
						// conditions by datum id
						loadDataForFinding(f);
						loadFilesForFinding(f);
					}
				}
			}
		} catch (Exception e) {
			System.out
					.println("Exception getting FindingsByCharacterizationId for charid="
							+ charId + ": " + e);
			builder.append("Exception getting FindingsByCharacterizationId for charid="
					+ charId + ": " + e + "\n");
		}
		System.out
				.println("Finished printing testGetFindingsByCharacterizationId results for charId: "
						+ charId);
	}

	private void loadDataForFinding(Finding finding) {
		System.out.println("Getting Data for Finding id=" + finding.getId());
		builder.append("Getting Data for Finding id=" + finding.getId() + "\n");
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName("gov.nih.nci.cananolab.domain.common.Datum");
		Association association = new Association();
		association.setName("gov.nih.nci.cananolab.domain.common.Finding");
		association.setRoleName("finding");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(finding.getId().toString());
		association.setAttribute(attribute);

		target.setAssociation(association);
		query.setTarget(target);
		try {
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.common.Datum");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			finding.setDatumCollection(new HashSet<Datum>());
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				Datum datum = (Datum) obj;
				if (datum != null) {
					System.out.println("Datum id=" + datum.getId() + "\tName="
							+ datum.getName() + "\tValueType="
							+ datum.getValueType() + "\tValueUnit="
							+ datum.getValueUnit() + "\tValue="
							+ datum.getValue());
					builder.append("Datum id=" + datum.getId() + "\tName="
							+ datum.getName() + "\tValueType="
							+ datum.getValueType() + "\tValueUnit="
							+ datum.getValueUnit() + "\tValue="
							+ datum.getValue() + "\n");
					loadConditionsForDatum(datum);
					finding.getDatumCollection().add(datum);
				}
			}
		} catch (Exception e) {
			System.out.println("Exception getting Data for Finding id="
					+ finding.getId() + ": " + e);
			builder.append("Exception getting Data for Finding id="
					+ finding.getId() + ": " + e + "\n");
		}
	}

	private void loadConditionsForDatum(Datum datum) {
		System.out.println("Getting Condition for Datum id=" + datum.getId());
		builder.append("Getting Condition for Datum id=" + datum.getId() + "\n");
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName("gov.nih.nci.cananolab.domain.common.Condition");
		Association association = new Association();
		association.setName("gov.nih.nci.cananolab.domain.common.Datum");
		association.setRoleName("datumCollection");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(datum.getId().toString());
		association.setAttribute(attribute);

		target.setAssociation(association);
		query.setTarget(target);
		try {
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.common.Condition");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			datum.setConditionCollection(new HashSet<Condition>());
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				Condition condition = (Condition) obj;
				if (condition != null) {
					System.out.println("Condition id=" + condition.getId()
							+ "\tName=" + condition.getName() + "\tProperty="
							+ condition.getProperty() + "\tValue="
							+ condition.getValue() + "\tValueType="
							+ condition.getValueType() + "\tValueUnit="
							+ condition.getValueUnit());
					builder.append("Condition id=" + condition.getId()
							+ "\tName=" + condition.getName() + "\tProperty="
							+ condition.getProperty() + "\tValue="
							+ condition.getValue() + "\tValueType="
							+ condition.getValueType() + "\tValueUnit="
							+ condition.getValueUnit() + "\n");
				}
				datum.getConditionCollection().add(condition);
			}
		} catch (Exception e) {
			System.out.println("Exception getting Conditions for Datum id="
					+ datum.getId() + ": " + e);
			builder.append("Exception getting Conditions for Datum id="
					+ datum.getId() + ": " + e + "\n");
		}
	}

	private void loadFilesForFinding(Finding finding) {
		System.out.println("Getting Files for Finding id=" + finding.getId());
		builder.append("Getting Files for Finding id=" + finding.getId() + "\n");
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName("gov.nih.nci.cananolab.domain.common.File");
		Association association = new Association();
		association.setName("gov.nih.nci.cananolab.domain.common.Finding");
		association.setRoleName("findingCollection");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(finding.getId().toString());
		association.setAttribute(attribute);

		target.setAssociation(association);
		query.setTarget(target);
		try {
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.common.File");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			finding.setFileCollection(new HashSet<File>());
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				File file = (File) obj;
				if (file != null) {
					System.out.println("File id=" + file.getId() + "\tName="
							+ file.getName() + "\tDesc="
							+ file.getDescription() + "\tTitle="
							+ file.getTitle() + "\tUri=" + file.getUri());
					builder.append("File id=" + file.getId() + "\tName="
							+ file.getName() + "\tDesc="
							+ file.getDescription() + "\tTitle="
							+ file.getTitle() + "\tUri=" + file.getUri() + "\n");
				}
				finding.getFileCollection().add(file);
			}
		} catch (Exception e) {
			System.out.println("Exception getting Files for Finding id="
					+ finding.getId() + ": " + e);
			builder.append("Exception getting Files for Finding id="
					+ finding.getId() + ": " + e + "\n");
		}
	}

	public void testGetProtocolByCharacterizationId(String charId) {
		// String charId = "21867791";
		System.out.println("Testing testGetProtocolByCharacterizationId : "
				+ charId);
		builder.append("Testing testGetProtocolByCharacterizationId : "
				+ charId + "\n");
		try {
			Protocol p = gridClient.getProtocolByCharacterizationId(charId);
			if (p != null) {
				System.out.println("Protocol Id: " + p.getId()
						+ "\tCreatedBy: " + p.getCreatedBy() + "\tName: "
						+ p.getName() + "\tType: " + p.getType());
				builder.append("Protocol Id: " + p.getId()
						+ "\tCreatedBy: " + p.getCreatedBy() + "\tName: "
						+ p.getName() + "\tType: " + p.getType() + "\n");
			}
		} catch (Exception e) {
			System.out
					.println("Exception getting ProtocolByCharacterizationId for charid="
							+ charId + ": " + e);
			builder.append("Exception getting ProtocolByCharacterizationId for charid="
					+ charId + ": " + e + "\n");
		}
		System.out
				.println("Finished printing testGetProtocolByCharacterizationId results for charId: "
						+ charId + "\n");
	}

	public void testGetPublicationsBySampleId(String sampleId) {
		// String sampleId = "20917507";
		System.out.println("Testing getPublicationBySampleId : " + sampleId);
		builder.append("Testing getPublicationBySampleId : " + sampleId + "\n");
		try {
			Publication[] pubs = gridClient.getPublicationsBySampleId(sampleId);
			if (pubs != null) {
				for (Publication p : pubs) {
					if (p != null) {
						System.out.print("Publication Id: " + p.getId()
								+ "\tDesc: " + p.getDescription() + "\tName: "
								+ p.getName() + "\tJournalName: "
								+ p.getJournalName() + "\tTitle: "
								+ p.getTitle());
						builder.append("Publication Id: " + p.getId()
								+ "\tDesc: " + p.getDescription() + "\tName: "
								+ p.getName() + "\tJournalName: "
								+ p.getJournalName() + "\tTitle: "
								+ p.getTitle() + "\n");
						
					}
				}
			}
		} catch (Exception e) {
			System.out
					.println("Exception when testing getPublicationBySampleId for sampleId="
							+ sampleId);
			builder.append("Exception when testing getPublicationBySampleId for sampleId="
					+ sampleId + "\n");
		}
		System.out
				.println("Finished printing getPrimaryPointOfContactBySampleId results for sampleId: "
						+ sampleId);
	}

	public void testGetSampleIds(int index) {
		try {
			String[] sampleIds = gridClient.getSampleIds("", "", null, null,
					null, null, null);
			System.out
					.println("Testing getSampleIds operation.... \n"
							+ "For every sample, test get Publication, keywords, primary contact and other contact.");
			builder.append("Testing getSampleIds operation.... \n"
					+ "For every sample, test get Publication, keywords, primary contact and other contact." + "\n");
			int originalIndex = index;
			while(index <= sampleIds.length){
				//get the sample at location index
				String id = sampleIds[index];
				testSample(id);
				index = index + originalIndex;
				//System.out.println("index = " + index);
			}			
		} catch (Exception e) {
			System.out.println("Exception getting SampleIds: " + e);
			builder.append("Exception getting SampleIds: " + e + "\n");
		}
		System.out.println("\nFinished testing samples.... \n");
	}
	
	/*public void testCharacterizationAndCompositionBySampleIds(int index) {
		try {
			String[] sampleIds = gridClient.getSampleIds("", "", null, null,
					null, null, null);
			System.out
					.println("Testing getSampleIds operation.... \n"
							+ "For every sample, test get Publication, keywords, primary contact and other contact.");
			builder.append("Testing getSampleIds operation.... \n"
					+ "For every sample, test get Publication, keywords, primary contact and other contact." + "\n");
			
			int originalIndex = index;
			while(index <= sampleIds.length){
				//get the sample at location index
				String id = sampleIds[index];
				testGetCharacterizationsBySampleIdByCQLQuery(id);
				testGetCompositionBySampleIdByCQLQuery(id);
				index = index + originalIndex;
				System.out.println("index = " + index);
			}
			for (String id : sampleIds) {
				testGetCharacterizationsBySampleIdByCQLQuery(id);
				testGetCompositionBySampleIdByCQLQuery(id);
			}
		} catch (Exception e) {
			System.out.println("Exception getting SampleIds: " + e);
			builder.append("Exception getting SampleIds: " + e + "\n");
		}
		System.out.println("\nFinished testing samples.... \n");
	}*/


	public void testGetCharacterizationsBySampleIdByCQLQuery(String id) {
		// TODO add this CQL and related functions for experiment configs,
		// findings and point of contacts.
		try {
			CQLQuery query = new CQLQuery();
			/*
			 * QueryModifier modifier = new QueryModifier();
			 * modifier.setCountOnly(true); query.setQueryModifier(modifier);
			 */
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target
					.setName("gov.nih.nci.cananolab.domain.particle.Characterization");
			Association association = new Association();
			association.setName("gov.nih.nci.cananolab.domain.particle.Sample");
			association.setRoleName("sample");
			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(id);
			association.setAttribute(attribute);
			target.setAssociation(association);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.Characterization");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Characterization chara = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				chara = (Characterization) obj;
				if (chara != null) {
					testGetFindingsByCharacterizationId(chara.getId()
							.toString());
					testGetProtocolByCharacterizationId(chara.getId()
							.toString());
					testGetExperimentConfigsByCharacterizationId(chara.getId()
							.toString());
				}
			}

		} catch (Exception e) {
			System.out
					.println("Exception getting characterizations by Sample ID through CQL: "
							+ e);
			builder.append("Exception getting characterizations by Sample ID through CQL: "
							+ e + "\n");
		}
	}

	public void testGetCompositionBySampleIdByCQLQuery(String id) {
		try {
			// TODO add this CQl and other CQLs for chemical association,
			// nanomaterial entities, functionalizing entities by composition
			// id, and composing elements, functions by nanomaterial entity id,
			// functions by functionalizing entity id, and function for files
			// based on composition info id
			// find all nanomaterial entity class names, functionalizing entity
			// class names first
			String[] sampleViewStrs = gridClient.getSampleViewStrs(id);
			// column 8 contains characterization short class names
			String[] nanoEntityClassNames = null;
			if (sampleViewStrs.length > 5
					&& !StringUtils.isEmpty(sampleViewStrs[5])) {
				nanoEntityClassNames = sampleViewStrs[5]
						.split(Constants.VIEW_CLASSNAME_DELIMITER);
			}
			String[] funcEntityClassNames = null;
			if (sampleViewStrs.length > 6
					&& !StringUtils.isEmpty(sampleViewStrs[6])) {
				funcEntityClassNames = sampleViewStrs[6]
						.split(Constants.VIEW_CLASSNAME_DELIMITER);
			}
			String[] assocClassNames = null;
			if (sampleViewStrs.length > 8
					&& !StringUtils.isEmpty(sampleViewStrs[8])) {
				assocClassNames = sampleViewStrs[8]
						.split(Constants.VIEW_CLASSNAME_DELIMITER);
			}
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target
					.setName("gov.nih.nci.cananolab.domain.particle.SampleComposition");
			Association association = new Association();
			association.setName("gov.nih.nci.cananolab.domain.particle.Sample");
			association.setRoleName("sample");

			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(id);

			association.setAttribute(attribute);

			target.setAssociation(association);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.SampleComposition");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			SampleComposition sampleComposition = null;
			System.out
					.println("getting CompositionBySampleIdByCQLQuery... sampleId="
							+ id);
			builder.append("getting CompositionBySampleIdByCQLQuery... sampleId="
					+ id + "\n");
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				sampleComposition = (SampleComposition) obj;
				System.out.println("SampleComposition id="
						+ sampleComposition.getId());
				builder.append("SampleComposition id="
						+ sampleComposition.getId() + "\n");
				sampleComposition.getChemicalAssociationCollection();
				sampleComposition.getFunctionalizingEntityCollection();
				sampleComposition.getNanomaterialEntityCollection();
				// sampleComposition.
				loadCompositionAssociations(sampleComposition,
						nanoEntityClassNames, funcEntityClassNames,
						assocClassNames);
				// compositionBean = new CompositionBean(sampleComposition);
			}
		} catch (Exception e) {
			System.out
					.println("Exception getting composition by Sample ID through CQL: "
							+ e);
			builder.append("Exception getting composition by Sample ID through CQL: "
					+ e + "\n");
		}
	}

	private void loadCompositionAssociations(SampleComposition composition,
			String[] nanoEntityClassNames, String[] funcEntityClassNames,
			String[] assocClassNames) {
		try {
			loadNanomaterialEntitiesForComposition(composition,
					nanoEntityClassNames);
			loadFunctionalizingEntitiesForComposition(composition,
					funcEntityClassNames);
			loadChemicalAssociationsForComposition(composition, assocClassNames);
			File[] files = gridClient.getFilesByCompositionInfoId(composition
					.getId().toString(), ClassUtils
					.getShortClassName("SampleComposition"));
			if (files != null && files.length > 0) {
				// loadKeywordsForFiles(files);

				composition.setFileCollection(new HashSet<File>(Arrays
						.asList(files)));
			}
		} catch (Exception e) {
			System.out
					.println("Exception loading Composition Associationsfor: "
							+ e);
			builder.append("Exception loading Composition Associationsfor: "
					+ e + "\n");
		}
	}

	private void loadNanomaterialEntitiesForComposition(
			SampleComposition composition, String[] nanoEntityClassNames) {
		try {
			if (nanoEntityClassNames != null) {
				for (String name : nanoEntityClassNames) {
					String fullClassName = null;
					if (ClassUtils.getFullClass(name) != null) {
						fullClassName = ClassUtils.getFullClass(name)
								.getCanonicalName();
					} else {
						fullClassName = ClassUtils.getFullClass(
								OtherNanomaterialEntity.class
										.getCanonicalName()).getCanonicalName();
					}
					CQLQuery query = new CQLQuery();
					gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
					target.setName(fullClassName);
					Association association = new Association();
					association
							.setName("gov.nih.nci.cananolab.domain.particle.SampleComposition");
					association.setRoleName("sampleComposition");

					Attribute attribute = new Attribute();
					attribute.setName("id");
					attribute.setPredicate(Predicate.EQUAL_TO);
					attribute.setValue(composition.getId().toString());
					association.setAttribute(attribute);

					target.setAssociation(association);
					query.setTarget(target);
					CQLQueryResults results = gridClient.query(query);
					results.setTargetClassname(fullClassName);
					CQLQueryResultsIterator iter = new CQLQueryResultsIterator(
							results);
					NanomaterialEntity nanoEntity = null;
					composition
							.setNanomaterialEntityCollection(new HashSet<NanomaterialEntity>());
					while (iter.hasNext()) {
						java.lang.Object obj = iter.next();
						nanoEntity = (NanomaterialEntity) obj;
						if (nanoEntity != null) {
							System.out.println("NanomaterialEntity id="
									+ nanoEntity.getId() + "\tDesc="
									+ nanoEntity.getDescription());
							builder.append("NanomaterialEntity id="
									+ nanoEntity.getId() + "\tDesc="
									+ nanoEntity.getDescription() + "\n");
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Exception getting nanomaterial for composition"
					+ ": " + e);
			builder.append("Exception getting nanomaterial for composition"
					+ ": " + e + "\n");
		}
	}

	private void loadFunctionalizingEntitiesForComposition(
			SampleComposition composition, String[] funcEntityClassNames) {
		try {
			if (funcEntityClassNames != null) {
				for (String name : funcEntityClassNames) {
					String fullClassName = null;
					if (ClassUtils.getFullClass(name) != null) {
						fullClassName = ClassUtils.getFullClass(name)
								.getCanonicalName();
					} else {
						fullClassName = ClassUtils.getFullClass(
								OtherFunctionalizingEntity.class
										.getCanonicalName()).getCanonicalName();
					}
					CQLQuery query = new CQLQuery();
					gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
					target.setName(fullClassName);

					Association association = new Association();
					association
							.setName("gov.nih.nci.cananolab.domain.particle.SampleComposition");
					association.setRoleName("sampleComposition");

					Attribute attribute = new Attribute();
					attribute.setName("id");
					attribute.setPredicate(Predicate.EQUAL_TO);
					attribute.setValue(composition.getId().toString());
					association.setAttribute(attribute);

					target.setAssociation(association);
					query.setTarget(target);
					CQLQueryResults results = gridClient.query(query);
					results.setTargetClassname(fullClassName);
					CQLQueryResultsIterator iter = new CQLQueryResultsIterator(
							results);
					FunctionalizingEntity funcEntity = null;
					composition
							.setFunctionalizingEntityCollection(new HashSet<FunctionalizingEntity>());
					while (iter.hasNext()) {
						java.lang.Object obj = iter.next();
						funcEntity = (FunctionalizingEntity) obj;
						System.out.println("FunctionalizingEntity id="
								+ funcEntity.getId() + "\tName="
								+ funcEntity.getName() + "\tDesc="
								+ funcEntity.getDescription()
								+ "\tMolecularFormula="
								+ funcEntity.getMolecularFormula()
								+ "\tPubChemDataSourceName="
								+ funcEntity.getPubChemDataSourceName()
								+ "\tMolecularFormulaType="
								+ funcEntity.getMolecularFormulaType()
								+ "\tValueUnit=" + funcEntity.getValueUnit());
						builder.append("FunctionalizingEntity id="
								+ funcEntity.getId() + "\tName="
								+ funcEntity.getName() + "\tDesc="
								+ funcEntity.getDescription()
								+ "\tMolecularFormula="
								+ funcEntity.getMolecularFormula()
								+ "\tPubChemDataSourceName="
								+ funcEntity.getPubChemDataSourceName()
								+ "\tMolecularFormulaType="
								+ funcEntity.getMolecularFormulaType()
								+ "\tValueUnit=" + funcEntity.getValueUnit() + "\n");
						loadFunctionalizingEntityAssociations(funcEntity);
						composition.getFunctionalizingEntityCollection().add(
								funcEntity);
					}
				}
			}
		} catch (Exception e) {
			System.out
					.println("Exception getting FunctionalizingEntity for Composition for id="
							+ composition.getId() + ": " + e);
			builder.append("Exception getting FunctionalizingEntity for Composition for id="
					+ composition.getId() + ": " + e + "\n");
		}
	}

	private void loadChemicalAssociationsForComposition(
			SampleComposition composition, String[] assocClassNames) {
		try {
			if (assocClassNames != null) {
				for (String name : assocClassNames) {
					String fullClassName = null;
					if (ClassUtils.getFullClass(name) != null) {
						fullClassName = ClassUtils.getFullClass(name)
								.getCanonicalName();
					} else {
						fullClassName = ClassUtils.getFullClass(
								OtherChemicalAssociation.class
										.getCanonicalName()).getCanonicalName();
					}
					CQLQuery query = new CQLQuery();
					gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
					target.setName(fullClassName);

					Association association = new Association();
					association
							.setName("gov.nih.nci.cananolab.domain.particle.SampleComposition");
					association.setRoleName("sampleComposition");

					Attribute attribute = new Attribute();
					attribute.setName("id");
					attribute.setPredicate(Predicate.EQUAL_TO);
					attribute.setValue(composition.getId().toString());
					association.setAttribute(attribute);

					target.setAssociation(association);
					query.setTarget(target);
					CQLQueryResults results = gridClient.query(query);
					results.setTargetClassname(fullClassName);
					CQLQueryResultsIterator iter = new CQLQueryResultsIterator(
							results);
					ChemicalAssociation assoc = null;
					composition
							.setChemicalAssociationCollection(new HashSet<ChemicalAssociation>());
					while (iter.hasNext()) {
						java.lang.Object obj = iter.next();
						assoc = (ChemicalAssociation) obj;
						System.out.println("ChemicalAssociation id="
								+ assoc.getId() + "\tDesc="
								+ assoc.getDescription());
						builder.append("ChemicalAssociation id="
								+ assoc.getId() + "\tDesc="
								+ assoc.getDescription() + "\n");
						loadChemicalAssociationAssociations(assoc);
						composition.getChemicalAssociationCollection().add(
								assoc);
					}
				}
			}
		} catch (Exception e) {
			System.out
					.println("Exception loading ChemicalAssociation for Composition id="
							+ composition.getId() + ": " + e);
			builder.append("Exception loading ChemicalAssociation for Composition id="
					+ composition.getId() + ": " + e + "\n");
		}
	}

	private void loadChemicalAssociationAssociations(ChemicalAssociation assoc) {
		try {
			File[] files = gridClient.getFilesByCompositionInfoId(assoc.getId()
					.toString(), "ChemicalAssociation");
			if (files != null && files.length > 0) {
				// loadKeywordsForFiles(files);
				assoc
						.setFileCollection(new HashSet<File>(Arrays
								.asList(files)));
			}
			loadAssociatedElementsForChemicalAssociation(assoc, "A");
			loadAssociatedElementsForChemicalAssociation(assoc, "B");
		} catch (Exception e) {
			System.out.println("Exception loading ChemicalAssociation for id="
					+ assoc.getId() + ": " + e);
			builder.append("Exception loading ChemicalAssociation for id="
					+ assoc.getId() + ": " + e + "\n");
		}
	}

	private void loadAssociatedElementsForChemicalAssociation(
			ChemicalAssociation assoc, String elementNumber) {
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target
				.setName("gov.nih.nci.cananolab.domain.particle.AssociatedElement");
		Association association = new Association();
		association
				.setName("gov.nih.nci.cananolab.domain.particle.ChemicalAssociation");
		String roleName = "chemicalAssociation" + elementNumber + "Collection";
		association.setRoleName(roleName);
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(assoc.getId().toString());
		association.setAttribute(attribute);
		target.setAssociation(association);
		query.setTarget(target);
		try {
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.AssociatedElement");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				AssociatedElement element = (AssociatedElement) obj;
				if (element instanceof ComposingElement) {
					loadInherentFunctionsForComposingElement((ComposingElement) element);
				} else if (element instanceof FunctionalizingEntity) {
					loadFunctionalizingEntityAssociations((FunctionalizingEntity) element);
				}
				if (elementNumber.equals("A")) {
					assoc.setAssociatedElementA(element);
				} else if (elementNumber.equals("B")) {
					assoc.setAssociatedElementB(element);
				}
			}
		} catch (Exception e) {
			System.out
					.println("Exception getting AssocatedElement for ChenicalAssociation: "
							+ e);
			builder.append("Exception getting AssocatedElement for ChenicalAssociation: "
					+ e + "\n");
		}
	}

	private void loadFunctionalizingEntityAssociations(
			FunctionalizingEntity entity) throws Exception {
		File[] files = gridClient.getFilesByCompositionInfoId(entity.getId()
				.toString(), "FunctionalizingEntity");
		if (files != null && files.length > 0) {
			for (File file : files) {
				System.out.println("File desc: " + file.getDescription()
						+ "\tName: " + file.getName() + "\tUri: "
						+ file.getUri());
				builder.append("File desc: " + file.getDescription()
				+ "\tName: " + file.getName() + "\tUri: "
				+ file.getUri() + "\n");
				// TODO add CQL for keywords by file id
				loadKeywordsForFile(file);
			}
		}
	}

	private void loadKeywordsForFile(File file) {
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName("gov.nih.nci.cananolab.domain.common.Keyword");
		Association association = new Association();
		association.setName("gov.nih.nci.cananolab.domain.common.File");
		association.setRoleName("fileCollection");

		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(file.getId().toString());
		association.setAttribute(attribute);
		target.setAssociation(association);
		query.setTarget(target);
		try {
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.common.Keyword");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			file.setKeywordCollection(new HashSet<Keyword>());
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				Keyword keyword = (Keyword) obj;
				if (keyword != null) {
					System.out.println("Keyword id=" + keyword.getId()
							+ "\tName=" + keyword.getName());
					builder.append("Keyword id=" + keyword.getId()
							+ "\tName=" + keyword.getName() + "\n");
				}
				file.getKeywordCollection().add(keyword);
			}
		} catch (Exception e) {
			System.out.println("Exception getting keyword for file id="
					+ file.getId() + ": " + e);
			builder.append("Exception getting keyword for file id="
					+ file.getId() + ": " + e + "\n");
		}
	}

	public void testGetPublicationIdsBy(int index) {
		try {
			String[] pubIds = gridClient.getPublicationIdsBy("", "", "", null,
					null, null, null, null, null, null, null);
			System.out.println("Testing getPublicationIdsBy operation.... \n "
					+ "For every publication, test Publication");
			builder.append("Testing getPublicationIdsBy operation.... \n "
					+ "For every publication, test Publication" + "\n");
			if (pubIds != null) {
				int originalIndex = index;
				while(index <= pubIds.length){
					//get the sample at location index
					String id = pubIds[index];
					testPublication(id);
					index = index + originalIndex;
					//System.out.println("index = " + index);
				}
				
			}
		} catch (Exception e) {
			System.out.println("Exception getting PublicationIds: " + e);
			builder.append("Exception getting PublicationIds: " + e + "\n");
		}
		System.out.println("\nFinished testing publications.....\n");
	}

	public void testGetFileByProtocolId(String protocolId) {
		// String protocolId = "24390915";
		System.out.println("Testing getFileByProtocolId: " + protocolId);
		builder.append("Testing getFileByProtocolId: " + protocolId + "\n");
		try {
			File file = gridClient.getFileByProtocolId(protocolId);
			if (file != null) {
				System.out.println("File desc: " + file.getDescription()
						+ "\tName: " + file.getName() + "\tUri: "
						+ file.getUri());
				builder.append("File desc: " + file.getDescription()
						+ "\tName: " + file.getName() + "\tUri: "
						+ file.getUri() + "\n");
				// TODO add CQL for keywords by file id
				loadKeywordsForFile(file);
			}
		} catch (Exception e) {
			System.out
					.println("Exception getting ExperimentConfigsByCharacterizationId for protocolid="
							+ protocolId + ": " + e);
			builder.append("Exception getting ExperimentConfigsByCharacterizationId for protocolid="
					+ protocolId + ": " + e + "\n");
		}
	}

	public void testGetFilesByCompositionInfoId(String id, String className) {
		// String id = "21376285";//"21376285";
		// String className="NanoMaterialEntity";
		System.out.println("Test getFilesByCompositionInfoId: id=" + id
				+ ", className=" + className);
		builder.append("Test getFilesByCompositionInfoId: id=" + id
				+ ", className=" + className + "\n");
		try {
			File[] files = gridClient
					.getFilesByCompositionInfoId(id, className);
			if (files != null) {
				for (File file : files) {
					System.out.println("File desc: " + file.getDescription()
							+ "\tName: " + file.getName() + "\tUri: "
							+ file.getUri());
					builder.append("File desc: " + file.getDescription()
							+ "\tName: " + file.getName() + "\tUri: "
							+ file.getUri() + "\n");
					// TODO add CQL for keywords by file id
					loadKeywordsForFile(file);
				}
			}
		} catch (Exception e) {
			System.out
					.println("Exception getting FilesByCompositionInfoId for id="
							+ id + ", className=" + className + ": " + e);
			builder.append("Exception getting FilesByCompositionInfoId for id="
					+ id + ", className=" + className + ": " + e + "\n");
		}
	}

	/**
	 * load all InherentFunction for an associated ComposingElement
	 */
	private void loadInherentFunctionsForComposingElement(
			ComposingElement composingElement) {
		CQLQuery query = new CQLQuery();

		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName("gov.nih.nci.cananolab.domain.particle.Function");
		Association association = new Association();
		association
				.setName("gov.nih.nci.cananolab.domain.particle.ComposingElement");
		association.setRoleName("composingElement");

		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(composingElement.getId().toString());
		association.setAttribute(attribute);

		target.setAssociation(association);
		query.setTarget(target);
		try {
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.Function");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			composingElement
					.setInherentFunctionCollection(new HashSet<Function>());
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				Function function = (Function) obj;
				composingElement.getInherentFunctionCollection().add(function);
				System.out.println("Function for ComposingElement: id="
						+ function.getId() + "\tDesc="
						+ function.getDescription());
				builder.append("Function for ComposingElement: id="
						+ function.getId() + "\tDesc="
						+ function.getDescription() + "\n");
			}
		} catch (Exception e) {
			System.out
					.println("Exception getting Funtion for ComposingElement for id="
							+ composingElement.getId() + ": " + e);
			builder.append("Exception getting Funtion for ComposingElement for id="
					+ composingElement.getId() + ": " + e + "\n");
		}
	}

	public static void main(String[] args) {
		System.out.println("Running the Grid Service Client");
		try {
			if (!(args.length < 3)) {
				if (args[0].equals("-url")) {
					CaNanoLabServiceClient client = new CaNanoLabServiceClient(
							args[1]);
					int index = Integer.parseInt(args[2]);

					GridClientTest test = new GridClientTest(client);
					System.out.println(new Date());
					long start = System.currentTimeMillis();
					test.builder.append("Start time: " + new Date() + "\n" );
					test.testGetSampleIds(index);					
					test.testGetPublicationIdsBy(index);
					test.testGetAllProtocolsByCQLQuery();
					System.out.println(new Date());
					long end = System.currentTimeMillis();
					test.builder.append("End time: " + new Date()+ "\n"  );
					test.builder.append("Total time: " + (end - start)/1000 + " seconds");
					// test.testSampleComposition("6160390");						
					// test.testGetPrimaryPointOfContactBySampleId("10354688");
					// test.testGetCompositionBySampleIdByCQLQuery("9142300");
					// test.testGetPrimaryPointOfContactBySampleId("10354688");
					// // test.testGetAllCharacterizationByCQLQuery();					//
					// // these methods user can plug in any parameter
					// test.testGetFindingsByCharacterizationId("3932251");
					// test.testGetProtocolByCharacterizationId("3932251");
					// test
					// .testGetExperimentConfigsByCharacterizationId("3932251");
					// test.testCharacterization("3932251");
					// test.testGetFileByProtocolId("24390915");
					// test.testGetFilesByCompositionInfoId("21376285",
					// "NanomaterialEntity");
					// test.testNanomaterialEntity("6160399");
					// test.testFunction("10944705");
					// test.testComposingElement("6062106");
					// test.testFunctionalizingEntity("6225945");
					//
					// test.testChemicalAssociation("8847369");
					// test.testSampleComposition("6160390");
					// test.testActivationMethod("3833872");
					// test.testProtocol("24390915");
					 
					 test.writeOutput();

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
	
	public void writeOutput(){
		String fileName = "caNanoLabGridClientOutput.txt";
		/*java.io.File outputFile = new java.io.File(".");
		String dir = System.getProperty("java.io.tmpdir");
		String path = dir + fileName;
		System.out.println("path: " + path);
		java.io.File file = new java.io.File(dir + fileName + java.io.File.separator + "test.txt");
		
		if(!file.exists()){
			file.mkdirs();
		}*/
		DataOutputStream oStream = null;
		try {
			oStream = new DataOutputStream(new FileOutputStream(fileName));
			oStream.writeBytes(builder.toString());
			oStream.flush();
			System.out.println("Finished writing output file.");
		}  catch (FileNotFoundException ex) {
		     System.out.println(ex);
	    } catch (IOException ex) {
	      System.out.println(ex);
	    }
		finally {
			if (oStream != null) {
				try {
					oStream.close();
				} catch (Exception e) {
				}
			}
		}
	}
}