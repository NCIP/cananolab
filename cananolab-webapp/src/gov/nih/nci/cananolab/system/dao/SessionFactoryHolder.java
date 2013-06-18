/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.system.dao;

import org.hibernate.SessionFactory;

public class SessionFactoryHolder
{
	private SessionFactory instance;

	public SessionFactory getInstance() {
		return instance;
	}

	public void setInstance(SessionFactory instance) {
		this.instance = instance;
	}
}