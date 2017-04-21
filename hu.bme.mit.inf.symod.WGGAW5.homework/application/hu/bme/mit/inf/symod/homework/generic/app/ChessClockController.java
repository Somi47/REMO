package hu.bme.mit.inf.symod.homework.generic.app;

import hu.bme.mit.inf.symod.homework.generic.tests.ReflectiveTimeInterfaceProvider;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.yakindu.scr.chessclockwggaw5.ChessClockWGGAW5Statemachine;
import org.yakindu.scr.chessclockwggaw5.IChessClockWGGAW5Statemachine.SCIBeeperOperationCallback;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class ChessClockController {
    @FXML
    private Button startResetButton;
    @FXML
    private Button whiteIncrementButton;
    @FXML
    private Button modeButton;
    @FXML
    private Button blackDecrementButton;
    @FXML
    private ImageView whistle;
    @FXML
    private Label mainLabel;
    @FXML
    private Pane scene;
    @FXML
    private Label whiteLabel;
    @FXML
    private Label blackLabel;
    
    ChessClockWGGAW5Statemachine chessClock = new ChessClockWGGAW5Statemachine();	
    
    @FXML
    void initialize() throws UnsupportedEncodingException {
        final AudioClip pfiff = new AudioClip(ChessClock.class.getResource("Whistle.wav").toString());
        
        final Animation animation = new Transition() {
            {
            	setDelay(Duration.millis(600));
            	setCycleDuration(Duration.millis(10));
            }
        
            protected void interpolate(double frac) {
            	whistle.opacityProperty().setValue(1-frac);
            }
        
        };
        
    	chessClock.getSCIBeeper().setSCIBeeperOperationCallback(new SCIBeeperOperationCallback() {
    		@Override public void beep() {
    			pfiff.play();
    			whistle.opacityProperty().setValue(1);
    			animation.play();
    		}
    	});
    	ReflectiveTimeInterfaceProvider.setTimer(chessClock, ReflectiveTimeInterfaceProvider.providePhysicsTimer());
    	
        chessClock.init();
		chessClock.enter();
				        
        whiteIncrementButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e) {
				chessClock.getSCIButtons().raiseWhiteButton();
				chessClock.runCycle();
			}      	
        });
        
        blackDecrementButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e) {
				chessClock.getSCIButtons().raiseBlackButton();
				chessClock.runCycle();
			}      	
        });
        
        modeButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e) {
				chessClock.getSCIButtons().raiseModeButton();
				chessClock.runCycle();
			}      	
        });        
        
        startResetButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e) {
				chessClock.getSCIButtons().raiseStartButton();
				chessClock.runCycle();
			}      	
        });

        final SimpleLongProperty dynamicWhiteProperty = new SimpleLongProperty();           
        whiteLabel.textProperty().bind(
        		dynamicWhiteProperty.divide(60).asString("%02d").concat(":")
        		.concat(dynamicWhiteProperty.subtract(dynamicWhiteProperty.divide(60).multiply(60)).asString("%02d")));
        whiteLabel.visibleProperty().bind(Bindings.lessThanOrEqual(0, dynamicWhiteProperty));
        whiteLabel.setFont(Font.loadFont(URLDecoder.decode(ChessClock.class.getResource("REGISTER.TTF").toExternalForm(),"utf-8"),45));
               
        final SimpleLongProperty dynamicBlackProperty = new SimpleLongProperty();           
        blackLabel.textProperty().bind(
        		dynamicBlackProperty.divide(60).asString("%02d").concat(":")
        		.concat(dynamicBlackProperty.subtract(dynamicBlackProperty.divide(60).multiply(60)).asString("%02d")));
        blackLabel.visibleProperty().bind(Bindings.lessThanOrEqual(0, dynamicBlackProperty));
        blackLabel.setFont(Font.loadFont(URLDecoder.decode(ChessClock.class.getResource("REGISTER.TTF").toExternalForm(),"utf-8"),45));
              
        final SimpleStringProperty dynamicMainProperty = new SimpleStringProperty();  
        mainLabel.textProperty().bind(dynamicMainProperty);
        mainLabel.setFont(Font.loadFont(URLDecoder.decode(ChessClock.class.getResource("REGISTER.TTF").toExternalForm(),"utf-8"),40));
             
        Thread t = new Thread(
        		new Runnable() {
        			@Override
        			public void run() {
        				while (true) {
        					Platform.runLater(new Runnable() {
        						@Override
        						public void run() {
        							dynamicWhiteProperty.set(chessClock.getSCIDisplay().getWhiteDisplay());
        							dynamicBlackProperty.set(chessClock.getSCIDisplay().getBlackDisplay());
        							dynamicMainProperty.set(chessClock.getSCIDisplay().getText());
        							chessClock.runCycle();
        						}
        					});

        					try {
        						Thread.sleep(100);
        					} catch (InterruptedException ex) {
        						break;
        					}
        				}
        			}
        		});
        t.setName("Updater");
        t.setDaemon(true);
        t.start();
        
    }
}
