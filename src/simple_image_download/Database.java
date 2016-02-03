package downloadManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class Database extends VBox {
	private Scene scene;
	TableView tv;
	public Database(Scene scene) {
		tv = new TableView();
		createBrowseView();
		this.scene = scene;
		
	}
	private void createBrowseView() {
		tv.setPrefWidth(390);
		tv.setPrefHeight(250);

		TableColumn fileNameCol = new TableColumn("Name");
		fileNameCol.setCellValueFactory(
                new PropertyValueFactory<Data, String>("fileName"));
		TableColumn fileSizeCol = new TableColumn("Size");
		fileSizeCol.setCellValueFactory(
                new PropertyValueFactory<Data, String>("fileSize"));
		TableColumn fileTypeCol = new TableColumn("Type");
		fileTypeCol.setCellValueFactory(
                new PropertyValueFactory<Data, String>("fileType"));
		
		tv.getColumns().addAll(fileNameCol, fileSizeCol, fileTypeCol);
		
		final Label label = new Label("Database");
		this.setPadding(new Insets(0,0,0,0));
		this.getChildren().addAll(label, tv);
	}
	public void addFile() {
		Data test = new Data("testFileName", "png", "100MB");
		Data test2 = new Data("testFileName2", "png2", "200MB");
		ObservableList<Data> list =
		        FXCollections.observableArrayList(test,test2);
		tv.setItems(list);
	}
}
