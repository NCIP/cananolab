package gov.nih.nci.calab.service.workflow;

import gov.nih.nci.calab.db.HibernateUtil;
import gov.nih.nci.calab.domain.Aliquot;
import gov.nih.nci.calab.domain.DataStatus;
import gov.nih.nci.calab.domain.LabFile;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;

import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 * Generalizes Mask functionality for masking Aliquot, File, etc.
 * 
 * @author doswellj
 * @param strType
 *            Type of Mask (e.g., aliquot, file, run, etc.)
 * @param strId
 *            The id associated to the type
 * @param strDescription
 *            The mask description associated to the mask type and Id.
 * 
 */
public class MaskService {
	private static Logger logger = Logger.getLogger(MaskService.class);

	// This functionality is pending the completed Object Model
	public void setMask(String strType, String strId, String strDescription)
			throws Exception {

		try {
			Session session = HibernateUtil.currentSession();
			HibernateUtil.beginTransaction();

			if (strType.equals("aliquot")) {
				// TODO Find Aliquot record based on the strID
				// List aliquots = session.createQuery("from Aliquot aliquot
				// where
				// aliquot.name='" + strId + "'");
				Aliquot aliquot = (Aliquot) session.load(Aliquot.class,
						StringUtils.convertToLong(strId));
				DataStatus maskStatus = new DataStatus();
				maskStatus.setReason(strDescription);
				maskStatus.setStatus(CaNanoLabConstants.MASK_STATUS);
				session.save(maskStatus);

				aliquot.setDataStatus(maskStatus);
				session.saveOrUpdate(aliquot);
			}
			if (strType.equals("file")) {
				LabFile file = (LabFile) session.load(LabFile.class,
						StringUtils.convertToLong(strId));
				DataStatus maskStatus = new DataStatus();
				maskStatus.setReason(strDescription);
				maskStatus.setStatus(CaNanoLabConstants.MASK_STATUS);
				session.save(maskStatus);

				file.setDataStatus(maskStatus);
				session.saveOrUpdate(file);
			}
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			logger.error("Error in masking", e);
			throw new RuntimeException(e.getMessage());
		} finally {
			HibernateUtil.closeSession();
		}

	}

}
