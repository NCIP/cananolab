package gov.nih.nci.common.util;

import java.io.Serializable;

public class HQLCriteria implements Serializable{
	
	private String hqlString;
	
	public HQLCriteria(String hqlString)
	{
		setHqlString(hqlString);
	}
	
	public void setHqlString(String hqlString)
	{
		this.hqlString = hqlString;		
	}
	public String getHqlString()
	{
		return this.hqlString;
	}

}
