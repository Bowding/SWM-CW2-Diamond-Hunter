package com.neet.DiamondHunter.Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
			
			viewerStage.setScene(scene);
			viewerStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
