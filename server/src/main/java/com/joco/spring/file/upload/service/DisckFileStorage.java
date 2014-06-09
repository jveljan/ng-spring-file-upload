package com.joco.spring.file.upload.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DisckFileStorage implements FileStorage {
	
	private String BASE_DIR = "./files/";
	public DisckFileStorage() {
		new File(BASE_DIR).mkdirs();
	}
	
	public String uploadFile(String name, String contentType, InputStream is) throws IOException {
		String rv = UUID.randomUUID().toString();
		int length = writeFileData(rv, is);
		createFileInfo(rv, name, contentType, length);
		
		return rv;
	}
	
	private int writeFileData(String rv, InputStream is) throws IOException {
		FileOutputStream fos = new FileOutputStream(getFileDataFile(rv));
		try {
			return IOUtils.copy(is, fos);
		} finally {
			IOUtils.closeQuietly(fos);
		}
	}
	@Autowired
	ObjectMapper mapper;

	private File getFileDataFile(String id) {
		return new File(BASE_DIR, id + ".data");
	}
	private File getFileInfoJsonFile(String id) {
		return new File(BASE_DIR, id + ".json");
	}
	
	private void createFileInfo(String id, String name, String contentType, long contentLength) throws IOException {
		FileOutputStream fos = new FileOutputStream(getFileInfoJsonFile(id));
		Map<String, String> m = new HashMap<>();
		m.put("name", name);
		m.put("contentType", contentType);
		m.put("contentLength", new Long(contentLength).toString());
		mapper.writeValue(fos, m);
		fos.close();
	}
	
	private Map<String, String> readFileInfo(String id) {
		try {
			return mapper.readValue(getFileInfoJsonFile(id), new TypeReference<HashMap<String,String>>(){});
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	

	/* (non-Javadoc)
	 * @see com.joco.fu.bp.FileService#getFile(java.lang.String)
	 */
	@Override
	public FileInfo getFile(final String id) throws FileNotFoundException {
		FileInfo fi = new FileInfo() {
			@Override
			public InputStream getInputStream() throws IOException {
				return new FileInputStream(getFileDataFile(id));
			}
		};
		if(!exists(id)) {
			throw new FileNotFoundException();
		}
		Map<String, String> m = readFileInfo(id);
		fi.setName(m.get("name")); 
		fi.setContentType(m.get("contentType"));
		fi.setLength(Long.valueOf(m.get("contentLength")));
		return fi;
	}

	@Override
	public boolean exists(String id) {
		return getFileDataFile(id).exists() && getFileInfoJsonFile(id).exists();
	}
}
