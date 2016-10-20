package gov.nih.nci.cananolab.datamigration.service;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import gov.nih.nci.cananolab.security.CananoUserDetails;
import gov.nih.nci.cananolab.security.dao.UserDao;
import gov.nih.nci.cananolab.security.enums.CaNanoRoleEnum;
import gov.nih.nci.cananolab.security.enums.SecureClassesEnum;
import gov.nih.nci.cananolab.security.service.SpringSecurityAclService;
import gov.nih.nci.cananolab.datamigration.dao.MigrateDataDAO;
import gov.nih.nci.cananolab.datamigration.util.AESEncryption;

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
	public void migrateUserAccountsFromCSMToSpring() throws Exception
	{
		logger.info("Migrating all user accounts from CSM to Spring Security and granting Public role.");
		AESEncryption aesEncryption = new AESEncryption();
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
		
		List<CananoUserDetails> csmUserList = migrateDataDAO.getUsersFromCSM();
		for (CananoUserDetails csmUser : csmUserList)
		{
			CananoUserDetails userDetails = new CananoUserDetails();
			userDetails.setUsername(csmUser.getUsername());
			String password = aesEncryption.decrypt(csmUser.getPassword());
			
			String encodedPwd = bcrypt.encode(password);
			
			userDetails.setPassword(encodedPwd);
			userDetails.setFirstName(aesEncryption.decrypt(csmUser.getFirstName()));
			userDetails.setLastName(aesEncryption.decrypt(csmUser.getLastName()));
			userDetails.setOrganization(aesEncryption.decrypt(csmUser.getOrganization()));
			userDetails.setDepartment(aesEncryption.decrypt(csmUser.getDepartment()));
			userDetails.setTitle(aesEncryption.decrypt(csmUser.getTitle()));
			userDetails.setEmailId(aesEncryption.decrypt(csmUser.getEmailId()));
			userDetails.setPhoneNumber(aesEncryption.decrypt(csmUser.getPhoneNumber()));
			userDetails.setEnabled(true);
			int status = userDao.insertUser(userDetails);
			logger.info("User account created for " + csmUser + " status = " + status);

			status = userDao.insertUserAuthority(csmUser.getUsername(), CaNanoRoleEnum.ROLE_ANONYMOUS.toString());
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
				String value = id.getValue();
				if (value.endsWith(":COPY"))
				{
					value = value.substring(0, value.indexOf(":COPY"));
				}
					
				springSecurityAclService.saveDefaultAccessForNewObjectWithOwner(id.getKey(), dataType.getClazz(), value, rwdPerms, rPerms);
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
	
	@Override
	public void migrateCharacterizationAccessData()
	{
		logger.info("Starting transfer of access data for characterizations.");
		
		Long count = migrateDataDAO.getCharDataSize();

		logger.info("Characterization data size = " + count);
		
		// calculate the number of pages
		double pageCount = count / pageSize;
		int pageCnt = Double.valueOf(pageCount).intValue() + 1;
		logger.info("Characterization data size = " + count + ", page count = " + pageCnt);

		for (int i = 0; i < pageCnt; i++)
		{
			long rowMin = (i * pageSize) + 1 ;
			long rowMax = (i == pageCnt - 1) ? count : (i + 1) * pageSize;
			logger.info("Tranferring characterization data for page = " + i + ", rowMin = " + rowMin + ", rowMax = " + rowMax);
			
			List<AbstractMap.SimpleEntry<Long, Long>> charList = migrateDataDAO.getAllCharacterizations(rowMin, rowMax);
			if (charList != null)
			{
				logger.info("Size of charaterizations = " + charList.size());
				for (AbstractMap.SimpleEntry<Long, Long> charSample : charList)
				{
					springSecurityAclService.saveAccessForChildObject(charSample.getValue(), SecureClassesEnum.SAMPLE.getClazz(), charSample.getKey(), SecureClassesEnum.CHAR.getClazz());
				}
			}

		}
	}
	
	@Override
	public void migrateOrganizationAccessData()
	{
		logger.info("Starting transfer of access data for Organizations and Point of Contacts.");

		List<Long> orgPkIdList = migrateDataDAO.getAllOrganizations();
		if (orgPkIdList != null)
		{
			logger.info("Size of organizations = " + orgPkIdList.size());
			for (Long orgPkId : orgPkIdList)
			{
				springSecurityAclService.savePublicAccessForObject(orgPkId, SecureClassesEnum.ORG.getClazz());
			}
		}
		
		List<AbstractMap.SimpleEntry<Long, Long>> orgPocIdList = migrateDataDAO.getPOCsForOrgs();
		if (orgPocIdList != null)
		{
			logger.info("Size of POCs for organizations = " + orgPocIdList.size());
			for (AbstractMap.SimpleEntry<Long, Long> orgPocId : orgPocIdList)
			{
				springSecurityAclService.saveAccessForChildObject(orgPocId.getValue(), SecureClassesEnum.ORG.getClazz(), orgPocId.getKey(), SecureClassesEnum.POC.getClazz());
			}
		}
		logger.info("Organizations and Point of COntact access data migrated");
		
	}
	
	@Override
	public void bcryptPasswords()
	{
		logger.info("Start encyption of passwords in user table with BCrypt.");
		List<AbstractMap.SimpleEntry<String, String>> userPwdList = migrateDataDAO.getUserPasswords();
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
		
		for (AbstractMap.SimpleEntry<String, String> userPwd : userPwdList)
		{
			String value = bcrypt.encode(userPwd.getValue());
			int status = migrateDataDAO.updateEncryptedPassword(userPwd.getKey(), value);
		}
		
		logger.info("Encyption of passwords in user table with BCrypt completed.");
		
	}

}
