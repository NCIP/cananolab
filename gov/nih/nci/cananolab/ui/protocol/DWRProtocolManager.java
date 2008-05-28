package gov.nih.nci.cananolab.ui.protocol;
import org.apache.log4j.Logger;

/**
 * This class loads protocol data for ajax
 * 
 * @author tanq
 * 
 */
public class DWRProtocolManager {

	Logger logger = Logger.getLogger(DWRProtocolManager.class);
	public DWRProtocolManager() {}
	
	public String[] getProtocolTypes(String searchLocations) {
		return InitProtocolSetup.getInstance().getProtocolTypes(searchLocations);
	}
	
}
