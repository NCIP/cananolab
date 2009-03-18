package gov.nih.nci.cananolab.service.protocol.impl;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabServiceClient;
import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlquery.QueryModifier;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.domain.common.ProtocolFile;
import gov.nih.nci.cananolab.dto.common.ProtocolFileBean;
import gov.nih.nci.cananolab.exception.ProtocolException;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.util.Constants;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import org.apache.log4j.Logger;

/**
 * Remote implementation of ProtocolService
 * 
 * @author pansu
 * 
 */
public class ProtocolServiceRemoteImpl implements ProtocolService {
	private static Logger logger = Logger
			.getLogger(ProtocolServiceRemoteImpl.class);
	private CaNanoLabServiceClient gridClient;

	public ProtocolServiceRemoteImpl(String serviceUrl) throws Exception {
		gridClient = new CaNanoLabServiceClient(serviceUrl);
	}

	public ProtocolFileBean findProtocolFileById(String fileId)
			throws ProtocolException {

		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target.setName("gov.nih.nci.cananolab.domain.common.ProtocolFile");
			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(fileId);
			target.setAttribute(attribute);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.common.ProtocolFile");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			ProtocolFile protocolFile = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				protocolFile = (ProtocolFile) obj;
				loadProtocolForProtocolFile(protocolFile);
			}
			ProtocolFileBean protocolFileBean = new ProtocolFileBean(
					protocolFile);
			return protocolFileBean;
		} catch (Exception e) {
			String err = "Problem finding the protocol file by id: " + fileId;
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	/**
	 * Persist a new protocol file or update an existing protocol file
	 * 
	 * @param protocolFile
	 * @throws Exception
	 */
	public void saveProtocolFile(ProtocolFile protocolFile, byte[] fileData)
			throws ProtocolException {
		throw new ProtocolException("not implemented for grid service.");
	}

	public SortedSet<String> getProtocolNames(String protocolType)
			throws ProtocolException {
		throw new ProtocolException("not implemented for grid service.");
	}

	public List<ProtocolFileBean> findProtocolFilesBy(String protocolType,
			String protocolName, String fileTitle) throws ProtocolException {
		List<ProtocolFileBean> protocolFileBeans = new ArrayList<ProtocolFileBean>();
		try {
			ProtocolFile[] protocolFiles = gridClient.getProtocolFilesBy(
					protocolType, protocolName, fileTitle);
			if (protocolFiles != null) {
				for (ProtocolFile pf : protocolFiles) {
					loadProtocolForProtocolFile(pf);
					ProtocolFileBean pfb = new ProtocolFileBean(pf);
					protocolFileBeans.add(pfb);
				}
			}
			return protocolFileBeans;
		} catch (RemoteException e) {
			logger.error(Constants.NODE_UNAVAILABLE, e);
			throw new ProtocolException(Constants.NODE_UNAVAILABLE, e);		
		} catch (Exception e) {
			String err = "Problem finding protocol files.";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	private void loadProtocolForProtocolFile(ProtocolFile protocolFile)
			throws Exception {
		CQLQuery query = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
		target.setName("gov.nih.nci.cananolab.domain.common.Protocol");
		Association association = new Association();
		association.setName("gov.nih.nci.cananolab.domain.common.ProtocolFile");
		association.setRoleName("protocolFileCollection");
		Attribute attribute = new Attribute();
		attribute.setName("id");
		attribute.setPredicate(Predicate.EQUAL_TO);
		attribute.setValue(protocolFile.getId().toString());
		association.setAttribute(attribute);
		target.setAssociation(association);
		query.setTarget(target);
		CQLQueryResults results = gridClient.query(query);
		results
				.setTargetClassname("gov.nih.nci.cananolab.domain.common.Protocol");
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
		while (iter.hasNext()) {
			java.lang.Object obj = iter.next();
			Protocol protocol = (Protocol) obj;
			protocolFile.setProtocol(protocol);
		}
	}

	public int getNumberOfPublicProtocolFiles() throws ProtocolException {
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
