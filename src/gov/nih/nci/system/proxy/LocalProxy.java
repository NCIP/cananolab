package gov.nih.nci.system.proxy;

import gov.nih.nci.common.net.*;
import gov.nih.nci.system.delegator.*;

/**
 * @author Shaziya
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LocalProxy implements InterfaceProxy {

    public LocalProxy(){
        }
    
    public Response query(Request request)throws Exception{
		Response response = new Response();
		try{
			BaseDelegate client = new BaseDelegate();
			response = client.query(request);
			
			}catch(Exception ex){
				throw new Exception (ex.getMessage());
			}
		
		return response;
		}

}
