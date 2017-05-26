package gov.nih.nci.cananolab.security.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Component;

import gov.nih.nci.cananolab.security.enums.SecureClassesEnum;
import gov.nih.nci.cananolab.util.StringUtils;

@Component("aclDao")
public class AclDaoImpl extends NamedParameterJdbcDaoSupport implements AclDao
{
	protected Logger logger = Logger.getLogger(AclDaoImpl.class);
	
	@Autowired
	private DataSource dataSource;
	
	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}
	
	private static final String IDS_OF_CLASS_FOR_SID_SQL = "select distinct aoi.object_id_identity " +
													  "from acl_class ac, acl_sid asid, acl_object_identity aoi, acl_entry ae " +
													  "where ac.class = :clazz and asid.sid = :sidval " +
													  "and ae.sid = asid.id and aoi.object_id_class = ac.id " +
													  "and ae.acl_object_identity = aoi.id";

	private static final String PUBLIC_CHARACTERIZATION_SQL = "select distinct c.characterization_pk_id " +
															  "from acl_class ac, acl_sid asid, acl_object_identity aoi, acl_entry ae, characterization c " +
															  "where ac.class = :clazz  and asid.sid = :sidval and ae.sid = asid.id " +
															  "and aoi.object_id_class = ac.id and ae.acl_object_identity = aoi.parent_object " +
															  "and c.characterization_pk_id = aoi.object_id_identity and c.discriminator in (:discriminators)";
	
	private static final String IDS_OF_SAMPLE_SHARED_WITH_SID_SQL = "select distinct aoi.object_id_identity " +
																"from acl_class ac, acl_sid asid, acl_object_identity aoi, acl_entry ae, sample s " +
																"where ac.class = :clazz and asid.sid in (:sids) " +
																"and s.sample_pk_id = aoi.object_id_identity and s.created_by != :loggedInUser " +
																"and ae.sid = asid.id and aoi.object_id_class = ac.id and ae.acl_object_identity = aoi.id";

	private static final String IDS_OF_PROTOCOL_SHARED_WITH_SID_SQL = "select distinct aoi.object_id_identity " +
																"from acl_class ac, acl_sid asid, acl_object_identity aoi, acl_entry ae, protocol p " +
																"where ac.class = :clazz and asid.sid in (:sids) " +
																"and p.protocol_pk_id = aoi.object_id_identity and p.created_by != :loggedInUser " +
																"and ae.sid = asid.id and aoi.object_id_class = ac.id and ae.acl_object_identity = aoi.id";
	
	private static final String IDS_OF_PUB_SHARED_WITH_SID_SQL = "select distinct aoi.object_id_identity " +
																"from acl_class ac, acl_sid asid, acl_object_identity aoi, acl_entry ae, publication p, file f " +
																"where ac.class = :clazz and asid.sid in (:sids) " +
																"and p.publication_pk_id = aoi.object_id_identity and p.publication_pk_id = f.file_pk_id and f.created_by != :loggedInUser " +
																"and ae.sid = asid.id and aoi.object_id_class = ac.id and ae.acl_object_identity = aoi.id";

	private static final String DEL_SID_ACCESS_SQL = "delete from acl_entry ae, acl_sid s where ae.sid = s.id and s.sid = :sid";
	
	@Override
	public List<Long> getIdsOfClassForSid(String clazz, String sid)
	{
		logger.debug("Fetching class: " + clazz + " Ids accessible to: " + sid);
		List<Long> idList = new ArrayList<Long>();
		
		if (!StringUtils.isEmpty(clazz) && !StringUtils.isEmpty(sid))
		{
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			parameters.addValue("clazz", clazz);
			parameters.addValue("sidval", sid);
			idList = getNamedParameterJdbcTemplate().queryForList(IDS_OF_CLASS_FOR_SID_SQL, parameters, Long.class);
		}
		
		return idList;
	}
	
	@Override
	public List<Long> getCountOfPublicCharacterization(String clazz, String sid, List<String> charNames)
	{
		logger.debug("Fetching list of public characterization IDs of class: " + charNames);
		List<Long> idList = new ArrayList<Long>();
		
		if (!StringUtils.isEmpty(clazz) && !StringUtils.isEmpty(sid))
		{
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			parameters.addValue("clazz", clazz);
			parameters.addValue("sidval", sid);
			parameters.addValue("discriminators", charNames);
			idList = getNamedParameterJdbcTemplate().queryForList(PUBLIC_CHARACTERIZATION_SQL, parameters, Long.class);
		}
		return idList;
	}
	
	@Override
	public List<String> getIdsOfClassSharedWithSid(SecureClassesEnum classEnum, String loggedInUser, List<String> sids)
	{
		logger.debug("Fetching class: " + classEnum + " Ids Shared with: " + sids);
		List<String> idList = new ArrayList<String>();
		
		if (classEnum != null && !StringUtils.isEmpty(loggedInUser) && sids.size() > 0)
		{
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			parameters.addValue("clazz", classEnum.getClazz().getName());
			parameters.addValue("loggedInUser", loggedInUser);
			parameters.addValue("sids", sids);
			if (classEnum == SecureClassesEnum.SAMPLE)
				idList = getNamedParameterJdbcTemplate().queryForList(IDS_OF_SAMPLE_SHARED_WITH_SID_SQL, parameters, String.class);
			else if (classEnum == SecureClassesEnum.PROTOCOL)
				idList = getNamedParameterJdbcTemplate().queryForList(IDS_OF_PROTOCOL_SHARED_WITH_SID_SQL, parameters, String.class);
			else if (classEnum == SecureClassesEnum.PUBLICATION)
				idList = getNamedParameterJdbcTemplate().queryForList(IDS_OF_PUB_SHARED_WITH_SID_SQL, parameters, String.class);
		}
		
		return idList;
	}

	@Override
	public int deleteAllAccessToSid(String groupSid)
	{
		Object[] args = {groupSid};
		int status = this.getJdbcTemplate().update(DEL_SID_ACCESS_SQL, args);
		return status;
		
	}

}
