package gov.nih.nci.cananolab.datamigration.service;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import gov.nih.nci.cananolab.security.CananoUserDetails;
import gov.nih.nci.cananolab.security.dao.UserDao;
import gov.nih.nci.cananolab.security.enums.CaNanoRoleEnum;
import gov.nih.nci.cananolab.security.enums.SecureClassesEnum;
import gov.nih.nci.cananolab.security.service.SpringSecurityAclService;
import gov.nih.nci.cananolab.datamigration.dao.MigrateDataDAO;

@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
@Component("migrateDataService")
public class MigrateDataServiceImpl implements MigrateDataService
{
	protected Logger logger = Logger.getLogger(MigrateDataServiceImpl.class);

	private int pageSize = 200;

	@Autowired
	private MigrateDataDAO migrateDataDAO;

	@Autowired
	private SpringSecurityAclService springSecurityAclService;

	@Autowired
	private UserDao userDao;

	@Override
	public void migrateUserAccountsFromCSMToSpring()
	{
		logger.info("Migrating all user accounts from CSM to Spring Security and granting Public role.");
		List<String> csmUserList = migrateDataDAO.getUsersFromCSM();
		for (String csmUser : csmUserList)
		{
			CananoUserDetails userDetails = new CananoUserDetails();
			userDetails.setUsername(csmUser);
			userDetails.setPassword(csmUser);
			userDetails.setFirstName(csmUser);
			userDetails.setLastName(csmUser);
			userDetails.setEnabled(true);
			int status = userDao.insertUser(userDetails);
			logger.info("User account created for " + csmUser + " status = " + status);

			status = userDao.insertUserAuthority(csmUser, CaNanoRoleEnum.ROLE_ANONYMOUS.toString());
			logger.info(csmUser + " granted Public role with status = " + status);
		}
	}

	@Override 
	public void grantCuratorRoleToAccounts()
	{
		logger.info("Migrate users' Curator role from CSM to Spring Security.");
		List<String> csmUserList = migrateDataDAO.getCuratorUsersFromCSM();
		for (String csmUser : csmUserList)
		{
			int status = userDao.insertUserAuthority(csmUser, CaNanoRoleEnum.ROLE_CURATOR.toString());
			logger.info(csmUser + " granted Curator role with status = " + status);
		}
	}

	@Override
	public void migrateDefaultAccessDataFromCSMToSpring(SecureClassesEnum dataType)
	{
		logger.info("Starting default access control data transter for all : " + dataType.toString());
		Long totalCount = migrateDataDAO.getDataSize(dataType);

		// calculate the number of pages
		double pageCount = totalCount / pageSize;
		int pageCnt = Double.valueOf(pageCount).intValue() + 1;
		logger.info("Total data size = " + totalCount + ", page count = " + pageCnt);
		
		List<Permission> rwdPerms = new ArrayList<Permission>();
		rwdPerms.add(BasePermission.READ);
		rwdPerms.add(BasePermission.WRITE);
		rwdPerms.add(BasePermission.DELETE);
		
		List<Permission> rPerms = new ArrayList<Permission>();
		rPerms.add(BasePermission.READ);
		
		for (int i = 0; i < pageCnt; i++)
		{
			long rowMin = (i * pageSize) + 1 ;
			long rowMax = (i == pageCnt - 1) ? totalCount : (i + 1) * pageSize;
			logger.info("Tranferring default access data for page = " + i + ", rowMin = " + rowMin + ", rowMax = " + rowMax);
			
			List<AbstractMap.SimpleEntry<Long, String>> dataList = migrateDataDAO.getDataPage(rowMin, rowMax, dataType);
			for (AbstractMap.SimpleEntry<Long, String> id : dataList)
			{
				springSecurityAclService.saveDefaultAccessForNewObjectWithOwner(id.getKey(), dataType.getClazz(), id.getValue(), rwdPerms, rPerms);
			}
		}

	}

	@Override
	public void migratePublicAccessDataFromCSMToSpring(SecureClassesEnum dataType)
	{
		logger.info("Starting public access control data transter for : " + dataType.toString());
		Long publicCount = migrateDataDAO.getPublicDataSize(dataType);

		logger.info("Public sample data size = " + publicCount);
		
		// calculate the number of pages
		double pageCount = publicCount / pageSize;
		int pageCnt = Double.valueOf(pageCount).intValue() + 1;
		logger.info("Public data size = " + publicCount + ", page count = " + pageCnt);

		for (int i = 0; i < pageCnt; i++)
		{
			long rowMin = (i * pageSize) + 1 ;
			long rowMax = (i == pageCnt - 1) ? publicCount : (i + 1) * pageSize;
			logger.info("Tranferring public access data for page = " + i + ", rowMin = " + rowMin + ", rowMax = " + rowMax);
			
			List<AbstractMap.SimpleEntry<Long, String>> publicIdDataList = migrateDataDAO.getPublicDataPage(rowMin, rowMax, dataType);
			for (AbstractMap.SimpleEntry<Long, String> publicId : publicIdDataList)
			{
				springSecurityAclService.savePublicAccessForObject(publicId.getKey(), dataType.getClazz());
			}

		}
	}
	
	@Override
	public void migrateRWDUserAccessFromCSMToSpring(SecureClassesEnum dataType)
	{
		logger.info("Starting transfer of RWD access data for all users to Spring. " + dataType.toString());
		List<AbstractMap.SimpleEntry<Long, String>> accessList = migrateDataDAO.getRWDAccessDataForUsers(dataType);
		if (accessList != null)
		{
			logger.info("Size of RWD access data for " + dataType + " = " + accessList.size());
			for (AbstractMap.SimpleEntry<Long, String> idUser : accessList)
			{
				springSecurityAclService.saveAccessForObject(idUser.getKey(), dataType.getClazz(), idUser.getValue(), true, "RWD");
			}
		}
	}
	
	@Override
	public void migrateReadUserAccessFromCSMToSpring(SecureClassesEnum dataType)
	{
		logger.info("Starting transfer of Read access data for all users to Spring. " + dataType.toString());
		List<AbstractMap.SimpleEntry<Long, String>> accessList = migrateDataDAO.getReadAccessDataForUsers(dataType);
		if (accessList != null)
		{
			logger.info("Size of Read access data for " + dataType + " = " + accessList.size());
			for (AbstractMap.SimpleEntry<Long, String> idUser : accessList)
			{
				springSecurityAclService.saveAccessForObject(idUser.getKey(), dataType.getClazz(), idUser.getValue(), true, "R");
			}
		}
	}

}