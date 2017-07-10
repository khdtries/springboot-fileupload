package com.springboot.fileupload.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.fileupload.model.FileMetadata;

/**
 * It supports file handling as utility class.
 *
 */

public class FileUtil {
	private static final Logger log = LoggerFactory.getLogger(FileUtil.class);
	
	/* write uploaded files */
	public static void writeFile(FileMetadata metadata, String filesLocation, MultipartFile mpFile) throws IOException {
		byte[] bytes = mpFile.getBytes();
		Path path = Paths.get(filesLocation + "/" + metadata.getId() + mpFile.getOriginalFilename());
		
		//create the directory for filesLocation argument unless existing
		createFolders(new File(filesLocation));
		
		Files.write(path, bytes);
		
		log.info(mpFile.getOriginalFilename() + " has saved in " + filesLocation);
	}
	
	/* write metadata */
	public static void writeMetadata(String metadataLocation, String metadataFilename, FileMetadata fileMetadata)
			throws IOException {
		log.debug("metadataLocation : " + metadataLocation + ", metadataFilename : " + metadataFilename);
		
		File f = new File(metadataLocation + "/" + metadataFilename);
		
		//create metadataLocation unless existing
		createFolders(new File(metadataLocation));
		
		if(!f.exists()) {
			f.createNewFile();
		}
		
		log.debug("absolute path : " + f.getAbsolutePath());
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(f, fileMetadata);
		} catch (JsonGenerationException e) {
			log.error("exception occurred", e);
			throw new IOException(e);
		} catch (JsonMappingException e) {
			log.error("exception occurred", e);
			throw new IOException(e);
		}
	}
	
	/* read metadata from a stored file */
	public static StringBuilder readMetadataFile(File f) throws IOException {
		StringBuilder str = new StringBuilder();
		BufferedReader br = null;
		
		try {
			log.debug("readMetadataFile(), file location : " + f.getAbsolutePath());
			
			br = getBufferedReader(f);
		} catch (IOException e) {
			log.error("error while getting a BufferedReader", e);
			closeBufferedReader(br);
			throw e;
		}
		
		String line = null;
		try {
			while((line = br.readLine()) != null) {
				log.debug("readMetadataFile(), single line read : " + line);
				str.append(line);
			}
		} catch (IOException e) {
			log.error("readMetadataFile(), error while reading: ", e);
			throw e;
		}
		
		closeBufferedReader(br);
		
		return str;
		
	}
	
	/* provide a convenient way to have a BufferedReader */
	public static BufferedReader getBufferedReader(File f) throws IOException {
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		
		return br;
	}
	
	/* provide a convenient way to have a BufferedWriter */
	public static BufferedWriter getBufferedWriter(File f) throws IOException {
		if(f == null) {
			throw new IOException("parameter File object is null");
		}
		
		if(!f.exists()) {
			f.createNewFile();
		}
		
		FileWriter fw = new FileWriter(f);
		BufferedWriter bw = new BufferedWriter(fw);
		
		return bw;
	}
	
	/* provide a convenient way to close a BufferedReader */
	public static void closeBufferedReader(BufferedReader br) {
		if(br != null) {
			try {
				br.close();
			} catch (IOException e) {
				log.error("error while closing a BufferedReader", e);
			}
		}
	}
	
	/* provide a convenient way to close a BufferedWriter */
	public static void closeBufferedWriter(BufferedWriter bw) {
		if(bw != null) {
			try {
				bw.close();
			} catch (IOException e) {
				log.error("error while closing a BufferedWriter", e);
			}
		}
	}
	
	/* pull out json data from stored files and convert into FileMetadata Object */
	public static FileMetadata generateFileMetadataFromJsonString(String jsonStr) {
		ObjectMapper mapper = new ObjectMapper();
		FileMetadata metadata = null;
		
		try {
			metadata = mapper.readValue(jsonStr, FileMetadata.class);
		} catch (JsonParseException e) {
			log.error("error while json parsing ", e);
		} catch (JsonMappingException e) {
			log.error("error while json mapping ", e);
		} catch (IOException e) {
			log.error("error while converting from json string to object ", e);
		}
		
		return metadata;		
	}
	
	//create directories unless existing
	private static void createFolders(File dir) {
		if(dir.exists() && dir.isDirectory()) {
			return;
		}
		dir.mkdirs();
	}
}
