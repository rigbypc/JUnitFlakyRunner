package flaky;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import rigby.junit.flaky.runners.FlakyBlockRunner;
import rigby.junit.flaky.runners.FlakyFactory;
import rigby.junit.flaky.runners.FlakyTracker;

@RunWith(value=FlakyBlockRunner.class)
public class TestFlakyRunner {

	public static FlakyTracker tracker = FlakyFactory.getInstance().getFlakyTracker(TestFlakyRunner.class);
	
	RandomNormal randomNormal;

	@Before
	public void setup() {
		randomNormal = new RandomNormal();
	}
	
	@BeforeClass 
	public static void configureFlakyTracker() {
		//higher the value the more sensitive: Default = .05 or 5% error
		//tracker.setConfidence(.01); //very insensitive to change
		//tracker.setConfidence(.99); //very sensitive to change
		
		//tracker.recalibrate("testMostlyFail");
		//tracker.recalibrateAll();
	}
	
	@Test
	public void testPerfect() {
		assertTrue(randomNormal.getRandom(1));
	}
	
	@Test
	public void testMostlyPass() {
		assertTrue(randomNormal.getRandom(.9));
	}
	
	@Test
	public void testMostlyFail() {
		assertTrue(randomNormal.getRandom(.10));
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
