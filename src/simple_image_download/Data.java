package downloadManager;

import java.util.Date;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Data {
	private SimpleStringProperty fileName;
    private SimpleStringProperty fileSize;
    private SimpleStringProperty fileType;
    private DoubleProperty progress;
    private SimpleStringProperty fileStatus;
    private Boolean waiting = false;
    private Date scheduledDate;
    
    public Data(String fileName, String fileSize, String fileType) {
    	this.fileName = new SimpleStringProperty(fileName);
    	this.fileSize = new SimpleStringProperty(fileSize);
    	this.fileType = new SimpleStringProperty(fileType);
    	this.progress = new SimpleDoubleProperty(0.0);
    	this.fileStatus = new SimpleStringProperty("Downloading");
    	this.scheduledDate = new Date();
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
    public Boolean isWaiting() {
    	return waiting;
    }
    public void toggleWaiting() {
    	if(waiting == true) {
    		waiting = false;
    		this.fileStatus.set("Downloading");
    	}
    	else {
    		waiting = true;
    	}
    }
    public Date getScheduledDate() {
    	return scheduledDate;
    }
    public void setDate(Date date) {
    	this.scheduledDate = date;
    	this.fileStatus.set("Start at "+ getScheduledDate());
    }
    public void startDownload() {
    	this.toggleWaiting();
    }
	public void stopDownload() {
		this.fileStatus.set("Stopped");
		
	}
}
