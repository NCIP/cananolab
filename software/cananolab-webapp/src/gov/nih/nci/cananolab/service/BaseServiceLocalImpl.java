package gov.nih.nci.cananolab.service;

import gov.nih.nci.cananolab.domain.common.Condition;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.common.ExperimentConfig;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.function.Target;
import gov.nih.nci.cananolab.domain.function.TargetingFunction;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.exception.CharacterizationException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.dao.GroupSearchCriteria;
import gov.nih.nci.security.dao.SearchCriteria;
import gov.nih.nci.security.dao.UserSearchCriteria;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class BaseServiceLocalImpl implements BaseService {
	protected SecurityService securityService;
	protected Logger logger = Logger.getLogger(BaseServiceLocalImpl.class);
	protected AccessibilityUtils accessUtils;
	protected UserBean user;

	public BaseServiceLocalImpl() {
		try {
			securityService = new SecurityService(Constants.CSM_APP_NAME);
			accessUtils = new AccessibilityUtils();
		} catch (Exception e) {
			logger.error("Can't create authorization service: " + e);
		}
	}

	public BaseServiceLocalImpl(UserBean user) {
		try {
			securityService = new SecurityService(Constants.CSM_APP_NAME, user);
			this.user = securityService.getUserBean();
			accessUtils = new AccessibilityUtils();
		} catch (Exception e) {
			logger.error("Can't create authorization service: " + e);
		}
	}

	public BaseServiceLocalImpl(SecurityService securityService) {
		try {
			if (securityService != null) {
				this.securityService = securityService;
				this.user = securityService.getUserBean();
			} else {
				this.securityService = new SecurityService(
						Constants.CSM_APP_NAME);
			}
			accessUtils = new AccessibilityUtils();
		} catch (Exception e) {
			logger.error("Can't create authorization service: " + e);
		}
	}

	public List<AccessibilityBean> findGroupAccessibilities(String protectedData)
			throws SecurityException, NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		List<AccessibilityBean> groupAccesses = new ArrayList<AccessibilityBean>();
		try {
			if (!securityService.checkReadPermission(protectedData)) {
				throw new NoAccessException();
			}
			Map<String, String> groupRoles = securityService
					.getAllGroupRoles(protectedData);
			for (Map.Entry<String, String> entry : groupRoles.entrySet()) {
				String groupName = entry.getKey();
				Group group = securityService.getGroup(groupName);
				// include Public group, Curator group and groups that user has
				// access to
				if (group.getGroupName().equals(Constants.CSM_PUBLIC_GROUP)
						|| group.getGroupName().equals(
								Constants.CSM_DATA_CURATOR)
						|| securityService
								.checkReadPermission(Constants.CSM_COLLABORATION_GROUP_PREFIX
										+ group.getGroupId())) {
					String roleName = entry.getValue();
					AccessibilityBean access = new AccessibilityBean();
					access.setRoleName(roleName);
					access.setGroupName(groupName);
					access.setAccessBy(AccessibilityBean.ACCESS_BY_GROUP);
					groupAccesses.add(access);
				}
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error getting group access for protected data";
			throw new SecurityException(error, e);
		}
		return groupAccesses;
	}

	public List<AccessibilityBean> findUserAccessibilities(String protectedData)
			throws SecurityException, NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		List<AccessibilityBean> userAccesses = new ArrayList<AccessibilityBean>();
		try {
			if (!securityService.checkReadPermission(protectedData)) {
				throw new NoAccessException();
			}
			Map<String, String> userRoles = securityService
					.getAllUserRoles(protectedData);
			for (Map.Entry<String, String> entry : userRoles.entrySet()) {
				String userLoginName = entry.getKey();
				String roleName = entry.getValue();
				AccessibilityBean access = new AccessibilityBean();
				access.setRoleName(roleName);
				// don't need user ID nor password
				access.setUserBean(new UserBean(userLoginName, null));
				access.setAccessBy(AccessibilityBean.ACCESS_BY_USER);
				userAccesses.add(access);

			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error getting user access for protected data";
			throw new SecurityException(error, e);
		}
		return userAccesses;
	}

	public AccessibilityBean findAccessibilityByGroupName(String groupName,
			String protectedData) throws SecurityException, NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		AccessibilityBean access = null;
		try {
			if (!securityService.checkCreatePermission(protectedData)) {
				throw new NoAccessException();
			}
			Group group = securityService.getGroup(groupName);
			// include public group and groups that user has access to
			if (securityService
					.checkReadPermission(Constants.CSM_COLLABORATION_GROUP_PREFIX
							+ group.getGroupId())
					|| groupName.equals(Constants.CSM_PUBLIC_GROUP)) {
				String roleName = securityService.getGroupRole(protectedData,
						groupName);
				access = new AccessibilityBean();
				access.setRoleName(roleName);
				access.setGroupName(groupName);
				if (groupName.equals(Constants.CSM_PUBLIC_GROUP)) {
					access.setAccessBy(AccessibilityBean.ACCESS_BY_PUBLIC);
				} else {
					access.setAccessBy(AccessibilityBean.ACCESS_BY_GROUP);
				}
			} else {
				throw new NoAccessException();
			}
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error saving access for the protected data";
			throw new SecurityException(error, e);
		}
		return access;
	}

	public AccessibilityBean findAccessibilityByUserLoginName(
			String userLoginName, String protectedData)
			throws SecurityException, NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		AccessibilityBean access = null;
		try {
			if (!securityService.checkCreatePermission(protectedData)) {
				throw new NoAccessException();
			}
			String roleName = securityService.getUserRole(protectedData,
					userLoginName);
			access = new AccessibilityBean();
			access.setRoleName(roleName);
			// don't need user id nor password
			access.setUserBean(new UserBean(userLoginName, null));
			access.setAccessBy(AccessibilityBean.ACCESS_BY_USER);
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error saving access for the protected data";
			throw new SecurityException(error, e);
		}
		return access;
	}

	protected void saveAccessibility(AccessibilityBean access,
			String protectedData) throws SecurityException, NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		try {
			String roleName = access.getRoleName();
			if (access.getAccessBy().equals(AccessibilityBean.ACCESS_BY_GROUP)
					|| access.getAccessBy().equals(
							AccessibilityBean.ACCESS_BY_PUBLIC)) {
				String groupName = access.getGroupName();
				accessUtils.secureObject(protectedData, groupName, roleName);
			} else {
				UserBean user = access.getUserBean();
				accessUtils.secureObjectForUser(protectedData, user
						.getLoginName(), roleName);
			}
		} catch (Exception e) {
			String error = "Error saving access for the protected data";
			throw new SecurityException(error, e);
		}
	}

	protected void saveDefaultAccessibility(String protectedData)
			throws SecurityException, NoAccessException {
		for (AccessibilityBean access : this.getDefaultAccesses()) {
			this.saveAccessibility(access, protectedData);
		}
	}

	protected List<AccessibilityBean> getDefaultAccesses() {
		List<AccessibilityBean> defaultAccesses = new ArrayList<AccessibilityBean>();
		defaultAccesses.add(Constants.CSM_DEFAULT_ACCESS);
		if (!user.isCurator()) {
			AccessibilityBean userAccess = new AccessibilityBean();
			userAccess.setUserBean(user);
			userAccess.setRoleName(Constants.CSM_CURD_ROLE);
			userAccess.setAccessBy(AccessibilityBean.ACCESS_BY_USER);
			defaultAccesses.add(userAccess);
		}
		return defaultAccesses;
	}

	protected void deleteAccessibility(AccessibilityBean access,
			String protectedData) throws SecurityException, NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		try {
			String roleName = access.getRoleName();
			if (access.getAccessBy().equals(AccessibilityBean.ACCESS_BY_GROUP)
					|| access.getAccessBy().equals(
							AccessibilityBean.ACCESS_BY_PUBLIC)) {
				String groupName = access.getGroupName();
				accessUtils.removeSecureObject(protectedData, groupName,
						roleName);
			} else {
				UserBean user = access.getUserBean();
				accessUtils.removeSecureObjectForUser(protectedData, user
						.getLoginName(), roleName);
			}
		} catch (Exception e) {
			String error = "Error saving access for the protected data";
			throw new SecurityException(error, e);
		}
	}

	protected Boolean checkUserUpdatable(List<AccessibilityBean> userAccesses) {
		if (user == null) {
			return false;
		}
		if (user.isCurator()) {
			return true;
		}
		for (AccessibilityBean access : userAccesses) {
			if (access.getUserBean().getLoginName().equals(user.getLoginName())
					&& access.getRoleName().equals(Constants.CSM_CURD_ROLE)) {
				return true;
			}
		}
		return false;
	}

	public List<UserBean> findUserLoginNames(String loginNameSearchStr)
			throws SecurityException {
		List<UserBean> matchedUsers = new ArrayList<UserBean>();
		try {
			List<UserBean> allUsers = accessUtils.getAllUsers();
			for (UserBean user : allUsers) {
				if (StringUtils.isEmpty(loginNameSearchStr)
						|| user.getLoginName().toLowerCase().contains(
								loginNameSearchStr.toLowerCase())
						|| user.getFirstName().toLowerCase().contains(
								loginNameSearchStr.toLowerCase())
						|| user.getLastName().toLowerCase().contains(
								loginNameSearchStr.toLowerCase())) {
					matchedUsers.add(user);
				}
			}
		} catch (Exception e) {
			String error = "Error finding users based on login name search string";
			throw new SecurityException(error, e);
		}
		Collections.sort(matchedUsers, new Comparator<UserBean>() {
			public int compare(UserBean u1, UserBean u2) {
				return u1.compareTo(u2);
			}
		});

		return matchedUsers;
	}

	public List<String> findGroupNames(String groupNameSearchStr)
			throws SecurityException {
		List<String> matchedGroupNames = new ArrayList<String>();
		try {
			List<String> allGroupNames = accessUtils.getAllGroups();
			for (String groupName : allGroupNames) {
				// exclude Public and Curator
				if (groupName.equals(Constants.CSM_PUBLIC_GROUP)
						|| groupName.equals(Constants.CSM_DATA_CURATOR)) {
					continue;
				}
				if (StringUtils.isEmpty(groupNameSearchStr)
						|| groupName.toLowerCase().contains(
								groupNameSearchStr.toLowerCase())) {
					matchedGroupNames.add(groupName);
				}
			}
		} catch (Exception e) {
			String error = "Error finding groups based on group name search string";
			throw new SecurityException(error, e);
		}
		Collections.sort(matchedGroupNames);
		return matchedGroupNames;
	}

	public SecurityService getAuthService() {
		return securityService;
	}

	public Boolean isUserOwner(String createdBy) {
		// user is either a curator or the creator of the data
		if (user != null
				&& (user.getLoginName().equalsIgnoreCase(createdBy) || user
						.isCurator())) {
			return true;
		} else {
			return false;
		}
	}

	public void removeAllAccesses(String protectedData)
			throws SecurityException, NoAccessException {
		if (user == null) {
			throw new NoAccessException();
		}
		try {
			if (!securityService.checkDeletePermission(protectedData)) {
				throw new NoAccessException();
			}
			accessUtils.removeCSMEntries(protectedData);
		} catch (NoAccessException e) {
			throw e;
		} catch (Exception e) {
			String error = "Error in remove all access";
			throw new SecurityException(error, e);
		}
	}

	public List<AccessibilityBean> findSampleAccesses(String sampleId)
			throws Exception {
		List<AccessibilityBean> accesses = new ArrayList<AccessibilityBean>();
		accesses.addAll(this.findGroupAccessibilities(sampleId));
		accesses.addAll(this.findUserAccessibilities(sampleId));
		return accesses;
	}

	/**
	 * Utilities methods related to assign/remove access
	 *
	 * @author pansu
	 *
	 */
	protected class AccessibilityUtils {
		private AuthorizationManager authManager;

		private AccessibilityUtils() throws SecurityException {
			try {
				this.authManager = SecurityServiceProvider
						.getAuthorizationManager(Constants.CSM_APP_NAME);
			} catch (Exception e) {
				logger.error(e);
				throw new SecurityException(e);
			}
		}

		public void assignAccessibility(AccessibilityBean access,
				Characterization achar) throws Exception {
			String charId = achar.getId().toString();
			try {
				// characterization
				saveAccessibility(access, charId);
				if (achar.getFindingCollection() != null) {
					for (Finding finding : achar.getFindingCollection()) {
						if (finding != null) {
							this.assignAccessibility(access, finding);
						}
					}
				}
				// ExperimentConfiguration
				if (achar.getExperimentConfigCollection() != null) {
					for (ExperimentConfig config : achar
							.getExperimentConfigCollection()) {
						this.assignAccessibility(access, config);
					}
				}
			} catch (Exception e) {
				String err = "Error in assigning accessibility to the characterization "
						+ achar.getId();
				logger.error(err, e);
				throw new CharacterizationException(err, e);
			}
		}

		public void removeAccessibility(AccessibilityBean access,
				Characterization achar) throws Exception {
			// characterization
			if (achar != null) {
				deleteAccessibility(access, achar.getId().toString());

				for (Finding finding : achar.getFindingCollection()) {
					if (finding != null) {
						this.removeAccessibility(access, finding);
					}
				}
				// ExperimentConfiguration
				for (ExperimentConfig config : achar
						.getExperimentConfigCollection()) {
					this.removeAccessibility(access, config);
				}
			}
		}

		public void assignAccessibility(AccessibilityBean access,
				SampleComposition comp) throws Exception {
			saveAccessibility(access, comp.getId().toString());
			for (NanomaterialEntity entity : comp
					.getNanomaterialEntityCollection()) {
				this.assignAccessibility(access, entity);
			}
			for (FunctionalizingEntity entity : comp
					.getFunctionalizingEntityCollection()) {
				this.assignAccessibility(access, entity);
			}
			for (ChemicalAssociation assoc : comp
					.getChemicalAssociationCollection()) {
				this.assignAccessibility(access, assoc);
			}
			for (File file : comp.getFileCollection()) {
				saveAccessibility(access, file.getId().toString());
			}
		}

		public void removeAccessibility(AccessibilityBean access,
				SampleComposition comp) throws Exception {
			deleteAccessibility(access, comp.getId().toString());
			if (comp.getNanomaterialEntityCollection() != null)
				for (NanomaterialEntity entity : comp
						.getNanomaterialEntityCollection()) {
					this.removeAccessibility(access, entity);
				}
			if (comp.getFunctionalizingEntityCollection() != null)
				for (FunctionalizingEntity entity : comp
						.getFunctionalizingEntityCollection()) {
					this.removeAccessibility(access, entity);
				}
			if (comp.getChemicalAssociationCollection() != null)
				for (ChemicalAssociation assoc : comp
						.getChemicalAssociationCollection()) {
					this.removeAccessibility(access, assoc);
				}
			if (comp.getFileCollection() != null) {
				for (File file : comp.getFileCollection()) {
					deleteAccessibility(access, file.getId().toString());
				}
			}
		}

		public void assignAccessibility(AccessibilityBean access,
				NanomaterialEntity entity) throws Exception {
			if (entity != null) {
				saveAccessibility(access, entity.getId().toString());
				// nanomaterialEntityCollection.composingElementCollection,
				Collection<ComposingElement> composingElementCollection = entity
						.getComposingElementCollection();
				if (composingElementCollection != null) {
					for (ComposingElement composingElement : composingElementCollection) {
						if (composingElement != null) {
							saveAccessibility(access, composingElement.getId()
									.toString());
						}
						// composingElementCollection.inherentFucntionCollection
						Collection<Function> inherentFunctionCollection = composingElement
								.getInherentFunctionCollection();
						if (inherentFunctionCollection != null) {
							for (Function function : inherentFunctionCollection) {
								if (function != null) {
									saveAccessibility(access, function.getId()
											.toString());
								}
							}
						}
					}
				}
			}
		}

		public void removeAccessibility(AccessibilityBean access,
				NanomaterialEntity entity) throws Exception {
			deleteAccessibility(access, entity.getId().toString());

			// nanomaterialEntityCollection.composingElementCollection,
			Collection<ComposingElement> composingElementCollection = entity
					.getComposingElementCollection();
			if (composingElementCollection != null) {
				for (ComposingElement composingElement : composingElementCollection) {
					if (composingElement != null) {
						deleteAccessibility(access, composingElement.getId()
								.toString());
					}
					// composingElementCollection.inherentFucntionCollection
					Collection<Function> inherentFunctionCollection = composingElement
							.getInherentFunctionCollection();
					if (inherentFunctionCollection != null) {
						for (Function function : inherentFunctionCollection) {
							if (function != null) {
								deleteAccessibility(access, function.getId()
										.toString());
							}
						}
					}
				}
			}
			if (entity.getFileCollection() != null) {
				for (File file : entity.getFileCollection()) {
					deleteAccessibility(access, file.getId().toString());
				}
			}
		}

		public void assignAccessibility(AccessibilityBean access,
				FunctionalizingEntity functionalizingEntity) throws Exception {
			if (functionalizingEntity != null) {
				saveAccessibility(access, functionalizingEntity.getId()
						.toString());
				// activation method
				if (functionalizingEntity.getActivationMethod() != null) {
					saveAccessibility(access, functionalizingEntity
							.getActivationMethod().getId().toString());
				}
				// functionalizingEntityCollection.functionCollection
				Collection<Function> functionCollection = functionalizingEntity
						.getFunctionCollection();
				if (functionCollection != null) {
					for (Function function : functionCollection) {
						if (function != null) {
							saveAccessibility(access, function.getId()
									.toString());
							if (function instanceof TargetingFunction) {
								for (Target target : ((TargetingFunction) function)
										.getTargetCollection()) {
									saveAccessibility(access, target.getId()
											.toString());
								}
							}
						}
					}
				}
			}
		}

		public void removeAccessibility(AccessibilityBean access,
				FunctionalizingEntity functionalizingEntity) throws Exception {
			if (functionalizingEntity != null) {
				deleteAccessibility(access, functionalizingEntity.getId()
						.toString());
				if (functionalizingEntity.getActivationMethod() != null) {
					deleteAccessibility(access, functionalizingEntity
							.getActivationMethod().getId().toString());
				}
				// functionalizingEntityCollection.functionCollection
				Collection<Function> functionCollection = functionalizingEntity
						.getFunctionCollection();
				if (functionCollection != null) {
					for (Function function : functionCollection) {
						if (function != null) {
							deleteAccessibility(access, function.getId()
									.toString());
							if (function instanceof TargetingFunction) {
								for (Target target : ((TargetingFunction) function)
										.getTargetCollection()) {
									deleteAccessibility(access, target.getId()
											.toString());
								}
							}
						}
					}
				}
				if (functionalizingEntity.getFileCollection() != null) {
					for (File file : functionalizingEntity.getFileCollection()) {
						deleteAccessibility(access, file.getId().toString());
					}
				}
			}
		}

		public void assignAccessibility(AccessibilityBean access,
				ChemicalAssociation assoc) throws Exception {
			if (assoc != null) {
				// accessibility of the associated elements should already be
				// assigned when creating the entities
				saveAccessibility(access, assoc.getId().toString());
				for (File file : assoc.getFileCollection()) {
					saveAccessibility(access, file.getId().toString());
				}
			}
		}

		public void removeAccessibility(AccessibilityBean access,
				ChemicalAssociation chemicalAssociation) throws Exception {
			deleteAccessibility(access, chemicalAssociation.getId().toString());
			for (File file : chemicalAssociation.getFileCollection()) {
				deleteAccessibility(access, file.getId().toString());
			}
		}

		private void assignAccessibility(AccessibilityBean access,
				Finding finding) throws Exception {
			saveAccessibility(access, finding.getId().toString());
			// files
			if (finding.getFileCollection() != null) {
				for (File file : finding.getFileCollection()) {
					if (file != null && file.getId() != null) {
						saveAccessibility(access, file.getId().toString());
					}
				}
			}
			// datum, need to check for null for copy bean.
			if (finding.getDatumCollection() != null) {
				for (Datum datum : finding.getDatumCollection()) {
					if (datum != null && datum.getId() != null) {
						saveAccessibility(access, datum.getId().toString());
					}
					// condition
					if (datum.getConditionCollection() != null) {
						for (Condition condition : datum
								.getConditionCollection()) {
							saveAccessibility(access, condition.getId()
									.toString());
						}
					}
				}
			}
		}

		private void removeAccessibility(AccessibilityBean access,
				Finding finding) throws Exception {
			deleteAccessibility(access, finding.getId().toString());

			// datum
			if (finding.getDatumCollection() != null) {
				for (Datum datum : finding.getDatumCollection()) {
					if (datum != null) {
						deleteAccessibility(access, datum.getId().toString());
					}
					if (datum.getConditionCollection() != null) {
						for (Condition condition : datum
								.getConditionCollection()) {
							deleteAccessibility(access, condition.getId()
									.toString());
						}
					}
				}
			}
			// file
			if (finding.getFileCollection() != null) {
				for (File file : finding.getFileCollection()) {
					deleteAccessibility(access, file.getId().toString());
				}
			}
		}

		private void assignAccessibility(AccessibilityBean access,
				ExperimentConfig config) throws Exception {
			saveAccessibility(access, config.getId().toString());
			// assign instruments and technique to public visibility
			if (config.getTechnique() != null) {
				saveAccessibility(Constants.CSM_PUBLIC_ACCESS, config
						.getTechnique().getId().toString());
			}
			if (config.getInstrumentCollection() != null) {
				for (Instrument instrument : config.getInstrumentCollection()) {
					saveAccessibility(Constants.CSM_PUBLIC_ACCESS, instrument
							.getId().toString());
				}
			}
		}

		private void removeAccessibility(AccessibilityBean access,
				ExperimentConfig config) throws Exception {
			deleteAccessibility(access, config.getId().toString());
			deleteAccessibility(access, config.getTechnique().getId()
					.toString());

			if (config.getInstrumentCollection() != null) {
				for (Instrument instrument : config.getInstrumentCollection()) {
					deleteAccessibility(access, instrument.getId().toString());
				}
			}
		}

		private void removeCSMEntries(String objectName)
				throws SecurityException {
			try {
				ProtectionElement pe = securityService
						.getProtectionElement(objectName);
				ProtectionGroup pg = securityService
						.getProtectionGroup(objectName);
				authManager.removeProtectionElementsFromProtectionGroup(pg
						.getProtectionGroupId().toString(), new String[] { pe
						.getProtectionElementId().toString() });
				authManager.removeProtectionElement(pe.getProtectionElementId()
						.toString());
				authManager.removeProtectionGroup(pg.getProtectionGroupId()
						.toString());
			} catch (Exception e) {
				logger
						.error(
								"Error in removing existing protection element and protection group",
								e);
				throw new SecurityException();
			}
		}

		/**
		 * Remove the given objectName from the given groupName with the given
		 * roleName.
		 *
		 * @param objectName
		 * @param groupName
		 * @param roleName
		 * @throws SecurityException
		 */
		public void removeSecureObject(String objectName, String groupName,
				String roleName) throws SecurityException {
			try {
				// trim spaces in objectName
				objectName = objectName.trim();

				// create protection group
				ProtectionGroup pg = securityService
						.getProtectionGroup(objectName);

				// get group and role
				Group group = securityService.getGroup(groupName);
				Role role = securityService.getRole(roleName);

				// this will remove exising roles assigned. In caNanoLab, this
				// is
				// not an
				// issue since
				// only the R role has been assigned from the application.

				if (group == null) {
					throw new SecurityException("No such group exists.");
				}
				if (role == null) {
					throw new SecurityException("No such role exists.");
				}
				authManager.removeGroupRoleFromProtectionGroup(pg
						.getProtectionGroupId().toString(), group.getGroupId()
						.toString(), new String[] { role.getId().toString() });
			} catch (Exception e) {
				logger.error("Error in removing secured objects from group", e);
				throw new SecurityException();
			}
		}

		/**
		 * Assign the given objectName to the given userName with the given
		 * roleName. Add to existing roles the object has for the user.
		 *
		 * @param objectName
		 * @param userLoginName
		 * @param roleName
		 * @throws SecurityException
		 */
		private void secureObjectForUser(String objectName,
				String userLoginName, String roleName) throws SecurityException {
			try {
				// trim spaces in objectName
				objectName = objectName.trim();
				// create protection element
				ProtectionElement pe = securityService
						.getProtectionElement(objectName);

				// create protection group
				ProtectionGroup pg = securityService
						.getProtectionGroup(objectName);

				// assign protection element to protection group if not already
				// exists
				assignProtectionElementToProtectionGroup(pe, pg);

				// get group and role
				User user = authManager.getUser(userLoginName);
				if (user == null) {
					throw new SecurityException("User doesn't exist");
				}
				Role role = securityService.getRole(roleName);
				if (role == null) {
					throw new SecurityException("Role doesn't exist");
				}
				authManager.assignUserRoleToProtectionGroup(user.getUserId()
						.toString(), new String[] { role.getId().toString() },
						pg.getProtectionGroupId().toString());
			} catch (Exception e) {
				logger.error("Error in securing objects for user", e);
				throw new SecurityException();
			}
		}

		/**
		 * Assign the given objectName to the given groupName with the given
		 * roleName. Add to existing roles the object has for the group.
		 *
		 * @param objectName
		 * @param groupName
		 * @param roleName
		 * @throws SecurityException
		 */
		private void secureObject(String objectName, String groupName,
				String roleName) throws SecurityException {
			try {
				// trim spaces in objectName
				objectName = objectName.trim();
				// create protection element
				ProtectionElement pe = securityService
						.getProtectionElement(objectName);

				// create protection group
				ProtectionGroup pg = securityService
						.getProtectionGroup(objectName);

				// assign protection element to protection group if not already
				// exists
				assignProtectionElementToProtectionGroup(pe, pg);
				// get group and role
				Group group = securityService.getGroup(groupName);
				Role role = securityService.getRole(roleName);

				if (group == null) {
					throw new SecurityException("No such group exists.");
				}
				if (role == null) {
					throw new SecurityException("No such role exists.");
				}
				// this will remove existing roles assigned
				authManager.assignGroupRoleToProtectionGroup(pg
						.getProtectionGroupId().toString(), group.getGroupId()
						.toString(), new String[] { role.getId().toString() });
			} catch (Exception e) {
				logger.error("Error in securing objects", e);
				throw new SecurityException();
			}
		}

		/**
		 * Assign a ProtectionElement to a ProtectionGroup if not already
		 * assigned.
		 *
		 * @param pe
		 * @param pg
		 * @throws SecurityException
		 */
		private void assignProtectionElementToProtectionGroup(
				ProtectionElement pe, ProtectionGroup pg)
				throws SecurityException {
			try {
				Set<ProtectionGroup> assignedPGs = new HashSet<ProtectionGroup>(
						authManager.getProtectionGroups(pe
								.getProtectionElementId().toString()));
				// check to see if the assignment is already made to ignore CSM
				// exception.

				// contains doesn't work because CSM didn't implement hashCode
				// in
				// ProtectionGroup.
				if (assignedPGs.contains(pg)) {
					return;
				}
				authManager.assignProtectionElement(
						pg.getProtectionGroupName(), pe.getObjectId());
			} catch (Exception e) {
				logger
						.error(
								"Error in assigning protection element to protection group",
								e);
				throw new SecurityException();
			}
		}

		/**
		 * remove the given objectName from the given userName with the given
		 * roleName.
		 *
		 * @param objectName
		 * @param groupName
		 * @param roleName
		 * @throws SecurityException
		 */
		public void removeSecureObjectForUser(String objectName,
				String userLoginName, String roleName) throws SecurityException {
			try {
				// trim spaces in objectName
				objectName = objectName.trim();
				// create protection group
				ProtectionGroup pg = securityService
						.getProtectionGroup(objectName);
				// get group and role
				User user = authManager.getUser(userLoginName);
				if (user == null) {
					throw new SecurityException("User doesn't exist");
				}
				Role role = securityService.getRole(roleName);
				if (role == null) {
					throw new SecurityException("Role doesn't exist");
				}
				authManager.removeUserRoleFromProtectionGroup(pg
						.getProtectionGroupId().toString(), user.getUserId()
						.toString(), new String[] { role.getId().toString() });
			} catch (Exception e) {
				logger.error("Error in removing secured objects for user", e);
				throw new SecurityException();
			}
		}

		/**
		 * Get all users in the application
		 *
		 * @return
		 * @throws SecurityException
		 */
		public List<UserBean> getAllUsers() throws SecurityException {
			try {
				List<UserBean> users = new ArrayList<UserBean>();
				User dummy = new User();
				dummy.setLoginName("*");
				SearchCriteria sc = new UserSearchCriteria(dummy);
				List results = authManager.getObjects(sc);
				for (Object obj : results) {
					User doUser = (User) obj;
					users.add(new UserBean(doUser));
				}
				return users;
			} catch (Exception e) {
				logger.error("Error in getting all users.", e);
				throw new SecurityException();
			}
		}

		/**
		 * Get all userBean groups in the application
		 *
		 * @return
		 * @throws SecurityException
		 */
		public List<String> getAllGroups() throws SecurityException {
			try {
				List<String> groups = new ArrayList<String>();
				Group dummy = new Group();
				dummy.setGroupName("*");
				SearchCriteria sc = new GroupSearchCriteria(dummy);
				List results = authManager.getObjects(sc);
				for (Object obj : results) {
					Group doGroup = (Group) obj;
					groups.add(doGroup.getGroupName());
				}
				return groups;
			} catch (Exception e) {
				logger.error("Error in getting all groups.", e);
				throw new SecurityException();
			}
		}

		public void assignOwner(String protectedData, String userLoginName)
				throws SecurityException {
			try {
				ProtectionElement pe = securityService
						.getProtectionElement(protectedData);
				User user = authManager.getUser(userLoginName);
				authManager.assignOwners(
						pe.getProtectionElementId().toString(),
						new String[] { user.getUserId().toString() });
			} catch (Exception e) {
				logger.error("Error in assigning an owner", e);
				throw new SecurityException();
			}
		}
	}
}