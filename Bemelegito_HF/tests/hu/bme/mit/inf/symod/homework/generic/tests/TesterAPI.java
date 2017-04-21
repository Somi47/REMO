package hu.bme.mit.inf.symod.homework.generic.tests;

import org.junit.Assert;
import org.yakindu.scr.chessclockwggaw5.ChessClockWGGAW5Statemachine;

public class TesterAPI {
	protected ChessClockWGGAW5Statemachine chessClock;
	
	protected LogicTimer timer;
	
	protected String name;
	protected String log;
	
	public TesterAPI(String id, String description) {
		//System.out.println("Timer is available: " + ReflectiveTimeInterfaceProvider.isTimerAvailable());
		this.name = id;
		this.log = name + " Failed:\n\t"+description+"\n\t----------\n";
		chessClock = new ChessClockWGGAW5Statemachine();
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
		log+= "\t - " + event + " at "+timer.currentTime +"s\n";
	}
	int checkNumber = 1;
	protected void insertCheck(String check, boolean correct, String expected, String result) {
		if(correct) {
			log+= " - Successful " + check + " check #" + checkNumber + ": "+expected +"expected\n";
		} else {
			String error =  check + " check #" + checkNumber + ": expected \"" + expected + "\" but found \""+result+"\"";
			log+= " - Failed " + error+"\n";
			System.out.println(log);
			throw new AssertionError(name + " failed by "+error);
		}
		checkNumber++;
	}
	
	protected void handleEvent() {
		chessClock.runCycle();
		timer.clockForward(0);
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
		insertEvent("Button");
		chessClock.getSCIButtons().raiseButton();
		handleEvent();
	}
	
	public void clockForward(long length) {
		insertEvent("WAIT for " +length + " ms");
		timer.clockForward(length);
		handleEvent();
	}
}
