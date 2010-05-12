package gov.nih.nci.cananolab.service.protocol.helper;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.cananolab.util.TextMatchMode;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.Collections;
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
public class ProtocolServiceHelper {
	private static Logger logger = Logger
			.getLogger(ProtocolServiceHelper.class);
	private AuthorizationService authService;

	public ProtocolServiceHelper() {
		try {
			authService = new AuthorizationService(Constants.CSM_APP_NAME);
		} catch (Exception e) {
			logger.error("Can't create authorization service: " + e);
		}
	}

	public List<Protocol> findProtocolsBy(String protocolType,
			String protocolName, String protocolAbbreviation, String fileTitle,
			UserBean user) throws Exception {
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
		List filteredResults = new ArrayList(results);
		// get public data
		if (user == null) {
			filteredResults = authService.filterNonPublic(results);
		}
		// get user allowed data
		for (Object obj : filteredResults) {
			Protocol protocol = (Protocol) obj;
			if (user == null
					|| authService.checkReadPermission(user, protocol.getId()
							.toString())) {
				protocols.add(protocol);
			} else {
				logger.debug("User doesn't have access ot protocol with id "
						+ protocol.getId());
			}
		}
		Collections.sort(protocols,
				new Comparators.ProtocolNameVersionComparator());
		return protocols;
	}

	public Protocol findProtocolBy(String protocolType, String protocolName,
			String protocolVersion, UserBean user) throws Exception {
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
		if (authService.checkReadPermission(user, protocol.getId().toString())) {
			return protocol;
		} else {
			throw new NoAccessException();
		}
	}

	public File findFileByProtocolId(String protocolId, UserBean user)
			throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		HQLCriteria crit = new HQLCriteria(
				"select aProtocol.file from gov.nih.nci.cananolab.domain.common.Protocol aProtocol where aProtocol.id = "
						+ protocolId);
		List result = appService.query(crit);
		File file = null;
		if (!result.isEmpty()) {
			file = (File) result.get(0);
			if (authService.checkReadPermission(user, file.getId().toString())) {
				return file;
			} else {
				logger.debug("User doesn't have access file under protocol");
			}
		}
		return file;
	}

	public List<Long> findCharacterizationIdsByProtocolId(String protocolId,
			UserBean user) throws Exception {
		if (authService.checkReadPermission(user, protocolId)) {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(
					Characterization.class).setProjection(
					Projections.distinct(Property.forName("id")));
			crit.createAlias("protocol", "protocol");
			crit.add(Property.forName("protocol.id").eq(new Long(protocolId)));
			List results = appService.query(crit);
			List<Long> ids = new ArrayList<Long>();
			for (Object obj : results) {
				Long charId = (Long) obj;
				if (authService.checkReadPermission(user, charId.toString())) {
					ids.add(charId);
				} else {
					logger
							.debug("User doesn't have access to characterization "
									+ charId);
				}
			}
			return ids;
		} else {
			throw new NoAccessException(
					"User doesn't have acess to the protocol of id: "
							+ protocolId);
		}
	}

	public Protocol findProtocolById(String protocolId, UserBean user)
			throws Exception {
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
			if (!authService.checkReadPermission(user, protocol.getId()
					.toString())) {
				throw new NoAccessException();
			}
		}
		return protocol;
	}

	public AuthorizationService getAuthService() {
		return authService;
	}

	public void retrieveVisibility(ProtocolBean protocolBean) throws Exception {
		if (protocolBean != null) {
			// get assigned visible groups
			List<String> accessibleGroups = authService.getAccessibleGroups(
					protocolBean.getDomain().getId().toString(),
					Constants.CSM_READ_PRIVILEGE);
			String[] visibilityGroups = accessibleGroups.toArray(new String[0]);
			protocolBean.setVisibilityGroups(visibilityGroups);
		}
	}

	public List<String> removeVisibility(Protocol protocol, Boolean remove)
			throws Exception {
		List<String> entries = new ArrayList<String>();
		if (protocol != null) {
			if (remove == null || remove)
				authService.removeCSMEntry(protocol.getId().toString());
			entries.add(protocol.getId().toString());
			if (protocol.getFile() != null) {
				if (remove == null || remove) {
					authService.removeCSMEntry(protocol.getFile().getId()
							.toString());
				}
				entries.add(protocol.getFile().getId().toString());
			}
		}
		return entries;
	}

	public void assignVisibility(Protocol protocol, String[] visibilityGroups)
			throws Exception {
		authService.assignVisibility(protocol.getId().toString(),
				visibilityGroups, null);
		// set file visibility as well
		if (protocol.getFile() != null) {
			authService.assignVisibility(protocol.getFile().getId().toString(),
					visibilityGroups, null);
		}
	}

	public SortedSet<String> getProtocolNamesBy(String protocolType,
			UserBean user) throws Exception {
		SortedSet<String> protocolNames = new TreeSet<String>();
		List<Protocol> protocols = this.findProtocolsBy(protocolType, null,
				null, null, user);
		for (Protocol protocol : protocols) {
			protocolNames.add(protocol.getName());
		}
		return protocolNames;
	}

	public SortedSet<String> getProtocolVersionsBy(String protocolType,
			String protocolName, UserBean user) throws Exception {
		SortedSet<String> protocolVersions = new TreeSet<String>();
		List<Protocol> protocols = this.findProtocolsBy(protocolType,
				protocolName, null, null, user);
		for (Protocol protocol : protocols) {
			protocolVersions.add(protocol.getVersion());
		}
		return protocolVersions;
	}
}
