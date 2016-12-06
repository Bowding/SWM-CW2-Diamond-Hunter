package com.neet.DiamondHunter.Application;

import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;

public class MapViewer extends Application{

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage viewerStage){
		try {
			viewerStage.setTitle("Map Viewer");
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,640,700);
			viewerStage.setResizable(false);
			viewerStage.sizeToScene();
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Parent content = FXMLLoader.load(getClass().getClassLoader().getResource("map.fxml"));
			root.setCenter(content);
			
			viewerStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			    @Override
			    public void handle(WindowEvent event) {

			        // consume event
			        event.consume();

			        // show close dialog
			        Alert alert = new Alert(AlertType.CONFIRMATION);
			        alert.setTitle("Close Confirmation");
			        alert.setHeaderText("Do you really want to quit?");
			        alert.initOwner(viewerStage);
			        alert.setContentText("Your settings will not be saved if exit now : )");

			        Optional<ButtonType> result = alert.showAndWait();
			        if (result.get() == ButtonType.OK){
			            Platform.exit();
			        }
			    }
			});
			
			viewerStage.setScene(scene);
			viewerStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
