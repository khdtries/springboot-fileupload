package com.springboot.fileupload.model;

import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * It preserves File Meta Data.
 * used also in converting json to object and vice versa
 *
 */

@JsonIgnoreProperties(value={"sysFilename"})
public class FileMetadata {
	private long id;
	private String provider;
	private String desc;
	private Date date;
	private String filename;
	private String sysFilename;
	
	public FileMetadata() {
		this.setId();
		setDate(new Date(this.getId()));
	}
	
	public long getId() {
		return id;
	}
	public void setId() {
		this.id = createUniqueId();	
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
		setSysFilename(filename);
	}
	private long createUniqueId() {
		return Calendar.getInstance().getTimeInMillis();
	}
	
	public String getSysFilename() {
		return sysFilename;
	}

	private void setSysFilename(String sysFilename) {
		this.sysFilename = getId() + sysFilename;
	}
}
