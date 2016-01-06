package gov.nih.nci.cananolab.restful.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;


public class PublicationImageCache {
	private Logger logger = Logger.getLogger(PublicationImageCache.class);
	private byte[] imageCaNanoCached;
	private byte[] imageTranspCached;
	private transient boolean isCaNanoImageError;
	private transient boolean isTranspImageError;
	private transient Long caNanoImageFailedTs = System.currentTimeMillis();
	private transient Long transpImageFailedTs = caNanoImageFailedTs;
	
	public static final PublicationImageCache INSTANCE = new PublicationImageCache();
	
	private PublicationImageCache() {
		super();
	}

	public static PublicationImageCache getInstance() {
		return INSTANCE;
	}
	
	public InputStream getCaNanoImage(String fileRoot) throws FileNotFoundException {	
		if (imageCaNanoCached != null)//happy path taken from existed cache
			return new ByteArrayInputStream(imageCaNanoCached);

		if ((! isCaNanoImageError) || (isCaNanoImageError && (buildFileImageAgain(caNanoImageFailedTs)))) {
			synchronized(caNanoImageFailedTs) {
				imageCaNanoCached = buildByteArrayImage("canano_logo_mini.jpg", fileRoot);
			}
		}
		
		if (imageCaNanoCached != null && imageCaNanoCached.length > 0) {
		 ByteArrayInputStream bis = new ByteArrayInputStream(imageCaNanoCached);
		 return bis;
		}
		else {
			synchronized(caNanoImageFailedTs) {
				isCaNanoImageError = true;
				caNanoImageFailedTs = System.currentTimeMillis();
			}
			throw new FileNotFoundException("canano_logo_mini.jpg");
		}
	}
	
	public InputStream getTranspImage(String fileRoot) throws FileNotFoundException {
		if (imageTranspCached != null)//happy path taken from existed cache
			return new ByteArrayInputStream(imageTranspCached);

		if ((! isTranspImageError) || (isTranspImageError && (buildFileImageAgain(transpImageFailedTs)))) {
			synchronized(transpImageFailedTs) {
				imageTranspCached = buildByteArrayImage("doi-transparent.png", fileRoot);
			}
		}
		
		if (imageTranspCached != null && imageTranspCached.length > 0) {
		 ByteArrayInputStream bis = new ByteArrayInputStream(imageCaNanoCached);
		 return bis;
		}
		else {
			synchronized(transpImageFailedTs) {
				isTranspImageError = true;
				transpImageFailedTs = System.currentTimeMillis();
			}
			throw new FileNotFoundException("doi-transparent.png");
		}
	}


	private byte[] buildByteArrayImage(String imageFileName, String fileRoot) throws FileNotFoundException {
		byte[] imageArrayCache;
		ByteArrayOutputStream barr = new ByteArrayOutputStream(256);
		String fileName = null;		

		fileName = fileRoot + java.io.File.separator + imageFileName;

		java.io.File fileSuccess = new java.io.File(fileName);
		if (fileSuccess.exists()) {
			InputStream fis = new BufferedInputStream(new FileInputStream(fileSuccess));
			byte[] byteArrToRead = new byte[2048];
			int readBytes = 0;
			int readAll = 0;
			try {
				while ((readBytes = fis.read(byteArrToRead, 0, 2048)) != -1) {
					barr.write(byteArrToRead, 0, readBytes);
					readAll += readBytes;
				}
				barr.flush();
				imageArrayCache = new byte[readAll];
				if (readAll > 0) {
					imageArrayCache = barr.toByteArray();
				}
				else {
					logger.error("PublicationImageCache.buildByteArrayImage read zero image bytes from the file: " + fileName);
				}
				return imageArrayCache;
			}
			catch (IOException e) {
				logger.error("PublicationImageCache.buildByteArrayImage error when reading image file: " + fileName, e);
				return null;
			}
			finally{
				if (fis != null) {
					try {
						fis.close();
					}
					catch(IOException e) {
						logger.error("PublicationImageCache.buildByteArrayImage error when closing image file: " + fileName, e);
					}
				}				
			}
		}
		else {
			logger.error("PublicationImageCache.buildByteArrayImage image file does not exist: " + fileName);
			return null;
		}
	}
	
	private synchronized boolean buildFileImageAgain(long lastFailedTs) {
		return ((System.currentTimeMillis() - lastFailedTs) > 1800000);
	}
}
