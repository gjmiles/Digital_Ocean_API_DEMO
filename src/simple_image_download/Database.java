package downloadManager;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class Database extends VBox {
	private Scene scene;
	TableView<Data> tv;
	ObservableList<Data> list;
	Downloads dl;
	public Database(Scene scene, Downloads dl) {
		tv = new TableView();
		createBrowseView();
		this.scene = scene;
		this.dl = dl;
	}
	private void createBrowseView() {
		tv.setPrefWidth(390);
		tv.setPrefHeight(250);
		tv.setPlaceholder(new Label("No results"));
		TableColumn fileNameCol = new TableColumn("Name");
		fileNameCol.setCellValueFactory(
                new PropertyValueFactory<Data, String>("fileName"));
		TableColumn fileSizeCol = new TableColumn("Size");
		fileSizeCol.setCellValueFactory(
                new PropertyValueFactory<Data, String>("fileSize"));
		TableColumn fileTypeCol = new TableColumn("Type");
		fileTypeCol.setCellValueFactory(
                new PropertyValueFactory<Data, String>("fileType"));
		tv.setOnMousePressed(new EventHandler<MouseEvent>() {
		    @Override 
		    public void handle(MouseEvent event) {
		        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
		        	Data item = list.get(tv.getSelectionModel().getSelectedIndex());
			    	item.setStatus("Downloading");
			    	dl.sendToDownloads(item);                  
		        }
		    }
		});
		final MenuItem downloadItem = new MenuItem("Download");
		downloadItem.setOnAction(new EventHandler<ActionEvent>() {
			    	@Override
			public void handle(ActionEvent event) {
			    	Data item = list.get(tv.getSelectionModel().getSelectedIndex());
			    	item.setStatus("Downloading");
			    	dl.sendToDownloads(item);
		   	}
		});
		tv.setContextMenu(new ContextMenu(downloadItem));
		tv.getColumns().addAll(fileNameCol, fileSizeCol, fileTypeCol);
		
		final Label label = new Label("Database");
		this.setPadding(new Insets(0,0,0,0));
		this.getChildren().addAll(label, tv);
	}
	public void addFile() {
		Data test = new Data("testFileName", "100MB", "png");
		Data test2 = new Data("testFileName2", "200MB", "png");
		list = FXCollections.observableArrayList(test);
		list.add(test2);
		tv.setItems(list);
	}
}
