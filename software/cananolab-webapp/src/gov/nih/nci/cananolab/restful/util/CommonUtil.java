package gov.nih.nci.cananolab.restful.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

public class CommonUtil {
	
	/**
	 * Wrap an error message in a list
	 * 
	 * @param error
	 * @return
	 */
	public static List<String> wrapErrorMessageInList(String error) {
		List<String> msg = new ArrayList<String>();
		if (error == null)
			return msg;
		else
			msg.add(error);
		
		return msg;
	}
	
	/**
	 * Add "other" to the end of an option list
	 * 
	 * @param items
	 * @return
	 */
	public static List<String> addOtherToList(List<String> items) {
		List<String> theList = items;
		if (theList == null)
			theList = new ArrayList<String>();
		
		theList.add("other");
		
		return theList;
	}
	
	public static String getServerDir(HttpServletRequest httpRequest){
		String serverDir = "";
		if((httpRequest.getServerName().contains("localhost"))||(httpRequest.getServerName().contains("127.0.0.1"))){
			serverDir = "http://"+httpRequest.getServerName()+":"+httpRequest.getServerPort()+httpRequest.getContextPath();
		}else{
			serverDir = "https://"+httpRequest.getServerName()+":"+httpRequest.getServerPort()+httpRequest.getContextPath();
		}
		return serverDir;
	}
	
	public static File retrieveImage(HttpServletRequest httpRequest, Boolean flag){
		URL urlSuccess; URL urlError; BufferedImage imageSuccess = null; BufferedImage imageError = null;
		File fileSuccess = new File("canano_logo_mini.jpg");
		File fileError = new File("doi-transparent.png");
		String serverDir = getServerDir(httpRequest);
				
		try {
			urlSuccess = new URL(serverDir + "/images/canano_logo_mini.jpg");
			imageSuccess = ImageIO.read(urlSuccess.openStream());
			ImageIO.write(imageSuccess, "jpg", fileSuccess);
			urlError = new URL(serverDir + "/images/doi-transparent.png");
			imageError = ImageIO.read(urlError.openStream());
			ImageIO.write(imageError, "png", fileError);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(flag){
			return fileError;
		}
		return fileSuccess;
	}
}
