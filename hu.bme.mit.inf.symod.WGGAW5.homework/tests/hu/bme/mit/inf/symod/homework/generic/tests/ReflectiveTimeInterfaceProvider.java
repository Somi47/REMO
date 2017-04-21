package hu.bme.mit.inf.symod.homework.generic.tests;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

import org.yakindu.scr.chessclockwggaw5.ChessClockWGGAW5Statemachine;

public class ReflectiveTimeInterfaceProvider {
	public static boolean isTimerAvailable() {
		return iTimer()!= null;
	}
	
	public static Class<?> iTimer() {
		try {
			return Class.forName("org.yakindu.scr.ITimer");
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	public static Class<?> iTimerCallback() {
		if(isTimerAvailable()) {
			try {
				return Class.forName("org.yakindu.scr.ITimerCallback");
			} catch (ClassNotFoundException e) {
				//e.printStackTrace();
				return null;
			}
		}
		else return null;
	}
	
	public static void setTimer(ChessClockWGGAW5Statemachine machine, Object timer) {
		if(timer != null && isTimerAvailable()) {
			try {
				machine.getClass().getMethod("setTimer", iTimer()).invoke(machine, timer);
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException
					| SecurityException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Object provideLogicTimer(final LogicTimer timer) {
		if(isTimerAvailable()) {
			return Proxy.newProxyInstance(
				ReflectiveTimeInterfaceProvider.iTimer().getClassLoader(),
				new Class<?>[]{ReflectiveTimeInterfaceProvider.iTimer()}, 
				new LogicTimerInvocationHandler(timer));
		}
		else return null;
	}
	
	public static Object providePhysicsTimer() {
		if(isTimerAvailable()) {
			try {
				return Class.forName("org.yakindu.scr.TimerService").newInstance();
			} catch (InstantiationException | IllegalAccessException
					| ClassNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		}
		else return null;
	}
	
	public static void timeElapsed(Object callback, int eventID) {
		if(isTimerAvailable()) {
			try {
				iTimerCallback().getMethod("timeElapsed", int.class).invoke(callback, new Object[]{eventID});
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException
					| SecurityException e) {
				e.printStackTrace();
			}
		}
	}
}
