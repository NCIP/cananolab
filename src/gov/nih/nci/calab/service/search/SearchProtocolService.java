package gov.nih.nci.calab.service.search;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.AssociatedFile;
import gov.nih.nci.calab.domain.LabFile;
import gov.nih.nci.calab.domain.Report;
import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.common.ProtocolFileBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * This class includes methods invovled in searching reports.
 * 
 * @author pansu
 * 
 */
public class SearchProtocolService {
	private static Logger logger = Logger
	.getLogger(SearchReportService.class);	

	private UserService userService;
	
	public SearchProtocolService() throws Exception{
		userService = new UserService(
				CaNanoLabConstants.CSM_APP_NAME);

	}
	public ProtocolFileBean getProtocolFileBean(String fileBeanId) throws Exception {
		ProtocolFileBean fileBean = null;
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);

		try {

			ida.open();
			List results = ida
					.search("select protocolFile.title, protocolFile.description, protocolFile.filename, " +
							"protocolFile.version from ProtocolFile protocolFile " +
							"where protocolFile.id='" + fileBeanId + "'");

			for (Object obj : results) {
				String title = (String)(((Object[]) obj)[0]);
				String description = (String) (((Object[]) obj)[1]);
				String fileName = (String) (((Object[]) obj)[2]);
				String version = (String) (((Object[]) obj)[3]);

				fileBean = new ProtocolFileBean();
				fileBean.setId(fileBeanId);
				fileBean.setDescription(description);
				fileBean.setTitle(title);
				fileBean.setName(fileName);
				fileBean.setVersion(version);
			}

		} catch (Exception e) {
			logger.error("Problem finding protocol file info for protocol file Id: " + fileBeanId);
			throw e;
		} finally {
			ida.close();
		}
		// get visibilities
		UserService userService = new UserService(
				CaNanoLabConstants.CSM_APP_NAME);
		List<String> accessibleGroups = userService.getAccessibleGroups(
				fileBean.getId(), CaNanoLabConstants.CSM_READ_ROLE);
		String[] visibilityGroups = accessibleGroups.toArray(new String[0]);
		fileBean.setVisibilityGroups(visibilityGroups);
		return fileBean;
	}
}
