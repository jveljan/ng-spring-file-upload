package com.joco.fu.bp;

import java.io.IOException;
import java.io.InputStream;

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

import com.joco.fu.bp.service.FileInfo;
import com.joco.fu.bp.service.FileServiceDiskFile;

@Controller
@RequestMapping("/files")
public class FileUploadController {
	
	@Autowired
	FileServiceDiskFile fileService;
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public void getFile(@PathVariable String id, HttpServletResponse response) throws Exception {
		ServletOutputStream os = response.getOutputStream();
		FileInfo fi = fileService.getFile(id);
		response.setContentType(fi.getContentType());
		//response.setContentLength(fi.getLength());
		response.setHeader("contentLength", String.valueOf(fi.getLength()));
		try (InputStream is = fi.getInputStream()) {
			IOUtils.copy(is, os);
			os.flush();
		}
	}
	
//	@RequestMapping("/{id}")
//	public ResponseEntity<InputStreamResource> getFile(@PathVariable String id) throws Exception {
//		
//		FileInfo fi = fileService.getFile(id);
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		try (InputStream is = fi.getInputStream()) {
//			IOUtils.copyLarge(is, bos);
//		}
//		final HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.parseMediaType(fi.getContentType()));
//		headers.setContentLength(fi.getLength());
//		InputStreamResource isr = new InputStreamResource(fi.getInputStream());
//		return new ResponseEntity<>(isr, headers, HttpStatus.OK);
//	}
	
	@RequestMapping(value="/info/{id}", method=RequestMethod.GET)
    public @ResponseBody FileInfo getFileInfo(@PathVariable("id") String id) throws Exception {
        return fileService.getFile(id);
    }
	
	@RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("name") String name, @RequestParam("file") MultipartFile file) throws IOException {
        return fileService.uploadFile(name, file.getContentType(), file.getInputStream());
    }

}
