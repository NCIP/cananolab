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
import org.springframework.stereotype.Component;

import gov.nih.nci.cananolab.security.CananoUserDetails;
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
	
	private static final String FETCH_USER_GROUPS_SQL = "SELECT g.group_name FROM groups g, group_members gm where g.id = gm.group_id and gm.username = ?";
	
	private static final String FETCH_USERS_LIKE_SQL = "SELECT u.username, u.first_name, u.last_name, u.password, u.organization, u.department, " +
												 	   "u.title, u.phone_number, u.email_id, u.enabled FROM users " +
												 	   "WHERE UPPER(username) LIKE ? OR UPPER(first_name) LIKE ? OR UPPER(last_name) LIKE ?";
	
	private static final String FETCH_ALL_USERS_SQL = "SELECT u.username, u.first_name, u.last_name, u.password, u.organization, u.department, " +
												 	  "u.title, u.phone_number, u.email_id, u.enabled FROM users";

	@Override
	public CananoUserDetails getUserByName(String username)
	{
		logger.debug("Fetching user details for user login: " + username);
		
		CananoUserDetails user = null;
		
		if (!StringUtils.isEmpty(username))
		{
			Object[] params = new Object[] {username};
			user = (CananoUserDetails) getJdbcTemplate().queryForObject(FETCH_USER_SQL, params, new UserMapper());
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
			userList = getJdbcTemplate().queryForList(FETCH_USERS_LIKE_SQL, params, CananoUserDetails.class);
		}
		else
			userList = getJdbcTemplate().queryForList(FETCH_ALL_USERS_SQL, CananoUserDetails.class);
		
		return userList;
	}
	
	@Override
	public List<String> getUserGroups(String username)
	{
		logger.debug("Fetching all groups to which user belongs: " + username);
		
		List<String> userGroups = new ArrayList<String>();
		if (!StringUtils.isEmpty(username))
		{
			Object[] params = new Object[] {username};
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
