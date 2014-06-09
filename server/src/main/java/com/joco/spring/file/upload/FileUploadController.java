package com.joco.spring.file.upload;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.joco.spring.file.upload.service.FileInfo;
import com.joco.spring.file.upload.service.FileStorage;

@Controller
@RequestMapping("/files")
public class FileUploadController {
	
	@Autowired
	FileStorage fileStorage;
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public void getFile(@PathVariable String id, HttpServletResponse response) throws Exception {
		ServletOutputStream os = response.getOutputStream();
		FileInfo fi = fileStorage.getFileInfo(id);
		response.setContentType(fi.getContentType());
		response.setHeader("contentLength", String.valueOf(fi.getLength()));
		try (InputStream is = fileStorage.getFileInputStream(id)) {
			IOUtils.copy(is, os);
			os.flush();
		}
	}
	
	@RequestMapping(value="/info/{id}", method=RequestMethod.GET)
    public @ResponseBody FileInfo getFileInfo(@PathVariable("id") String id) throws Exception {
        return fileStorage.getFileInfo(id);
    }
	
	@RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody List<String> handleFilesUpload(@RequestParam("file") List<MultipartFile> files) throws IOException {
        List<String> fileIds = new ArrayList<>();
        for(MultipartFile file : files) {
        	String id = fileStorage.uploadFile(file.getOriginalFilename(), file.getContentType(), file.getInputStream());
        	fileIds.add(id);
        }
        return fileIds;
    }

}
