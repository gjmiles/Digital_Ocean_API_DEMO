package downloadManager;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;

public class SettingsScene {
	boolean selected = false;
	private Scene scene;

	public SettingsScene() {
		createScene();
		addActions(this.scene);
	}

	private void addActions(Scene scene2) {
		// TODO Auto-generated method stub

	}

	private void createScene() {

		FlowPane root = new FlowPane(Orientation.HORIZONTAL, 5, 5);
		root.setPadding(new Insets(5));
		this.scene = new Scene(root, 450, 150);
		final String userDirectoryString = System.getProperty("user.home");

		// directory button
		Button btnOpenDirectoryChooser = new Button("...");
		GridPane.setConstraints(btnOpenDirectoryChooser, 1, 0);

		// Labels needed
		Label Dsl = new Label("Down speed limit (KB/sec):");

		Label Destination = new Label("Destination directory: ");
		Destination.setPadding(new Insets(5, 20, 0, 0));

		// Define Textfieds
		final TextField DestTxt = new TextField();
		DestTxt.setText(userDirectoryString + "/Downloads");
		DestTxt.setPrefWidth(220);

		// Hbox for directory
		HBox hBox = new HBox();
		hBox.setPrefWidth(450);
		hBox.setPadding(new Insets(10, 0, 0, 0));
		// hBox.setStyle("-fx-background-color: BLACK;");

		// Hbox for the download speed limit
		HBox speedBox = new HBox(8);
		speedBox.setPrefWidth(350);
		speedBox.setPadding(new Insets(25, 12, 15, 0));

		// Declaring the spinners
		Spinner spinnerUp = new Spinner();

		spinnerUp.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000));
		spinnerUp.setEditable(true);
		spinnerUp.setPrefWidth(75);

		Spinner spinnerD = new Spinner();

		spinnerD.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000));
		spinnerD.setEditable(true);
		spinnerD.setPrefWidth(75);

		Spinner activeDspin = new Spinner();

		activeDspin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000));
		activeDspin.setEditable(true);
		activeDspin.setPrefWidth(75);


		// Button action, let user choose directory
		btnOpenDirectoryChooser.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				DirectoryChooser directoryChooser = new DirectoryChooser();
				directoryChooser.setInitialDirectory(new File(userDirectoryString + "/Downloads"));
				File selectedDirectory = directoryChooser.showDialog(null);

				if (selectedDirectory == null) {
					DestTxt.setText("No Directory selected");
				} else {
					DestTxt.setText(selectedDirectory.getAbsolutePath());
				}
			}
		});

		// add each node respectively
		hBox.getChildren().add(Destination);
		hBox.getChildren().addAll(DestTxt, btnOpenDirectoryChooser);
		speedBox.getChildren().addAll(Dsl, spinnerUp);

		// Set the nodes to the root to display
		root.getChildren().add(hBox);
		root.getChildren().add(speedBox);

	}

	public Scene getScene() {
		return this.scene;
	}
}