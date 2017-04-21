package hu.bme.mit.inf.symod.homework.generic.tests;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogicTimerInvocationHandler implements InvocationHandler {
	private final LogicTimer timer;

	public LogicTimerInvocationHandler(LogicTimer timer) {
		this.timer = timer;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		switch (method.getName()) {
		case "setTimer": 
			timer.setTimer(args[0], (Integer)args[1], (Long)args[2], (Boolean)args[3]);
			return null;
		case "unsetTimer":
			timer.unsetTimer(args[0], (Integer)args[1]);
			return null;
		}
		return null;
	}
}
