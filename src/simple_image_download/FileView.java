package downloadManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableView;

public class FileView extends TableView{
	private Scene scene;
	public FileView(Scene scene) {
		createDownloadsView();
		this.scene = scene;
	}
	private void createDownloadsView() {
		
		this.setPrefWidth(390);
		this.setPrefHeight(230);
	}
}
