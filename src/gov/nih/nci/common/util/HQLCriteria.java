/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

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
