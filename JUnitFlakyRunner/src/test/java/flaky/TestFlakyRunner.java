package flaky;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import rigby.junit.flaky.runners.FlakyBlockRunner;
import rigby.junit.flaky.runners.FlakyFactory;
import rigby.junit.flaky.runners.FlakyTracker;

@RunWith(value=FlakyBlockRunner.class)
public class TestFlakyRunner {

	public static FlakyTracker tracker = FlakyFactory.getInstance().getFlakyTracker(TestFlakyRunner.class);
	
	@BeforeClass 
	public static void configureFlakyTracker() {
		//higher the value the more sensitive: Default = .05 or 5% error
		//tracker.setConfidence(.01); //very insensitive to change
		//tracker.setConfidence(.99); //very sensitive to change
		
		//tracker.recalibrate("testGetWorse");
		//tracker.recalibrate("testGetBetter");
		//tracker.recalibrate("testAllFails");
		//tracker.recalibrateAll();
	}

	@Test
	public void testPerfect() {
		//originally 1
		Threader fc = new Threader(1);
		assertTrue(fc.threadProcessed());
	}
	
	@Test
	public void testGetWorse() {
		//originally .9
		Threader fc = new Threader(.90);
		assertTrue(fc.threadProcessed());
	}
	
	@Test
	public void testGetBetter() {
		//originally .75
		Threader fc = new Threader(.75);
		assertTrue(fc.threadProcessed());
	}
	
	@Test
	public void testTrueRandom() {
		//originally .5
		Threader fc = new Threader(.5);
		assertTrue(fc.threadProcessed());
	}
	
	@Test
	public void testMostlyFail() {
		//originally .1
		Threader fc = new Threader(.1);
		assertTrue(fc.threadProcessed());
	}
	
	@Test
	public void testAllFails() {
		assertTrue(false);
	}
	
	@AfterClass
	public static void showFlakyResults() {
		tracker.showFlakyTests();
	}
	
}
