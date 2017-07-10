package com.springboot.fileupload.dao;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.fileupload.model.FileMetadata;

/**
 * DAO interface
 * Persistence type is File System
 */

public interface FileMetadataDao {
	public FileMetadata uploadAndPersist(FileMetadata metadata, MultipartFile mpFile
			, String metadataLocation, String filesLocation) throws IOException;
	
	public List<String> getAllMetadata(String metadataLocation);
	public List<String> getMetadataById(String metadataLocation, String id);
	public List<FileMetadata> getMetadataByProvider(String metadataLocation, String provider);
	public FileMetadata getFileDownloadInfo(String metadataLocation, String id) throws IOException;
	
	public List<String> getNewItems(String metadataLocation, long ts);
}
