package downloadManager;
	
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("Download Manager");

			BorderPane borderPane = new BorderPane();
			Scene scene = new Scene(borderPane,800,480);
			OptionBar optionBar = new OptionBar();
			
			Downloads dl = new Downloads(scene);
			Database db = new Database(scene, dl);
			
			FileView fv = new FileView(scene);
			
			db.addFile(); //pre-gen data

			borderPane.setTop(optionBar);
			borderPane.setLeft(db);
			borderPane.setRight(dl);
			borderPane.setBottom(fv);
			
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
