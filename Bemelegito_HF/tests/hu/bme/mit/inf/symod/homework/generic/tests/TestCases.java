package hu.bme.mit.inf.symod.homework.generic.tests;

import org.junit.Test;

public class TestCases {
	public static void main(String[] args) {
		TestCases testCases = new TestCases();
		try{ testCases.neptun1(); }catch(AssertionError e) {}try{ testCases.neptun2(); }catch(AssertionError e) {}try{ testCases.neptun3(); }catch(AssertionError e) {}try{ testCases.neptun4(); }catch(AssertionError e) {}
	}
	
	@Test(timeout=10000)
	public void neptun1() {
		TesterAPI c = new TesterAPI("neptun1","Initially the display shows \"Hello Yakindu World!\"");
	c.expectText("Hello Yakindu World!");
	System.out.println("neptun1 Succeeded!");
	}
	
	@Test(timeout=10000)
	public void neptun2() {
		TesterAPI c = new TesterAPI("neptun2","After pressing a button it shows your Neptun code WGGAW5");
	c.pushStart();
	c.expectText("WGGAW5");
	System.out.println("neptun2 Succeeded!");
	}
	
	@Test(timeout=10000)
	public void neptun3() {
		TesterAPI c = new TesterAPI("neptun3","Pressing the button twice shows \"Hello Yakindu World!\"");
	c.pushStart();
	c.pushStart();
	c.expectText("Hello Yakindu World!");
	System.out.println("neptun3 Succeeded!");
	}
	
	@Test(timeout=10000)
	public void neptun4() {
		TesterAPI c = new TesterAPI("neptun4","Pressing the button three times shows your Neptun code WGGAW5 again");
	c.pushStart();
	c.pushStart();
	c.pushStart();
	c.expectText("WGGAW5");
	System.out.println("neptun4 Succeeded!");
	}
	
}
