package vc.voice;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class VoiceCheckMain extends Application {
	@Override
	public void start(Stage primaryStage) {

		primaryStage.setTitle("VoiceChecker");

		Scene scene = new Scene(new VoiceCheckController());

		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	          public void handle(WindowEvent we) {
	        	  VoiceCheckController.sysclose();
	        	  
	              System.out.println("Stage is closing");
	              System.exit(0);
	          }
	      });     
		
	}

	public static void main(String[] args) {
		
		launch(args);
	}
}
