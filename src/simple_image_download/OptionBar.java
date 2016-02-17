package downloadManager;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class OptionBar extends MenuBar {
	public OptionBar() {
		createOptionBar();
	}
	public void createOptionBar() {
		Menu menuFile = new Menu("File");
		Menu menuSettings = new Menu("Settings");
		
		MenuItem exitItem = new MenuItem("Exit");
		MenuItem settingsItem = new MenuItem("Download Options");
	    settingsItem.setOnAction(new EventHandler<ActionEvent>() {
	            public void handle(ActionEvent t) {
	            	SettingsScene settingsScene = new SettingsScene();
	            	Popup pop = new Popup("Download Options", settingsScene.getScene());
	            	pop.show();
	            	
	            }
	    });
	    exitItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
            	System.exit(0);
            }
    }); 
	    menuFile.getItems().addAll(exitItem);
	    menuSettings.getItems().addAll(settingsItem);
		this.getMenus().addAll(menuFile, menuSettings);
		
	}
	
}
