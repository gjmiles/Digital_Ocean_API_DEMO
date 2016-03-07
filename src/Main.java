package src.downloadManager;
	
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
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
			
			db.populateDB();
			
			
			String dir = ("C:/Users/Andy/Desktop/eclipse437/DownloadManager/src/");
			
			Timer dbTimer = new Timer();
			//Timer to refresh database
			dbTimer.scheduleAtFixedRate(new TimerTask() {
				  public void run() {
					  try {
						  
						db.populateDB();
						} catch (IOException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				  }
				}, 60*1000, 60*1000);
			

			Timer dlTimer = new Timer();
			//Timer to refresh database
			dlTimer.scheduleAtFixedRate(new TimerTask() {
				  public void run() {
						  
						for(int i = 0; i < dl.downloadsList.size(); i++){
							String filename = dl.downloadsList.get(i).getFileName();
							String filetype = dl.downloadsList.get(i).getFileType();
							File fileCheck = new File(dir + filename + "." + filetype);
							if(fileCheck.exists())
							dl.completeDownload(dl.downloadsList.get(i));
						}
				  }
				}, 5*1000, 5*1000);
			
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