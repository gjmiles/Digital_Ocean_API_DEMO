package downloadManager;

import java.util.Calendar;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Downloads extends VBox{
	private Scene scene;
	TableView<Data> tv;
	ObservableList<Data> downloadsList = FXCollections.observableArrayList();
	public Downloads(Scene scene) {
		tv = new TableView<Data>();
		createDownloadsView();
		this.scene = scene;
	}
	private void createDownloadsView() {
		tv.setPrefWidth(390);
		tv.setPrefHeight(250);
		tv.setPlaceholder(new Label("No current downloads"));
		
		//table columns
		TableColumn<Data, String> fileNameCol = new TableColumn<Data, String>("Name");
		fileNameCol.setCellValueFactory(
                new PropertyValueFactory<Data, String>("fileName"));
		TableColumn<Data, String> fileSizeCol = new TableColumn<Data, String>("Size");
		fileSizeCol.setCellValueFactory(
                new PropertyValueFactory<Data, String>("fileSize"));
		TableColumn<Data, Double> fileProgressCol = new TableColumn<Data, Double>("Progress");
		fileProgressCol.setCellValueFactory(
                new PropertyValueFactory<Data, Double>("progress"));
		fileProgressCol.setCellFactory(ProgressBarTableCell.<Data>forTableColumn());
		TableColumn<Data, String> fileStatusCol = new TableColumn<Data, String>("Status");
		fileStatusCol.setCellValueFactory(
                new PropertyValueFactory<Data, String>("fileStatus"));
		tv.getColumns().addAll(fileNameCol, fileSizeCol, fileProgressCol, fileStatusCol);
		
		//start all downloads currently in "stopped"
		Button startAllBtn = new Button("Start All");
		startAllBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				for(int i = 0; i < downloadsList.size(); i++) {
					//if file is not scheduled for later and has not yet stopped then start download
					//loop for each item in downloads view
					if(downloadsList.get(i).isWaiting() == false && downloadsList.get(i).getFileStatus() == "Stopped") {
						downloadsList.get(i).startDownload();
						refresh();
					}
				}
				
			}
		});
		
		//stop all downloads currently in "downloading"
		Button stopAllBtn = new Button("Stop All");
		stopAllBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				for(int i = 0; i < downloadsList.size(); i++) {
					//if file is not scheduled for later but is currently downloading then stop download
					//loop for each item in downloads view
					if(downloadsList.get(i).isWaiting() == false && downloadsList.get(i).getFileStatus().equals("Downloading") == true) {
						downloadsList.get(i).stopDownload();
						refresh();
					}
				}
				
			}
		});
		
		//start the highlighted download regardless of scheduled time or stopped
		Button startBtn = new Button("Start");
		startBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//if not downloading then start, scheduled or not
				if(downloadsList.get(tv.getSelectionModel().getSelectedIndex()).getFileStatus().equals("Downloading") == false) {
					downloadsList.get(tv.getSelectionModel().getSelectedIndex()).startDownload();
					refresh();
				}
			}
		});
		
		//stop the highlighted download if "downloading"
		Button stopBtn = new Button("Stop");
		stopBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//if file is not scheduled for later and is currently downloading then stop download
				if(downloadsList.get(tv.getSelectionModel().getSelectedIndex()).isWaiting() == false && downloadsList.get(tv.getSelectionModel().getSelectedIndex()).getFileStatus().equals("Downloading") == true) {
					downloadsList.get(tv.getSelectionModel().getSelectedIndex()).stopDownload();
					refresh();
				}
			}
		});
		
		//remove download from download view
		Button removeBtn = new Button("Remove");
		removeBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				downloadsList.get(tv.getSelectionModel().getSelectedIndex()).stopDownload();
				downloadsList.remove(tv.getSelectionModel().getSelectedIndex());
			}
		});
		
		final Label label = new Label("Downloads");
		HBox hbox = new HBox(5);
		this.setPadding(new Insets(0,5,5,5));
		hbox.getChildren().addAll(label, startAllBtn, stopAllBtn, startBtn, stopBtn, removeBtn);
		this.getChildren().addAll(hbox,tv);
		tv.setItems(downloadsList);
			
	}
	public void sendToDownloads(Data item) {
		downloadsList.add(item);
		
	}
	public void startDownload(Data item) {
		item.setStatus("Downloading");
		//Add start download stuff here
	}
	//every 5 seconds from the Timer in main
	//this is called to check if a scheduled item has passed scheduled time, start download if so
	public void checkQueue() {
		for(int i = 0; i < downloadsList.size(); i++) {
			if(downloadsList.get(i).isWaiting()) {
				Calendar cal = Calendar.getInstance();
				Date dt = downloadsList.get(i).getScheduledDate();
				cal.setTime(dt);
				if(Calendar.getInstance().getTimeInMillis() > cal.getTimeInMillis()) {
					downloadsList.get(i).startDownload();
				}
			}
		}
	}
	//refreshes the view
	public void refresh() {
		tv.getColumns().get(0).setVisible(false);
		tv.getColumns().get(0).setVisible(true);
	}
}
