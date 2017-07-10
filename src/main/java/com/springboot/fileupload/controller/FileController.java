package com.springboot.fileupload.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.fileupload.model.DownloadFile;
import com.springboot.fileupload.model.FileMetadata;
import com.springboot.fileupload.service.FileService;

@Controller
public class FileController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private FileService fileService;

	/* basic page with file upload functionality */
	@GetMapping("/")
	public String goIndexPage() {
		log.debug("/ url called");
		
		return "index";
	}
	
	/* provide all list of uploaded file and its metadata
	 * provide search and file download functions too
	 */
	@GetMapping("/listpage")
	public String goListPage() {
		log.debug("/listpage url called");
		
		return "list";
	}
	
	/* retrieve all matadata */
	@GetMapping("/list")
	public @ResponseBody List<FileMetadata> getAllMetadata() {
		log.debug("/list url called");

		return fileService.getAllMetadata();
	}
	
	/* retrieve metadata with only corresponding id (like search) */
	@GetMapping("/list/id/{idValue}")
	public @ResponseBody List<FileMetadata> getMetadataById(@PathVariable("idValue") String id) {
		log.debug("/list/id/{idValue} url called. id : " + id);

		return fileService.getMetadataById(id);
	}
	
	/* retrieve metadata with only corresponding provider name (like search) */
	@GetMapping("/list/provider/{providerValue}")
	public @ResponseBody List<FileMetadata> getMetadataByProvider(@PathVariable("providerValue") String provider) {
		log.debug("/list/id/{providerValue} url called. provider : " + provider);

		return fileService.getMetadataByProvider(provider);
	}
	
	/* file upload */
	@PostMapping("/upload")
	public @ResponseBody FileMetadata uploadAndPersist(@RequestParam("uploadedFile") MultipartFile mpFile
			,@RequestParam("provider") String provider, @RequestParam("desc") String desc) throws IOException {
		log.debug("/upload url called");
		
		return fileService.uploadAndpersist(mpFile, provider, desc);
	}
	
	/* file download by id */
	@GetMapping("/download/{id}")
	public ResponseEntity<InputStreamResource> downloadFile(HttpServletResponse response, @PathVariable("id") String fileId) throws IOException {
		log.debug("/download/{id} url called, id : " + fileId);
		
		DownloadFile downloadFile = null;
		File f = null;
		FileMetadata metadata = null;
		
		try {
			downloadFile = fileService.getFileToDownload(fileId);
			f = downloadFile.getDownloadFile();
			metadata = downloadFile.getMetadata();
		} catch (IOException e) {
			log.error("no files can be downloaded", e);
			throw e;
		}

		if(f != null) {
			log.debug("f.getAbsolutePath() : " + f.getAbsolutePath());
			
			HttpHeaders hdrs = new HttpHeaders();
			hdrs.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			hdrs.setContentDispositionFormData("attachment", metadata.getFilename());
			
			InputStreamResource inputStrmRsc = new InputStreamResource(new FileInputStream(f));
			return new ResponseEntity<InputStreamResource>(inputStrmRsc, hdrs, HttpStatus.OK);
		}
		
		return null;
	}
}
