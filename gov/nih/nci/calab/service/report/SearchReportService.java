package gov.nih.nci.calab.service.report;

import gov.nih.nci.calab.db.HibernateUtil;
import gov.nih.nci.calab.domain.AssociatedFile;
import gov.nih.nci.calab.domain.Report;
import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.common.ReportBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.exception.CaNanoLabSecurityException;
import gov.nih.nci.calab.exception.ReportException;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;

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

	public SearchReportService() throws CaNanoLabSecurityException {
		this.userService = new UserService(CaNanoLabConstants.CSM_APP_NAME);
	}

	public List<ReportBean> getReportByParticle(String particleId, UserBean user)
			throws ReportException, CaNanoLabSecurityException {
		List<ReportBean> reportBeans = new ArrayList<ReportBean>();

		try {

			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();
			List results = session.createQuery(
					"select report from Nanoparticle particle join particle.reportCollection "
							+ " report where particle.id=" + particleId).list();

			for (Object obj : results) {
				Report report = (Report) obj;
				report.getParticleCollection();
				ReportBean reportBean = new ReportBean(report);
				reportBean.setInstanceType(CaNanoLabConstants.REPORT);
				reportBeans.add(reportBean);
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem finding report info for particle: "
					+ particleId, e);
			throw new ReportException();
		} finally {
			HibernateUtil.closeSession();
		}
		if (reportBeans.isEmpty()) {
			return reportBeans;
		}
		List<ReportBean> filteredReportBeans = this.userService
				.getFilteredReports(user, reportBeans);
		return filteredReportBeans;
	}

	public List<ReportBean> searchReports(String reportTitle,
			String reportType, String particleType, String[] functionTypes,
			UserBean user) throws ReportException, CaNanoLabSecurityException {
		List<ReportBean> reports = new ArrayList<ReportBean>();
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
				ReportBean reportBean = null;
				if (obj instanceof Report) {
					Report report = (Report) obj;
					report.getParticleCollection();
					reportBean = new ReportBean(report);
					reportBean.setInstanceType(CaNanoLabConstants.REPORT);
				} else {
					reportBean = new ReportBean((AssociatedFile) obj);
					reportBean
							.setInstanceType(CaNanoLabConstants.ASSOCIATED_FILE);
				}
				reports.add(reportBean);
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem finding report info.", e);
			throw new ReportException();
		} finally {
			HibernateUtil.closeSession();
		}

		List<ReportBean> filteredReports = this.userService.getFilteredReports(
				user, reports);

		return filteredReports;
	}

	/**
	 * retrieve sample report information including reportCollection and
	 * associatedFileCollection
	 * 
	 * @param particleName
	 * @param particleType
	 * @return List of LabFileBean
	 * @throws ReportException
	 * @throws CaNanoLabSecurityException
	 */
	public List<ReportBean> getReportInfo(String particleId, String reportType,
			UserBean user) throws ReportException, CaNanoLabSecurityException {
		List<ReportBean> fileBeans = new ArrayList<ReportBean>();

		String reportJoin = "reportCollection";
		String associatedFileJoin = "associatedFileCollection";

		String hql = "select report from Nanoparticle particle join particle.reportType"
				+ " report where particle.id=" + particleId;
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
				ReportBean fileBean = null;
				if (obj instanceof Report) {
					fileBean = new ReportBean((Report) obj);
				} else {
					fileBean = new ReportBean((AssociatedFile) obj);
				}
				fileBean.setInstanceType(reportType);
				fileBeans.add(fileBean);
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error("Problem finding report info for particle: "
					+ particleId, e);
			throw new ReportException();
		} finally {
			HibernateUtil.closeSession();
		}

		List<ReportBean> filteredReports = this.userService.getFilteredReports(
				user, fileBeans);

		// retrieve visible groups
		UserService userService = new UserService(
				CaNanoLabConstants.CSM_APP_NAME);

		for (LabFileBean fileBean : filteredReports) {
			List<String> accessibleGroups = userService.getAccessibleGroups(
					fileBean.getId(), CaNanoLabConstants.CSM_READ_ROLE);
			String[] visibilityGroups = accessibleGroups.toArray(new String[0]);
			fileBean.setVisibilityGroups(visibilityGroups);
		}
		return filteredReports;
	}

	public String[] getAllReportTypes() {
		String[] allReportTypes = new String[] { CaNanoLabConstants.REPORT,
				CaNanoLabConstants.ASSOCIATED_FILE };
		return allReportTypes;
	}

}
