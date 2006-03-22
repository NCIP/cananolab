package gov.nih.nci.calab.service.workflow;

public class MaskService 
{
    public void setMask(String strType, String strID, String strDescription)
    {
        if (strType.equals("Aliquot"))
        {
            //TODO Find Aliquot record based on the its id and set the status to "Masked" and its Description
        }
        if (strType.equals("File"))
        {
        	//TODO Find File record based on the its id and set the status to "Masked" and its Description
        }
        	
        	 
    }
    
}
