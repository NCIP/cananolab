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
	
	private static final String POC_OF_PUBLIC_ORGS_SQL = "select distinct o.organization_pk_id " +
														 "from acl_class ac, acl_sid asid, acl_object_identity aoi, acl_entry ae , sample s, point_of_contact poc, organization o " +
														 "where ac.class = :clazz and asid.sid = :sidval and ae.sid = asid.id " + 
														 "and aoi.object_id_class = ac.id and ae.acl_object_identity = aoi.id " +
														 "and aoi.object_id_identity = s.sample_pk_id and s.primary_contact_pk_id = poc.poc_pk_id " +
														 "and poc.organization_pk_id = o.organization_pk_id";

	private static final String PUBLIC_CHARACTERIZATION_SQL = "select distinct c.characterization_pk_id " +
															  "from acl_class ac, acl_sid asid, acl_object_identity aoi, acl_entry ae, characterization c " +
															  "where ac.class = :clazz  and asid.sid = :sidval and ae.sid = asid.id " +
															  "and aoi.object_id_class = ac.id and ae.acl_object_identity = aoi.parent_object " +
															  "and c.characterization_pk_id = aoi.object_id_identity and c.discriminator in (:discriminators)";

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
	public List<Long> getPocOfPublicSamples(String clazz, String sid)
	{
		logger.debug("Fetching Poc of Samples Ids accessible to: " + sid);
		List<Long> idList = new ArrayList<Long>();
		
		if (!StringUtils.isEmpty(clazz) && !StringUtils.isEmpty(sid))
		{
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			parameters.addValue("clazz", clazz);
			parameters.addValue("sidval", sid);
			idList = getNamedParameterJdbcTemplate().queryForList(POC_OF_PUBLIC_ORGS_SQL, parameters, Long.class);
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

}
