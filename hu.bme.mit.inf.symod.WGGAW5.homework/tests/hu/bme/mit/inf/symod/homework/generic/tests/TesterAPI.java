package hu.bme.mit.inf.symod.homework.generic.tests;

import org.junit.Assert;
import org.yakindu.scr.chessclockwggaw5.*;
import org.yakindu.scr.chessclockwggaw5.IChessClockWGGAW5Statemachine.SCIBeeperOperationCallback;

public class TesterAPI {
	protected ChessClockWGGAW5Statemachine chessClock;
	
	protected boolean hasBeepedInThePreviousStep = false;
	protected LogicTimer timer;
	
	protected String name;
	protected String log;
	
	public TesterAPI(String name, String description) {
		//System.out.println("Timer is available: " + ReflectiveTimeInterfaceProvider.isTimerAvailable());
		this.name = name;
		this.log = "\n" + name + "\n----------\n";
		chessClock = new ChessClockWGGAW5Statemachine();
		chessClock.getSCIBeeper().setSCIBeeperOperationCallback(new SCIBeeperOperationCallback() {
			@Override
			public void beep() {
				hasBeepedInThePreviousStep = true;
				
			}
		});
		this.timer = new LogicTimer(chessClock);
		ReflectiveTimeInterfaceProvider.setTimer(
				chessClock,
				ReflectiveTimeInterfaceProvider.provideLogicTimer(this.timer));
		chessClock.init();
		chessClock.enter();
		timer.clockForward(0);
		handleEvent();
	}
	
	protected void insertEvent(String event) {
		log+= " - " + event + " at "+timer.currentTime +"s\n";
	}
	int checkNumber = 1;
	protected void insertCheck(String check, boolean correct, String expected, String result) {
		if(correct) {
			log+= "\t - Successful " + check + " check #" + checkNumber + ": "+expected +" expected\n";
		} else {
			String error =  check + " check #" + checkNumber + ": expected \"" + expected + "\" but found \""+result+"\"";
			log+= "\t - Failed " + error+"\n";
			System.out.println(log);
			throw new AssertionError(name + " failed by "+error);
		}
		checkNumber++;
	}
	
	protected void handleEvent() {
		chessClock.runCycle();
		timer.clockForward(0);
	}
	
	public boolean readBeep() {
		return hasBeepedInThePreviousStep;
	}
	
	public void expectBeep(boolean expected) {
		insertCheck("beep", readBeep() == expected, ""+expected, ""+readBeep());
	}
	
	public long readWhiteNumber() {
		return chessClock.getSCIDisplay().getWhiteDisplay();
	}
	public long readBlackNumber() {
		return chessClock.getSCIDisplay().getBlackDisplay();
	}
	
	public void expectWhiteNumber(long l) {
		insertCheck("White player display", readWhiteNumber() == l, ""+l, ""+readWhiteNumber());
	}
	public void expectBlackNumber(long l) {
		insertCheck("Black player display", readBlackNumber() == l, ""+l, ""+readBlackNumber());
	}
	
	public String readText() {
		return chessClock.getSCIDisplay().getText();
	}
	
	public void expectText(String value) {
		String expected = value == null?"":value;
		String result = readText() == null?"":readText();
		boolean correct =
			expected.replaceAll("[^a-zA-Z]", "").toLowerCase().equals(
			  result.replaceAll("[^a-zA-Z]", "").toLowerCase());
		insertCheck("main display", correct, expected, result);
	}
	
	public void pushStart() {
		hasBeepedInThePreviousStep = false;
		insertEvent("START");
		chessClock.getSCIButtons().raiseStartButton();
		handleEvent();
	}
	public void pushMod() {
		hasBeepedInThePreviousStep = false;
		insertEvent("MOD");
		chessClock.getSCIButtons().raiseModeButton();
		handleEvent();
	}
	public void pushWhite() {
		hasBeepedInThePreviousStep = false;
		insertEvent("White");
		chessClock.getSCIButtons().raiseWhiteButton();
		handleEvent();
	}
	public void pushBlack() {
		hasBeepedInThePreviousStep = false;
		insertEvent("Black");
		chessClock.getSCIButtons().raiseBlackButton();
		handleEvent();
	}
	
	public void clockForward(long length) {
		hasBeepedInThePreviousStep = false;
		insertEvent("WAIT for " +length + " ms");
		timer.clockForward(length);
		handleEvent();
	}
}

