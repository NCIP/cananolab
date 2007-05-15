package gov.nih.nci.calab.service.search;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.AssociatedFile;
import gov.nih.nci.calab.domain.Report;
import gov.nih.nci.calab.dto.common.LabFileBean;
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
public class SearchReportService {
	private static Logger logger = Logger
	.getLogger(SearchReportService.class);	

	private UserService userService;
	
	public SearchReportService() throws Exception{
		userService = new UserService(
				CaNanoLabConstants.CSM_APP_NAME);

	}
	public List<LabFileBean> getReportByParticle(String particleName,
			String particleType, UserBean user) throws Exception {
		List<LabFileBean> fileBeans = new ArrayList<LabFileBean>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);

		try {

			ida.open();
			List results = ida
					.search("select report.id, report.filename, report.path from Nanoparticle particle join particle.reportCollection "
							+ " report where particle.name='"
							+ particleName
							+ "' and particle.type='" + particleType + "'");

			for (Object obj : results) {
				String reportId = ((Object[]) obj)[0].toString();
				String fileName = (String) (((Object[]) obj)[1]);
				String path = (String) (((Object[]) obj)[2]);
				String toolTip = "";
				int idx = path.lastIndexOf(File.separator);
				if (idx > 0)
					toolTip = path.substring(idx + 1);

				LabFileBean fileBean = new LabFileBean();
				fileBean.setId(reportId);
				fileBean.setPath(path);
				fileBean.setName(fileName);
				fileBean.setType(CaNanoLabConstants.REPORT);
				fileBeans.add(fileBean);
			}

			fileBeans = userService.getFilteredFiles(user, fileBeans);
		} catch (Exception e) {
			logger.error("Problem finding report info for particle: "
					+ particleName);
			throw e;
		} finally {
			ida.close();
		}
		return fileBeans;
	}

	public List<LabFileBean> searchReports(String reportTitle,
			String reportType, String particleType, String[] functionTypes,
			UserBean user) throws Exception {
		List<LabFileBean> reports = new ArrayList<LabFileBean>();
		IDataAccess ida = (new DataAccessProxy())
				.getInstance(IDataAccess.HIBERNATE);
		try {

			ida.open();
			List<Object> paramList = new ArrayList<Object>();
			List<String> whereList = new ArrayList<String>();
			String where = "";
			String functionTypeFrom = "";

			if (reportTitle.length() > 0) {
				where = "where ";
				if (reportTitle.indexOf("*") != -1) {
					reportTitle = reportTitle.replace('*', '%');
					whereList.add("report.title like ?");
				} else {
					whereList.add("report.title=? ");
				}
				paramList.add(reportTitle.toUpperCase());
			}
			if (particleType.length() > 0) {
				paramList.add(particleType);
				where = "where ";
				whereList.add("particle.type=? ");
			}
			if (functionTypes != null && functionTypes.length > 0) {
				List<String> inList = new ArrayList<String>();
				where = "where ";
				for (String functionType : functionTypes) {
					paramList.add(functionType);
					inList.add("?");
				}
				functionTypeFrom = "join particle.functionCollection function ";
				whereList.add("function.type in ("
						+ StringUtils.join(inList, ", ") + ") ");
			}
			String whereStr = StringUtils.join(whereList, " and ");

			String hqlString = "select distinct report from Nanoparticle particle ";
			List results = null;
			if (reportType.length() == 0) {
				String hqlString1 = hqlString
						+ "join particle.reportCollection report "
						+ functionTypeFrom + where + whereStr;
				results = ida.searchByParam(hqlString1, paramList);
				String hqlString2 = hqlString
						+ "join particle.associatedFileCollection report "
						+ functionTypeFrom + where + whereStr;
				List results2 = ida.searchByParam(hqlString2, paramList);
				if (results2 != null) {
					results.addAll(results2);
				}
			} else {
				if (reportType.equals(CaNanoLabConstants.REPORT)) {
					hqlString += "join particle.reportCollection report ";
				} else if (reportType.equals(CaNanoLabConstants.ASSOCIATED_FILE)) {
					hqlString += "join particle.associatedFileCollection report ";
				}
				hqlString += functionTypeFrom + where + whereStr;
				results = ida.searchByParam(hqlString, paramList);
			}

			for (Object obj : results) {
				LabFileBean fileBean =null;
				if (obj instanceof Report) {
					fileBean=new LabFileBean((Report)obj, CaNanoLabConstants.REPORT);
				}
				else {
					fileBean=new LabFileBean((AssociatedFile)obj, CaNanoLabConstants.ASSOCIATED_FILE);
				}
				reports.add(fileBean);
			}
		} catch (Exception e) {
			logger.error("Problem finding report info.");
			throw e;
		} finally {
			ida.close();
		}

		List<LabFileBean> filteredReports = userService.getFilteredFiles(
				user, reports);

		return filteredReports;
	}

}
