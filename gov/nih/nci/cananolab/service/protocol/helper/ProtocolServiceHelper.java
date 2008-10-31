package gov.nih.nci.cananolab.service.protocol.helper;

import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.domain.common.ProtocolFile;
import gov.nih.nci.cananolab.dto.common.ProtocolFileBean;
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

	public ProtocolFile findProtocolFileById(String fileId) throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(ProtocolFile.class)
				.add(Property.forName("id").eq(new Long(fileId)));
		List result = appService.query(crit);
		ProtocolFile pf = null;
		if (!result.isEmpty()) {
			pf = (ProtocolFile) result.get(0);
		}
		return pf;
	}

	public List<ProtocolFile> findProtocolFilesBy(String protocolType,
			String protocolName, String fileTitle) throws Exception {
		List<ProtocolFile> protocolFiles = new ArrayList<ProtocolFile>();
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(ProtocolFile.class);
		if (protocolType != null && protocolType.length() > 0
				|| protocolName != null && protocolName.length() > 0) {
			crit.createAlias("protocol", "protocol",
					CriteriaSpecification.LEFT_JOIN);
			if (protocolType != null && protocolType.length() > 0) {
				crit.add(Restrictions.eq("protocol.type", protocolType));
			}
			if (protocolName != null && protocolName.length() > 0) {
				TextMatchMode protocolNameMatchMode = new TextMatchMode(
						protocolName);
				crit.add(Restrictions.ilike("protocol.name",
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
			ProtocolFile pf = (ProtocolFile) obj;
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
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List results = appService.query(crit);
		for (Object obj : results) {
			protocol = (Protocol) obj;
		}
		return protocol;
	}

	public String getProtocolFileUriById(String fileId) {
		String uri = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			DetachedCriteria crit = DetachedCriteria.forClass(
					ProtocolFile.class).add(
					Property.forName("id").eq(new Long(fileId)));
			List result = appService.query(crit);
			if (!result.isEmpty()) {
				ProtocolFile pf = (ProtocolFile) result.get(0);
				uri = pf.getUri();
			}
			return uri;
		} catch (Exception e) {
			return "";
		}
	}

	public String getProtocolFileNameById(String fileId) {
		String name = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			DetachedCriteria crit = DetachedCriteria.forClass(
					ProtocolFile.class).add(
					Property.forName("id").eq(new Long(fileId)));
			List result = appService.query(crit);
			if (!result.isEmpty()) {
				ProtocolFile pf = (ProtocolFile) result.get(0);
				name = pf.getName();
			}
			return name;
		} catch (Exception e) {
			return "";
		}
	}

	public String getProtocolFileVersionById(String fileId) {
		String version = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			DetachedCriteria crit = DetachedCriteria.forClass(
					ProtocolFile.class).add(
					Property.forName("id").eq(new Long(fileId)));
			List result = appService.query(crit);
			if (!result.isEmpty()) {
				ProtocolFile pf = (ProtocolFile) result.get(0);
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

	public List<ProtocolFileBean> getProtocolFiles(String protocolType,
			String protocolName) {
		List<ProtocolFileBean> protocolFiles = new ArrayList<ProtocolFileBean>();
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria
					.forClass(ProtocolFile.class);
			if (protocolType != null && protocolType.length() > 0
					|| protocolName != null && protocolName.length() > 0) {
				crit.createAlias("protocol", "protocol",
						CriteriaSpecification.LEFT_JOIN);
				if (protocolType != null && protocolType.length() > 0) {
					crit.add(Restrictions.eq("protocol.type", protocolType));
				}
				if (protocolName != null && protocolName.length() > 0) {

					crit.add(Restrictions.eq("protocol.name", protocolName));
				}
			}
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List results = appService.query(crit);
			for (Object obj : results) {
				ProtocolFile pf = (ProtocolFile) obj;
				ProtocolFileBean pfb = new ProtocolFileBean(pf);
				protocolFiles.add(pfb);
			}
			return protocolFiles;
		} catch (Exception e) {
			String err = "Problem finding protocol files.";
			logger.error(err, e);
			return null;
		}
	}

	public int getNumberOfPublicProtocolFiles() throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List<String> publicData = appService.getPublicData();
		HQLCriteria crit = new HQLCriteria(
				"select id from gov.nih.nci.cananolab.domain.common.ProtocolFile");
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
