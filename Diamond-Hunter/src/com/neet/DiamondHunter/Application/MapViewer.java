/**
 * Entry of the Map Viewer application
 * Able to load a predefined map, and allow user to set position of items
 * @author psynw1 (Ning WANG)
 */

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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;

public class MapViewer extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage viewerStage){
		try {
			viewerStage.setTitle("Diamond Hunter - Map Viewer");
			BorderPane root = new BorderPane();
			
			//create a scene of appropriate size
			Scene scene = new Scene(root,640,700);
			viewerStage.setResizable(false);
			viewerStage.sizeToScene();
			
			//connect with .css and .fxml file
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Parent content = FXMLLoader.load(getClass().getClassLoader().getResource("map.fxml"));
			root.setCenter(content);
			
			//assign new handler to close window event: close confirmation alert
			viewerStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			    @Override
			    public void handle(WindowEvent event) {

			        //consume event
			        event.consume();

			        //set alert content
			        Alert alert = new Alert(AlertType.CONFIRMATION);
			        alert.setTitle("Close Confirmation");
			        alert.setHeaderText("Do you really want to quit?");
			        alert.initOwner(viewerStage);
			        alert.setContentText("Your settings will not be saved if exit now : )");

			        //set default button to "cancel" instead of "OK"
			        Button yesButton = (Button) alert.getDialogPane().lookupButton( ButtonType.OK );
			        yesButton.setDefaultButton( false );

			        Button noButton = (Button) alert.getDialogPane().lookupButton( ButtonType.CANCEL );
			        noButton.setDefaultButton( true );

			        //show alert and check user option
			        Optional<ButtonType> result = alert.showAndWait();
			        if (result.get() == ButtonType.OK){
			            Platform.exit();
			        }
			    }
			});
			
			//set and display scene
			viewerStage.setScene(scene);
			viewerStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
