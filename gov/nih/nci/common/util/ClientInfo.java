package gov.nih.nci.common.util;

import java.io.Serializable;


public class ClientInfo implements Serializable
{

	private static final long serialVersionUID = 1L;

	private String sessionKey;
	private int recordsCount;
	private boolean caseSensitivity;
	
	public boolean getCaseSensitivity()
	{
		return caseSensitivity;
	}
	
	public void setCaseSensitivity(boolean caseSensitivity)
	{
		this.caseSensitivity = caseSensitivity;
	}
	
	public int getRecordsCount()
	{
		return recordsCount;
	}
	
	public void setRecordsCount(int recordsCount)
	{
		this.recordsCount = recordsCount;
	}
	
	public String getSessionKey()
	{
		return sessionKey;
	}
	
	public void setSessionKey(String sessionKey)
	{
		this.sessionKey = sessionKey;
	}

}
