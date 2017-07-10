package com.springboot.fileupload.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.fileupload.model.FileMetadata;
import com.springboot.fileupload.util.FileUtil;

/*
 * DAO Implementation
 * Notice - This is not a DAO to Databases so that its annotation is @Component
 */

@Component
public class FileMetadataDaoImpl implements FileMetadataDao {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	/* file upload and saving metadata and files */
	public FileMetadata uploadAndPersist(FileMetadata metadata, MultipartFile mpFile
			, String metadataLocation, String filesLocation) throws IOException {
		log.debug("uploadAndPersist() in FileMetadataDaoImpl");
		
		FileUtil.writeMetadata(metadataLocation, String.valueOf(metadata.getId()), metadata);
		FileUtil.writeFile(metadata, filesLocation, mpFile);
		
		log.info("Metadata and uploaded file(" + metadata.getFilename() + ") have been saved");
		
		return metadata;
	}

	/* retrieve all metadata */
	public List<String> getAllMetadata(String metadataLocation) {
		File f = new File(metadataLocation);
		File[] files = f.listFiles();
		
		List<String> list = new ArrayList<String>();
		
		if(files == null) {
			return list;
		}
		
		for(File file : files) {
			try {
				log.debug("getAllMetadata(), file info : " + file.getAbsolutePath());
				
				StringBuilder str = FileUtil.readMetadataFile(file);
				
				log.debug("getAllMetadata(), json string : " + str.toString());
				
				list.add(str.toString());
			} catch (IOException e) {
				log.error("error while reading a file : " + file.getName());
			}
		}
		
		return list;
	}
	
	/* retrieve corresponding metadata by id */
	public List<String> getMetadataById(String metadataLocation, String id) {
		File f = new File(metadataLocation);
		File[] files = f.listFiles();
		
		List<String> list = new ArrayList<String>();
		
		if(files == null) {
			return list;
		}
		
		for(File file : files) {
			try {
				//unless it contains id, it skips.
				if(!file.getName().contains(id)) {
					continue;
				}
				
				log.debug("getMetadataById(), file info : " + file.getAbsolutePath());
				
				StringBuilder str = FileUtil.readMetadataFile(file);
				
				log.debug("getMetadataById(), json string : " + str.toString());
				
				list.add(str.toString());
			} catch (IOException e) {
				log.error("error while reading a file : " + file.getName());
			}
		}
		return list;
	}
	
	/* retrieve corresponding metadata by provider */
	public List<FileMetadata> getMetadataByProvider(String metadataLocation, String provider) {
		File f = new File(metadataLocation);
		File[] files = f.listFiles();
		
		List<FileMetadata> list = new ArrayList<FileMetadata>();
		
		if(files == null) {
			return list;
		}
		
		for(File file : files) {
			try {
				log.debug("getMetadataByProvider(), file info : " + file.getAbsolutePath());
				
				StringBuilder str = FileUtil.readMetadataFile(file);
				
				log.debug("getMetadataByProvider(), json string : " + str.toString());
				
				FileMetadata fmetadata = FileUtil.generateFileMetadataFromJsonString(str.toString());
				
				if(fmetadata != null) {
					String providerInfo = fmetadata.getProvider();
					
					if(providerInfo != null && providerInfo.contains(provider)) {
						list.add(fmetadata);	
					}
				}
			} catch (IOException e) {
				log.error("error while reading a file : " + file.getName());
			}
		}
		
		return list;
	}
	
	/* retrieve FileMetadata to support file download */
	public FileMetadata getFileDownloadInfo(String metadataLocation, String id) throws IOException {
		File f = new File(metadataLocation + "/" + id);
		BufferedReader br = null;
		
		try {
			br = FileUtil.getBufferedReader(f);
		} catch (IOException e) {
			log.error("error while getting bufferedReader", e);
			FileUtil.closeBufferedReader(br);
		}
		
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			log.error("error while reading a file", e);
			FileUtil.closeBufferedReader(br);
			throw e;
		}
		
		if(sb.length() > 0) {
			return FileUtil.generateFileMetadataFromJsonString(sb.toString());
		}
		
		return null;
	}
	
	/* related with Spring Scheduling (see @Scheduled annotation in ScheduledJobs)
	 * retrieve new items only based on the time Scheduling kicks off
	 */
	public List<String> getNewItems(String metadataLocation, long ts) {
		File f = new File(metadataLocation);
		File[] files = f.listFiles();
		
		List<String> newItemList = new ArrayList<String>();
		
		if(files == null) {
			return newItemList;
		}
		
		long tempTs = 0;
		
		for(File aFile : files) {
			tempTs = Long.parseLong(aFile.getName());
			
			if(tempTs >= ts) {
				newItemList.add(aFile.getName());
			}
		}
		
		return newItemList;
	}

}
