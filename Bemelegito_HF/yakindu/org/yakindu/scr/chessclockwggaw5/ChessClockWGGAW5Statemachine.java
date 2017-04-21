package org.yakindu.scr.chessclockwggaw5;

public class ChessClockWGGAW5Statemachine implements IChessClockWGGAW5Statemachine {

	protected class SCIButtonsImpl implements SCIButtons {
	
		private boolean button;
		
		public void raiseButton() {
			button = true;
		}
		
		protected void clearEvents() {
			button = false;
		}
	}
	
	protected SCIButtonsImpl sCIButtons;
	
	protected class SCIDisplayImpl implements SCIDisplay {
	
		private String text;
		
		public String getText() {
			return text;
		}
		
		public void setText(String value) {
			this.text = value;
		}
		
	}
	
	protected SCIDisplayImpl sCIDisplay;
	
	private boolean initialized = false;
	
	public enum State {
		main_region_Working,
		main_region_Sate2,
		$NullState$
	};
	
	private final State[] stateVector = new State[1];
	
	private int nextStateIndex;
	
	public ChessClockWGGAW5Statemachine() {
		sCIButtons = new SCIButtonsImpl();
		sCIDisplay = new SCIDisplayImpl();
	}
	
	public void init() {
		this.initialized = true;
		for (int i = 0; i < 1; i++) {
			stateVector[i] = State.$NullState$;
		}
		clearEvents();
		clearOutEvents();
		sCIDisplay.setText("Initial text");
	}
	
	public void enter() {
		if (!initialized) {
			throw new IllegalStateException(
					"The state machine needs to be initialized first by calling the init() function.");
		}
		enterSequence_main_region_default();
	}
	
	public void exit() {
		exitSequence_main_region();
	}
	
	/**
	 * @see IStatemachine#isActive()
	 */
	public boolean isActive() {
		return stateVector[0] != State.$NullState$;
	}
	
	/** 
	* Always returns 'false' since this state machine can never become final.
	*
	* @see IStatemachine#isFinal()
	*/
	public boolean isFinal() {
		return false;
	}
	/**
	* This method resets the incoming events (time events included).
	*/
	protected void clearEvents() {
		sCIButtons.clearEvents();
	}
	
	/**
	* This method resets the outgoing events.
	*/
	protected void clearOutEvents() {
	}
	
	/**
	* Returns true if the given state is currently active otherwise false.
	*/
	public boolean isStateActive(State state) {
	
		switch (state) {
		case main_region_Working:
			return stateVector[0] == State.main_region_Working;
		case main_region_Sate2:
			return stateVector[0] == State.main_region_Sate2;
		default:
			return false;
		}
	}
	
	public SCIButtons getSCIButtons() {
		return sCIButtons;
	}
	
	public SCIDisplay getSCIDisplay() {
		return sCIDisplay;
	}
	
	private boolean check_main_region_Working_tr0_tr0() {
		return sCIButtons.button;
	}
	
	private boolean check_main_region_Sate2_tr0_tr0() {
		return sCIButtons.button;
	}
	
	private void effect_main_region_Working_tr0() {
		exitSequence_main_region_Working();
		enterSequence_main_region_Sate2_default();
	}
	
	private void effect_main_region_Sate2_tr0() {
		exitSequence_main_region_Sate2();
		enterSequence_main_region_Working_default();
	}
	
	/* Entry action for state 'Working'. */
	private void entryAction_main_region_Working() {
		sCIDisplay.setText("Hello Yakindu World!");
	}
	
	/* Entry action for state 'Sate2'. */
	private void entryAction_main_region_Sate2() {
		sCIDisplay.setText("WGGAW5");
	}
	
	/* 'default' enter sequence for state Working */
	private void enterSequence_main_region_Working_default() {
		entryAction_main_region_Working();
		nextStateIndex = 0;
		stateVector[0] = State.main_region_Working;
	}
	
	/* 'default' enter sequence for state Sate2 */
	private void enterSequence_main_region_Sate2_default() {
		entryAction_main_region_Sate2();
		nextStateIndex = 0;
		stateVector[0] = State.main_region_Sate2;
	}
	
	/* 'default' enter sequence for region main region */
	private void enterSequence_main_region_default() {
		react_main_region__entry_Default();
	}
	
	/* Default exit sequence for state Working */
	private void exitSequence_main_region_Working() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state Sate2 */
	private void exitSequence_main_region_Sate2() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for region main region */
	private void exitSequence_main_region() {
		switch (stateVector[0]) {
		case main_region_Working:
			exitSequence_main_region_Working();
			break;
		case main_region_Sate2:
			exitSequence_main_region_Sate2();
			break;
		default:
			break;
		}
	}
	
	/* The reactions of state Working. */
	private void react_main_region_Working() {
		if (check_main_region_Working_tr0_tr0()) {
			effect_main_region_Working_tr0();
		}
	}
	
	/* The reactions of state Sate2. */
	private void react_main_region_Sate2() {
		if (check_main_region_Sate2_tr0_tr0()) {
			effect_main_region_Sate2_tr0();
		}
	}
	
	/* Default react sequence for initial entry  */
	private void react_main_region__entry_Default() {
		enterSequence_main_region_Working_default();
	}
	
	public void runCycle() {
		if (!initialized)
			throw new IllegalStateException(
					"The state machine needs to be initialized first by calling the init() function.");
		clearOutEvents();
		for (nextStateIndex = 0; nextStateIndex < stateVector.length; nextStateIndex++) {
			switch (stateVector[nextStateIndex]) {
			case main_region_Working:
				react_main_region_Working();
				break;
			case main_region_Sate2:
				react_main_region_Sate2();
				break;
			default:
				// $NullState$
			}
		}
		clearEvents();
	}
}
