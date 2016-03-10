package src.downloadManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class FileView extends VBox{
	private Scene scene;
	TableView tv;
	public FileView(Scene scene) {
		tv = new TableView();
		createFileView();
		
		this.scene = scene;
	}
	private void createFileView() {
		tv.setPrefWidth(390);
		tv.setPrefHeight(250);
	}
}
