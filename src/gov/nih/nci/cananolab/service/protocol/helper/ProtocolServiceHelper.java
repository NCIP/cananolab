package gov.nih.nci.cananolab.service.protocol.helper;

import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.TextMatchMode;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
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

	public Protocol findProtocolById(String protocolId) throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		DetachedCriteria crit = DetachedCriteria.forClass(Protocol.class).add(
				Property.forName("id").eq(new Long(protocolId)));
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
			String protocolName, String protocolAbbreviation, String fileTitle)
			throws Exception {
		List<Protocol> protocols = new ArrayList<Protocol>();
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(Protocol.class);
		crit.setFetchMode("file", FetchMode.JOIN);
		crit.setFetchMode("file.keywordCollection", FetchMode.JOIN);
		if (protocolType != null && protocolType.length() > 0) {
			crit.add(Restrictions.eq("type", protocolType));
		}
		if (protocolName != null && protocolName.length() > 0) {
			TextMatchMode protocolNameMatchMode = new TextMatchMode(
					protocolName);
			crit.add(Restrictions.ilike("name", protocolNameMatchMode
					.getUpdatedText(), protocolNameMatchMode.getMatchMode()));
		}
		if (protocolAbbreviation != null && protocolAbbreviation.length() > 0) {
			TextMatchMode protocolAbbreviationMatchMode = new TextMatchMode(
					protocolAbbreviation);
			crit.add(Restrictions.ilike("abbreviation",
					protocolAbbreviationMatchMode.getUpdatedText(),
					protocolAbbreviationMatchMode.getMatchMode()));
		}
		if (fileTitle != null && fileTitle.length() > 0) {
			TextMatchMode titleMatchMode = new TextMatchMode(fileTitle);
			crit.add(Restrictions.ilike("file.title", titleMatchMode
					.getUpdatedText(), titleMatchMode.getMatchMode()));
		}
		crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List results = appService.query(crit);
		for (Object obj : results) {
			Protocol protocol = (Protocol) obj;
			protocols.add(protocol);
		}
		return protocols;
	}
}
