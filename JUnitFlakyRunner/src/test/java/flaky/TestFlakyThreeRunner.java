package flaky;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import rigby.junit.flaky.runners.Flaky;
import rigby.junit.flaky.runners.FlakyThreeRunner;

@RunWith(value=FlakyThreeRunner.class)
public class TestFlakyThreeRunner {

	RandomNormal randomNormal;
	
	@Before
	public void setup() {
		randomNormal = new RandomNormal();
	}
	
	@Test
	public void testPerfect() {
		assertTrue(randomNormal.getRandom(1));
	}
	
	@Test @Flaky
	public void testMostlyPass() {
		assertTrue(randomNormal.getRandom(.9));
	}
	
	@Test @Flaky
	public void testMostlyFail() {
		assertTrue(randomNormal.getRandom(.10));
	}
	
	@Test @Flaky
	public void testAllFails() {
		assertTrue(false);
	}
	
}
