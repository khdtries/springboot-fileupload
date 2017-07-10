package com.springboot.fileupload.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.fileupload.dao.FileMetadataDao;
import com.springboot.fileupload.model.DownloadFile;
import com.springboot.fileupload.model.FileMetadata;

/**
 * Service Layer Implementation
 */
@Service("fileService")
public class FileServiceImpl implements FileService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Value("${file.upload.metadata.location}")
	private String metadataLocation;
	
	@Value("${file.upload.files.location}")
	private String filesLocation;
	
	@Autowired
	private FileMetadataDao fileMetadataDao;
	
	/* file upload and save metadata */
	public FileMetadata uploadAndpersist(MultipartFile mpFile, String provider, String desc) throws IOException {
		log.debug("uploadAndpersist(), metadataLocation : "+ metadataLocation + ", filesLocation : "+ filesLocation);
		
		FileMetadata inputMetadata = new FileMetadata();
		inputMetadata.setProvider(provider);
		inputMetadata.setDesc(desc);
		inputMetadata.setFilename(mpFile.getOriginalFilename());
		
		return fileMetadataDao.uploadAndPersist(inputMetadata, mpFile, metadataLocation, filesLocation);
	}

	/* retrieve all metadata */
	public List<FileMetadata> getAllMetadata() {
		List<String> list = fileMetadataDao.getAllMetadata(metadataLocation);
		
		List<FileMetadata> rtnList = new ArrayList<FileMetadata>();
		ObjectMapper mapper = new ObjectMapper();
		
		for(String s : list) {
			try {
				log.debug("json string : " + s);
				
				FileMetadata metadata = mapper.readValue(s, FileMetadata.class);
				rtnList.add(metadata);
				
			} catch (JsonParseException e) {
				log.error("error while parsing between json string and object", e);
			} catch (JsonMappingException e) {
				log.error("error while mapping between json string and object", e);
			} catch (IOException e) {
				log.error("error while mapping between json string and object", e);
			}
		}
		
		return rtnList;
	}
	
	/* retrieve corresponding metadata by id */
	public List<FileMetadata> getMetadataById(String id) {
		List<String> list = fileMetadataDao.getMetadataById(metadataLocation, id);
		
		List<FileMetadata> rtnList = new ArrayList<FileMetadata>();
		ObjectMapper mapper = new ObjectMapper();
		
		for(String s : list) {
			try {
				log.debug("json string : " + s);
				
				FileMetadata metadata = mapper.readValue(s, FileMetadata.class);
				rtnList.add(metadata);
				
			} catch (JsonParseException e) {
				log.error("error while parsing between json string and object", e);
			} catch (JsonMappingException e) {
				log.error("error while mapping between json string and object", e);
			} catch (IOException e) {
				log.error("error while mapping between json string and object", e);
			}
		}
		
		return rtnList;
	}
	
	/* retrieve corresponding metadata by provider */
	public List<FileMetadata> getMetadataByProvider(String provider) {
		return fileMetadataDao.getMetadataByProvider(metadataLocation, provider);
	}

	/* retrieve an info to get the file downloaded */
	public DownloadFile getFileToDownload(String id) throws IOException {
		FileMetadata metadata = fileMetadataDao.getFileDownloadInfo(metadataLocation, id);
		File downloadedFile = new File(filesLocation + "/" + metadata.getSysFilename());
		
		if(downloadedFile.exists() && downloadedFile.isFile()) {
			return new DownloadFile(downloadedFile, metadata);
		}
		
		return null;
	}
}
