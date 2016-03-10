package src.downloadManager;

import java.util.Date;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;


public class Data {
	private SimpleStringProperty fileName;
    private SimpleStringProperty fileSize;
    private SimpleStringProperty fileType;
    private SimpleStringProperty fileDate;
    private DoubleProperty progress;
    private SimpleStringProperty fileStatus;
    private Boolean waiting = false;
    private Date scheduledDate;
    private String Link;
    
    private download_handler downloader;
    //private String rest_api_ip;
    //private String rest_api_url;
    
    public Data(String fileName, String fileSize, String fileType, String fileDate, String link) {
    	this.fileName = new SimpleStringProperty(fileName);
    	this.fileSize = new SimpleStringProperty(fileSize);
    	this.fileType = new SimpleStringProperty(fileType);
    	this.fileDate = new SimpleStringProperty(fileDate);
    	this.progress = new SimpleDoubleProperty(0.0);
    	this.fileStatus = new SimpleStringProperty("Downloading");
    	this.Link = link;
    	this.scheduledDate = new Date();
    	this.downloader = new download_handler();
    }
    public String getFileName() {
    	return fileName.get();
    }
    public String getFileSize() {
    	return fileSize.get();
    }
    public String getFileType() {
    	return fileType.get();
    }
    public void setProgress() {
    	this.progress.set(0.1);
    }
    public void setStatus(String status) {
    	this.fileStatus.setValue(status);
    }
    public String getFileStatus() {
    	return fileStatus.get();
    }
    public String getFileDate() {
    	return fileDate.get();
    }
    public void setFileDate(String date) {
    	this.fileDate.set(date);
    }
    public Boolean isWaiting() {
    	return waiting;
    }
    public void setWaiting(Boolean wait) {
    	this.waiting = wait;
    }
    public Date getScheduledDate() {
    	return scheduledDate;
    }
    public download_handler getDownloadHandler(){
    	return this.downloader;
    }
    public void setDate(Date date) {
    	this.scheduledDate = date;
    	this.fileStatus.set("Start at "+ getScheduledDate());
    }
    public void startDownload() {
    	this.fileStatus.set("Downloading");
    	//System.out.println("Are we downloading???");
    	//downloader.download_file("another_acoustic_coupler.jpg","104.131.146.106:8080");
    	System.out.println("File link: " + this.Link);
    	downloader.download_file(this.fileName.getValue() + "." + this.fileType.getValue(), this.Link );
    }
	public void stopDownload() {
		this.fileStatus.set("Stopped");
		
	}
	public String getLink() {
		return Link;
	}
	public void setLink(String link) {
		Link = link;
	}

}
