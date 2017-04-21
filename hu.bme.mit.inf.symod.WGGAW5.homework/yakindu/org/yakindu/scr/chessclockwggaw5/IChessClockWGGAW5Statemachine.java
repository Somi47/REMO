package org.yakindu.scr.chessclockwggaw5;

import org.yakindu.scr.IStatemachine;
import org.yakindu.scr.ITimerCallback;

public interface IChessClockWGGAW5Statemachine extends ITimerCallback,IStatemachine {

	public interface SCIButtons {
	
		public void raiseModeButton();
		
		public void raiseStartButton();
		
		public void raiseBlackButton();
		
		public void raiseWhiteButton();
		
	}
	
	public SCIButtons getSCIButtons();
	
	public interface SCIBeeper {
	
		public void setSCIBeeperOperationCallback(SCIBeeperOperationCallback operationCallback);
	
	}
	
	public interface SCIBeeperOperationCallback {
	
		public void beep();
		
	}
	
	public SCIBeeper getSCIBeeper();
	
	public interface SCIDisplay {
	
		public String getText();
		
		public void setText(String value);
		
		public long getWhiteDisplay();
		
		public void setWhiteDisplay(long value);
		
		public long getBlackDisplay();
		
		public void setBlackDisplay(long value);
		
	}
	
	public SCIDisplay getSCIDisplay();
	
}
