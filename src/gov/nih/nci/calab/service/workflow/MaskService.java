package gov.nih.nci.calab.service.workflow;

/**
 * Generalizes Mask functionality for masking Aliquot, File, etc.  
 * @author doswellj
 * @param strType Type of Mask (e.g., aliquot, file, run, etc.)
 * @param strId	  The id associated to the type
 * @param strDescription The mask description associated to the mask type and Id. 
 *
 */
public class MaskService 
{
    
	//This functionality is pending the completed Object Model
	public void setMask(String strType, String strId, String strDescription)
    {
        if (strType.equals("aliquot"))
        {
            //TODO Find Aliquot record based on the strID
        	
        	//TODO Set File Status record to "Masked". 
        	
        }
        if (strType.equals("file"))
        {
        	//TODO Find File record based on the its strID
        	
        	//TODO Set File Status record to "Masked". 
        }
        	
        	 
    }
    
}
