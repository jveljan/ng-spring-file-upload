package com.joco.spring.file.upload.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Basic File Storage 
 * 
 * @author joco
 *
 */
public interface FileStorage {
	/**
	 * Get File Info
	 * @param id
	 * @return
	 * @throws FileNotFoundException
	 */
	FileInfo getFileInfo(String id) throws FileNotFoundException;
	
	/**
	 * Get file input stream
	 * 
	 * @param id
	 * @return
	 * @throws FileNotFoundException
	 */
	InputStream getFileInputStream(final String id) throws FileNotFoundException;
	
	
	/**
	 * Remove file by id
	 * 
	 * @param id
	 */
	void removeFile(String id);
	
	/**
	 * Check if file exists
	 * @param id
	 * @return
	 */
	boolean exists(String id);
	
	
	/**
	 * Uploads file and returns file ID that can be used in getFileInfo and getFileInputStream
	 * 
	 * @param name
	 * @param contentType
	 * @param is
	 * @return
	 * @throws IOException
	 */
	String uploadFile(String name, String contentType, InputStream is) throws IOException;
}