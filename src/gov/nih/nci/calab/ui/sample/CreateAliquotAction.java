package gov.nih.nci.calab.ui.sample;

/**
 * This class saves user entered new aliquot information 
 * into the database.
 * 
 * @author pansu
 */

/* CVS $Id: CreateAliquotAction.java,v 1.8 2007-12-19 16:19:11 pansu Exp $ */

import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.sample.AliquotBean;
import gov.nih.nci.calab.dto.sample.ContainerInfoBean;
import gov.nih.nci.calab.exception.CaNanoLabSecurityException;
import gov.nih.nci.calab.exception.SampleException;
import gov.nih.nci.calab.service.sample.AliquotService;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
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

public class CreateAliquotAction extends AbstractDispatchAction {

	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		HttpSession session = request.getSession();

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		boolean fromAliquot = ((String) theForm.get("fromAliquot"))
				.equals("true") ? true : false;
		String containerName = (String) theForm.get("containerName");
		String parentAliquotName = (String) theForm.get("parentAliquotName");
		String parentName = (fromAliquot) ? parentAliquotName : containerName;
		String fullParentName = (!fromAliquot) ? "Sample Container "
				+ containerName : "Aliquot " + parentAliquotName;
		request.setAttribute("fullParentName", fullParentName);
		ActionMessages msgs = new ActionMessages();
		if (session.getAttribute("aliquotMatrix") != null) {
			List<AliquotBean[]> aliquotMatrix = new ArrayList<AliquotBean[]>(
					(List<? extends AliquotBean[]>) session
							.getAttribute("aliquotMatrix"));
			AliquotService manageAliquotService = new AliquotService();
			manageAliquotService.saveAliquots(fromAliquot, parentName,
					aliquotMatrix);
			ActionMessage msg = new ActionMessage("message.createAliquot");
			msgs.add("message", msg);
			saveMessages(request, msgs);
			forward = mapping.findForward("success");
		} else {
			ActionMessage msg = new ActionMessage("msg.empty.aliquotMatrix");
			msgs.add("message", msg);
			saveMessages(request, msgs);
			forward=mapping.getInputForward();
		}
		return forward;
	}

	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();

		DynaValidatorForm theForm = (DynaValidatorForm) form;

		boolean fromAliquot = ((String) theForm.get("fromAliquot"))
				.equals("true") ? true : false;
		String containerName = (String) theForm.get("containerName");
		String parentAliquotName = (String) theForm.get("parentAliquotName");
		String parentName = (fromAliquot) ? parentAliquotName : containerName;

		String numberOfAliquots = (String) theForm.get("numberOfAliquots");
		int numAliquots = Integer.parseInt(numberOfAliquots);

		AliquotBean template = (AliquotBean) theForm.get("template");

		AliquotService manageAliquotService = new AliquotService();
		int colNum = manageAliquotService.getDefaultAliquotMatrixColumnNumber();
		int rowNum = (int) Math.ceil((float) numAliquots / colNum);

		// calculate the first aliquot Id to use
		int firstAliquotNum = manageAliquotService.getFirstAliquotNum(
				fromAliquot, parentName);
		String aliquotPrefix = manageAliquotService
				.getAliquotPrefix(parentName);

		// get user and date from session
		UserBean creator = (UserBean) session.getAttribute("user");
		// create a 2-D matrix for aliquot
		List<AliquotBean[]> aliquotMatrix = createAliquotMatrix(colNum, rowNum,
				numAliquots, aliquotPrefix, firstAliquotNum, template, creator
						.getLoginName());
		session.setAttribute("aliquotMatrix", aliquotMatrix);

		// update editable drop down list to include new entries
		updateAllEditables(request.getSession(), theForm);
		return mapping.findForward("setup");

	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		InitSampleSetup.getInstance().setAllSampleContainers(session);
		InitSampleSetup.getInstance().setAllSampleUnmaskedAliquots(session);
		InitSampleSetup.getInstance().setAllAliquotContainerTypes(session);
		InitSampleSetup.getInstance().setAllAliquotContainerInfo(session);
		InitSampleSetup.getInstance().setAllAliquotCreateMethods(session);

		session.removeAttribute("createAliquotForm");
		session.removeAttribute("aliquotMatrix");

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

	/**
	 * 
	 * @return a 2-D matrix of aliquots
	 */
	private List<AliquotBean[]> createAliquotMatrix(int colNum, int rowNum,
			int numAliquots, String aliquotPrefix, int firstAliquotName,
			AliquotBean template, String aliquotCreator) {

		List<AliquotBean[]> aliquotMatrix = new ArrayList<AliquotBean[]>();
		int aliquotName = firstAliquotName;
		for (int i = 0; i < rowNum; i++) {
			// calculate number of columsn per row
			int cols = colNum;
			if (numAliquots % colNum < colNum && numAliquots % colNum > 0
					&& i == rowNum - 1) {
				cols = numAliquots % colNum;
			}
			AliquotBean[] aliquotRow = new AliquotBean[colNum];

			for (int j = 0; j < cols; j++) {
				AliquotBean aliquot = new AliquotBean(aliquotPrefix
						+ aliquotName, template.getContainer(), template
						.getHowCreated(), aliquotCreator, new Date());
				aliquotRow[j] = aliquot;
				aliquotName++;
			}
			aliquotMatrix.add(aliquotRow);
		}

		return aliquotMatrix;
	}

	private void updateAllEditables(HttpSession session,
			DynaValidatorForm theForm) throws Exception {
		AliquotBean template = ((AliquotBean) theForm.get("template"));
		InitSessionSetup.getInstance().updateEditableDropdown(session,
				template.getContainer().getContainerType(),
				"allAliquotContainerTypes");
		ContainerInfoBean containerInfo = (ContainerInfoBean) session
				.getAttribute("aliquotContainerInfo");
		String newRoom = template.getContainer().getStorageLocation().getRoom();
		String newFreezer = template.getContainer().getStorageLocation()
				.getFreezer();
		String newShelf = template.getContainer().getStorageLocation()
				.getShelf();
		String newBox = template.getContainer().getStorageLocation().getBox();
		containerInfo.getStorageRooms().add(newRoom);
		containerInfo.getStorageFreezers().add(newFreezer);
		containerInfo.getStorageShelves().add(newShelf);
		containerInfo.getStorageBoxes().add(newBox);
	}

	public boolean canUserExecute(UserBean user)
			throws CaNanoLabSecurityException {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				CaNanoLabConstants.CSM_PG_SAMPLE);
	}
}
