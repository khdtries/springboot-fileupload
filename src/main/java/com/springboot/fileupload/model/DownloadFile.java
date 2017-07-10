package com.springboot.fileupload.model;

import java.io.File;

/**
 * This contains the file information to get downloaded
 * and its real file name in the metadata
 *
 */
public class DownloadFile {
	//info to get downloaded from the server end
	private File downloadFile;
	//metadata in which the real file name can be found
	private FileMetadata metadata;
	
	public DownloadFile(File downloadFile, FileMetadata metadata) {
		this.downloadFile = downloadFile;
		this.metadata = metadata;
	}
	
	public File getDownloadFile() {
		return downloadFile;
	}
	public FileMetadata getMetadata() {
		return metadata;
	}
}
