package downloadManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class ScheduleScene {
	boolean selected = false;
	private Scene scene;
	private Data item;
	private LocalDate local;
	private Downloads dl;
	public ScheduleScene(Data item, Downloads dl) {
		createScene();
		this.item = item;
		this.dl = dl;
	}

	private void createScene() {

		GridPane grid = new GridPane();
		this.scene = new Scene(grid, 250, 150);
		grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(25, 25, 25, 25));
		//Submit button
		Button submitBtn = new Button("Submit");

		// Labels needed
		Label dateLabel = new Label("Date: ");
		Label timeLabel = new Label("Time: ");
		Label timeColon = new Label(" : ");
		
		// Hbox for date
		HBox hBoxDate = new HBox();
		hBoxDate.setPrefWidth(450);
		hBoxDate.setPadding(new Insets(10, 0, 0, 0));

		// Hbox for the time
		HBox hBoxTime = new HBox();
		hBoxTime.setPrefWidth(450);
		hBoxTime.setPadding(new Insets(10, 0, 0, 0));
		
		HBox hBoxBtn = new HBox();
		hBoxBtn.setPrefWidth(450);
		hBoxBtn.setPadding(new Insets(10, 0, 0, 50));
		
		final DatePicker datePicker = new DatePicker();
	    datePicker.setEditable( false );
	    datePicker.setFocusTraversable( false );
	    datePicker.setPromptText( "Select date" );
	    datePicker.setPrefWidth(170);

		final ComboBox comboHr;
		final ComboBox comboMin;
		
		ObservableList<String> hrs = FXCollections.observableArrayList();
		for(int i = 0; i < 24; i++) {
			if(i < 10) {
				hrs.add("0"+i);
			}
			else {
				hrs.add(i+"");
			}
		}
		comboHr = new ComboBox(hrs);
		comboHr.setEditable(false);
		comboHr.setPrefWidth(75);
		
		ObservableList<String> mins = FXCollections.observableArrayList();
		for(int i = 0; i < 60; i++) {
			if(i < 10) {
				mins.add("0"+i);
			}
			else {
				mins.add(i+"");
			}
		}
		comboMin = new ComboBox(mins);
		comboMin.setEditable(false);
		comboMin.setPrefWidth(75);

		submitBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				DateFormat df = new SimpleDateFormat("MM/dd/yy H:mm");
				local = datePicker.getValue();
				String date = (local.getMonthValue() + "/" + local.getDayOfMonth() + "/" + local.getYear());
				String time = comboHr.getValue() + ":" + comboMin.getValue();
				Date dt = new Date();
				
				try {
					dt = df.parse(date + " " + time);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				item.setDate(dt);
				item.toggleWaiting();
		    	dl.sendToDownloads(item);
				getScene().getWindow().hide();
			}
		});
		
		hBoxDate.getChildren().addAll(dateLabel, datePicker);
		hBoxTime.getChildren().addAll(timeLabel, comboHr, timeColon, comboMin);
		hBoxBtn.getChildren().addAll(submitBtn);

		grid.add(hBoxDate, 1, 0);
		grid.add(hBoxTime, 1, 1);
		grid.add(hBoxBtn, 1, 2);

	}
	public Scene getScene() {
		return this.scene;
	}
}