package com.joco.spring.file.upload.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DisckFileStorage implements FileStorage, InitializingBean {
	
	@Value("${disk.storage.dir:./.files/}")
	private String diskStorageDir;
	
	private ObjectMapper mapper;
	
	@Autowired
	public DisckFileStorage(ObjectMapper mapper) {
		this.mapper = mapper;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		new File(diskStorageDir).mkdirs();
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
	

	private File getFileDataFile(String id) {
		return new File(diskStorageDir, id + ".data");
	}
	private File getFileInfoJsonFile(String id) {
		return new File(diskStorageDir, id + ".json");
	}
	
	private void createFileInfo(String id, String name, String contentType, long contentLength) throws IOException {
		FileOutputStream fos = new FileOutputStream(getFileInfoJsonFile(id));
		FileInfo fi = new FileInfo();
		fi.setContentType(contentType);
		fi.setCreatedOn(new Date());
		fi.setName(name);
		fi.setLength(contentLength);
		mapper.writeValue(fos, fi);
		fos.close();
	}
	
	private FileInfo readFileInfo(String id) {
		try {
			return mapper.readValue(getFileInfoJsonFile(id), FileInfo.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public FileInfo getFileInfo(final String id) throws FileNotFoundException {
		if(!exists(id)) throw new FileNotFoundException();
		return readFileInfo(id);
	}
	@Override
	public InputStream getFileInputStream(final String id) throws FileNotFoundException {
		if(!exists(id)) throw new FileNotFoundException();
		return new FileInputStream(getFileDataFile(id));
	}

	@Override
	public boolean exists(String id) {
		return getFileDataFile(id).exists() && getFileInfoJsonFile(id).exists();
	}

	@Override
	public void removeFile(String id) {
		if (exists(id)) {
			getFileInfoJsonFile(id).delete();
			getFileDataFile(id).delete();
		}
	}

	
}
