/**
 * 
 */
package com.moons.xst.track.bean;

public class XSTFileInfo extends Entity {

	private String FilePath;// 文件路径

	public String getFilePath() {
		return FilePath;
	}

	public void setFilePath(String path) {
		this.FilePath = path;
	}

	private String FileName;// 文件名称

	public String getFileName() {
		return FileName;
	}

	public void setFileName(String fileName) {
		this.FileName = fileName;
	}

	private String FileDesc;// 描述

	public String getFileDesc() {
		return FileDesc;
	}

	public void setFileDesc(String desc) {
		this.FileDesc = desc;
	}

	private String FileTime;// 时间

	public String getFileTime() {
		return FileTime;
	}

	public void setFileTime(String todotime) {
		this.FileTime = todotime;
	}

	private String FileType;// 类型

	/**
	 *
	 * 
	 */
	public String getFileType() {
		return this.FileType;
	}

	public void setFileType(String fileType) {
		this.FileType = fileType;
	}
	
	private String FileLen;// 大小

	public String getFileLen() {
		return this.FileLen;
	}

	public void setFileLen(String fileLen) {
		this.FileLen = fileLen;
	}
	
	private boolean DirectoryYN;
	public boolean getDirectoryYN() {
		return this.DirectoryYN;
	}
	public void setDirectoryYN(boolean directoryYN) {
		this.DirectoryYN = directoryYN;
	}
	
	private String SubDirectoryCount;
	public String getSubDirectoryCount() {
		return this.SubDirectoryCount;
	}
	public void setSubDirectoryCount(String subDirectoryCount) {
		this.SubDirectoryCount = subDirectoryCount;
	}
	
	private String SubFileCount;
	public String getSubFileCount() {
		return this.SubFileCount;
	}
	public void setSubFileCount(String subFileCount) {
		this.SubFileCount = subFileCount;
	}
}
