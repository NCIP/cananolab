package gov.nih.nci.cananolab.service.common;

import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.PointOfContactException;
import gov.nih.nci.cananolab.service.BaseServiceLocalImpl;
import gov.nih.nci.cananolab.service.common.helper.PointOfContactServiceHelper;
import gov.nih.nci.cananolab.service.common.impl.PointOfContactService;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class PointOfContactServiceLocalImpl extends BaseServiceLocalImpl
		implements PointOfContactService {

	private static Logger logger = Logger
			.getLogger(SampleServiceLocalImpl.class);

	private PointOfContactServiceHelper helper;

	public PointOfContactServiceLocalImpl() {
		super();
		helper = new PointOfContactServiceHelper(this.securityService);
	}

	public PointOfContactServiceLocalImpl(UserBean user) {
		super(user);
		helper = new PointOfContactServiceHelper(user);
	}

	public PointOfContactServiceLocalImpl(SecurityService securityService) {
		super(securityService);
		helper = new PointOfContactServiceHelper(this.securityService);

	}

	public PointOfContactBean findPointOfContactById(String pocId)
			throws PointOfContactException {
		PointOfContactBean pocBean = null;
		try {
			PointOfContact poc = helper.findPointOfContactById(pocId);
			pocBean = new PointOfContactBean(poc);
		} catch (Exception e) {
			String err = "Problem finding point of contact for the given id.";
			logger.error(err, e);
			throw new PointOfContactException(err, e);
		}
		return pocBean;
	}

	public List<PointOfContactBean> findPointOfContactsBySampleId(
			String sampleId) throws PointOfContactException {
		List<PointOfContactBean> pocBeans = new ArrayList<PointOfContactBean>();
		try {
			List<PointOfContact> pocs = helper
					.findPointOfContactsBySampleId(sampleId);
			for (PointOfContact poc : pocs) {
				pocBeans.add(new PointOfContactBean(poc));
			}
		} catch (Exception e) {
			String err = "Problem finding point of contacts for the given sample id.";
			logger.error(err, e);
			throw new PointOfContactException(err, e);
		}
		return pocBeans;
	}

	public List<PointOfContactBean> findPointOfContactsByStudyId(String studyId)
			throws PointOfContactException {
		List<PointOfContactBean> pocBeans = new ArrayList<PointOfContactBean>();
		try {
			List<PointOfContact> pocs = helper
					.findPointOfContactsBySampleId(studyId);
			for (PointOfContact poc : pocs) {
				pocBeans.add(new PointOfContactBean(poc));
			}
		} catch (Exception e) {
			String err = "Problem finding point of contacts for the given study id.";
			logger.error(err, e);
			throw new PointOfContactException(err, e);
		}
		return pocBeans;
	}

	public void savePointOfContact(PointOfContactBean pointOfContactBean)
			throws PointOfContactException, NoAccessException {
		// TODO Auto-generated method stub

	}

	public PointOfContactServiceHelper getHelper() {
		return helper;
	}
}
