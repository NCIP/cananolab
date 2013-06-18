/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.system.applicationservice;

import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.WritableApplicationService;

import java.io.Serializable;
import java.util.List;

/**
 * Customized to contain more CRUD operations.
 *
 * @author pansu
 *
 */
public interface CaNanoLabApplicationService extends WritableApplicationService {

	public void saveOrUpdate(Object object) throws ApplicationException;

	public Object load(Class domainClass, Serializable id)
			throws ApplicationException;

	public Object get(Class domainClass, Serializable id)
			throws ApplicationException;

	public void delete(Object object) throws ApplicationException;

	public void deleteById(Class domainClass, Serializable id)
			throws ApplicationException;

	public List getAll(Class domainClass) throws ApplicationException;

	public Object getObject(Class domainClass, String uniqueKeyName,
			Object uniqueKeyValue) throws ApplicationException;

	public List directSQL(String directSQL, String[] columns,
			Object[] columnTypes) throws ApplicationException;

	public List<String> getAllPublicData() throws ApplicationException;
}
