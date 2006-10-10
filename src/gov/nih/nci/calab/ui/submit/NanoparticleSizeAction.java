package gov.nih.nci.calab.ui.submit;

/**
 * This class sets up input form for size characterization. 
 *  
 * @author pansu
 */

/* CVS $Id: NanoparticleSizeAction.java,v 1.5 2006-10-10 14:05:18 chand Exp $ */

import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.CharacterizationTable;

import gov.nih.nci.calab.dto.characterization.CharacterizationFileBean;
import gov.nih.nci.calab.dto.characterization.CharacterizationTableBean;
import gov.nih.nci.calab.dto.characterization.SizeBean;
import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.service.search.SearchNanoparticleService;
import gov.nih.nci.calab.service.submit.SubmitNanoparticleService;
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

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.File;


public class NanoparticleSizeAction extends AbstractDispatchAction {
	private static Logger logger = Logger.getLogger(NanoparticleSizeAction.class);

	/**
	 * Add or update the data to database
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");
		SizeBean sizeChar=(SizeBean) theForm.get("achar");
		
		if (sizeChar.getId() == null || sizeChar.getId() == "") {
			
			sizeChar.setId( (String) theForm.get("characterizationId") );
			
		}
		
		int fileNumber = 0;
		for (CharacterizationTableBean obj : sizeChar.getCharacterizationTables()) {
			CharacterizationFileBean fileBean = (CharacterizationFileBean) request.getSession().getAttribute("characterizationFile" + fileNumber);
			if (fileBean != null) {		
				logger.info("************set fileBean to " + fileNumber);
				obj.setFile(fileBean);
			}
			fileNumber++;
		}

		
		// set createdBy and createdDate for the composition
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		Date date = new Date();
		sizeChar.setCreatedBy(user.getLoginName());
		sizeChar.setCreatedDate(date);

		request.getSession().setAttribute("newCharacterizationCreated", "true");
		SubmitNanoparticleService service = new SubmitNanoparticleService();
		service.addParticleSize(particleType, particleName, sizeChar);

		ActionMessages msgs = new ActionMessages();
		ActionMessage msg = new ActionMessage("message.addParticleSize");
		msgs.add("message", msg);
		saveMessages(request, msgs);
		forward = mapping.findForward("success");

		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);

		return forward;
	}

	/**
	 * Set up the input forms for adding data
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;

		HttpSession session = request.getSession();
		// clear session data from the input forms
		clearMap(session, theForm, mapping);
		initSetup(request, theForm);
		return mapping.getInputForward();
	}

	private void clearMap(HttpSession session, DynaValidatorForm theForm,
			ActionMapping mapping) throws Exception {
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");

		// clear session data from the input forms
		theForm.getMap().clear();

		theForm.set("particleName", particleName);
		theForm.set("particleType", particleType);
		theForm.set("achar", new SizeBean());
	}

	private void initSetup(HttpServletRequest request, DynaValidatorForm theForm)
			throws Exception {
		HttpSession session = request.getSession();
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");
		InitSessionSetup.getInstance().setAllInstrumentTypes(session);
		InitSessionSetup.getInstance().setAllSizeDistributionGraphTypes(session);
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		InitSessionSetup.getInstance().setManufacturerPerType(session);
	}

	/**
	 * Set up the input forms for updating data
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward setupUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleType = (String) theForm.get("particleType");
		String compositionId = (String) theForm.get("characterizationId");
		
		SearchNanoparticleService service = new SearchNanoparticleService();
		Characterization aChar = service.getCharacterizationAndTableBy(compositionId);
		
		HttpSession session = request.getSession();
		// clear session data from the input forms
		clearMap(session, theForm, mapping);

		theForm.set("characterizationId", compositionId);
		theForm.set("characterizationSource", aChar.getSource());
		theForm.set("viewTitle", aChar.getIdentificationName());
		theForm.set("description", aChar.getDescription());

		
		int fileNumber = 0;
		
		for (CharacterizationTable obj : aChar.getCharacterizationTableCollection()) {
			
			if (obj.getFile() != null) {
				CharacterizationFileBean fileBean = new CharacterizationFileBean();
				fileBean.setName(this.getName(obj.getFile()));
				fileBean.setPath(this.getPath(obj.getFile()));
				fileBean.setId(Integer.toString(fileNumber)); 
	
				request.getSession().setAttribute("characterizationFile" + fileNumber,
						fileBean);
			} else {
				request.getSession().removeAttribute("characterizationFile" + fileNumber);
			}
			fileNumber++;
		}
			
		
	
		SizeBean sChar = new SizeBean(aChar);
		
		theForm.set("achar", sChar);
		
		initSetup(request, theForm);

		return mapping.getInputForward();
	}

	/**
	 * Set up the input fields for read only view data
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward view(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return setupUpdate(mapping, form, request, response);
	}

	/**
	 * Update multiple children on the same form
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleType = (String) theForm.get("particleType");
		String particleName = (String) theForm.get("particleName");
		SizeBean achar = (SizeBean) theForm.get("achar");
		updateCharacterizationTables(achar);
		theForm.set("achar", achar);
		InitSessionSetup.getInstance().setSideParticleMenu(request,
				particleName, particleType);
		return mapping.getInputForward();
	}

	/**
	 * Set up information needed for loading a characterization file
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward loadFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String particleName=(String)theForm.get("particleName");
		String fileNumber=(String)theForm.get("fileNumber");	
		request.setAttribute("particleName", particleName);	
		request.setAttribute("fileNumber", fileNumber);		
		request.setAttribute("loadFileForward", "sizeInputForm");
		return mapping.findForward("loadFile");
	}
	
	public void updateCharacterizationTables(SizeBean achar) {
		String numberOfCharacterizationTables = achar.getNumberOfCharacterizationTables();
		int tableNum = Integer.parseInt(numberOfCharacterizationTables);
		List<CharacterizationTableBean> origTables = achar.getCharacterizationTables();
		int origNum = (origTables == null) ? 0 : origTables
				.size();
		List<CharacterizationTableBean> tables = new ArrayList<CharacterizationTableBean>();
		// create new ones
		if (origNum == 0) {

			for (int i = 0; i < tableNum; i++) {
				CharacterizationTableBean table = new CharacterizationTableBean();
				tables.add(table);
			}
		}
		// use keep original table info
		else if (tableNum <= origNum) {
			for (int i = 0; i < tableNum; i++) {
				tables.add((CharacterizationTableBean) origTables.get(i));
			}
		} else {
			for (int i = 0; i < origNum; i++) {
				tables.add((CharacterizationTableBean) origTables.get(i));
			}
			for (int i = origNum; i < tableNum; i++) {
				tables.add(new CharacterizationTableBean());
			}
		}
		achar.setCharacterizationTables(tables);
	}

	public boolean loginRequired() {
		return true;
	}
	
	/**
	 * Download action to handle download characterization file
	 * @param 
	 * @return
	 */
	public ActionForward download (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String fileId=request.getParameter("fileId");

		/*
		String filename = null;
		SizeBean aChar=(SizeBean) theForm.get("achar");
		for (CharacterizationTableBean obj : aChar.getCharacterizationTables()) {
			if (obj.getId().toString().equals(fileId)) {
				filename = obj.getFile().getName();
			}
		}
*/
		
		CharacterizationFileBean fileBean = (CharacterizationFileBean) request.getSession().getAttribute("characterizationFile" + fileId);
		String filename = fileBean.getPath() + fileBean.getName();
		
		logger.info("*************filename=" + filename);
		
		File dFile = new File(filename);
		if (dFile.exists()) {
			response.setContentType("application/octet-stream");
			response.setHeader("Content-disposition", "attachment;filename=" + this.getName(filename));
			response.setHeader("Cache-Control", "no-cache");
		
			java.io.InputStream in = new FileInputStream (dFile);
			java.io.OutputStream out = response.getOutputStream();

			byte[] bytes = new byte[32768];
	
			int numRead = 0;
			while ((numRead = in.read(bytes)) > 0) {
				out.write(bytes, 0, numRead);
			}
			out.close();
		} else {
			throw new Exception ("ERROR: file not found.");
		}
			
		
		return null;
	}
	
	/**
	 * Retrieve the file name from the full path
	 * @param fullPath
	 * @return
	 */
	private String getName(String fullPath) {
		String rv = null;
		
        String separator = fullPath.indexOf('/') < 0 ? "\\" : "/"; 
		
		int idx = fullPath.lastIndexOf(separator);
		
		if (idx >= 0)
			rv = fullPath.substring(idx+1); 
		else
			rv = fullPath;
				
		return rv;
	}

	/**
	 * Retrieve the path from the full path
	 * @param fullPath
	 * @return
	 */
	private String getPath(String fullPath) {
		String rv = null;
		
        String separator = fullPath.indexOf('/') < 0 ? "\\" : "/"; 
		
		int idx = fullPath.lastIndexOf(separator);
		
		if (idx >= 0)
			rv = fullPath.substring(0, idx+1);
		else
			rv = fullPath;
				
		return rv;
	}
	
}
