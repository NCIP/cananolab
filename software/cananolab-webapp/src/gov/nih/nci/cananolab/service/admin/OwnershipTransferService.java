/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.service.admin;

import gov.nih.nci.cananolab.exception.AdministrationException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.BaseService;

import java.util.List;

/**
 * Interface for transfer of ownership.
 */
public interface OwnershipTransferService {
	public static final String DATA_TYPE_SAMPLE = "sample";
	public static final String DATA_TYPE_PROTOCOL = "protocol";
	public static final String DATA_TYPE_PUBLICATION = "publication";
	public static final String DATA_TYPE_GROUP = "collaboration group";

	public int transferOwner(BaseService baseService, List<String> dataIds,
			String currentOwner, String newOwner)
			throws AdministrationException, NoAccessException;

	public int transferOwner(List<String> dataIds, String dataType, String currentOwner,
			String newOwner) throws AdministrationException, NoAccessException;

}
