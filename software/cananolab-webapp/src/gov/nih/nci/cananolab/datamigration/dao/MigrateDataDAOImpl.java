package gov.nih.nci.cananolab.datamigration.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import gov.nih.nci.cananolab.security.dao.GroupDaoImpl;
import gov.nih.nci.cananolab.security.enums.SecureClassesEnum;

@Component("migrateDataDAO")
public class MigrateDataDAOImpl extends JdbcDaoSupport implements MigrateDataDAO
{
	protected Logger logger = Logger.getLogger(GroupDaoImpl.class);
	
	@Autowired
	private DataSource dataSource;
	
	private static final String GET_CSM_USERS_SQL = "SELECT u.login_name FROM csm_user u order by u.user_id;";
	
	private static final String GET_CSM_CURATORS_SQL = "SELECT u.LOGIN_NAME FROM csm_user u, csm_user_group ug " +
													  "WHERE u.user_id = ug.user_id AND ug.group_id = 2";
	
	private static final String PUBLIC_SAMPLE_COUNT_SQL = "SELECT count(*) from csm_protection_group a, csm_role b, csm_user_group_role_pg c, csm_group d, sample s " +
														"where a.protection_group_id = c.protection_group_id and b.role_id = c.role_id " + 
														"and c.group_id = d.group_id and s.sample_pk_id = a.protection_group_name " +
														"and d.group_name = 'Public' and b.role_name = 'R'";
	
	private static final String PUBLIC_PROTOCOL_COUNT_SQL = "SELECT count(*) from csm_protection_group a, csm_role b, csm_user_group_role_pg c, csm_group d, protocol p " +
														"where a.protection_group_id = c.protection_group_id and b.role_id = c.role_id " + 
														"and c.group_id = d.group_id and p.protocol_pk_id = a.protection_group_name " +
														"and d.group_name = 'Public' and b.role_name = 'R'";
	
	private static final String PUBLIC_PUBLICATION_COUNT_SQL = "SELECT count(*) from csm_protection_group a, csm_role b, csm_user_group_role_pg c, csm_group d, protocol p " +
														"where a.protection_group_id = c.protection_group_id and b.role_id = c.role_id " + 
														"and c.group_id = d.group_id and p.protocol_pk_id = a.protection_group_name " +
														"and d.group_name = 'Public' and b.role_name = 'R'";

	private static final String PUBLIC_SAMPLE_SQL = "SELECT rs.ID, rs.username FROM (SELECT @rownum:=@rownum+1 'rank', a.protection_group_name ID, s.created_by username " + 
													"FROM (SELECT @rownum:=0) r, csm_protection_group a, csm_role b, csm_user_group_role_pg c, csm_group d, sample s " +
													"WHERE a.protection_group_id = c.protection_group_id and b.role_id = c.role_id " +  
													"AND c.group_id = d.group_id AND s.sample_pk_id = a.protection_group_name " +
													"AND d.group_name = 'Public' AND b.role_name = 'R' ORDER BY sample_pk_id asc) rs WHERE rank >= ? AND rank <= ?";

	private static final String PUBLIC_PROTOCOL_SQL = "SELECT rs.ID, rs.username FROM (SELECT @rownum:=@rownum+1 'rank', a.protection_group_name ID, p.created_by username " +
													"FROM (SELECT @rownum:=0) r, csm_protection_group a, csm_role b, csm_user_group_role_pg c, csm_group d, protocol p " +
													"WHERE a.protection_group_id = c.protection_group_id AND b.role_id = c.role_id " + 
													"AND c.group_id = d.group_id AND p.protocol_pk_id = a.protection_group_name " +
													"AND d.group_name = 'Public' AND b.role_name = 'R' ORDER BY protocol_pk_id asc) rs WHERE rank >= ? AND rank <= ?";

	private static final String PUBLIC_PUBLICATION_SQL = "SELECT rs.ID, rs.username FROM (SELECT @rownum:=@rownum+1 'rank', a.protection_group_name ID, f.created_by username " +
														"FROM (SELECT @rownum:=0) r, csm_protection_group a, csm_role b, csm_user_group_role_pg c, csm_group d, publication p, file f " +
														"where a.protection_group_id = c.protection_group_id and b.role_id = c.role_id " + 
														"and c.group_id = d.group_id and p.publication_pk_id = a.protection_group_name and p.publication_pk_id = f.file_pk_id " +
														"and d.group_name = 'Public' and b.role_name = 'R' ORDER BY publication_pk_id asc) rs WHERE rank >= ? AND rank <= ?";
	
	private static final String SAMPLE_COUNT_SQL = "SELECT count(*) FROM sample";
	
	private static final String PROTOCOL_COUNT_SQL = "SELECT count(*) FROM protocol";
	
	private static final String PUBLICATION_COUNT_SQL = "SELECT count(*) FROM publication";
	
	private static final String SAMPLE_SQL = "select rs.sample_pk_id ID, rs.created_by username " +
											 "FROM (SELECT @rownum:=@rownum+1 'rank', s.sample_pk_id, s.created_by FROM sample s, (SELECT @rownum:=0) r ORDER BY sample_pk_id asc) rs " +
											 "WHERE rank >= ? AND rank <= ?";
	
	private static final String PROTOCOL_SQL = "select rp.protocol_pk_id ID, rp.created_by username " +
											 "FROM (SELECT @rownum:=@rownum+1 'rank', p.protocol_pk_id, p.created_by FROM protocol p, (SELECT @rownum:=0) r ORDER BY protocol_pk_id asc) rp " +
											 "WHERE rank >= ? AND rank <= ?";
	
	private static final String PUBLICATION_SQL = "SELECT rp.publication_pk_id ID, rp.created_by username " +
												"FROM (SELECT @rownum:=@rownum+1 'rank', p.publication_pk_id, f.created_by " + 
												"FROM (SELECT @rownum:=0) r, publication p LEFT JOIN file f on p.publication_pk_id = f.file_pk_id ORDER BY publication_pk_id asc) rp " +
												"WHERE rank >= ? AND rank <= ?";
	
	private static final String FETCH_ACCESS_FOR_SAMPLE_SQL = "SELECT a.protection_group_name ID, d.login_name USERNAME " +
																 "FROM csm_protection_group a, csm_role b, csm_user_group_role_pg c, csm_user d, sample s " +
																 "WHERE a.protection_group_id = c.protection_group_id AND b.role_id = c.role_id AND c.user_id = d.user_id " +
																 "AND s.sample_pk_id = a.protection_group_name AND b.role_id = ? AND s.created_by != d.login_name" +
																 "AND c.group_id IS NULL";
	
	private static final String FETCH_ACCESS_FOR_PROTOCOL_SQL = "SELECT a.protection_group_name ID, d.login_name USERNAME " +
																 "FROM csm_protection_group a, csm_role b, csm_user_group_role_pg c, csm_user d, protocol p " +
																 "WHERE a.protection_group_id = c.protection_group_id AND b.role_id = c.role_id AND c.user_id = d.user_id " +
																 "AND p.protocol_pk_id = a.protection_group_name AND b.role_id = ? AND p.created_by != d.login_name" +
																 "AND c.group_id IS NULL";
	
	private static final String FETCH_ACCESS_FOR_PUBLICATION_SQL = "SELECT a.protection_group_name ID, d.login_name USERNAME " +
																 "FROM csm_protection_group a, csm_role b, csm_user_group_role_pg c, csm_user d, publication p, file f " +
																 "WHERE a.protection_group_id = c.protection_group_id AND b.role_id = c.role_id AND c.user_id = d.user_id " +
																 "AND p.publication_pk_id = a.protection_group_name AND b.role_id = ? AND f.created_by != d.login_name" +
																 "AND c.group_id IS NULL AND p.publication_pk_id = f.file_pk_id";
	
	
	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
		getJdbcTemplate().setFetchSize(200);
	}
	
	@Override
	public List<String> getUsersFromCSM()
	{
		List<String> userList = getJdbcTemplate().queryForList(GET_CSM_USERS_SQL, String.class);
		return userList;
	}

	@Override
	public List<String> getCuratorUsersFromCSM()
	{
		List<String> userList = getJdbcTemplate().queryForList(GET_CSM_CURATORS_SQL, String.class);
		return userList;
	}
	
	@Override
	public Long getDataSize(SecureClassesEnum dataType)
	{
		String sql = "";
		if (dataType == SecureClassesEnum.SAMPLE)
			sql = SAMPLE_COUNT_SQL;
		else if (dataType == SecureClassesEnum.PROTOCOL)
			sql = PROTOCOL_COUNT_SQL;
		else if (dataType == SecureClassesEnum.PUBLICATION)
			sql = PUBLICATION_COUNT_SQL;
		
		Long count = getJdbcTemplate().queryForObject(sql, Long.class);

		return count;
	}

	@Override
	public Long getPublicDataSize(SecureClassesEnum dataType)
	{
		String sql = "";
		if (dataType == SecureClassesEnum.SAMPLE)
			sql = PUBLIC_SAMPLE_COUNT_SQL;
		else if (dataType == SecureClassesEnum.PROTOCOL)
			sql = PUBLIC_PROTOCOL_COUNT_SQL;
		else if (dataType == SecureClassesEnum.PUBLICATION)
			sql = PUBLIC_PUBLICATION_COUNT_SQL;
		
		Long count = getJdbcTemplate().queryForObject(sql, Long.class);

		return count;
	}

	@Override
	public List<AbstractMap.SimpleEntry<Long, String>> getPublicDataPage(long rowMin, long rowMax, SecureClassesEnum dataType)
	{
		String sql = "";
		
		if (dataType == SecureClassesEnum.SAMPLE)
			sql = PUBLIC_SAMPLE_SQL;
		else if (dataType == SecureClassesEnum.PROTOCOL)
			sql = PUBLIC_PROTOCOL_SQL;
		else if (dataType == SecureClassesEnum.PUBLICATION)
			sql = PUBLIC_PUBLICATION_SQL;
		
		Object[] params = new Object[] {Long.valueOf(rowMin), Long.valueOf(rowMax)};
		
		List<AbstractMap.SimpleEntry<Long, String>> dataPage= getJdbcTemplate().query(sql, params, new CSMDataMapper());

		return dataPage;
	}
	
	@Override
	public List<AbstractMap.SimpleEntry<Long, String>> getDataPage(long rowMin, long rowMax, SecureClassesEnum dataType)
	{
		String sql = "";
		
		if (dataType == SecureClassesEnum.SAMPLE)
			sql = SAMPLE_SQL;
		else if (dataType == SecureClassesEnum.PROTOCOL)
			sql = PROTOCOL_SQL;
		else if (dataType == SecureClassesEnum.PUBLICATION)
			sql = PUBLICATION_SQL;
		
		Object[] params = new Object[] {Long.valueOf(rowMin), Long.valueOf(rowMax)};
		
		List<AbstractMap.SimpleEntry<Long, String>> dataPage= getJdbcTemplate().query(sql, params, new CSMDataMapper());

		return dataPage;
	}
	
	@Override
	public List<AbstractMap.SimpleEntry<Long, String>> getRWDAccessDataForUsers(SecureClassesEnum dataType)
	{
		String sql = "";
		
		if (dataType == SecureClassesEnum.SAMPLE)
			sql = FETCH_ACCESS_FOR_SAMPLE_SQL;
		else if (dataType == SecureClassesEnum.PROTOCOL)
			sql = FETCH_ACCESS_FOR_PROTOCOL_SQL;
		else if (dataType == SecureClassesEnum.PUBLICATION)
			sql = FETCH_ACCESS_FOR_PUBLICATION_SQL;
		
		Object[] params = new Object[] {Integer.valueOf(5)};
		
		List<AbstractMap.SimpleEntry<Long, String>> dataAccessForUsers= getJdbcTemplate().query(sql, params, new CSMDataMapper());

		return dataAccessForUsers;
	}
	
	@Override
	public List<AbstractMap.SimpleEntry<Long, String>> getReadAccessDataForUsers(SecureClassesEnum dataType)
	{
		String sql = "";
		
		if (dataType == SecureClassesEnum.SAMPLE)
			sql = FETCH_ACCESS_FOR_SAMPLE_SQL;
		else if (dataType == SecureClassesEnum.PROTOCOL)
			sql = FETCH_ACCESS_FOR_PROTOCOL_SQL;
		else if (dataType == SecureClassesEnum.PUBLICATION)
			sql = FETCH_ACCESS_FOR_PUBLICATION_SQL;
		
		Object[] params = new Object[] {Integer.valueOf(1)};
		
		List<AbstractMap.SimpleEntry<Long, String>> dataAccessForUsers= getJdbcTemplate().query(sql, params, new CSMDataMapper());

		return dataAccessForUsers;
	}
	
	private static final class CSMDataMapper implements RowMapper
	{
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Long id = rs.getLong("ID");
			String createdBy = rs.getString("USERNAME");
			AbstractMap.SimpleEntry<Long, String> entry = new AbstractMap.SimpleEntry<Long, String>(id, createdBy);
			
			return entry;
		}
	}

}
