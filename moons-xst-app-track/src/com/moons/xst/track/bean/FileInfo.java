/**
 * 
 */
package com.moons.xst.track.bean;

/**
 * @author LKZ
 * 
 */
public class FileInfo extends Entity {
	private int FileID;// ID

	public int getFileID() {
		return FileID;
	}

	public void setFileID(int FileID) {
		this.FileID = FileID;
	}

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

	/**
	 * 
	 * 
	 */
	public void setFileType(String fileType) {
		this.FileType = fileType;
	}
}
