package downloadManager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Popup extends Stage {
	
	public Popup(String title, Scene scene) {
		
		// Sets the title and scene
		this.setTitle(title);
		this.setScene(scene);
		
	}
}
