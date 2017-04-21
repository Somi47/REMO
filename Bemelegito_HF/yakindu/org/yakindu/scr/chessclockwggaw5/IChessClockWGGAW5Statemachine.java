package org.yakindu.scr.chessclockwggaw5;

import org.yakindu.scr.IStatemachine;

public interface IChessClockWGGAW5Statemachine extends IStatemachine {

	public interface SCIButtons {
	
		public void raiseButton();
		
	}
	
	public SCIButtons getSCIButtons();
	
	public interface SCIDisplay {
	
		public String getText();
		
		public void setText(String value);
		
	}
	
	public SCIDisplay getSCIDisplay();
	
}
