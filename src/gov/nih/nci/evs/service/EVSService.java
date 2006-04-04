
package gov.nih.nci.evs.service;
import java.util.*;
import java.io.*;

/**
 * @author Shaziya
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EVSService implements Serializable {

	private static final long serialVersionUID = 1234567890L;
	private String methodName;
	private List parameterList;
	
	public EVSService(){
		methodName = null;
		parameterList = new ArrayList();
		}
	
	public void setMethodName(String methodName){
		this.methodName = methodName;
		}
	public String getMethodName(){
		return methodName;
		}
	public void setParameterList(List param){
		this.parameterList = param;
		}
	public List getParameterList(){
		return parameterList;
		}
	
	
	
}
