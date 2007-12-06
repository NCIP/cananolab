package gov.nih.nci.calab.service.util;

import gov.nih.nci.calab.exception.CaNanoLabException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ActionUtil {
	public static String getHostUriString(HttpServletRequest request)
			throws ServletException, IOException {
		String scheme = request.getScheme();
		String host = request.getServerName();
		int port = request.getServerPort();

		String url = "";
		if (port != -1)
			url = scheme + "://" + host + ":" + port;
		else
			url = scheme + "://" + host;

		return url;
	}

	public void writeBinaryStream(File file, HttpServletResponse response)
			throws CaNanoLabException {
		if (file == null || response == null) {
			throw new CaNanoLabException(
					"Unable to write file to HttpServletResponse: "
							+ "Either pathName or response is null.");
		}
		try {
			// set a non-standard content type to force brower to open Save As
			// dialog
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ file.getName());
			ServletOutputStream sos = response.getOutputStream();
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));
			byte[] buf = new byte[1024 * 10]; // ~= 10KB
			for (int len = -1; (len = bis.read(buf)) != -1;) {
				sos.write(buf, 0, len);
			}
			response.setContentLength((int) file.length());
			sos.flush(); // let the Servlet container handle closing of this
			// ServletOutputStream
			bis.close();
		} catch (Exception e) {
			throw new CaNanoLabException(
					"Unable to write file to client, exception is "
							+ e.toString());
		}
	}
}
