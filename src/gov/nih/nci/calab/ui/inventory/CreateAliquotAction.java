package gov.nih.nci.calab.ui.inventory;

/**
 * This class saves user entered new aliquot information 
 * into the database.
 * 
 * @author pansu
 */

/* CVS $Id: CreateAliquotAction.java,v 1.6.2.1 2007-07-03 19:42:44 pansu Exp $ */

import gov.nih.nci.calab.dto.inventory.AliquotBean;
import gov.nih.nci.calab.dto.inventory.ContainerInfoBean;
import gov.nih.nci.calab.exception.CalabException;
import gov.nih.nci.calab.service.inventory.ManageAliquotService;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

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
		if (session.getAttribute("aliquotMatrix") != null) {
			List<AliquotBean[]> aliquotMatrix = new ArrayList<AliquotBean[]>(
					(List<? extends AliquotBean[]>) session
							.getAttribute("aliquotMatrix"));
			ManageAliquotService manageAliquotService = new ManageAliquotService();
			manageAliquotService.saveAliquots(fromAliquot, parentName,
					aliquotMatrix);
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage("message.createAliquot");
			msgs.add("message", msg);
			saveMessages(request, msgs);

			// set a flag to indicate that new aliquots have been created so
			// session can
			// be refreshed in initSession.do
			session.setAttribute("newAliquotCreated", "yes");

			forward = mapping.findForward("success");
		} else {
			throw new CalabException(
					"Can't find the aliquot matrix to save.  Please click on 'Update Aliquots' button before submitting");
		}
		return forward;
	}

	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();

		// TODO fill in details for sample information */
		DynaValidatorForm theForm = (DynaValidatorForm) form;

		boolean fromAliquot = ((String) theForm.get("fromAliquot"))
				.equals("true") ? true : false;
		String containerName = (String) theForm.get("containerName");
		String parentAliquotName = (String) theForm.get("parentAliquotName");
		String parentName = (fromAliquot) ? parentAliquotName : containerName;

		String numberOfAliquots = (String) theForm.get("numberOfAliquots");
		int numAliquots = Integer.parseInt(numberOfAliquots);

		AliquotBean template = (AliquotBean) theForm.get("template");

		ManageAliquotService manageAliquotService = new ManageAliquotService();
		int colNum = manageAliquotService.getDefaultAliquotMatrixColumnNumber();
		int rowNum = (int) Math.ceil((float) numAliquots / colNum);

		// calculate the first aliquot Id to use
		int firstAliquotNum = manageAliquotService.getFirstAliquotNum(
				fromAliquot, parentName);
		String aliquotPrefix = manageAliquotService
				.getAliquotPrefix(parentName);

		// get user and date from session
		String creator = (String) session.getAttribute("creator");

		// create a 2-D matrix for aliquot
		List<AliquotBean[]> aliquotMatrix = createAliquotMatrix(colNum, rowNum,
				numAliquots, aliquotPrefix, firstAliquotNum, template, creator);
		session.setAttribute("aliquotMatrix", aliquotMatrix);

		// update editable drop down list to include new entries
		updateAllEditables(request.getSession(), theForm);
		return mapping.findForward("setup");

	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		InitSessionSetup.getInstance().clearWorkflowSession(session);
		InitSessionSetup.getInstance().clearSearchSession(session);

		InitSessionSetup.getInstance().setAllSampleContainers(session);
		InitSessionSetup.getInstance().setAllSampleUnmaskedAliquots(session);
		InitSessionSetup.getInstance().setAllAliquotContainerTypes(session);
		InitSessionSetup.getInstance().setAllAliquotContainerInfo(session);
		InitSessionSetup.getInstance().setAllAliquotCreateMethods(session);
		InitSessionSetup.getInstance().setCurrentUser(session);

		// TODO fill in details for sample information */
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
		String newFreezer = template.getContainer().getStorageLocation().getFreezer();
		String newShelf = template.getContainer().getStorageLocation().getShelf();
		String newBox = template.getContainer().getStorageLocation().getBox();
		containerInfo.getStorageRooms().add(newRoom);
		containerInfo.getStorageFreezers().add(newFreezer);
		containerInfo.getStorageShelves().add(newShelf);
		containerInfo.getStorageBoxes().add(newBox);
	}
}
