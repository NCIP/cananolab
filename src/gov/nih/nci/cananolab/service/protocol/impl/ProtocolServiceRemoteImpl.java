package gov.nih.nci.cananolab.service.protocol.impl;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabServiceClient;
import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Group;
import gov.nih.nci.cagrid.cqlquery.LogicalOperator;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlquery.QueryModifier;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.ProtocolException;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.axis.utils.StringUtils;
import org.apache.log4j.Logger;

/**
 * Local implementation of ProtocolService
 * 
 * @author pansu
 * 
 */
public class ProtocolServiceRemoteImpl implements ProtocolService {
	private static Logger logger = Logger
			.getLogger(ProtocolServiceRemoteImpl.class);
	private CaNanoLabServiceClient gridClient;

	public ProtocolServiceRemoteImpl(String serviceUrl)
			throws ProtocolException {
		try {
			gridClient = new CaNanoLabServiceClient(serviceUrl);
		} catch (Exception e) {
			throw new ProtocolException("Can't create grid client succesfully.");
		}
	}

	public ProtocolBean findProtocolById(String protocolId, UserBean user)
			throws ProtocolException {
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target.setName("gov.nih.nci.cananolab.domain.common.Protocol");
			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(protocolId);
			target.setAttribute(attribute);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.common.Protocol");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Protocol protocol = null;
			ProtocolBean protocolBean = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				protocol = (Protocol) obj;
				if (protocol != null) {
					loadProtocolAssociations(protocol);
					protocolBean = new ProtocolBean(protocol);
				}
			}
			return protocolBean;
		} catch (Exception e) {
			String err = "Problem finding the protocol by id: " + protocolId;
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	private void loadProtocolAssociations(Protocol protocol) throws Exception {
		// load protocol file
		File file = gridClient.getFileByProtocolId(protocol.getId().toString());
		protocol.setFile(file);
		// load protocol file keywords
		// loadKeywordsForFile(protocol.getFile());
	}

	private void loadKeywordsForFile(File file) throws Exception {
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
		attribute.setValue(file.getId().toString());
		association.setAttribute(attribute);
		target.setAssociation(association);
		query.setTarget(target);
		CQLQueryResults results = gridClient.query(query);
		results
				.setTargetClassname("gov.nih.nci.cananolab.domain.common.Keyword");
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		Set<Keyword> keywords = new HashSet<Keyword>();
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			Keyword keyword = (Keyword) obj;
			keywords.add(keyword);
		}
		file.setKeywordCollection(keywords);
	}

	/**
	 * Persist a new protocol file or update an existing protocol file
	 * 
	 * @param protocolBean
	 * @throws Exception
	 */
	public void saveProtocol(ProtocolBean protocolBean, UserBean user)
			throws ProtocolException {
		throw new ProtocolException("Not implemented for grid service");
	}

	public ProtocolBean findProtocolBy(String protocolType,
			String protocolName, String protocolVersion, UserBean user)
			throws ProtocolException {
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target.setName("gov.nih.nci.cananolab.domain.common.Protocol");
			Group group = new Group();
			group.setLogicRelation(LogicalOperator.AND);
			Attribute[] attributes = new Attribute[3];
			attributes[0].setName("type");
			attributes[0].setPredicate(Predicate.EQUAL_TO);
			attributes[0].setValue(protocolType);
			attributes[1].setName("name");
			attributes[1].setPredicate(Predicate.EQUAL_TO);
			attributes[1].setValue(protocolName);
			attributes[2].setName("version");
			attributes[2].setPredicate(Predicate.EQUAL_TO);
			attributes[2].setValue(protocolVersion);
			group.setAttribute(attributes);
			target.setGroup(group);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.common.Protocol");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Protocol protocol = null;
			ProtocolBean protocolBean = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				protocol = (Protocol) obj;
				if (protocol != null) {
					loadProtocolAssociations(protocol);
					protocolBean = new ProtocolBean(protocol);
				}
			}
			return protocolBean;
		} catch (Exception e) {
			String err = "Problem finding the protocol by type name and version";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	public List<ProtocolBean> findProtocolsBy(String protocolType,
			String protocolName, String protocolAbbreviation, String fileTitle,
			UserBean user) throws ProtocolException {
		List<ProtocolBean> protocolBeans = new ArrayList<ProtocolBean>();
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target.setName("gov.nih.nci.cananolab.domain.common.Protocol");
			if (!StringUtils.isEmpty(protocolType)
					|| !StringUtils.isEmpty(protocolName)
					|| !StringUtils.isEmpty(protocolAbbreviation)
					|| !StringUtils.isEmpty(fileTitle)) {
				List<Attribute> attributeList = new ArrayList<Attribute>();
				List<Association> associationList = new ArrayList<Association>();
				Group group = new Group();
				if (!StringUtils.isEmpty(protocolType)) {
					Attribute attr = new Attribute();
					attr.setName("type");
					attr.setPredicate(Predicate.EQUAL_TO);
					attr.setValue(protocolType);
					attributeList.add(attr);
				}
				if (!StringUtils.isEmpty(protocolName)) {
					Attribute attr = new Attribute();
					if (protocolName.indexOf("*") != -1) {
						attr.setPredicate(Predicate.LIKE);
						protocolName = protocolName.replaceAll("[*]", "%");
					} else {
						attr.setPredicate(Predicate.EQUAL_TO);
					}
					attr.setName("name");
					attr.setValue(protocolName);
					attributeList.add(attr);
				}
				if (!StringUtils.isEmpty(protocolAbbreviation)) {
					Attribute attr = new Attribute();
					if (protocolAbbreviation.indexOf("*") != -1) {
						attr.setPredicate(Predicate.LIKE);
						protocolAbbreviation = protocolAbbreviation.replaceAll(
								"[*]", "%");
					} else {
						attr.setPredicate(Predicate.EQUAL_TO);
					}
					attr.setName("abbreviation");
					attr.setValue(protocolAbbreviation);
					attributeList.add(attr);
				}
				if (!StringUtils.isEmpty(fileTitle)) {
					Attribute attribute = new Attribute();
					if (fileTitle.indexOf("*") != -1) {
						attribute.setPredicate(Predicate.LIKE);
						fileTitle = fileTitle.replaceAll("[*]", "%");
					} else {
						attribute.setPredicate(Predicate.EQUAL_TO);
					}
					Association association = new Association();
					association
							.setName("gov.nih.nci.cananolab.domain.common.File");
					association.setRoleName("file");
					attribute.setName("title");
					attribute.setValue(fileTitle);
					association.setAttribute(attribute);
					association.setGroup(group);
				}
				if (attributeList.size() > 1) {					
					group.setAttribute(attributeList.toArray(new Attribute[0]));
					group.setLogicRelation(LogicalOperator.AND);					
					target.setGroup(group);
				} else {
					if (associationList.size() == 0) {
						target.setAttribute(attributeList.get(0));
					} else if (associationList.size() == 1) {
						target.setAssociation(associationList.get(0));
					}
				}
			}
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.common.Protocol");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);

			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				Protocol protocol = (Protocol) obj;
				if (protocol != null) {
					loadProtocolAssociations(protocol);
					ProtocolBean protocolBean = new ProtocolBean(protocol);
					protocolBeans.add(protocolBean);
				}
			}
			return protocolBeans;
		} catch (Exception e) {
			String err = "Problem finding the protocols by type, name, abbreviation and file title";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	public int getNumberOfPublicProtocols() throws ProtocolException {
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target.setName("gov.nih.nci.cananolab.domain.common.ProtocolFile");
			query.setTarget(target);
			QueryModifier modifier = new QueryModifier();
			modifier.setCountOnly(true);
			query.setQueryModifier(modifier);

			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.common.ProtocolFile");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			int count = 0;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				count = ((Long) obj).intValue();
			}
			return count;
		} catch (Exception e) {
			String err = "Error finding counts of public remote protocol files.";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}
}
