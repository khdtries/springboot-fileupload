package com.springboot.fileupload.service;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.fileupload.model.DownloadFile;
import com.springboot.fileupload.model.FileMetadata;

/*
 * Service layer
 */

public interface FileService {
	public FileMetadata uploadAndpersist(MultipartFile mpFile, String provider, String desc) throws IOException;
	public List<FileMetadata> getAllMetadata();
	public List<FileMetadata> getMetadataById(String id);
	public List<FileMetadata> getMetadataByProvider(String provider);
	public DownloadFile getFileToDownload(String uid) throws IOException;	
}
