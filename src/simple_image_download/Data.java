package downloadManager;

import java.util.Date;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.net.URL

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
	this.download_file();
    }
	public void stopDownload() {
		this.fileStatus.set("Stopped");
		
	}

    public int download_file()
    {	
	rest_api_ip = "localhost:8080";
	rest_api_url = "http://" + rest_api_ip + "/download_manager/GetImageServlet?name=" + this.fileName;

	try{
	    URL download_url = new URL(rest_api_url);
	    InputStream attempt_connect = download_url.openStream();
	    int in_bytes = attempt_connect.read();

	    File save_file =  new File(this.fileName);
	    OutputStream save_stream = new FileOutputStream(save_file);
	    byte[] buffer = new byte[8*1024];
	    int bytes_read = 0;
	    int remeber_download = 0;
	    
	    while((bytes_read = attempt_connect.read(buffer)) != -1)
		{
		    save_stream.write(buffer,remeber_download,bytes_read);
		}
	    attempt_connect.close();
	    save_stream.close();
	}
	catch (MalformedURLException e){
	    e.printStackTrace();
	}
    }
}
