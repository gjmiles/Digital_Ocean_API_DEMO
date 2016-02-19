package downloadManager;
	
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
	private ReadOnlyDoubleProperty sceneHeight;
	private ReadOnlyDoubleProperty sceneWidth;
	private ReadOnlyDoubleProperty paneWidth;
	
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("Download Manager");

			BorderPane borderPane = new BorderPane();
			Scene scene = new Scene(borderPane,800,480);
			OptionBar optionBar = new OptionBar();
			
			sceneHeight = scene.heightProperty();
			sceneWidth = scene.widthProperty();
			
			borderPane.prefHeightProperty().bind(sceneHeight);
			borderPane.prefHeightProperty().bind(sceneWidth);
			paneWidth = borderPane.widthProperty();
			
			final Downloads dl = new Downloads(scene);
			Database db = new Database(scene, dl);
			FileView fv = new FileView(scene);
			
			db.addFile(); //pre-gen data

			HBox center = new HBox();
			center.prefWidthProperty().bind(paneWidth);		
			db.prefWidthProperty().bind(paneWidth.divide(2));
			dl.prefWidthProperty().bind(paneWidth.divide(2));
	        center.getChildren().addAll(db, dl);
			
			borderPane.setTop(optionBar);
			borderPane.setCenter(center);
			borderPane.setBottom(fv);
			
			primaryStage.setScene(scene);
			primaryStage.show();
			Timeline timer = new Timeline(new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
			    @Override
			    public void handle(ActionEvent event) {
			    	dl.refresh();
			        dl.checkQueue();
			    }
			}));
			timer.setCycleCount(Timeline.INDEFINITE);
			timer.play();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
}
