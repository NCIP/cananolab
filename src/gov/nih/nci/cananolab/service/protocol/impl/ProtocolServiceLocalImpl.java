package gov.nih.nci.cananolab.service.protocol.impl;

import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.ProtocolException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.impl.FileServiceLocalImpl;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.helper.ProtocolServiceHelper;
import gov.nih.nci.cananolab.service.security.AuthorizationService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;

/**
 * Local implementation of ProtocolService
 *
 * @author pansu
 *
 */
public class ProtocolServiceLocalImpl implements ProtocolService {
	private static Logger logger = Logger
			.getLogger(ProtocolServiceLocalImpl.class);
	private ProtocolServiceHelper helper = new ProtocolServiceHelper();

	public ProtocolBean findProtocolById(String protocolId)
			throws ProtocolException {
		ProtocolBean protocolBean = null;
		try {
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
				protocolBean = new ProtocolBean(protocol);
			}
		} catch (Exception e) {
			String err = "Problem finding the protocol by id: " + protocolId;
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
		return protocolBean;
	}

	/**
	 * Persist a new protocol file or update an existing protocol file
	 *
	 * @param protocol
	 * @throws Exception
	 */
	public void saveProtocol(Protocol protocol, byte[] fileData)
			throws ProtocolException {
		try {
			FileService fileService = new FileServiceLocalImpl();
			fileService.prepareSaveFile(protocol.getFile());

			Protocol dbProtocol = findProtocolBy(protocol.getType(), protocol
					.getName(), protocol.getVersion());
			if (dbProtocol != null) {
				protocol.setCreatedBy(dbProtocol.getCreatedBy());
				protocol.setCreatedDate(dbProtocol.getCreatedDate());
			}
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			appService.saveOrUpdate(protocol);

			// save to the file system fileData is not empty
			fileService.writeFile(protocol.getFile(), fileData);

		} catch (Exception e) {
			String err = "Error in saving the protocol file.";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	public Protocol findProtocolBy(String protocolType, String protocolName,
			String protocolVersion) throws ProtocolException {
		try {
			Protocol protocol = null;
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Protocol.class)
					.add(Property.forName("type").eq(protocolType)).add(
							Property.forName("name").eq(protocolName)).add(
							Property.forName("version").eq(protocolVersion));
			crit.setFetchMode("file", FetchMode.JOIN);
			crit.setFetchMode("file.keywordCollection", FetchMode.JOIN);
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List results = appService.query(crit);
			for (Object obj : results) {
				protocol = (Protocol) obj;
			}
			return protocol;
		} catch (Exception e) {
			String err = "Problem finding protocol by name and type.";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	public List<ProtocolBean> findProtocolsBy(String protocolType,
			String protocolName, String protocolAbbreviation, String fileTitle)
			throws ProtocolException {
		List<ProtocolBean> protocolBeans = new ArrayList<ProtocolBean>();
		try {
			List<Protocol> protocols = helper.findProtocolsBy(protocolType,
					protocolName, protocolAbbreviation, fileTitle, false);

			for (Protocol protocol : protocols) {
				ProtocolBean protocolBean = new ProtocolBean(protocol);
				protocolBeans.add(protocolBean);
			}
			return protocolBeans;
		} catch (Exception e) {
			String err = "Problem finding protocols.";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	public int getNumberOfPublicProtocols() throws ProtocolException {
		try {
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
		} catch (Exception e) {
			String err = "Error finding counts of public protocols.";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	public void retrieveVisibility(ProtocolBean protocolBean, UserBean user)
			throws ProtocolException {
		try {
			if (protocolBean != null) {
				AuthorizationService auth = new AuthorizationService(
						Constants.CSM_APP_NAME);
				if (protocolBean.getDomain().getId() != null
						&& auth.isUserAllowed(protocolBean.getDomain().getId()
								.toString(), user)) {
					protocolBean.setHidden(false);
					// get assigned visible groups
					List<String> accessibleGroups = auth.getAccessibleGroups(
							protocolBean.getDomain().getId().toString(),
							Constants.CSM_READ_PRIVILEGE);
					String[] visibilityGroups = accessibleGroups
							.toArray(new String[0]);
					protocolBean.setVisibilityGroups(visibilityGroups);
				} else {
					protocolBean.setHidden(true);
				}
			}
		} catch (Exception e) {
			String err = "Error in setting file visibility for "
					+ protocolBean.getDisplayName();
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

}
