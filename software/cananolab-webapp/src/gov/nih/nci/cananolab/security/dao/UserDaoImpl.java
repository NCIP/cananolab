package gov.nih.nci.cananolab.security.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import gov.nih.nci.cananolab.security.CananoUserDetails;
import gov.nih.nci.cananolab.security.enums.CaNanoRoleEnum;
import gov.nih.nci.cananolab.util.StringUtils;

@Component("userDao")
public class UserDaoImpl extends JdbcDaoSupport implements UserDao 
{
	protected Logger logger = Logger.getLogger(UserDaoImpl.class);
	
	@Autowired
	private DataSource dataSource;
	
	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}
	
	private static final String FETCH_USER_SQL = "select u.username, u.first_name, u.last_name, u.password, u.organization, u.department, " +
												 "u.title, u.phone_number, u.email_id, u.enabled from users u where u.username = ?";
	
	private static final String FETCH_USER_ROLES_SQL = "SELECT a.authority rolename FROM authorities a where a.username = ?";
	
	
	private static final String FETCH_USER_GROUPS_SQL = "SELECT DISTINCT g.group_name FROM groups g LEFT JOIN group_members gm ON g.id = gm.group_id WHERE (g.created_by = ? OR gm.username = ?)";
	private static final String FETCH_USERS_LIKE_SQL = "SELECT u.username, u.first_name, u.last_name, u.password, u.organization, u.department, " +
												 	   "u.title, u.phone_number, u.email_id, u.enabled FROM users u " +
												 	   "WHERE UPPER(username) LIKE ? OR UPPER(first_name) LIKE ? OR UPPER(last_name) LIKE ?";
	
	private static final String FETCH_ALL_USERS_SQL = "SELECT u.username, u.first_name, u.last_name, u.password, u.organization, u.department, " +
												 	  "u.title, u.phone_number, u.email_id, u.enabled FROM users u";
	
	private static final String INSERT_USER_SQL = "insert into users(username, password, first_name, last_name, organization, department, title, phone_number, email_id, enabled) " +
												  "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String INSERT_USER_AUTHORITY_SQL = "INSERT INTO authorities(username, authority) values (?, ?)";
	
	private static final String RESET_PASSWORD_SQL = "UPDATE users SET password = ? WHERE username = ?";
	
	private static final String FETCH_PASSWORD_SQL = "SELECT password FROM users WHERE username = ?";
	
	private static final String UPDATE_USER_SQL = "UPDATE users SET first_name = ?, last_name = ?, organization = ?, department = ?, title = ?, phone_number = ?, email_id = ?, enabled = ? " +
												  "WHERE username = ?";
	
	private static final String DELETE_ROLES_SQL = "DELETE FROM authorities WHERE username = ? AND authority != '" + CaNanoRoleEnum.ROLE_ANONYMOUS.toString() + "'";

	@Override
	public CananoUserDetails getUserByName(String username)
	{
		logger.debug("Fetching user details for user login: " + username);
		
		CananoUserDetails user = null;
		
		if (!StringUtils.isEmpty(username))
		{
			Object[] params = new Object[] {username};
			List<CananoUserDetails> userList = (List<CananoUserDetails>) getJdbcTemplate().query(FETCH_USER_SQL, params, new UserMapper());
			if (userList != null && userList.size() == 1)
				user = userList.get(0);
		}
		return user;
	}
	
	@Override
	public List<CananoUserDetails> getUsers(String likeStr)
	{
		logger.debug("Fetching user details with username like: " + likeStr);
		
		List<CananoUserDetails> userList = new ArrayList<CananoUserDetails>();
		
		if (!StringUtils.isEmpty(likeStr))
		{
			String matchStr = "%" + likeStr.toUpperCase() + "%";
			Object[] params = new Object[] {matchStr, matchStr, matchStr};
			userList = getJdbcTemplate().query(FETCH_USERS_LIKE_SQL, params, new UserMapper());
		}
		else
			userList = getJdbcTemplate().query(FETCH_ALL_USERS_SQL, new UserMapper());
		
		return userList;
	}
	
	@Override
	public List<String> getUserGroups(String username)
	{
		logger.debug("Fetching all groups to which user belongs: " + username);
		
		List<String> userGroups = new ArrayList<String>();
		if (!StringUtils.isEmpty(username))
		{
			Object[] params = new Object[] {username, username};
			userGroups = (List<String>) getJdbcTemplate().queryForList(FETCH_USER_GROUPS_SQL, params, String.class);
		}
		return userGroups;
	}
	
	@Override
	public List<String> getUserRoles(String username)
	{
		logger.debug("Fetching all roles assigned to user: " + username);
		
		List<String> userRoles = new ArrayList<String>();
		if (!StringUtils.isEmpty(username))
		{
			Object[] params = new Object[] {username};
			userRoles = (List<String>) getJdbcTemplate().queryForList(FETCH_USER_ROLES_SQL, params, String.class);
		}
		return userRoles;
	}
	
	@Override
	public int insertUser(CananoUserDetails user)
	{
		logger.debug("Insert user : " + user);
		int enabled = (user.isEnabled()) ? 1 : 0;
		Object[] params = new Object[] {user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(),
										user.getOrganization(), user.getDepartment(), user.getTitle(), user.getPhoneNumber(),
										user.getEmailId(), Integer.valueOf(enabled)};

		int status = getJdbcTemplate().update(INSERT_USER_SQL, params);
		return status;
	}
	
	@Override
	public int insertUserAuthority(String userName, String authority)
	{
		logger.debug("Insert user authority: user = " + userName + ", authority = " + authority);
		Object[] params = new Object[] {userName, authority};

		int status = getJdbcTemplate().update(INSERT_USER_AUTHORITY_SQL, params);
		return status;
	}
	
	@Override
	public int resetPassword(String userName, String password)
	{
		logger.info("Reset password for user: " + userName);
		Object[] params = new Object[] {password, userName};
		
		int status = getJdbcTemplate().update(RESET_PASSWORD_SQL, params);
		return status;
	}
	
	@Override
	public String readPassword(String userName)
	{
		logger.info("Read password for user : " + userName);
		String currPassword = (String) getJdbcTemplate().queryForObject(FETCH_PASSWORD_SQL, new Object[] {userName}, String.class);
		
		return currPassword;
	}

	@Override
	public int updateUser(CananoUserDetails userDetails)
	{
		logger.info("Update user account for user: " + userDetails.getUsername());
		int enabled = (userDetails.isEnabled()) ? 1 : 0;
		
		Object[] params = new Object[] {userDetails.getFirstName(), userDetails.getLastName(), userDetails.getOrganization(), 
										userDetails.getDepartment(), userDetails.getTitle(), userDetails.getPhoneNumber(),
										userDetails.getEmailId(), Integer.valueOf(enabled), userDetails.getUsername()};
		
		int status = getJdbcTemplate().update(UPDATE_USER_SQL, params);
		return status;
	}
	
	@Override
	public int deleteUserAssignedRoles(String username)
	{
		logger.info("Delete all user assigned roles for :" + username);
	
		Object[] params = new Object[] {username};

		int status = getJdbcTemplate().update(DELETE_ROLES_SQL, params);
		return status;
	}
	
	private static final class UserMapper implements RowMapper
	{
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			CananoUserDetails user = new CananoUserDetails();
			user.setUsername(rs.getString("USERNAME"));
			user.setFirstName(rs.getString("FIRST_NAME"));
			user.setLastName(rs.getString("LAST_NAME"));
			user.setPassword(rs.getString("PASSWORD"));
			user.setOrganization(rs.getString("ORGANIZATION"));
			user.setDepartment(rs.getString("DEPARTMENT"));
			user.setTitle(rs.getString("TITLE"));
			user.setPhoneNumber(rs.getString("PHONE_NUMBER"));
			user.setEmailId(rs.getString("EMAIL_ID"));
			user.setEnabled(rs.getBoolean("ENABLED"));
			
			return user;
		}
	}

}
