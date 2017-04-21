package hu.bme.mit.inf.symod.homework.generic.app;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ChessClock extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			Pane page = (Pane) FXMLLoader.load(ChessClock.class.getResource("ChessClockDesign.fxml"));
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Chess Clock");
            primaryStage.show();
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent arg0) {
					System.exit(0);
				} 
			});
        } catch (Exception ex) {
            Logger.getLogger(ChessClock.class.getName()).log(Level.SEVERE, null, ex);
        } 
	}

	public static void main(String[] args) {
		launch(args);
	}
}
