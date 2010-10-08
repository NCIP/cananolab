package gov.nih.nci.cananolab.service.protocol.impl;

import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.ProtocolException;
import gov.nih.nci.cananolab.service.BaseServiceLocalImpl;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.helper.ProtocolServiceHelper;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.Comparators;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

/**
 * Local implementation of ProtocolService
 *
 * @author pansu
 *
 */
public class ProtocolServiceLocalImpl extends BaseServiceLocalImpl implements
		ProtocolService {
	private static Logger logger = Logger
			.getLogger(ProtocolServiceLocalImpl.class);
	private ProtocolServiceHelper helper;

	public ProtocolServiceLocalImpl() {
		super();
		helper = new ProtocolServiceHelper(this.securityService);
	}

	public ProtocolServiceLocalImpl(UserBean user) {
		super(user);
		helper = new ProtocolServiceHelper(this.securityService);
	}

	public ProtocolServiceLocalImpl(SecurityService securityService) {
		super(securityService);
		helper = new ProtocolServiceHelper(this.securityService);
	}

	public ProtocolBean findProtocolById(String protocolId)
			throws ProtocolException, NoAccessException {
		ProtocolBean protocolBean = null;
		try {
			Protocol protocol = helper.findProtocolById(protocolId);
			if (protocol != null) {
				protocolBean = loadProtocolBean(protocol);
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String err = "Problem finding the protocol by id: " + protocolId;
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
		return protocolBean;
	}

	private ProtocolBean loadProtocolBean(Protocol protocol) throws Exception {
		ProtocolBean protocolBean = new ProtocolBean(protocol);
		if (user != null) {
			List<AccessibilityBean> groupAccesses = super
					.findGroupAccessibilities(protocol.getId().toString());
			List<AccessibilityBean> userAccesses = super
					.findUserAccessibilities(protocol.getId().toString());

			protocolBean.setUserAccesses(userAccesses);
			protocolBean.setGroupAccesses(groupAccesses);
			protocolBean.setUserUpdatable(this.checkUserUpdatable(
					groupAccesses, userAccesses));
			protocolBean.setUserDeletable(this.checkUserDeletable(
					groupAccesses, userAccesses));
			protocolBean.setUserIsOwner(this.checkUserOwner(protocolBean
					.getDomain().getCreatedBy()));
		}
		return protocolBean;
	}

	/**
	 * Persist a new protocol file or update an existing protocol file
	 *
	 * @param protocolBean
	 * @throws Exception
	 */
	public void saveProtocol(ProtocolBean protocolBean)
			throws ProtocolException, NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		try {
			Boolean newProtocol = true;
			if (protocolBean.getDomain().getId() != null) {
				newProtocol = false;
				if (!securityService.checkCreatePermission(protocolBean
						.getDomain().getId().toString())) {
					throw new NoAccessException();
				}
			}
			if (protocolBean.getFileBean() != null) {
				fileUtils.prepareSaveFile(protocolBean.getFileBean()
						.getDomainFile());
			}
			Protocol dbProtocol = helper.findProtocolBy(protocolBean
					.getDomain().getType(), protocolBean.getDomain().getName(),
					protocolBean.getDomain().getVersion());
			if (dbProtocol != null) {
				if (dbProtocol.getId() != protocolBean.getDomain().getId()) {
					protocolBean.getDomain().setId(dbProtocol.getId());
				}
				protocolBean.getDomain()
						.setCreatedBy(dbProtocol.getCreatedBy());
				protocolBean.getDomain().setCreatedDate(
						dbProtocol.getCreatedDate());
			}
			// protocol type, name or version has been updated but protocol ID
			// was kept
			else if (protocolBean.getDomain().getId() != null) {
				protocolBean.getDomain().setId(null);
				protocolBean.getDomain().setCreatedBy(
						helper.getUser().getLoginName());
				protocolBean.getDomain().setCreatedDate(new Date());
			}
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			appService.saveOrUpdate(protocolBean.getDomain());

			// save to the file system fileData is not empty
			if (protocolBean.getFileBean() != null) {
				fileUtils.writeFile(protocolBean.getFileBean());
			}

			// save default accesses
			if (newProtocol) {
				super.saveDefaultAccessibilities(protocolBean.getDomain()
						.getId().toString());
				if (protocolBean.getFileBean() != null) {
					super.saveDefaultAccessibilities(protocolBean.getFileBean()
							.getDomainFile().getId().toString());
				}
			}
		} catch (Exception e) {
			String err = "Error in saving the protocol file.";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	public ProtocolBean findProtocolBy(String protocolType,
			String protocolName, String protocolVersion)
			throws ProtocolException, NoAccessException {
		try {
			Protocol protocol = helper.findProtocolBy(protocolType,
					protocolName, protocolVersion);
			if (protocol != null) {
				ProtocolBean protocolBean = loadProtocolBean(protocol);
				return protocolBean;
			} else {
				return null;
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String err = "Problem finding protocol by name and type.";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	public List<ProtocolBean> findProtocolsBy(String protocolType,
			String protocolName, String protocolAbbreviation, String fileTitle)
			throws ProtocolException {
		List<ProtocolBean> protocolBeans = new ArrayList<ProtocolBean>();
		try {
			List<Protocol> protocols = helper.findProtocolsBy(protocolType,
					protocolName, protocolAbbreviation, fileTitle);
			Collections.sort(protocols,
					new Comparators.ProtocolNameVersionComparator());
			for (Protocol protocol : protocols) {
				// don't need to load accessibility
				ProtocolBean protocolBean = new ProtocolBean(protocol);
				protocolBeans.add(protocolBean);
			}
			return protocolBeans;
		} catch (Exception e) {
			String err = "Problem finding protocols.";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	public int getNumberOfPublicProtocols() throws ProtocolException {
		try {
			int count = helper.getNumberOfPublicProtocols();
			return count;
		} catch (Exception e) {
			String err = "Error finding counts of public protocols.";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	public void deleteProtocol(Protocol protocol) throws ProtocolException,
			NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}

		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			// assume protocol is loaded with protocol file
			// find associated characterizations
			// List<Long> charIds = findCharacterizationIdsByProtocolId(protocol
			// .getId().toString());
			// CharacterizationServiceHelper charServiceHelper = new
			// CharacterizationServiceHelper();
			// for (Long id : charIds) {
			// Characterization achar = charServiceHelper
			// .findCharacterizationById(id.toString());
			// achar.setProtocol(null);
			// appService.saveOrUpdate(achar);
			// }
			List<Characterization> chars = this
					.findCharacterizationsByProtocolId(protocol.getId()
							.toString());
			for (Characterization achar : chars) {
				achar.setProtocol(null);
				appService.saveOrUpdate(achar);
			}
			appService.delete(protocol);
		} catch (Exception e) {
			String err = "Error in deleting the protocol.";
			logger.error(err, e);
			throw new ProtocolException(err, e);
		}
	}

	private List<Long> findCharacterizationIdsByProtocolId(String protocolId)
			throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria.forClass(
				Characterization.class).setProjection(
				Projections.distinct(Property.forName("id")));
		crit.createAlias("protocol", "protocol");
		crit.add(Property.forName("protocol.id").eq(new Long(protocolId)));
		List results = appService.query(crit);
		List<Long> ids = new ArrayList<Long>();
		for (Object obj : results) {
			Long charId = (Long) obj;
			ids.add(charId);
		}
		return ids;
	}

	private List<Characterization> findCharacterizationsByProtocolId(
			String protocolId) throws Exception {
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		DetachedCriteria crit = DetachedCriteria
				.forClass(Characterization.class);
		crit.createAlias("protocol", "protocol");
		crit.add(Property.forName("protocol.id").eq(new Long(protocolId)));
		List results = appService.query(crit);
		List<Characterization> chars = new ArrayList<Characterization>();
		for (Object obj : results) {
			Characterization achar = (Characterization) obj;
			chars.add(achar);
		}
		return chars;
	}

	public void assignAccessibility(AccessibilityBean access, Protocol protocol)
			throws ProtocolException, NoAccessException {
		if (!isUserOwner(protocol.getCreatedBy())) {
			throw new NoAccessException();
		}
		try {
			// get existing accessibilities
			List<AccessibilityBean> groupAccesses = this
					.findGroupAccessibilities(protocol.getId().toString());
			List<AccessibilityBean> userAccesses = this
					.findUserAccessibilities(protocol.getId().toString());
			// do nothing is access already exist
			if (groupAccesses.contains(access)) {
				return;
			} else if (userAccesses.contains(access)) {
				return;
			}

			// if access is Public, remove all other access except Public
			// Curator and owner
			if (access.getGroupName()
					.equals(AccessibilityBean.CSM_PUBLIC_GROUP)) {
				for (AccessibilityBean acc : groupAccesses) {
					// remove group accesses that are not public or curator
					if (!acc.getGroupName().equals(
							AccessibilityBean.CSM_PUBLIC_GROUP)
							&& !acc.getGroupName().equals(
									(AccessibilityBean.CSM_DATA_CURATOR))) {
						this.removeAccessibility(acc, protocol);
					}
				}
				for (AccessibilityBean acc : userAccesses) {
					// remove accesses that are not owner
					if (!acc.getUserBean().getLoginName().equals(
							protocol.getCreatedBy())) {
						this.removeAccessibility(acc, protocol);
					}
				}
			}
			// if protocol is already public, retract from public
			else {
				if (groupAccesses.contains(AccessibilityBean.CSM_PUBLIC_ACCESS)) {
					this.removeAccessibility(
							AccessibilityBean.CSM_PUBLIC_ACCESS, protocol);
				}
			}

			super.saveAccessibility(access, protocol.getId().toString());
			if (protocol.getFile() != null) {
				super.saveAccessibility(access, protocol.getFile().getId()
						.toString());
			}
		} catch (Exception e) {
			String error = "Error in assigning access to protocol";
			throw new ProtocolException(error, e);
		}
	}

	public void removeAccessibility(AccessibilityBean access, Protocol protocol)
			throws ProtocolException, NoAccessException {
		if (!isUserOwner(protocol.getCreatedBy())) {
			throw new NoAccessException();
		}
		try {
			if (protocol != null) {
				super.deleteAccessibility(access, protocol.getId().toString());
				if (protocol.getFile() != null) {
					super.deleteAccessibility(access, protocol.getFile()
							.getId().toString());
				}
			}
		} catch (Exception e) {
			String error = "Error in assigning access to protocol";
			throw new ProtocolException(error, e);
		}
	}

	public Map<String, String> findProtocolsByOwner(String currentOwner)
			throws Exception {
		Map<String, String> protocols = new HashMap<String, String>();

		Protocol p = new Protocol();

		DetachedCriteria crit = DetachedCriteria.forClass(Protocol.class)
				.setProjection(
						Projections.projectionList().add(
								Projections.property("id")).add(
								Projections.property("name")));
		crit.add(Restrictions.eq("createdBy", currentOwner));
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List results = appService.query(crit);
		for (Object obj : results) {
			Object[] row = (Object[]) obj;
			protocols.put(row[0].toString(), row[1].toString());
		}
		return protocols;
	}

	public ProtocolServiceHelper getHelper() {
		return helper;
	}

	public void transferOwner(Set<String> ids, String currentOwner,
			String newOwner) throws NoAccessException, Exception {
		if (!this.securityService.getUserBean().isAdmin()) {
			throw new NoAccessException();
		}
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		for (String protocolId : ids) {
			Protocol protocol = helper.findProtocolById(protocolId);
			protocol.setCreatedBy(newOwner);
			appService.saveOrUpdate(protocol);
			handleAccessibility(currentOwner, newOwner, protocol);
		}
	}
	
	private void handleAccessibility(String currentOwner, String newOwner, 
			Protocol protocol) throws Exception{
		String protocolId = protocol.getId().toString();
		List<AccessibilityBean> userAccesses = super.findUserAccessibilities(protocolId);
		List<AccessibilityBean> groupAccesses = super.findGroupAccessibilities(protocolId);
		
		List<AccessibilityBean> newUserAccesses = new ArrayList<AccessibilityBean>(userAccesses);
		// save the new user access
		//need to retrieve new user info
		//In another words, if user accesses return empty on the previous owner, we’d search for group accesses and 
		//copy the roles from the group accesses and generate new user accesses with the same roles for the new owner.
		List<UserBean> newUserBean = super.findUserBeans(newOwner);
		UserBean newUser=null;
		if(!newUserBean.isEmpty()){
			for(UserBean bean : newUserBean){
				if(newOwner.equals(bean.getLoginName())){
					newUser = bean;
					break;
				}
			}
		}
		if(newUser == null){
			throw new Exception("The new owner entered doesn't exist. " + newOwner);
		}else if(!newUser.isCurator()){
			if(newUserAccesses.isEmpty()){
				AccessibilityBean newOwnerBean = new AccessibilityBean();
				for(AccessibilityBean groupAccess : groupAccesses){
					//System.out.println("group: " + groupAccess.getGroupName() + "\trole: " + groupAccess.getRoleName());
					String role = groupAccess.getRoleName();
					newOwnerBean.setRoleName(role);
					newOwnerBean.setUserBean(newUser);					
				}
				this.assignAccessibility(newOwnerBean, protocol);
				//need to remove access for the previous owner if not a curator
			}else{
				for( AccessibilityBean newOwnerUser : newUserAccesses){
					UserBean user = newOwnerUser.getUserBean();
					//System.out.println("currentUser loginName: " + user.getLoginName());
					String loginName = user.getLoginName();
					if(currentOwner.endsWith(loginName)){
						newOwnerUser.setUserBean(newUser);
						System.out.println("currentowner match with user login name ");
						//super.saveAccessibility(newOwnerUser, sampleId);
						this.assignAccessibility(newOwnerUser, protocol);
					}
				}
				
			}
		}
		//need to remove access for the previous owner if not a curator
		List<UserBean> previousUserBean = super.findUserBeans(currentOwner);
		UserBean previousUser=null;
		if(!previousUserBean.isEmpty()){
			for(UserBean bean : previousUserBean){
				if(currentOwner.equals(bean.getLoginName())){
					previousUser = bean;
					break;
				}
			}
		}
		if(previousUser == null){
			throw new Exception("The current owner entered doesn't exist. " + currentOwner);
		}else if(!previousUser.isCurator()){
			if(userAccesses.isEmpty()){
				AccessibilityBean previousOwnerBean = new AccessibilityBean();
				for(AccessibilityBean groupAccess : groupAccesses){
					//System.out.println("group: " + groupAccess.getGroupName() + "\trole: " + groupAccess.getRoleName());
					UserBean user = groupAccess.getUserBean();
					String loginName = user.getLoginName();
					if(currentOwner.endsWith(loginName)){					
						String role = groupAccess.getRoleName();
						previousOwnerBean.setRoleName(role);
						previousOwnerBean.setUserBean(user);	
						
					}
				}
				this.removeAccessibility(previousOwnerBean, protocol);
			}else{
				AccessibilityBean previousOwnerBean = new AccessibilityBean();
				for( AccessibilityBean previousOwnerUser : userAccesses){
					UserBean user = previousOwnerUser.getUserBean();
					//System.out.println("currentUser loginName: " + user.getLoginName());
					String loginName = user.getLoginName();
					if(currentOwner.endsWith(loginName)){
						previousOwnerBean.setUserBean(user);
						System.out.println("currentowner match with user login name ");
					}
				}
				this.removeAccessibility(previousOwnerBean, protocol);
			}
		}
		
	}
}
