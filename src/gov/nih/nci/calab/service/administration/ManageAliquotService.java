package gov.nih.nci.calab.service.administration;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author pansu
 * 
 */

/* CVS $Id: ManageAliquotService.java,v 1.2 2006-03-16 21:54:49 pansu Exp $ */

public class ManageAliquotService {

	/**
	 * 
	 * @return all methods for creating aliquots
	 */
	public List<String> getAliquotCreateMethods() {
		List createMethods = new ArrayList();
		createMethods.add("Solubilized");
		createMethods.add("Liatholized");
		return createMethods;
	}
	
	public int getDefaultAliquotMatrixColumnNumber() {
		return 10;
	}
}