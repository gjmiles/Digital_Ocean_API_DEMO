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

public class Downloads extends VBox{
	private Scene scene;
	TableView tv;
	public Downloads(Scene scene) {
		tv = new TableView();
		createDownloadsView();
		this.scene = scene;
	}
	private void createDownloadsView() {
		tv.setPrefWidth(390);
		tv.setPrefHeight(250);
		
		TableColumn fileNameCol = new TableColumn("Name");
		fileNameCol.setCellValueFactory(
                new PropertyValueFactory<Data, String>("fileName"));
		TableColumn fileSizeCol = new TableColumn("Size");
		fileSizeCol.setCellValueFactory(
                new PropertyValueFactory<Data, String>("fileSize"));
		TableColumn fileProgressCol = new TableColumn("Progress");
		tv.getColumns().addAll(fileNameCol, fileSizeCol, fileProgressCol);
		
		final Label label = new Label("Downloads");
		this.setPadding(new Insets(0,0,0,0));
		this.getChildren().addAll(label,tv);
	}
}
