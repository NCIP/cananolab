package gov.nih.nci.calab.service.workflow.httpfileuploadapplet;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class HttpTunnelingFetchFileList 
{
	private URL url;
	private URLConnection connection;
	private ArrayList list = new ArrayList();
	
	public ArrayList FetchFileList(String tunnelURL, String command) throws Exception
	{
		try
		{
			url = new URL(tunnelURL);
			connection = url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestProperty("CONTENT_TYPE", "application/octet-stream");
			
			ObjectOutputStream oos = new ObjectOutputStream(connection.getOutputStream());
			oos.writeObject(command);
			oos.close();
			
			ObjectInputStream ois = new ObjectInputStream(connection.getInputStream());
			Object object = ois.readObject();
			
			if (object instanceof Exception)
			{
				throw (Exception)object;
			}
			else
			{
				list = (ArrayList)object;
			}
			
			ois.close();
			
		}
		catch (Exception e)
		{
			throw e;
		}
		
		return list;
	}
	
}
