package downloadManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Database extends VBox {
	private Scene scene;
	private TextField filterField;
	TableView<Data> tv;
	ObservableList<Data> list;
	Downloads dl;
	public Database(Scene scene, Downloads dl) {
		tv = new TableView();
		list = FXCollections.observableArrayList();
		filterField = new TextField();
		createBrowseView();
		this.scene = scene;
		this.dl = dl;
		
	}
	private void createBrowseView() {
		tv.setPrefWidth(390);
		tv.setPrefHeight(250);
		tv.setPlaceholder(new Label("No results"));
		TableColumn<Data, String> fileNameCol = new TableColumn<Data, String>("Name");
		fileNameCol.setCellValueFactory(
                new PropertyValueFactory<Data, String>("fileName"));
		TableColumn<Data, String> fileSizeCol = new TableColumn<Data, String>("Size");
		fileSizeCol.setCellValueFactory(
                new PropertyValueFactory<Data, String>("fileSize"));
		TableColumn<Data, String> fileTypeCol = new TableColumn<Data, String>("Type");
		fileTypeCol.setCellValueFactory(
                new PropertyValueFactory<Data, String>("fileType"));
		TableColumn<Data, String> fileDateCol = new TableColumn<Data, String>("Date");
		fileDateCol.setCellValueFactory(
                new PropertyValueFactory<Data, String>("fileDate"));
		
		tv.setOnMousePressed(new EventHandler<MouseEvent>() {
		    @Override 
		    public void handle(MouseEvent event) {
		        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
		        	Data item = list.get(tv.getSelectionModel().getSelectedIndex());
			    	item.setStatus("Downloading");
			    	item.setWaiting(false);
			    	dl.sendToDownloads(item);                  
		        }
		    }
		});
		final MenuItem downloadItem = new MenuItem("Download");
		downloadItem.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Data temp = list.get(tv.getSelectionModel().getSelectedIndex());
		    	Data item = new Data(temp.getFileName(), temp.getFileSize(), temp.getFileType(), temp.getFileDate());
			    	item.setStatus("Downloading");
			    	item.setWaiting(false);
			    	dl.sendToDownloads(item);
		   	}
		});
		
		final MenuItem downloadLaterItem = new MenuItem("Download At...");
		downloadLaterItem.setOnAction(new EventHandler<ActionEvent>() {
			    	@Override
			public void handle(ActionEvent event) {
			    	Data temp = list.get(tv.getSelectionModel().getSelectedIndex());
			    	Data item = new Data(temp.getFileName(), temp.getFileSize(), temp.getFileType(), temp.getFileDate());
			    	item.setStatus("Downloading At");
			    	ScheduleScene scene = new ScheduleScene(item, dl);
			    	Popup pop = new Popup("Download At...", scene.getScene());
			    	pop.show();
		   	}
		});
		
		Button dlBtn = new Button("Download");
		dlBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Data temp = list.get(tv.getSelectionModel().getSelectedIndex());
		    	Data item = new Data(temp.getFileName(), temp.getFileSize(), temp.getFileType(), temp.getFileDate());
		    	item.setWaiting(false);
		    	dl.sendToDownloads(item);
			}
		});
		
		Button dlAtBtn = new Button("Download At...");
		dlAtBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Data temp = list.get(tv.getSelectionModel().getSelectedIndex());
		    	Data item = new Data(temp.getFileName(), temp.getFileSize(), temp.getFileType(), temp.getFileDate());
		    	item.setStatus("Downloading At");
		    	ScheduleScene scene = new ScheduleScene(item, dl);
		    	Popup pop = new Popup("Download At...", scene.getScene());
		    	pop.show();
			}
		});
		FilteredList<Data> filteredData = new FilteredList<Data>(list, p -> true);
		
		filterField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(item -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				
				if (item.getFileName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} 
				else if (item.getFileSize().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} 
				else if (item.getFileType().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}
				return false;
			    });
		    });
		
		SortedList<Data> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tv.comparatorProperty());
		
		tv.setItems(sortedData);
		tv.setContextMenu(new ContextMenu(downloadItem,downloadLaterItem));
		tv.getColumns().addAll(fileNameCol, fileSizeCol, fileTypeCol, fileDateCol);
		
		final Label label = new Label("Database");
		this.setPadding(new Insets(0,5,5,5));
		HBox hBox = new HBox(5);
		hBox.getChildren().addAll(label, dlBtn, dlAtBtn, filterField);
		this.getChildren().addAll(hBox, tv);
	}
	
	public void addFile() {
		Data test = new Data("testFileName", "100MB", "png", "01-01-2016");
		Data test2 = new Data("testFileName2", "200MB", "png", "01-01-2016");
		Data test3 = new Data("mars", "123MB", "png","02-15-2015");
		Data test4 = new Data("moon", "250MB", "tif","03-02-2013");
		Data test5 = new Data("earth", "1GB", "jpg","04-09-2014");
		Data test6 = new Data("pluto", "10MB", "jpg","05-20-2012");
		Data test7 = new Data("textFile", "1KB", "txt","06-10-2011");
		list.addAll(test,test2,test3,test4,test5,test6,test7);
	}
}
