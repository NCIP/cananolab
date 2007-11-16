package gov.nih.nci.calab.ui.sample;

/**
 * This class saves user entered sample and container information 
 * into the database.
 * 
 * @author pansu
 */

/* CVS $Id: CreateSampleAction.java,v 1.3 2007-11-16 22:30:15 pansu Exp $ */

import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.sample.ContainerBean;
import gov.nih.nci.calab.dto.sample.ContainerInfoBean;
import gov.nih.nci.calab.dto.sample.SampleBean;
import gov.nih.nci.calab.service.sample.ManageSampleService;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.PropertyReader;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;
import gov.nih.nci.calab.ui.security.InitSecuritySetup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

public class CreateSampleAction extends AbstractDispatchAction {
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		HttpSession session = request.getSession();

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SampleBean sample = (SampleBean) theForm.get("sample");

		// read from properties file first
		String samplePrefix = PropertyReader.getProperty(
				CaNanoLabConstants.CANANOLAB_PROPERTY, "samplePrefix");
		// if not available, use the default
		if (samplePrefix == null)
			samplePrefix = CaNanoLabConstants.DEFAULT_SAMPLE_PREFIX;

		// throw an error is the sample name prefix doesn't start with the
		// preconfigured prefix.
		if (!sample.getSampleNamePrefix().startsWith(samplePrefix)) {
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage(
					"error.createSample.SampleIDFormat", samplePrefix);
			msgs.add("error", msg);
			saveMessages(request, msgs);

			forward = mapping.findForward("input");

			return forward;
		}
		ManageSampleService manageSampleService = new ManageSampleService();
		String fullSampleName = manageSampleService.getSampleName(sample
				.getSampleNamePrefix(), sample.getLotId());
		sample.setSampleName(fullSampleName);
		// get user information from session
		UserBean sampleSubmitter = (UserBean) session.getAttribute("user");
		sample.setAccessionDate(new Date());
		sample.setSampleSubmitter(sampleSubmitter.getLoginName());
		
		List<ContainerBean> containers = sample.getContainers();
		// update container name to be full container name
		String containerPrefix = manageSampleService.getContainerPrefix(sample
				.getSampleName(), sample.getLotId());
		for (ContainerBean container : containers) {
			container.setContainerName(containerPrefix + "-"
					+ container.getContainerName());
		}		
		manageSampleService.saveSample(sample);
		request.setAttribute("sample", sample);

		// create a new user group for the source specified
		UserService userService = new UserService(
				CaNanoLabConstants.CSM_APP_NAME);
		userService.createAGroup(sample.getSampleSource());

		// set a flag to indicate that new sample have been created so session
		// can be refreshed in initSession.do
		session.setAttribute("newSampleCreated", "yes");
		session.setAttribute("newSampleSourceCreated", "true");

		forward = mapping.findForward("success");

		return forward;
	}

	public ActionForward addContainer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SampleBean sample=(SampleBean)theForm.get("sample");	
		List<ContainerBean> origContainers = sample
				.getContainers();
		int origNum = (origContainers == null) ? 0 : origContainers.size();
		List<ContainerBean> containers  = new ArrayList<ContainerBean>();
		for (int i = 0; i < origNum; i++) {
			containers.add(origContainers.get(i));
		}
		// add a new one to be the same as template
		containers.add(new ContainerBean(origContainers.get(0)));
		sample.setContainers(containers);
		return input(mapping, form, request, response);
	}

	public ActionForward removeContainer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String indexStr = (String) request.getParameter("containerInd");
		int ind = Integer.parseInt(indexStr);
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SampleBean sample = (SampleBean) theForm
				.get("sample");
		List<ContainerBean> origContainers = sample.getContainers();
		int origNum = (origContainers == null) ? 0 : origContainers.size();
		List<ContainerBean> containers = new ArrayList<ContainerBean>();
		for (int i = 0; i < origNum; i++) {
			containers.add(origContainers.get(i));
		}
		// remove the one at the index
		if (origNum > 0) {
			containers.remove(ind);
		}
		sample.setContainers(containers);
		return input(mapping, form, request, response);
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		InitSampleSetup.getInstance().setAllSampleSources(session);
		InitSampleSetup.getInstance().setAllSampleSOPs(session);
		InitSampleSetup.getInstance().setAllSampleContainerTypes(session);
		InitSampleSetup.getInstance().setAllSampleContainerInfo(session);

		ManageSampleService mangeSampleService = new ManageSampleService();
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		SampleBean sample =new SampleBean();
		sample.setSampleNamePrefix(mangeSampleService.getDefaultSampleName());
		String configuredSampleNamePrefix = PropertyReader.getProperty(
				CaNanoLabConstants.CANANOLAB_PROPERTY, "samplePrefix");
		// if not available, use the default
		if (configuredSampleNamePrefix == null)
			configuredSampleNamePrefix = CaNanoLabConstants.DEFAULT_SAMPLE_PREFIX;
		theForm.set("configuredSampleNamePrefix", configuredSampleNamePrefix);
		theForm.set("sample", sample);
		return mapping.findForward("setup");
	}

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		updateAllEditables(request.getSession(), theForm);
		return mapping.findForward("setup");
	}

	public boolean loginRequired() {
		return true;
	}

	private void updateAllEditables(HttpSession session,
			DynaValidatorForm theForm) throws Exception {
		SampleBean sample=(SampleBean)theForm.get("sample");
		InitSessionSetup.getInstance().updateEditableDropdown(session,
				sample.getSampleSource(), "allSampleSources");
		ContainerInfoBean containerInfo = (ContainerInfoBean) session
				.getAttribute("sampleContainerInfo");

		for (ContainerBean containerBean : sample.getContainers()) {
			InitSessionSetup.getInstance()
					.updateEditableDropdown(session,
							containerBean.getContainerType(),
							"allSampleContainerTypes");
			String newRoom = containerBean.getStorageLocation().getRoom();
			String newFreezer = containerBean.getStorageLocation().getFreezer();
			String newShelf = containerBean.getStorageLocation().getShelf();
			String newBox = containerBean.getStorageLocation().getBox();

			containerInfo.getStorageRooms().add(newRoom);
			containerInfo.getStorageFreezers().add(newFreezer);
			containerInfo.getStorageShelves().add(newShelf);
			containerInfo.getStorageBoxes().add(newBox);
		}
	}

	public boolean canUserExecute(UserBean user) throws Exception {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				CaNanoLabConstants.CSM_PG_SAMPLE);
	}
}
