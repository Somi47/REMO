package org.yakindu.scr.chessclockwggaw5;
import org.yakindu.scr.ITimer;

public class ChessClockWGGAW5Statemachine implements IChessClockWGGAW5Statemachine {

	protected class SCIButtonsImpl implements SCIButtons {
	
		private boolean modeButton;
		
		public void raiseModeButton() {
			modeButton = true;
		}
		
		private boolean startButton;
		
		public void raiseStartButton() {
			startButton = true;
		}
		
		private boolean blackButton;
		
		public void raiseBlackButton() {
			blackButton = true;
		}
		
		private boolean whiteButton;
		
		public void raiseWhiteButton() {
			whiteButton = true;
		}
		
		protected void clearEvents() {
			modeButton = false;
			startButton = false;
			blackButton = false;
			whiteButton = false;
		}
	}
	
	protected SCIButtonsImpl sCIButtons;
	
	protected class SCIBeeperImpl implements SCIBeeper {
	
		private SCIBeeperOperationCallback operationCallback;
		
		public void setSCIBeeperOperationCallback(
				SCIBeeperOperationCallback operationCallback) {
			this.operationCallback = operationCallback;
		}
	}
	
	protected SCIBeeperImpl sCIBeeper;
	
	protected class SCIDisplayImpl implements SCIDisplay {
	
		private String text;
		
		public String getText() {
			return text;
		}
		
		public void setText(String value) {
			this.text = value;
		}
		
		private long whiteDisplay;
		
		public long getWhiteDisplay() {
			return whiteDisplay;
		}
		
		public void setWhiteDisplay(long value) {
			this.whiteDisplay = value;
		}
		
		private long blackDisplay;
		
		public long getBlackDisplay() {
			return blackDisplay;
		}
		
		public void setBlackDisplay(long value) {
			this.blackDisplay = value;
		}
		
	}
	
	protected SCIDisplayImpl sCIDisplay;
	
	private boolean initialized = false;
	
	public enum State {
		main_region_Ready_to_Play,
		main_region_Play,
		main_region_Play_inner_region_Black_adjourned,
		main_region_Play_inner_region_Black_moves,
		main_region_Play_inner_region_White_moves,
		main_region_Play_inner_region_White_adjourned,
		main_region_Play_inner_region_Black_incremented,
		main_region_Play_inner_region_White_incremented,
		main_region_Play_inner_region_White_fallen,
		main_region_Play_inner_region_Black_fallen,
		main_region_Init_time_white,
		main_region_Init_time_black,
		main_region_Increment_time,
		$NullState$
	};
	
	private final State[] stateVector = new State[1];
	
	private int nextStateIndex;
	
	private ITimer timer;
	
	private final boolean[] timeEvents = new boolean[8];
	private long whiteInitialTime;
	
	protected void setWhiteInitialTime(long value) {
		whiteInitialTime = value;
	}
	
	protected long getWhiteInitialTime() {
		return whiteInitialTime;
	}
	
	private long blackInitialTime;
	
	protected void setBlackInitialTime(long value) {
		blackInitialTime = value;
	}
	
	protected long getBlackInitialTime() {
		return blackInitialTime;
	}
	
	private long incrementTime;
	
	protected void setIncrementTime(long value) {
		incrementTime = value;
	}
	
	protected long getIncrementTime() {
		return incrementTime;
	}
	
	private boolean whiteAdjourned;
	
	protected void setWhiteAdjourned(boolean value) {
		whiteAdjourned = value;
	}
	
	protected boolean getWhiteAdjourned() {
		return whiteAdjourned;
	}
	
	private boolean blackAdjourned;
	
	protected void setBlackAdjourned(boolean value) {
		blackAdjourned = value;
	}
	
	protected boolean getBlackAdjourned() {
		return blackAdjourned;
	}
	
	public ChessClockWGGAW5Statemachine() {
		sCIButtons = new SCIButtonsImpl();
		sCIBeeper = new SCIBeeperImpl();
		sCIDisplay = new SCIDisplayImpl();
	}
	
	public void init() {
		this.initialized = true;
		if (timer == null) {
			throw new IllegalStateException("timer not set.");
		}
		for (int i = 0; i < 1; i++) {
			stateVector[i] = State.$NullState$;
		}
		clearEvents();
		clearOutEvents();
		sCIDisplay.setText("Chess Clock");
		
		sCIDisplay.setWhiteDisplay(-1);
		
		sCIDisplay.setBlackDisplay(-1);
		
		setWhiteInitialTime(90);
		
		setBlackInitialTime(90);
		
		setIncrementTime(0);
		
		setWhiteAdjourned(false);
		
		setBlackAdjourned(false);
	}
	
	public void enter() {
		if (!initialized) {
			throw new IllegalStateException(
					"The state machine needs to be initialized first by calling the init() function.");
		}
		if (timer == null) {
			throw new IllegalStateException("timer not set.");
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
		for (int i=0; i<timeEvents.length; i++) {
			timeEvents[i] = false;
		}
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
		case main_region_Ready_to_Play:
			return stateVector[0] == State.main_region_Ready_to_Play;
		case main_region_Play:
			return stateVector[0].ordinal() >= State.
					main_region_Play.ordinal()&& stateVector[0].ordinal() <= State.main_region_Play_inner_region_Black_fallen.ordinal();
		case main_region_Play_inner_region_Black_adjourned:
			return stateVector[0] == State.main_region_Play_inner_region_Black_adjourned;
		case main_region_Play_inner_region_Black_moves:
			return stateVector[0] == State.main_region_Play_inner_region_Black_moves;
		case main_region_Play_inner_region_White_moves:
			return stateVector[0] == State.main_region_Play_inner_region_White_moves;
		case main_region_Play_inner_region_White_adjourned:
			return stateVector[0] == State.main_region_Play_inner_region_White_adjourned;
		case main_region_Play_inner_region_Black_incremented:
			return stateVector[0] == State.main_region_Play_inner_region_Black_incremented;
		case main_region_Play_inner_region_White_incremented:
			return stateVector[0] == State.main_region_Play_inner_region_White_incremented;
		case main_region_Play_inner_region_White_fallen:
			return stateVector[0] == State.main_region_Play_inner_region_White_fallen;
		case main_region_Play_inner_region_Black_fallen:
			return stateVector[0] == State.main_region_Play_inner_region_Black_fallen;
		case main_region_Init_time_white:
			return stateVector[0] == State.main_region_Init_time_white;
		case main_region_Init_time_black:
			return stateVector[0] == State.main_region_Init_time_black;
		case main_region_Increment_time:
			return stateVector[0] == State.main_region_Increment_time;
		default:
			return false;
		}
	}
	
	/**
	* Set the {@link ITimer} for the state machine. It must be set
	* externally on a timed state machine before a run cycle can be correct
	* executed.
	* 
	* @param timer
	*/
	public void setTimer(ITimer timer) {
		this.timer = timer;
	}
	
	/**
	* Returns the currently used timer.
	* 
	* @return {@link ITimer}
	*/
	public ITimer getTimer() {
		return timer;
	}
	
	public void timeElapsed(int eventID) {
		timeEvents[eventID] = true;
	}
	
	public SCIButtons getSCIButtons() {
		return sCIButtons;
	}
	
	public SCIBeeper getSCIBeeper() {
		return sCIBeeper;
	}
	
	public SCIDisplay getSCIDisplay() {
		return sCIDisplay;
	}
	
	private boolean check_main_region_Ready_to_Play_tr0_tr0() {
		return sCIButtons.startButton;
	}
	
	private boolean check_main_region_Ready_to_Play_tr1_tr1() {
		return sCIButtons.modeButton;
	}
	
	private boolean check_main_region_Play_tr0_tr0() {
		return sCIButtons.startButton;
	}
	
	private boolean check_main_region_Play_inner_region_Black_adjourned_tr0_tr0() {
		return sCIButtons.modeButton;
	}
	
	private boolean check_main_region_Play_inner_region_Black_moves_tr0_tr0() {
		return sCIButtons.modeButton;
	}
	
	private boolean check_main_region_Play_inner_region_Black_moves_tr1_tr1() {
		return sCIButtons.blackButton;
	}
	
	private boolean check_main_region_Play_inner_region_Black_moves_tr2_tr2() {
		return (timeEvents[0]) && (sCIDisplay.getBlackDisplay()>1);
	}
	
	private boolean check_main_region_Play_inner_region_Black_moves_tr3_tr3() {
		return (timeEvents[1]) && (sCIDisplay.getBlackDisplay()<=1);
	}
	
	private boolean check_main_region_Play_inner_region_White_moves_tr0_tr0() {
		return sCIButtons.modeButton;
	}
	
	private boolean check_main_region_Play_inner_region_White_moves_tr1_tr1() {
		return sCIButtons.whiteButton;
	}
	
	private boolean check_main_region_Play_inner_region_White_moves_tr2_tr2() {
		return (timeEvents[2]) && (sCIDisplay.getWhiteDisplay()>1);
	}
	
	private boolean check_main_region_Play_inner_region_White_moves_tr3_tr3() {
		return (timeEvents[3]) && (sCIDisplay.getWhiteDisplay()<=1);
	}
	
	private boolean check_main_region_Play_inner_region_White_adjourned_tr0_tr0() {
		return sCIButtons.modeButton;
	}
	
	private boolean check_main_region_Play_inner_region_Black_incremented_tr0_tr0() {
		return (timeEvents[4]) && (getBlackAdjourned()==true);
	}
	
	private boolean check_main_region_Play_inner_region_Black_incremented_tr1_tr1() {
		return (timeEvents[5]) && (getBlackAdjourned()==false);
	}
	
	private boolean check_main_region_Play_inner_region_White_incremented_tr0_tr0() {
		return (timeEvents[6]) && (getWhiteAdjourned()==true);
	}
	
	private boolean check_main_region_Play_inner_region_White_incremented_tr1_tr1() {
		return (timeEvents[7]) && (getWhiteAdjourned()==false);
	}
	
	private boolean check_main_region_Init_time_white_tr0_tr0() {
		return (sCIButtons.whiteButton) && (getWhiteInitialTime()<180);
	}
	
	private boolean check_main_region_Init_time_white_tr1_tr1() {
		return (sCIButtons.blackButton) && (getWhiteInitialTime()>30);
	}
	
	private boolean check_main_region_Init_time_white_tr2_tr2() {
		return sCIButtons.modeButton;
	}
	
	private boolean check_main_region_Init_time_white_tr3_tr3() {
		return sCIButtons.startButton;
	}
	
	private boolean check_main_region_Init_time_black_tr0_tr0() {
		return (sCIButtons.blackButton) && (getBlackInitialTime()>30);
	}
	
	private boolean check_main_region_Init_time_black_tr1_tr1() {
		return (sCIButtons.whiteButton) && (getBlackInitialTime()<180);
	}
	
	private boolean check_main_region_Init_time_black_tr2_tr2() {
		return sCIButtons.startButton;
	}
	
	private boolean check_main_region_Init_time_black_tr3_tr3() {
		return sCIButtons.modeButton;
	}
	
	private boolean check_main_region_Increment_time_tr0_tr0() {
		return (sCIButtons.blackButton) && (getIncrementTime()>0);
	}
	
	private boolean check_main_region_Increment_time_tr1_tr1() {
		return (sCIButtons.whiteButton) && (getIncrementTime()<30);
	}
	
	private boolean check_main_region_Increment_time_tr2_tr2() {
		return sCIButtons.startButton;
	}
	
	private boolean check_main_region_Increment_time_tr3_tr3() {
		return sCIButtons.modeButton;
	}
	
	private void effect_main_region_Ready_to_Play_tr0() {
		exitSequence_main_region_Ready_to_Play();
		sCIDisplay.setWhiteDisplay(whiteInitialTime);
		
		sCIDisplay.setBlackDisplay(blackInitialTime);
		
		setWhiteAdjourned(false);
		
		setBlackAdjourned(false);
		
		enterSequence_main_region_Play_inner_region_White_moves_default();
	}
	
	private void effect_main_region_Ready_to_Play_tr1() {
		exitSequence_main_region_Ready_to_Play();
		sCIBeeper.operationCallback.beep();
		
		enterSequence_main_region_Init_time_white_default();
	}
	
	private void effect_main_region_Play_tr0() {
		exitSequence_main_region_Play();
		enterSequence_main_region_Ready_to_Play_default();
	}
	
	private void effect_main_region_Play_inner_region_Black_adjourned_tr0() {
		exitSequence_main_region_Play_inner_region_Black_adjourned();
		enterSequence_main_region_Play_inner_region_White_moves_default();
	}
	
	private void effect_main_region_Play_inner_region_Black_moves_tr0() {
		exitSequence_main_region_Play_inner_region_Black_moves();
		setBlackAdjourned(true);
		
		enterSequence_main_region_Play_inner_region_Black_moves_default();
	}
	
	private void effect_main_region_Play_inner_region_Black_moves_tr1() {
		exitSequence_main_region_Play_inner_region_Black_moves();
		sCIDisplay.setBlackDisplay(sCIDisplay.getBlackDisplay() + incrementTime);
		
		enterSequence_main_region_Play_inner_region_Black_incremented_default();
	}
	
	private void effect_main_region_Play_inner_region_Black_moves_tr2() {
		exitSequence_main_region_Play_inner_region_Black_moves();
		sCIDisplay.setBlackDisplay(sCIDisplay.getBlackDisplay() - 1);
		
		enterSequence_main_region_Play_inner_region_Black_moves_default();
	}
	
	private void effect_main_region_Play_inner_region_Black_moves_tr3() {
		exitSequence_main_region_Play_inner_region_Black_moves();
		sCIDisplay.setBlackDisplay(sCIDisplay.getBlackDisplay() - 1);
		
		enterSequence_main_region_Play_inner_region_Black_fallen_default();
	}
	
	private void effect_main_region_Play_inner_region_White_moves_tr0() {
		exitSequence_main_region_Play_inner_region_White_moves();
		setWhiteAdjourned(true);
		
		enterSequence_main_region_Play_inner_region_White_moves_default();
	}
	
	private void effect_main_region_Play_inner_region_White_moves_tr1() {
		exitSequence_main_region_Play_inner_region_White_moves();
		sCIDisplay.setWhiteDisplay(sCIDisplay.getWhiteDisplay() + incrementTime);
		
		enterSequence_main_region_Play_inner_region_White_incremented_default();
	}
	
	private void effect_main_region_Play_inner_region_White_moves_tr2() {
		exitSequence_main_region_Play_inner_region_White_moves();
		sCIDisplay.setWhiteDisplay(sCIDisplay.getWhiteDisplay() - 1);
		
		enterSequence_main_region_Play_inner_region_White_moves_default();
	}
	
	private void effect_main_region_Play_inner_region_White_moves_tr3() {
		exitSequence_main_region_Play_inner_region_White_moves();
		sCIDisplay.setWhiteDisplay(sCIDisplay.getWhiteDisplay() - 1);
		
		enterSequence_main_region_Play_inner_region_White_fallen_default();
	}
	
	private void effect_main_region_Play_inner_region_White_adjourned_tr0() {
		exitSequence_main_region_Play_inner_region_White_adjourned();
		enterSequence_main_region_Play_inner_region_Black_moves_default();
	}
	
	private void effect_main_region_Play_inner_region_Black_incremented_tr0() {
		exitSequence_main_region_Play_inner_region_Black_incremented();
		enterSequence_main_region_Play_inner_region_Black_adjourned_default();
	}
	
	private void effect_main_region_Play_inner_region_Black_incremented_tr1() {
		exitSequence_main_region_Play_inner_region_Black_incremented();
		enterSequence_main_region_Play_inner_region_White_moves_default();
	}
	
	private void effect_main_region_Play_inner_region_White_incremented_tr0() {
		exitSequence_main_region_Play_inner_region_White_incremented();
		enterSequence_main_region_Play_inner_region_White_adjourned_default();
	}
	
	private void effect_main_region_Play_inner_region_White_incremented_tr1() {
		exitSequence_main_region_Play_inner_region_White_incremented();
		enterSequence_main_region_Play_inner_region_Black_moves_default();
	}
	
	private void effect_main_region_Init_time_white_tr0() {
		exitSequence_main_region_Init_time_white();
		setWhiteInitialTime(getWhiteInitialTime() + 5);
		
		enterSequence_main_region_Init_time_white_default();
	}
	
	private void effect_main_region_Init_time_white_tr1() {
		exitSequence_main_region_Init_time_white();
		setWhiteInitialTime(getWhiteInitialTime() - 5);
		
		enterSequence_main_region_Init_time_white_default();
	}
	
	private void effect_main_region_Init_time_white_tr2() {
		exitSequence_main_region_Init_time_white();
		sCIBeeper.operationCallback.beep();
		
		enterSequence_main_region_Init_time_black_default();
	}
	
	private void effect_main_region_Init_time_white_tr3() {
		exitSequence_main_region_Init_time_white();
		setWhiteInitialTime(90);
		
		enterSequence_main_region_Init_time_white_default();
	}
	
	private void effect_main_region_Init_time_black_tr0() {
		exitSequence_main_region_Init_time_black();
		setBlackInitialTime(getBlackInitialTime() - 5);
		
		enterSequence_main_region_Init_time_black_default();
	}
	
	private void effect_main_region_Init_time_black_tr1() {
		exitSequence_main_region_Init_time_black();
		setBlackInitialTime(getBlackInitialTime() + 5);
		
		enterSequence_main_region_Init_time_black_default();
	}
	
	private void effect_main_region_Init_time_black_tr2() {
		exitSequence_main_region_Init_time_black();
		setBlackInitialTime(90);
		
		enterSequence_main_region_Init_time_black_default();
	}
	
	private void effect_main_region_Init_time_black_tr3() {
		exitSequence_main_region_Init_time_black();
		sCIBeeper.operationCallback.beep();
		
		enterSequence_main_region_Increment_time_default();
	}
	
	private void effect_main_region_Increment_time_tr0() {
		exitSequence_main_region_Increment_time();
		setIncrementTime(getIncrementTime() - 1);
		
		enterSequence_main_region_Increment_time_default();
	}
	
	private void effect_main_region_Increment_time_tr1() {
		exitSequence_main_region_Increment_time();
		setIncrementTime(getIncrementTime() + 1);
		
		enterSequence_main_region_Increment_time_default();
	}
	
	private void effect_main_region_Increment_time_tr2() {
		exitSequence_main_region_Increment_time();
		setIncrementTime(0);
		
		enterSequence_main_region_Increment_time_default();
	}
	
	private void effect_main_region_Increment_time_tr3() {
		exitSequence_main_region_Increment_time();
		sCIBeeper.operationCallback.beep();
		
		enterSequence_main_region_Ready_to_Play_default();
	}
	
	/* Entry action for state 'Ready_to_Play'. */
	private void entryAction_main_region_Ready_to_Play() {
		sCIDisplay.setText("Ready to play");
		
		sCIDisplay.setWhiteDisplay(-1);
		
		sCIDisplay.setBlackDisplay(-1);
	}
	
	/* Entry action for state 'Black_adjourned'. */
	private void entryAction_main_region_Play_inner_region_Black_adjourned() {
		sCIDisplay.setText("Black adjourned");
		
		setBlackAdjourned(false);
	}
	
	/* Entry action for state 'Black_moves'. */
	private void entryAction_main_region_Play_inner_region_Black_moves() {
		timer.setTimer(this, 0, 1 * 1000, true);
		
		timer.setTimer(this, 1, 1 * 1000, true);
		
		sCIDisplay.setText("Black moves");
		
		if (sCIDisplay.getBlackDisplay()<=5) {
			sCIBeeper.operationCallback.beep();
		}
	}
	
	/* Entry action for state 'White_moves'. */
	private void entryAction_main_region_Play_inner_region_White_moves() {
		timer.setTimer(this, 2, 1 * 1000, true);
		
		timer.setTimer(this, 3, 1 * 1000, true);
		
		sCIDisplay.setText("White moves");
		
		if (sCIDisplay.getWhiteDisplay()<=5) {
			sCIBeeper.operationCallback.beep();
		}
	}
	
	/* Entry action for state 'White_adjourned'. */
	private void entryAction_main_region_Play_inner_region_White_adjourned() {
		sCIDisplay.setText("White adjourned");
		
		setWhiteAdjourned(false);
	}
	
	/* Entry action for state 'Black_incremented'. */
	private void entryAction_main_region_Play_inner_region_Black_incremented() {
		timer.setTimer(this, 4, 0 * 1000, false);
		
		timer.setTimer(this, 5, 0 * 1000, false);
		
		if (sCIDisplay.getBlackDisplay()>1200) {
			sCIDisplay.setBlackDisplay(1200);
		}
	}
	
	/* Entry action for state 'White_incremented'. */
	private void entryAction_main_region_Play_inner_region_White_incremented() {
		timer.setTimer(this, 6, 0 * 1000, false);
		
		timer.setTimer(this, 7, 0 * 1000, false);
		
		if (sCIDisplay.getWhiteDisplay()>1200) {
			sCIDisplay.setWhiteDisplay(1200);
		}
	}
	
	/* Entry action for state 'White_fallen'. */
	private void entryAction_main_region_Play_inner_region_White_fallen() {
		sCIBeeper.operationCallback.beep();
		
		sCIDisplay.setText("White flag fallen");
	}
	
	/* Entry action for state 'Black_fallen'. */
	private void entryAction_main_region_Play_inner_region_Black_fallen() {
		sCIBeeper.operationCallback.beep();
		
		sCIDisplay.setText("Black flag fallen");
	}
	
	/* Entry action for state 'Init_time_white'. */
	private void entryAction_main_region_Init_time_white() {
		sCIDisplay.setText("White initial time");
		
		sCIDisplay.setWhiteDisplay(whiteInitialTime);
		
		sCIDisplay.setBlackDisplay(-1);
	}
	
	/* Entry action for state 'Init_time_black'. */
	private void entryAction_main_region_Init_time_black() {
		sCIDisplay.setText("Black initial time");
		
		sCIDisplay.setWhiteDisplay(-1);
		
		sCIDisplay.setBlackDisplay(blackInitialTime);
	}
	
	/* Entry action for state 'Increment_time'. */
	private void entryAction_main_region_Increment_time() {
		sCIDisplay.setText("Increment time");
		
		sCIDisplay.setWhiteDisplay(incrementTime);
		
		sCIDisplay.setBlackDisplay(-1);
	}
	
	/* Exit action for state 'Black_moves'. */
	private void exitAction_main_region_Play_inner_region_Black_moves() {
		timer.unsetTimer(this, 0);
		
		timer.unsetTimer(this, 1);
	}
	
	/* Exit action for state 'White_moves'. */
	private void exitAction_main_region_Play_inner_region_White_moves() {
		timer.unsetTimer(this, 2);
		
		timer.unsetTimer(this, 3);
	}
	
	/* Exit action for state 'Black_incremented'. */
	private void exitAction_main_region_Play_inner_region_Black_incremented() {
		timer.unsetTimer(this, 4);
		
		timer.unsetTimer(this, 5);
	}
	
	/* Exit action for state 'White_incremented'. */
	private void exitAction_main_region_Play_inner_region_White_incremented() {
		timer.unsetTimer(this, 6);
		
		timer.unsetTimer(this, 7);
	}
	
	/* 'default' enter sequence for state Ready_to_Play */
	private void enterSequence_main_region_Ready_to_Play_default() {
		entryAction_main_region_Ready_to_Play();
		nextStateIndex = 0;
		stateVector[0] = State.main_region_Ready_to_Play;
	}
	
	/* 'default' enter sequence for state Black_adjourned */
	private void enterSequence_main_region_Play_inner_region_Black_adjourned_default() {
		entryAction_main_region_Play_inner_region_Black_adjourned();
		nextStateIndex = 0;
		stateVector[0] = State.main_region_Play_inner_region_Black_adjourned;
	}
	
	/* 'default' enter sequence for state Black_moves */
	private void enterSequence_main_region_Play_inner_region_Black_moves_default() {
		entryAction_main_region_Play_inner_region_Black_moves();
		nextStateIndex = 0;
		stateVector[0] = State.main_region_Play_inner_region_Black_moves;
	}
	
	/* 'default' enter sequence for state White_moves */
	private void enterSequence_main_region_Play_inner_region_White_moves_default() {
		entryAction_main_region_Play_inner_region_White_moves();
		nextStateIndex = 0;
		stateVector[0] = State.main_region_Play_inner_region_White_moves;
	}
	
	/* 'default' enter sequence for state White_adjourned */
	private void enterSequence_main_region_Play_inner_region_White_adjourned_default() {
		entryAction_main_region_Play_inner_region_White_adjourned();
		nextStateIndex = 0;
		stateVector[0] = State.main_region_Play_inner_region_White_adjourned;
	}
	
	/* 'default' enter sequence for state Black_incremented */
	private void enterSequence_main_region_Play_inner_region_Black_incremented_default() {
		entryAction_main_region_Play_inner_region_Black_incremented();
		nextStateIndex = 0;
		stateVector[0] = State.main_region_Play_inner_region_Black_incremented;
	}
	
	/* 'default' enter sequence for state White_incremented */
	private void enterSequence_main_region_Play_inner_region_White_incremented_default() {
		entryAction_main_region_Play_inner_region_White_incremented();
		nextStateIndex = 0;
		stateVector[0] = State.main_region_Play_inner_region_White_incremented;
	}
	
	/* 'default' enter sequence for state White_fallen */
	private void enterSequence_main_region_Play_inner_region_White_fallen_default() {
		entryAction_main_region_Play_inner_region_White_fallen();
		nextStateIndex = 0;
		stateVector[0] = State.main_region_Play_inner_region_White_fallen;
	}
	
	/* 'default' enter sequence for state Black_fallen */
	private void enterSequence_main_region_Play_inner_region_Black_fallen_default() {
		entryAction_main_region_Play_inner_region_Black_fallen();
		nextStateIndex = 0;
		stateVector[0] = State.main_region_Play_inner_region_Black_fallen;
	}
	
	/* 'default' enter sequence for state Init_time_white */
	private void enterSequence_main_region_Init_time_white_default() {
		entryAction_main_region_Init_time_white();
		nextStateIndex = 0;
		stateVector[0] = State.main_region_Init_time_white;
	}
	
	/* 'default' enter sequence for state Init_time_black */
	private void enterSequence_main_region_Init_time_black_default() {
		entryAction_main_region_Init_time_black();
		nextStateIndex = 0;
		stateVector[0] = State.main_region_Init_time_black;
	}
	
	/* 'default' enter sequence for state Increment_time */
	private void enterSequence_main_region_Increment_time_default() {
		entryAction_main_region_Increment_time();
		nextStateIndex = 0;
		stateVector[0] = State.main_region_Increment_time;
	}
	
	/* 'default' enter sequence for region main region */
	private void enterSequence_main_region_default() {
		react_main_region__entry_Default();
	}
	
	/* Default exit sequence for state Ready_to_Play */
	private void exitSequence_main_region_Ready_to_Play() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state Play */
	private void exitSequence_main_region_Play() {
		exitSequence_main_region_Play_inner_region();
	}
	
	/* Default exit sequence for state Black_adjourned */
	private void exitSequence_main_region_Play_inner_region_Black_adjourned() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state Black_moves */
	private void exitSequence_main_region_Play_inner_region_Black_moves() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
		
		exitAction_main_region_Play_inner_region_Black_moves();
	}
	
	/* Default exit sequence for state White_moves */
	private void exitSequence_main_region_Play_inner_region_White_moves() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
		
		exitAction_main_region_Play_inner_region_White_moves();
	}
	
	/* Default exit sequence for state White_adjourned */
	private void exitSequence_main_region_Play_inner_region_White_adjourned() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state Black_incremented */
	private void exitSequence_main_region_Play_inner_region_Black_incremented() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
		
		exitAction_main_region_Play_inner_region_Black_incremented();
	}
	
	/* Default exit sequence for state White_incremented */
	private void exitSequence_main_region_Play_inner_region_White_incremented() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
		
		exitAction_main_region_Play_inner_region_White_incremented();
	}
	
	/* Default exit sequence for state White_fallen */
	private void exitSequence_main_region_Play_inner_region_White_fallen() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state Black_fallen */
	private void exitSequence_main_region_Play_inner_region_Black_fallen() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state Init_time_white */
	private void exitSequence_main_region_Init_time_white() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state Init_time_black */
	private void exitSequence_main_region_Init_time_black() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state Increment_time */
	private void exitSequence_main_region_Increment_time() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for region main region */
	private void exitSequence_main_region() {
		switch (stateVector[0]) {
		case main_region_Ready_to_Play:
			exitSequence_main_region_Ready_to_Play();
			break;
		case main_region_Play_inner_region_Black_adjourned:
			exitSequence_main_region_Play_inner_region_Black_adjourned();
			break;
		case main_region_Play_inner_region_Black_moves:
			exitSequence_main_region_Play_inner_region_Black_moves();
			break;
		case main_region_Play_inner_region_White_moves:
			exitSequence_main_region_Play_inner_region_White_moves();
			break;
		case main_region_Play_inner_region_White_adjourned:
			exitSequence_main_region_Play_inner_region_White_adjourned();
			break;
		case main_region_Play_inner_region_Black_incremented:
			exitSequence_main_region_Play_inner_region_Black_incremented();
			break;
		case main_region_Play_inner_region_White_incremented:
			exitSequence_main_region_Play_inner_region_White_incremented();
			break;
		case main_region_Play_inner_region_White_fallen:
			exitSequence_main_region_Play_inner_region_White_fallen();
			break;
		case main_region_Play_inner_region_Black_fallen:
			exitSequence_main_region_Play_inner_region_Black_fallen();
			break;
		case main_region_Init_time_white:
			exitSequence_main_region_Init_time_white();
			break;
		case main_region_Init_time_black:
			exitSequence_main_region_Init_time_black();
			break;
		case main_region_Increment_time:
			exitSequence_main_region_Increment_time();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region inner region */
	private void exitSequence_main_region_Play_inner_region() {
		switch (stateVector[0]) {
		case main_region_Play_inner_region_Black_adjourned:
			exitSequence_main_region_Play_inner_region_Black_adjourned();
			break;
		case main_region_Play_inner_region_Black_moves:
			exitSequence_main_region_Play_inner_region_Black_moves();
			break;
		case main_region_Play_inner_region_White_moves:
			exitSequence_main_region_Play_inner_region_White_moves();
			break;
		case main_region_Play_inner_region_White_adjourned:
			exitSequence_main_region_Play_inner_region_White_adjourned();
			break;
		case main_region_Play_inner_region_Black_incremented:
			exitSequence_main_region_Play_inner_region_Black_incremented();
			break;
		case main_region_Play_inner_region_White_incremented:
			exitSequence_main_region_Play_inner_region_White_incremented();
			break;
		case main_region_Play_inner_region_White_fallen:
			exitSequence_main_region_Play_inner_region_White_fallen();
			break;
		case main_region_Play_inner_region_Black_fallen:
			exitSequence_main_region_Play_inner_region_Black_fallen();
			break;
		default:
			break;
		}
	}
	
	/* The reactions of state Ready_to_Play. */
	private void react_main_region_Ready_to_Play() {
		if (check_main_region_Ready_to_Play_tr0_tr0()) {
			effect_main_region_Ready_to_Play_tr0();
		} else {
			if (check_main_region_Ready_to_Play_tr1_tr1()) {
				effect_main_region_Ready_to_Play_tr1();
			}
		}
	}
	
	/* The reactions of state Black_adjourned. */
	private void react_main_region_Play_inner_region_Black_adjourned() {
		if (check_main_region_Play_tr0_tr0()) {
			effect_main_region_Play_tr0();
		} else {
			if (check_main_region_Play_inner_region_Black_adjourned_tr0_tr0()) {
				effect_main_region_Play_inner_region_Black_adjourned_tr0();
			}
		}
	}
	
	/* The reactions of state Black_moves. */
	private void react_main_region_Play_inner_region_Black_moves() {
		if (check_main_region_Play_tr0_tr0()) {
			effect_main_region_Play_tr0();
		} else {
			if (check_main_region_Play_inner_region_Black_moves_tr0_tr0()) {
				effect_main_region_Play_inner_region_Black_moves_tr0();
			} else {
				if (check_main_region_Play_inner_region_Black_moves_tr1_tr1()) {
					effect_main_region_Play_inner_region_Black_moves_tr1();
				} else {
					if (check_main_region_Play_inner_region_Black_moves_tr2_tr2()) {
						effect_main_region_Play_inner_region_Black_moves_tr2();
					} else {
						if (check_main_region_Play_inner_region_Black_moves_tr3_tr3()) {
							effect_main_region_Play_inner_region_Black_moves_tr3();
						}
					}
				}
			}
		}
	}
	
	/* The reactions of state White_moves. */
	private void react_main_region_Play_inner_region_White_moves() {
		if (check_main_region_Play_tr0_tr0()) {
			effect_main_region_Play_tr0();
		} else {
			if (check_main_region_Play_inner_region_White_moves_tr0_tr0()) {
				effect_main_region_Play_inner_region_White_moves_tr0();
			} else {
				if (check_main_region_Play_inner_region_White_moves_tr1_tr1()) {
					effect_main_region_Play_inner_region_White_moves_tr1();
				} else {
					if (check_main_region_Play_inner_region_White_moves_tr2_tr2()) {
						effect_main_region_Play_inner_region_White_moves_tr2();
					} else {
						if (check_main_region_Play_inner_region_White_moves_tr3_tr3()) {
							effect_main_region_Play_inner_region_White_moves_tr3();
						}
					}
				}
			}
		}
	}
	
	/* The reactions of state White_adjourned. */
	private void react_main_region_Play_inner_region_White_adjourned() {
		if (check_main_region_Play_tr0_tr0()) {
			effect_main_region_Play_tr0();
		} else {
			if (check_main_region_Play_inner_region_White_adjourned_tr0_tr0()) {
				effect_main_region_Play_inner_region_White_adjourned_tr0();
			}
		}
	}
	
	/* The reactions of state Black_incremented. */
	private void react_main_region_Play_inner_region_Black_incremented() {
		if (check_main_region_Play_tr0_tr0()) {
			effect_main_region_Play_tr0();
		} else {
			if (check_main_region_Play_inner_region_Black_incremented_tr0_tr0()) {
				effect_main_region_Play_inner_region_Black_incremented_tr0();
			} else {
				if (check_main_region_Play_inner_region_Black_incremented_tr1_tr1()) {
					effect_main_region_Play_inner_region_Black_incremented_tr1();
				}
			}
		}
	}
	
	/* The reactions of state White_incremented. */
	private void react_main_region_Play_inner_region_White_incremented() {
		if (check_main_region_Play_tr0_tr0()) {
			effect_main_region_Play_tr0();
		} else {
			if (check_main_region_Play_inner_region_White_incremented_tr0_tr0()) {
				effect_main_region_Play_inner_region_White_incremented_tr0();
			} else {
				if (check_main_region_Play_inner_region_White_incremented_tr1_tr1()) {
					effect_main_region_Play_inner_region_White_incremented_tr1();
				}
			}
		}
	}
	
	/* The reactions of state White_fallen. */
	private void react_main_region_Play_inner_region_White_fallen() {
		if (check_main_region_Play_tr0_tr0()) {
			effect_main_region_Play_tr0();
		} else {
		}
	}
	
	/* The reactions of state Black_fallen. */
	private void react_main_region_Play_inner_region_Black_fallen() {
		if (check_main_region_Play_tr0_tr0()) {
			effect_main_region_Play_tr0();
		} else {
		}
	}
	
	/* The reactions of state Init_time_white. */
	private void react_main_region_Init_time_white() {
		if (check_main_region_Init_time_white_tr0_tr0()) {
			effect_main_region_Init_time_white_tr0();
		} else {
			if (check_main_region_Init_time_white_tr1_tr1()) {
				effect_main_region_Init_time_white_tr1();
			} else {
				if (check_main_region_Init_time_white_tr2_tr2()) {
					effect_main_region_Init_time_white_tr2();
				} else {
					if (check_main_region_Init_time_white_tr3_tr3()) {
						effect_main_region_Init_time_white_tr3();
					}
				}
			}
		}
	}
	
	/* The reactions of state Init_time_black. */
	private void react_main_region_Init_time_black() {
		if (check_main_region_Init_time_black_tr0_tr0()) {
			effect_main_region_Init_time_black_tr0();
		} else {
			if (check_main_region_Init_time_black_tr1_tr1()) {
				effect_main_region_Init_time_black_tr1();
			} else {
				if (check_main_region_Init_time_black_tr2_tr2()) {
					effect_main_region_Init_time_black_tr2();
				} else {
					if (check_main_region_Init_time_black_tr3_tr3()) {
						effect_main_region_Init_time_black_tr3();
					}
				}
			}
		}
	}
	
	/* The reactions of state Increment_time. */
	private void react_main_region_Increment_time() {
		if (check_main_region_Increment_time_tr0_tr0()) {
			effect_main_region_Increment_time_tr0();
		} else {
			if (check_main_region_Increment_time_tr1_tr1()) {
				effect_main_region_Increment_time_tr1();
			} else {
				if (check_main_region_Increment_time_tr2_tr2()) {
					effect_main_region_Increment_time_tr2();
				} else {
					if (check_main_region_Increment_time_tr3_tr3()) {
						effect_main_region_Increment_time_tr3();
					}
				}
			}
		}
	}
	
	/* Default react sequence for initial entry  */
	private void react_main_region__entry_Default() {
		enterSequence_main_region_Ready_to_Play_default();
	}
	
	public void runCycle() {
		if (!initialized)
			throw new IllegalStateException(
					"The state machine needs to be initialized first by calling the init() function.");
		clearOutEvents();
		for (nextStateIndex = 0; nextStateIndex < stateVector.length; nextStateIndex++) {
			switch (stateVector[nextStateIndex]) {
			case main_region_Ready_to_Play:
				react_main_region_Ready_to_Play();
				break;
			case main_region_Play_inner_region_Black_adjourned:
				react_main_region_Play_inner_region_Black_adjourned();
				break;
			case main_region_Play_inner_region_Black_moves:
				react_main_region_Play_inner_region_Black_moves();
				break;
			case main_region_Play_inner_region_White_moves:
				react_main_region_Play_inner_region_White_moves();
				break;
			case main_region_Play_inner_region_White_adjourned:
				react_main_region_Play_inner_region_White_adjourned();
				break;
			case main_region_Play_inner_region_Black_incremented:
				react_main_region_Play_inner_region_Black_incremented();
				break;
			case main_region_Play_inner_region_White_incremented:
				react_main_region_Play_inner_region_White_incremented();
				break;
			case main_region_Play_inner_region_White_fallen:
				react_main_region_Play_inner_region_White_fallen();
				break;
			case main_region_Play_inner_region_Black_fallen:
				react_main_region_Play_inner_region_Black_fallen();
				break;
			case main_region_Init_time_white:
				react_main_region_Init_time_white();
				break;
			case main_region_Init_time_black:
				react_main_region_Init_time_black();
				break;
			case main_region_Increment_time:
				react_main_region_Increment_time();
				break;
			default:
				// $NullState$
			}
		}
		clearEvents();
	}
}
