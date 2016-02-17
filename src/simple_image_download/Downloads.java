package downloadManager;

import java.io.File;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class Downloads<ActionEvent> extends VBox{
	private Scene scene;
	TableView tv;
	ObservableList<Data> downloadsList = FXCollections.observableArrayList();
	public Downloads(Scene scene) {
		tv = new TableView();
		createDownloadsView();
		this.scene = scene;
	}
	private void createDownloadsView() {
		tv.setPrefWidth(390);
		tv.setPrefHeight(250);
		tv.setPlaceholder(new Label("No current downloads"));
		TableColumn fileNameCol = new TableColumn("Name");
		fileNameCol.setCellValueFactory(
                new PropertyValueFactory<Data, String>("fileName"));
		TableColumn fileSizeCol = new TableColumn("Size");
		fileSizeCol.setCellValueFactory(
                new PropertyValueFactory<Data, String>("fileSize"));
		TableColumn fileProgressCol = new TableColumn("Progress");
		fileProgressCol.setCellValueFactory(
                new PropertyValueFactory<Data, Double>("progress"));
		fileProgressCol.setCellFactory(ProgressBarTableCell.<Data>forTableColumn());
		TableColumn fileStatusCol = new TableColumn("Status");
		fileStatusCol.setCellValueFactory(
                new PropertyValueFactory<Data, String>("fileStatus"));
		tv.getColumns().addAll(fileNameCol, fileSizeCol, fileProgressCol, fileStatusCol);
		
		final Label label = new Label("Downloads");
		this.setPadding(new Insets(0,0,0,0));
		this.getChildren().addAll(label,tv);
		tv.setItems(downloadsList);
			
	}
	
	public void sendToDownloads(Data item) {
		downloadsList.add(item);
		
	}
}
