package com.joco.spring.file.upload;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.joco.spring.file.upload.service.DisckFileStorage;
import com.joco.spring.file.upload.service.FileInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class DiskFileStorageTest {

	@Autowired
	DisckFileStorage disckFileStorage;
	
	@Test
	public void contextLoads() throws IOException {
		String id = disckFileStorage.uploadFile("test", "content/type", IOUtils.toInputStream("my data"));
		
		FileInfo fi = disckFileStorage.getFileInfo(id);
		Assert.assertEquals("test", fi.getName());
		Assert.assertEquals(7L, fi.getLength());
		Assert.assertNotNull(fi.getCreatedOn());
		
		InputStream is = disckFileStorage.getFileInputStream(id);
		Assert.assertNotNull(fi.getCreatedOn());
		String data = IOUtils.toString(is);
		Assert.assertEquals("my data", data);
		
		disckFileStorage.removeFile(id);
	}

}
