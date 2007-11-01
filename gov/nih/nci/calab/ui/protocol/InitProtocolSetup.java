package gov.nih.nci.calab.ui.protocol;

import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
import gov.nih.nci.calab.dto.characterization.CharacterizationTypeBean;
import gov.nih.nci.calab.dto.common.InstrumentBean;
import gov.nih.nci.calab.dto.common.LabFileBean;
import gov.nih.nci.calab.dto.common.ProtocolBean;
import gov.nih.nci.calab.dto.common.ProtocolFileBean;
import gov.nih.nci.calab.dto.common.UserBean;
import gov.nih.nci.calab.dto.function.FunctionBean;
import gov.nih.nci.calab.dto.remote.GridNodeBean;
import gov.nih.nci.calab.dto.sample.AliquotBean;
import gov.nih.nci.calab.dto.sample.ContainerBean;
import gov.nih.nci.calab.dto.sample.ContainerInfoBean;
import gov.nih.nci.calab.dto.sample.SampleBean;
import gov.nih.nci.calab.service.common.LookupService;
import gov.nih.nci.calab.service.particle.SearchNanoparticleService;
import gov.nih.nci.calab.service.particle.SubmitNanoparticleService;
import gov.nih.nci.calab.service.remote.GridSearchService;
import gov.nih.nci.calab.service.report.SearchReportService;
import gov.nih.nci.calab.service.security.UserService;
import gov.nih.nci.calab.service.util.CaNanoLabComparators;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.security.exceptions.CSException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.util.LabelValueBean;

/**
 * This class sets up session level or servlet context level variables to be
 * used in various actions during the setup of query forms.
 * 
 * @author pansu
 * 
 */
public class InitProtocolSetup {
	private static LookupService lookupService;

	private InitProtocolSetup() throws Exception {
		lookupService = new LookupService();
	}

	public static InitProtocolSetup getInstance() throws Exception {
		return new InitProtocolSetup();
	}

	public void setProtocolType(HttpSession session) throws Exception {
		if (session.getAttribute("protocolTypes") == null
				|| session.getAttribute("newProtocolCreated") != null) {
			SortedSet<String> protocolTypes = lookupService
					.getAllLookupTypes("ProtocolType");
			session.setAttribute("protocolTypes", protocolTypes);
		}
		session.removeAttribute("newProtocolCreated");
	}

	public void setProtocolSubmitPage(HttpSession session, UserBean user)
			throws Exception {
		// set protocol types, and protocol names for all these types
		setProtocolType(session);
		SortedSet<String> protocolTypes = (SortedSet<String>) session
				.getAttribute("protocolTypes");
		SortedSet<ProtocolBean> pbs = lookupService.getAllProtocols(user);
		// Now generate two maps: one for type and nameList,
		// and one for type and protocolIdList (for the protocol name dropdown
		// box)
		Map<String, List<String>> typeNamesMap = new HashMap<String, List<String>>();
		Map<String, List<String>> typeIdsMap = new HashMap<String, List<String>>();
		Map<String, List<String>> nameVersionsMap = new HashMap<String, List<String>>();
		Map<String, List<String>> nameIdsMap = new HashMap<String, List<String>>();
		for (String type : protocolTypes) {
			for (ProtocolBean pb : pbs) {
				if (type.equals(pb.getType())) {
					List<String> nameList = typeNamesMap.get(type);
					List<String> idList = typeIdsMap.get(type);
					if (nameList == null) {
						nameList = new ArrayList<String>();
						nameList.add(pb.getName());
						typeNamesMap.put(type, nameList);
					} else {
						nameList.add(pb.getName());
					}
					if (idList == null) {
						idList = new ArrayList<String>();
						idList.add(pb.getId().toString());
						typeIdsMap.put(type, idList);
					} else {
						idList.add(pb.getId().toString());
					}
				}
			}
		}
		for (ProtocolBean pb : pbs) {
			String id = pb.getId();
			List<String> versionList = new ArrayList<String>();
			List<String> idList = new ArrayList<String>();
			List<ProtocolFileBean> fileBeanList = pb.getFileBeanList();
			Map<String, ProtocolFileBean> map = new HashMap<String, ProtocolFileBean>();

			for (ProtocolFileBean fb : fileBeanList) {
				versionList.add(fb.getVersion());
				map.put(fb.getVersion(), fb);
			}
			String[] vlist = versionList.toArray(new String[0]);
			Arrays.sort(vlist);
			versionList.clear();
			for (int i = 0; i < vlist.length; i++) {
				ProtocolFileBean fb = map.get(vlist[i]);
				versionList.add(fb.getVersion());
				idList.add(fb.getId());
			}

			nameVersionsMap.put(id, versionList);
			nameIdsMap.put(id, idList);
		}
		session.setAttribute("AllProtocolTypeNames", typeNamesMap);
		session.setAttribute("AllProtocolTypeIds", typeIdsMap);
		session.setAttribute("protocolNames", new ArrayList<String>());
		session.setAttribute("AllProtocolNameVersions", nameVersionsMap);
		session.setAttribute("AllProtocolNameFileIds", nameIdsMap);
		session.setAttribute("protocolVersions", new ArrayList<String>());
	}

	public void setAllProtocolNameVersionsByType(HttpSession session,
			String type) throws Exception {
		// set protocol name and its versions for a given protocol type.
		Map<ProtocolBean, List<ProtocolFileBean>> nameVersions = lookupService
				.getAllProtocolNameVersionByType(type);
		SortedSet<LabelValueBean> set = new TreeSet<LabelValueBean>();
		Set keySet = nameVersions.keySet();

		for (Iterator it = keySet.iterator(); it.hasNext();) {
			ProtocolBean pb = (ProtocolBean) it.next();
			List<ProtocolFileBean> fbList = nameVersions.get(pb);
			for (ProtocolFileBean fb : fbList) {
				set.add(new LabelValueBean(pb.getName() + " - "
						+ fb.getVersion(), fb.getId()));
			}
		}
		session.setAttribute("protocolNameVersionsByType", set);
	}
}
