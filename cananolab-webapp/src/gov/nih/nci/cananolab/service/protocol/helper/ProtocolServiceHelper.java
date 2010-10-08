package gov.nih.nci.cananolab.service.protocol.helper;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.BaseServiceHelper;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.cananolab.util.TextMatchMode;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

/**
 * This class includes methods involved in searching protocols that can be used
 * in both local and remote searches
 *
 * @author pansu
 *
 */
public class ProtocolServiceHelper extends BaseServiceHelper {
	private static Logger logger = Logger
			.getLogger(ProtocolServiceHelper.class);

	public ProtocolServiceHelper() {
		super();
	}

	public ProtocolServiceHelper(UserBean user) {
		super(user);
	}

	public ProtocolServiceHelper(SecurityService securityService) {
		super(securityService);
	}

	public List<Protocol> findProtocolsBy(String protocolType,
			String protocolName, String protocolAbbreviation, String fileTitle)
			throws Exception {
		List<Protocol> protocols = new ArrayList<Protocol>();
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(Protocol.class);
		crit.createAlias("file", "file", CriteriaSpecification.LEFT_JOIN);
		crit.setFetchMode("file.keywordCollection", FetchMode.JOIN);
		if (!StringUtils.isEmpty(protocolType)) {
			// case insensitive
			crit.add(Restrictions.ilike("type", protocolType, MatchMode.EXACT));
		}
		if (!StringUtils.isEmpty(protocolName)) {
			TextMatchMode protocolNameMatchMode = new TextMatchMode(
					protocolName);
			crit.add(Restrictions.ilike("name", protocolNameMatchMode
					.getUpdatedText(), protocolNameMatchMode.getMatchMode()));
		}
		if (!StringUtils.isEmpty(protocolAbbreviation)) {
			TextMatchMode protocolAbbreviationMatchMode = new TextMatchMode(
					protocolAbbreviation);
			crit.add(Restrictions.ilike("abbreviation",
					protocolAbbreviationMatchMode.getUpdatedText(),
					protocolAbbreviationMatchMode.getMatchMode()));
		}
		if (!StringUtils.isEmpty(fileTitle)) {
			TextMatchMode titleMatchMode = new TextMatchMode(fileTitle);
			crit.add(Restrictions.ilike("file.title", titleMatchMode
					.getUpdatedText(), titleMatchMode.getMatchMode()));
		}
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List results = appService.query(crit);
		for (Object obj : results) {
			Protocol protocol = (Protocol) obj;
			if (getAccessibleData().contains(protocol.getId().toString())) {
				protocols.add(protocol);
			} else {
				logger.debug("User doesn't have access ot protocol with id "
						+ protocol.getId());
			}
		}
		return protocols;
	}

	public Protocol findProtocolBy(String protocolType, String protocolName,
			String protocolVersion) throws Exception {
		Protocol protocol = null;
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(Protocol.class).add(
				Property.forName("type").eq(protocolType).ignoreCase()).add(
				Property.forName("name").eq(protocolName).ignoreCase()).add(
				Property.forName("version").eq(protocolVersion).ignoreCase());
		crit.setFetchMode("file", FetchMode.JOIN);
		crit.setFetchMode("file.keywordCollection", FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List results = appService.query(crit);
		if (results.isEmpty()) {
			return null;
		}
		protocol = (Protocol) results.get(0);
		if (getAccessibleData().contains(protocol.getId().toString())) {
			return protocol;
		} else {
			throw new NoAccessException();
		}
	}

	public File findFileByProtocolId(String protocolId) throws Exception {
		if (!getAccessibleData().contains(protocolId)) {
			new NoAccessException("User has no access to the protocol "
					+ protocolId);
		}
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		HQLCriteria crit = new HQLCriteria(
				"select aProtocol.file from gov.nih.nci.cananolab.domain.common.Protocol aProtocol where aProtocol.id = "
						+ protocolId);
		List result = appService.query(crit);
		File file = null;
		if (!result.isEmpty()) {
			file = (File) result.get(0);
		}
		return file;
	}

	public int getNumberOfPublicProtocols() throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		HQLCriteria crit = new HQLCriteria(
				"select id from gov.nih.nci.cananolab.domain.common.Protocol");
		List results = appService.query(crit);
		List<String> publicIds = new ArrayList<String>();
		for (Object obj : results) {
			String id = (String) obj.toString();
			if (getAccessibleData().contains(id)) {
				publicIds.add(id);
			}
		}
		return publicIds.size();
	}

	public Protocol findProtocolById(String protocolId) throws Exception {
		if (!getAccessibleData().contains(protocolId)) {
			new NoAccessException("User has no access to the protocol "
					+ protocolId);
		}
		Protocol protocol = null;

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(Protocol.class).add(
				Property.forName("id").eq(new Long(protocolId)));
		crit.setFetchMode("file", FetchMode.JOIN);
		crit.setFetchMode("file.keywordCollection", FetchMode.JOIN);
		List result = appService.query(crit);
		if (!result.isEmpty()) {
			protocol = (Protocol) result.get(0);
		}
		return protocol;
	}

	public SortedSet<String> getProtocolNamesBy(String protocolType)
			throws Exception {
		SortedSet<String> protocolNames = new TreeSet<String>();
		List<Protocol> protocols = this.findProtocolsBy(protocolType, null,
				null, null);
		for (Protocol protocol : protocols) {
			protocolNames.add(protocol.getName());
		}
		return protocolNames;
	}

	public SortedSet<String> getProtocolVersionsBy(String protocolType,
			String protocolName) throws Exception {
		SortedSet<String> protocolVersions = new TreeSet<String>();
		List<Protocol> protocols = this.findProtocolsBy(protocolType,
				protocolName, null, null);
		for (Protocol protocol : protocols) {
			protocolVersions.add(protocol.getVersion());
		}
		return protocolVersions;
	}

	public List<String> findProtocolIdsByOwner(String currentOwner)
			throws Exception {
		List<String> protocolIds = new ArrayList<String>();
		DetachedCriteria crit = DetachedCriteria.forClass(Protocol.class)
				.setProjection(
						Projections.projectionList().add(
								Projections.property("id")));
		crit.add(Restrictions.eq("createdBy", currentOwner));
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List results = appService.query(crit);
		for (Object obj : results) {
			String protocolId = obj.toString();
			if (getAccessibleData().contains(protocolId)) {
				protocolIds.add(protocolId);
			} else {
				logger.debug("User doesn't have access to protocol of ID: "
						+ protocolId);
			}
		}
		return protocolIds;
	}

}
