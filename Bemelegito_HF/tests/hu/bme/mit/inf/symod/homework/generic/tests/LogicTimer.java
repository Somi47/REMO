package hu.bme.mit.inf.symod.homework.generic.tests;

import java.util.Comparator;
import java.util.PriorityQueue;

import org.yakindu.scr.IStatemachine;

public class LogicTimer /*implements ITimer*/{
	class TimerEntry {
		protected /*ITimerCallback*/ Object callback;
		protected int eventID;
		protected long activationTime;
		protected long periodTime;
		public TimerEntry(/*ITimerCallback*/ Object callback, int eventID, long actualTime, long time, boolean isPeriodic) {
			super();
			this.callback = callback;
			this.eventID = eventID;
			this.activationTime = actualTime+time;
			this.periodTime = isPeriodic? time : -1;
		}
		public /*ITimerCallback*/ Object getCallback() {
			return callback;
		}
		public int getEventID() {
			return eventID;
		}
		public long getActivationTime() {
			return activationTime;
		}
		public boolean isPeriodic() {
			return periodTime>0;
		}
		public void addPeriod() {
			this.activationTime+=periodTime;
		}
	}
	
	IStatemachine machine;
	long currentTime = 0;
	
	
	public LogicTimer(IStatemachine machine) {
		super();
		this.machine = machine;
	}

	PriorityQueue<TimerEntry> entries = new PriorityQueue<TimerEntry>(20,new Comparator<TimerEntry>() {
		@Override
		public int compare(TimerEntry arg0, TimerEntry arg1) {
			return Long.compare(arg0.getActivationTime(),arg1.getActivationTime());
		}		
	});
	
	//@Override
	public void setTimer(/*ITimerCallback*/ Object callback, int eventID, long time,
			boolean isPeriodic) {
		this.entries.add(new TimerEntry(callback, eventID, this.currentTime, time, isPeriodic));
	}

	//@Override
	public void unsetTimer(/*ITimerCallback*/ Object callback, int eventID) {
		TimerEntry entryToRemove = null;
		for(TimerEntry entry : this.entries) {
			if(entry.getCallback() == callback && entry.getEventID() == eventID) {
				entryToRemove = entry;
			}
		}
		if(entryToRemove != null) {
			this.entries.remove(entryToRemove);
		}
	}
	
	/**
	 * Simulate the statechart for a given time.
	 * @param time The amount of time.
	 */
	public void clockForward(long time) {
		// This is the target time
		final long targetTime = this.currentTime+time;
		
		// While there are events between the current and the target time, they have to be simulated
		while(!entries.isEmpty() && entries.element().getActivationTime()<=targetTime) {
			TimerEntry entryToExecute = entries.element();
			
			// Wait until the first event
			this.currentTime = entryToExecute.getActivationTime();
			
			// Execute the event:
			// 1. Remove from the event queue
			entries.remove(entryToExecute);
			// 2. execute the callback
			ReflectiveTimeInterfaceProvider.timeElapsed(entryToExecute.callback, entryToExecute.getEventID());
			machine.runCycle();
			// 3. If the event is periodic, put it back for the next period.
			if(entryToExecute.isPeriodic()) {
				entryToExecute.addPeriod();
				entries.add(entryToExecute);
			}
		}
		
		// If there are no more events then the time can be simulated without event execution
		this.currentTime = targetTime;
	}
}
