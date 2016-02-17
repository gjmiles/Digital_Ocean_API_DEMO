package downloadManager;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.cell.PropertyValueFactory;

public class Data {
	private final SimpleStringProperty fileName;
    private final SimpleStringProperty fileSize;
    private final SimpleStringProperty fileType;
    private final DoubleProperty progress;
    private final SimpleStringProperty fileStatus;
    
    public Data(String fileName, String fileSize, String fileType) {
    	this.fileName = new SimpleStringProperty(fileName);
    	this.fileSize = new SimpleStringProperty(fileSize);
    	this.fileType = new SimpleStringProperty(fileType);
    	this.progress = new SimpleDoubleProperty(0.0);
    	this.fileStatus = new SimpleStringProperty("Downloading");
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
    	this.progress.add(0.1);
    }
    public void setStatus(String status) {
    	this.fileStatus.setValue(status);
    }
    public String getFileStatus() {
    	return fileStatus.get();
    }

}
