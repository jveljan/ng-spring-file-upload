package com.joco.fu.bp.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FileStorage {
	/**
	 * Get File Info
	 * @param id
	 * @return
	 * @throws FileNotFoundException
	 */
	FileInfo getFile(String id) throws FileNotFoundException;
	
	/**
	 * Check if file exists
	 * @param id
	 * @return
	 */
	boolean exists(String id);
	
	/**
	 * Upload file
	 * 
	 * @param name
	 * @param contentType
	 * @param is
	 * @return
	 * @throws IOException
	 */
	String uploadFile(String name, String contentType, InputStream is) throws IOException;
}