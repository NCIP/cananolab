package gov.nih.nci.cananolab.ui.protocol;

import gov.nih.nci.cananolab.domain.common.ProtocolFile;
import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.domain.particle.characterization.invitro.InvitroCharacterization;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.PhysicalCharacterization;
import gov.nih.nci.cananolab.dto.common.ProtocolFileBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.impl.ProtocolServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.directwebremoting.impl.DefaultWebContextBuilder;

/**
 * This class sets up session level or servlet context level variables to be
 * used in various actions during the setup of query forms.
 * 
 * @author pansu
 * 
 */
public class InitProtocolSetup {
	Logger logger = Logger.getLogger(InitProtocolSetup.class);
	private InitProtocolSetup() {
	}

	public static InitProtocolSetup getInstance() {
		return new InitProtocolSetup();
	}

	public void setProtocolDropdowns(HttpServletRequest request)
			throws Exception {
		InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
				"protocolTypes", "Protocol", "type", "otherType", true);
		InitSecuritySetup.getInstance().getAllVisibilityGroups(request);
	}

	public List<ProtocolFileBean> getProtocolFilesByChar(
			HttpServletRequest request, CharacterizationBean charBean)
			throws Exception {
		String protocolType = null;
		//create a new instance of type className
		Class clazz = ClassUtils.getFullClass(charBean.getClassName());
		if (clazz!=null) {
			Characterization achar = (Characterization) clazz.newInstance();
			if (achar instanceof PhysicalCharacterization) {
				protocolType = CaNanoLabConstants.PHYSICAL_ASSAY_PROTOCOL;
			} else if (achar instanceof InvitroCharacterization) {
				protocolType = CaNanoLabConstants.INVITRO_ASSAY_PROTOCOL;
			} else {
				protocolType = null; // update if in vivo is implemented
			}
		}
		ProtocolService service = new ProtocolServiceLocalImpl();
		List<ProtocolFileBean> protocolFiles = service.findProtocolFilesBy(
				protocolType, null, null);
		request.getSession().setAttribute("characterizationProtocolFiles",
				protocolFiles);
		return protocolFiles;
	}

	public void persistProtocolDropdowns(HttpServletRequest request,
			ProtocolFileBean protocolFile) throws Exception {
		InitSetup.getInstance().persistLookup(
				request,
				"Protocol",
				"type",
				"otherType",
				((ProtocolFile) protocolFile.getDomainFile()).getProtocol()
						.getType());
		setProtocolDropdowns(request);
	}
	
	//for ajax
	public String[] getProtocolTypes(String searchLocations) {
		DefaultWebContextBuilder dwcb = new DefaultWebContextBuilder();
		org.directwebremoting.WebContext webContext = dwcb.get();
		HttpServletRequest request = webContext.getHttpServletRequest();
		try {
			boolean isLocal = false;
			if ("local".equals(searchLocations)){
				isLocal = true;
			}
			SortedSet<String> types = null;
		    if (isLocal){
		    	types = InitSetup.getInstance().getDefaultAndOtherLookupTypes(request,
						"protocolTypes", "Protocol", "type", "otherType", true);
			}else{
				types = LookupService.findLookupValues("Protocol", "type");			
			}
		    types.add("");
		    String[] eleArray = new String[types.size()];
			return types.toArray(eleArray);
		} catch (Exception e) {
			logger.error("Problem setting protocol types: \n", e);
			e.printStackTrace();
		}	
		return new String[] { "" };
	}
}
