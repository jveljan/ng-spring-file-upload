package com.joco.spring.file.upload.service;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class FileInfo {
	private String name;
	private String contentType;
	private long length;
	
	@JsonIgnore
	public abstract InputStream getInputStream() throws IOException;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public long getLength() {
		return length;
	}
	public void setLength(long length) {
		this.length = length;
	}
}
