package gov.nih.nci.calab.ui.inventory;

/**
 * This class saves user edited aliquot data. 
 * 
 * @author pansu
 */

/* CVS $Id: EditAliquotAction.java,v 1.2.2.1 2007-07-03 19:42:44 pansu Exp $ */

import gov.nih.nci.calab.dto.inventory.AliquotBean;
import gov.nih.nci.calab.dto.inventory.ContainerInfoBean;
import gov.nih.nci.calab.exception.CalabException;
import gov.nih.nci.calab.ui.core.AbstractDispatchAction;
import gov.nih.nci.calab.ui.core.InitSessionSetup;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class EditAliquotAction extends AbstractDispatchAction {
	public ActionForward edit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		HttpSession session = request.getSession();
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		AliquotBean aliquot = (AliquotBean) theForm.get("aliquot");
		aliquot.setCreationDate(new Date());
		int rowNum = Integer.parseInt((String) theForm.get("rowNum"));
		int colNum = Integer.parseInt((String) theForm.get("colNum"));
		if (session.getAttribute("aliquotMatrix") != null) {
			List aliquotMatrx = (List) session.getAttribute("aliquotMatrix");
			// replace the aliquot in the matrix and reset the session variable
			((AliquotBean[]) aliquotMatrx.get(rowNum))[colNum] = aliquot;
			forward = mapping.findForward("success");
		} else {
			throw new CalabException(
					"Session containing the aliquot matrix either is expired or doesn't exist");
		}
		return forward;
	}

	public ActionForward input(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		updateAllEditables(request.getSession(), theForm);
		return mapping.findForward("setup");
	}

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		HttpSession session = request.getSession();

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		int rowNum = Integer.parseInt((String) theForm.get("rowNum"));
		int colNum = Integer.parseInt((String) theForm.get("colNum"));
		if (session.getAttribute("aliquotMatrix") != null) {
			List aliquotMatrx = (List) session.getAttribute("aliquotMatrix");
			AliquotBean aliquot = ((AliquotBean[]) aliquotMatrx.get(rowNum))[colNum];
			theForm.set("aliquot", aliquot);
			forward = mapping.getInputForward();
		} else {
			throw new CalabException(
					"Session containing the aliquot matrix either is expired or doesn't exist");
		}

		// update editable drop-down to include new entry
		updateAllEditables(request.getSession(), theForm);
		return forward;
	}

	public boolean loginRequired() {
		return true;
	}

	private void updateAllEditables(HttpSession session,
			DynaValidatorForm theForm) throws Exception {
		AliquotBean aliquot = ((AliquotBean) theForm.get("aliquot"));
		InitSessionSetup.getInstance().updateEditableDropdown(session,
				aliquot.getContainer().getContainerType(),
				"allAliquotContainerTypes");
		ContainerInfoBean containerInfo = (ContainerInfoBean) session
				.getAttribute("aliquotContainerInfo");
		String newRoom = aliquot.getContainer().getStorageLocation().getRoom();
		String newFreezer = aliquot.getContainer().getStorageLocation()
				.getFreezer();
		String newShelf = aliquot.getContainer().getStorageLocation()
				.getShelf();
		String newBox = aliquot.getContainer().getStorageLocation().getBox();
		containerInfo.getStorageRooms().add(newRoom);
		containerInfo.getStorageFreezers().add(newFreezer);
		containerInfo.getStorageShelves().add(newShelf);
		containerInfo.getStorageBoxes().add(newBox);
	}
}
