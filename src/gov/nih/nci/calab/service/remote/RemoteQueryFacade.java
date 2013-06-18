/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.calab.service.remote;

import java.util.List;

/**
 * This interface defines methods available to http clients.
 * 
 * @author pansu
 * 
 */
public interface RemoteQueryFacade {

	public boolean isPublicId(String dataId) throws Exception;

	public List<String> getPublicDataIds(String[] dataIds) throws Exception;

	public byte[] retrievePublicFileContent(Long fileId) throws Exception;
}