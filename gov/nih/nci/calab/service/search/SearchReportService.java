package gov.nih.nci.calab.service.search;

import gov.nih.nci.calab.db.HibernateUtil;
import gov.nih.nci.calab.domain.AssociatedFile;
import gov.nih.nci.calab.domain.LabFile;
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
import org.hibernate.Session;

/**
 * This class includes methods invovled in searching reports.
 * 
 * @author pansu
 * 
 */
public class SearchReportService {
	private static Logger logger = Logger.getLogger(SearchReportService.class);

	private UserService userService;

	public SearchReportService() throws Exception {
		userService = new UserService(CaNanoLabConstants.CSM_APP_NAME);

	}

	public List<LabFileBean> getReportByParticle(String particleName,
			String particleType, UserBean user) throws Exception {
		List<LabFileBean> fileBeans = new ArrayList<LabFileBean>();

		try {

			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			List results = session
					.createQuery(
							"select report.id, report.filename, report.uri from Nanoparticle particle join particle.reportCollection "
									+ " report where particle.name='"
									+ particleName
									+ "' and particle.type='"
									+ particleType + "'").list();

			for (Object obj : results) {
				String reportId = ((Object[]) obj)[0].toString();
				String fileName = (String) (((Object[]) obj)[1]);
				String uri = (String) (((Object[]) obj)[2]);
				String toolTip = "";
				int idx = uri.lastIndexOf(File.separator);
				if (idx > 0)
					toolTip = uri.substring(idx + 1);

				LabFileBean fileBean = new LabFileBean();
				fileBean.setId(reportId);
				fileBean.setUri(uri);
				fileBean.setName(fileName);
				fileBean.setType(CaNanoLabConstants.REPORT);
				fileBeans.add(fileBean);
			}
			fileBeans = userService.getFilteredFiles(user, fileBeans);
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem finding report info for particle: "
					+ particleName, e);
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return fileBeans;
	}

	public List<LabFileBean> searchReports(String reportTitle,
			String reportType, String particleType, String[] functionTypes,
			UserBean user) throws Exception {
		List<LabFileBean> reports = new ArrayList<LabFileBean>();
		try {
			HibernateUtil.beginTransaction();
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
				results = HibernateUtil.createQueryByParam(hqlString1,
						paramList).list();
				String hqlString2 = hqlString
						+ "join particle.associatedFileCollection report "
						+ functionTypeFrom + where + whereStr;
				List results2 = HibernateUtil.createQueryByParam(hqlString2,
						paramList).list();
				if (results2 != null) {
					results.addAll(results2);
				}
			} else {
				if (reportType.equals(CaNanoLabConstants.REPORT)) {
					hqlString += "join particle.reportCollection report ";
				} else if (reportType
						.equals(CaNanoLabConstants.ASSOCIATED_FILE)) {
					hqlString += "join particle.associatedFileCollection report ";
				}
				hqlString += functionTypeFrom + where + whereStr;
				results = HibernateUtil
						.createQueryByParam(hqlString, paramList).list();
			}

			for (Object obj : results) {
				LabFileBean fileBean = null;
				if (obj instanceof Report) {
					fileBean = new LabFileBean((Report) obj);
					fileBean.setInstanceType(CaNanoLabConstants.REPORT);
				} else {
					fileBean = new LabFileBean((AssociatedFile) obj);
					fileBean
							.setInstanceType(CaNanoLabConstants.ASSOCIATED_FILE);
				}
				reports.add(fileBean);
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem finding report info.", e);
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}

		List<LabFileBean> filteredReports = userService.getFilteredFiles(user,
				reports);

		return filteredReports;
	}

	/**
	 * retrieve sample report information including reportCollection and
	 * associatedFileCollection
	 * 
	 * @param particleName
	 * @param particleType
	 * @return List of LabFileBean
	 * @throws Exception
	 */
	public List<LabFileBean> getReportInfo(String particleName,
			String particleType, String reportType, UserBean user)
			throws Exception {
		List<LabFileBean> fileBeans = new ArrayList<LabFileBean>();

		String reportJoin = "reportCollection";
		String associatedFileJoin = "associatedFileCollection";

		String hql = "select report from Nanoparticle particle join particle.reportType"
				+ " report where particle.name='"
				+ particleName
				+ "' and particle.type='" + particleType + "'";
		if (reportType.equals(CaNanoLabConstants.REPORT)) {
			hql = hql.replaceAll("reportType", reportJoin);
		} else if (reportType.equals(CaNanoLabConstants.ASSOCIATED_FILE)) {
			hql = hql.replaceAll("reportType", associatedFileJoin);
		}
		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			List results = session.createQuery(hql).list();
			for (Object obj : results) {
				LabFileBean fileBean = new LabFileBean((LabFile) obj);
				fileBean.setInstanceType(reportType);
				fileBeans.add(fileBean);
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem finding report info for particle: "
					+ particleName, e);
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}

		List<LabFileBean> filteredReports = userService.getFilteredFiles(user,
				fileBeans);
		
		// retrieve visible groups
		UserService userService = new UserService(
				CaNanoLabConstants.CSM_APP_NAME);

		for (LabFileBean fileBean: filteredReports) {
			List<String> accessibleGroups=userService.getAccessibleGroups(fileBean.getId(),
							CaNanoLabConstants.CSM_READ_ROLE);
			String[] visibilityGroups = accessibleGroups
					.toArray(new String[0]);
			fileBean.setVisibilityGroups(visibilityGroups);
		}
		return filteredReports;
	}
}
