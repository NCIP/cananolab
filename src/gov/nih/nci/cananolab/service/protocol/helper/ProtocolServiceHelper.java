package gov.nih.nci.cananolab.service.protocol.helper;

import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
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
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

/**
 * This class includes methods involved in creating and searching protocols
 *
 * @author pansu
 *
 */
public class ProtocolServiceHelper {
	private static Logger logger = Logger
			.getLogger(ProtocolServiceHelper.class);

	public Protocol findProtocolById(String protocolId) throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(Protocol.class)
				.add(Property.forName("id").eq(new Long(protocolId)));
		crit.setFetchMode("file", FetchMode.JOIN);
		crit.setFetchMode("file.keywordCollection", FetchMode.JOIN);
		List result = appService.query(crit);
		Protocol protocol = null;
		if (!result.isEmpty()) {
			protocol = (Protocol) result.get(0);
		}
		return protocol;
	}

	public List<Protocol> findProtocolsBy(String protocolType,
			String protocolName, String fileTitle) throws Exception {
		List<Protocol> protocolFiles = new ArrayList<Protocol>();
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(Protocol.class);
		crit.setFetchMode("file", FetchMode.JOIN);
		crit.setFetchMode("file.keywordCollection", FetchMode.JOIN);
		if (protocolType != null && protocolType.length() > 0
				|| protocolName != null && protocolName.length() > 0) {
			if (protocolType != null && protocolType.length() > 0) {
				crit.add(Restrictions.eq("type", protocolType));
			}
			if (protocolName != null && protocolName.length() > 0) {
				TextMatchMode protocolNameMatchMode = new TextMatchMode(
						protocolName);
				crit.add(Restrictions.ilike("name",
						protocolNameMatchMode.getUpdatedText(),
						protocolNameMatchMode.getMatchMode()));
			}
		}
		if (fileTitle != null && fileTitle.length() > 0) {
			TextMatchMode titleMatchMode = new TextMatchMode(fileTitle);
			crit.add(Restrictions.ilike("title", titleMatchMode
					.getUpdatedText(), titleMatchMode.getMatchMode()));
		}
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List results = appService.query(crit);
		for (Object obj : results) {
			Protocol pf = (Protocol) obj;
			protocolFiles.add(pf);
		}
		return protocolFiles;
	}

	public Protocol findProtocolBy(String protocolType, String protocolName)
			throws Exception {
		Protocol protocol = null;

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(Protocol.class).add(
				Property.forName("type").eq(protocolType)).add(
				Property.forName("name").eq(protocolName));
		crit.setFetchMode("file", FetchMode.JOIN);
		crit.setFetchMode("file.keywordCollection", FetchMode.JOIN);
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List results = appService.query(crit);
		for (Object obj : results) {
			protocol = (Protocol) obj;
		}
		return protocol;
	}

	public String getProtocolUriById(String protocolId) {
		String uri = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			DetachedCriteria crit = DetachedCriteria.forClass(
					Protocol.class).add(
					Property.forName("id").eq(new Long(protocolId)));
			List result = appService.query(crit);
			if (!result.isEmpty()) {
				Protocol protocol = (Protocol) result.get(0);
				uri = protocol.getFile().getUri();
			}
			return uri;
		} catch (Exception e) {
			return "";
		}
	}

	public String getProtocolNameById(String protocolId) {
		String name = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			DetachedCriteria crit = DetachedCriteria.forClass(
					Protocol.class).add(
					Property.forName("id").eq(new Long(protocolId)));
			List result = appService.query(crit);
			if (!result.isEmpty()) {
				Protocol pf = (Protocol) result.get(0);
				name = pf.getName();
			}
			return name;
		} catch (Exception e) {
			return "";
		}
	}

	public String getProtocolVersionById(String protocolId) {
		String version = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			DetachedCriteria crit = DetachedCriteria.forClass(
					Protocol.class).add(
					Property.forName("id").eq(new Long(protocolId)));
			List result = appService.query(crit);
			if (!result.isEmpty()) {
				Protocol pf = (Protocol) result.get(0);
				version = pf.getVersion();
			}
			return version;
		} catch (Exception e) {
			return "";
		}
	}

	public SortedSet<String> getProtocolNames(String protocolType) {
		if (protocolType == null || protocolType.length() == 0) {
			return null;
		}
		SortedSet<String> protocolNames = new TreeSet<String>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Protocol.class)
					.add(Property.forName("type").eq(protocolType));
			List results = appService.query(crit);
			for (Object obj : results) {
				Protocol protocol = (Protocol) obj;
				protocolNames.add(protocol.getName());
			}
			return protocolNames;
		} catch (Exception e) {
			String err = "Problem finding protocols base on protocol type.";
			logger.error(err, e);
			return null;
		}
	}

	public List<ProtocolBean> getProtocols(String protocolType,
			String protocolName) {
		List<ProtocolBean> protocols = new ArrayList<ProtocolBean>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria
					.forClass(Protocol.class);
			crit.setFetchMode("file", FetchMode.JOIN);
			crit.setFetchMode("file.keywordCollection", FetchMode.JOIN);
			if (protocolType != null && protocolType.length() > 0
					|| protocolName != null && protocolName.length() > 0) {
				if (protocolType != null && protocolType.length() > 0) {
					crit.add(Restrictions.eq("type", protocolType));
				}
				if (protocolName != null && protocolName.length() > 0) {

					crit.add(Restrictions.eq("name", protocolName));
				}
			}
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List results = appService.query(crit);
			for (Object obj : results) {
				Protocol protocol = (Protocol) obj;
				ProtocolBean pb = new ProtocolBean(protocol);
				protocols.add(pb);
			}
			return protocols;
		} catch (Exception e) {
			String err = "Problem finding protocol.";
			logger.error(err, e);
			return null;
		}
	}

	public int getNumberOfPublicProtocols() throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List<String> publicData = appService.getPublicData();
		HQLCriteria crit = new HQLCriteria(
				"select id from gov.nih.nci.cananolab.domain.common.Protocol");
		List results = appService.query(crit);
		List<String> publicIds = new ArrayList<String>();
		for (Object obj : results) {
			String id = (String) obj.toString();
			if (StringUtils.containsIgnoreCase(publicData, id)) {
				publicIds.add(id);
			}
		}
		return publicIds.size();
	}
}
