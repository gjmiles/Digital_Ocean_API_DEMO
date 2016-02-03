package downloadManager;

import javafx.beans.property.SimpleStringProperty;

public class Data {
	private final SimpleStringProperty fileName;
    private final SimpleStringProperty fileSize;
    private final SimpleStringProperty fileType;
    
    public Data(String fileName, String fileSize, String fileType) {
    	this.fileName = new SimpleStringProperty(fileName);
    	this.fileSize = new SimpleStringProperty(fileSize);
    	this.fileType = new SimpleStringProperty(fileType);
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

}
