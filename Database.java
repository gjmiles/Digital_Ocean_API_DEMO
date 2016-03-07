package src.downloadManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
	private TableView<Data> tv;
	private ObservableList<Data> list;
	private ObservableList<Data> filteredList;
	Downloads dl;
	public Database(Scene scene, Downloads dl) {
		tv = new TableView();
		list = FXCollections.observableArrayList();
		filteredList = FXCollections.observableArrayList();
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
		    	Data item = new Data(temp.getId(), temp.getFileName(), temp.getFileSize(), temp.getFileType(), temp.getFileDate());
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
			    	Data item = new Data(temp.getId(), temp.getFileName(), temp.getFileSize(), temp.getFileType(), temp.getFileDate());
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
		    	Data item = new Data(temp.getId(), temp.getFileName(), temp.getFileSize(), temp.getFileType(), temp.getFileDate());
		    	item.setWaiting(false);
		    	dl.sendToDownloads(item);
			}
		});
		
		Button dlAtBtn = new Button("Download At...");
		dlAtBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Data temp = list.get(tv.getSelectionModel().getSelectedIndex());
		    	Data item = new Data(temp.getId(), temp.getFileName(), temp.getFileSize(), temp.getFileType(), temp.getFileDate());
		    	item.setStatus("Downloading At");
		    	ScheduleScene scene = new ScheduleScene(item, dl);
		    	Popup pop = new Popup("Download At...", scene.getScene());
		    	pop.show();
			}
		});
		
		tv.setItems(filteredList);
		filterField.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				filteredList.clear();
				for(Data d : list) {
					if(matches(d)) {
						filteredList.add(d);
					}
				}
				ArrayList<TableColumn<Data, ?>> order = new ArrayList<>(tv.getSortOrder());
		        tv.getSortOrder().clear();
		        tv.getSortOrder().addAll(order);
			}
		});
		tv.setContextMenu(new ContextMenu(downloadItem,downloadLaterItem));
		tv.getColumns().addAll(fileNameCol, fileSizeCol, fileTypeCol, fileDateCol);
		
		final Label label = new Label("Database");
		this.setPadding(new Insets(0,5,5,5));
		HBox hBox = new HBox(5);
		hBox.getChildren().addAll(label, dlBtn, dlAtBtn, filterField);
		this.getChildren().addAll(hBox, tv);
	}
	
	public Boolean matches(Data d) {
		String filterInput = filterField.getText();
		String lowerCaseFilter = filterInput.toLowerCase();
		if(filterInput == null || filterInput.isEmpty()) {
			return true;
		}	
		if (d.getFileName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
			return true;
		}
		else if (d.getFileType().toLowerCase().indexOf(lowerCaseFilter) != -1) {
			return true;
		}
		else if (d.getFileDate().toLowerCase().indexOf(lowerCaseFilter) != -1) {
			return true;
		}
		return false;
	}
	
	
	public void populateDB() throws IOException, JSONException {
		
		list.clear();
		filteredList.clear();
		String url = "http://104.131.152.196:8080/do_servlet_controller/access?all";
		
		
		    URL download_url = new URL(url);
		    Scanner scan = new Scanner(download_url.openStream());
		    String str = new String();
		    while (scan.hasNext())
		        str += scan.nextLine();
		    scan.close();
		    //str = str.replaceAll("[\\[\\]]","");
		    //System.out.print(str);
		 
		    //Grabs metadata for Image array
		    /*
		    JSONObject obj = new JSONObject(str);
		    JSONArray res = obj.getJSONArray("Images");
		    for (int i = 0; i < res.length(); ++i) {
		    JSONObject imageMD = res.getJSONObject(i);
		    System.out.println(imageMD.getString("id") + " " + imageMD.getString("filename") + " " +
		    		imageMD.getDouble("filesize") + " " + imageMD.getString("filetype") + " " +
		    		imageMD.getString("filedate"));
		    
		    Data image = new Data(imageMD.getString("filename"), imageMD.getDouble("filesize"),
		    		imageMD.getString("filetype"), imageMD.getString("filedate"));
			list.addAll(image);
			filteredList.addAll(list);
		    
		    }
		    */
		    JSONArray res = new JSONArray(str);
		    //System.out.println(metadata.length());
		    for(int i = 0;i < res.length();i++)
		    {
		    	JSONObject metadata = res.getJSONObject(i);
		    	/*JSONObject metadata = new JSONObject(i);
		    	//System.out.println(metadata.length());*/
		    	String id = metadata.getString("id");
		    	String filename = metadata.getString("filename");
		    	double filesize = metadata.getInt("filesize");
		    	String filetype = metadata.getString("filetype");
		    	String filedate = metadata.getString("filedate");
		    	String fileLink = metadata.getString("link");
		    	Data image = new Data(id, filename, String.valueOf(filesize), filetype, filedate);
		    	image.setLink(fileLink);
		    	list.add(image);
		    }
	    	filteredList.addAll(list);
	}
}